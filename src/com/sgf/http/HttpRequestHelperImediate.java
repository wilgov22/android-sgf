package com.sgf.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ProtocolVersion;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.Log;

/** Objecto Helper para realizar pedidos HTTP, abstraindo o utilizador da criacao de HttpClient, respectivo HttpMethod
 * e preocupações com questoes de autenticação
 * @author wgovindji
 * 
 */
public class HttpRequestHelperImediate {
	
		private static final int POST_TYPE=1;
		private static final int GET_TYPE=2;
		private static final int DELETE_TYPE=3;
		private static final int PUT_TYPE=4;

		private static final String CONTENT_TYPE="Content-Type";
		private static final String CONTENT_LENGTH="Content-Length";
		
		public static final String MIME_JSON = "application/json";
		public static final String AUTHORIZATION = "Authorization";
		
		public static final String TAG = "HTTPRequestHelper";
		

		
		public HttpRequestHelperImediate(){
			
		}
		
		//Classes Wrapper
		
		/** Class Wrapper para executar um HttpGet
		 * @param url URL a invocar
		 * @param user username
		 * @param pass password
		 * @param additionalHeaders headers adicionais
		 * @return
		 */
		public HttpResponse performGet (String url, String user, String pass,
				final Map<String, String> additionalHeaders) {
				return performRequest(null, url, user, pass, additionalHeaders, null, HttpRequestHelperImediate.GET_TYPE);
		}
	
		/** Class Wrapper para executar um HttpDelete
		 * @param url URL a invocar
		 * @param user username
		 * @param pass password
		 * @param additionalHeaders headers adicionais
		 */
		public HttpResponse performDelete (String url, String user, String pass,
				final Map<String, String> additionalHeaders) {
				return performRequest(null, url, user, pass, additionalHeaders, null, HttpRequestHelperImediate.DELETE_TYPE);
		}
		/** Class Wrapper para executar um HttpPost
		 * @param contentType
		 * @param url
		 * @param user
		 * @param pass
		 * @param additionalHeaders
		 * @param params Parametros adicionais
		 */
		public HttpResponse performPost (String contentType,String url, String user, String pass,
				Map<String, String> additionalHeaders,
				Map<String, String> params){
			return  performRequest(contentType, url, user, pass,additionalHeaders, params, HttpRequestHelperImediate.POST_TYPE);
			
		}		
		/** Class Wrapper para executar um HttpPut
		 * @param contentType
		 * @param url
		 * @param user
		 * @param pass
		 * @param additionalHeaders headers HTTP
		 * @param params parametros HTTP
		 */
		public HttpResponse performPut (String contentType,String url, String user, String pass,
				Map<String, String> additionalHeaders,
				Map<String, String> params
				) {
			return performRequest(contentType, url, user, pass,additionalHeaders, params, HttpRequestHelperImediate.PUT_TYPE);
		}	
		
		
		private static String inputStreamToString(final InputStream stream) throws IOException {
	        BufferedReader br = new BufferedReader(new InputStreamReader(stream));
	        StringBuilder sb = new StringBuilder();
	        String line = null;
	        while ((line = br.readLine()) != null) {
	            sb.append(line + "\n");
	        }
	        br.close();
	        return sb.toString();
	    }
		
		private HttpResponse performRequest(
				String contentType,
				String url,
				String user,
				String pass,
				Map<String, String> headers,
				Map<String, String> params,
				int requestType
				){
			HttpResponse resp=null;
			//Criaçao de um HttpClient
			DefaultHttpClient client= new DefaultHttpClient();
			
			//HttpHost proxy = new HttpHost("127.0.0.1", 8888);
			HttpHost proxy = new HttpHost("192.168.1.4", 8888);
			//client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
			
			
			//mapa de Headers
			final Map<String,String> sendHeaders = new HashMap<String, String>();
			
			if((headers!=null) && (headers.size()>0)){
				sendHeaders.putAll(headers);
			}
			//Header de autorizacao - user:pass encoded em base64
			sendHeaders.put(HttpRequestHelperImediate.AUTHORIZATION,getB64Auth(user, pass));
			
			//Se for um pedido HTTP POST tem que levar o tipo de conteudo
			if(requestType == HttpRequestHelperImediate.POST_TYPE){
				sendHeaders.put(HttpRequestHelperImediate.CONTENT_TYPE, contentType);
			}
			
			if(sendHeaders.size()> 0 ){
				client.addRequestInterceptor(new HttpRequestInterceptor() {
					
					@Override
					public void process(final HttpRequest request, final HttpContext context)
							throws HttpException, IOException {
						// TODO Auto-generated method stub
						for(String key : sendHeaders.keySet()){
							if (!request.containsHeader(key)){
								request.addHeader(key, sendHeaders.get(key));
							}
						}
						
					}
				});
			}
			String res="";
			if(requestType==HttpRequestHelperImediate.POST_TYPE){
				HttpPost method= new HttpPost(url);
				List<NameValuePair> nvps=null;
				if((params!=null) && (params.size()>0)){
					nvps= new ArrayList<NameValuePair>();
					for(String key: params.keySet()){
						nvps.add(new BasicNameValuePair(key, params.get(key)));
					}
				}
				
				if(nvps!= null){
					
					try{
						String content= nvps.get(0).getValue();
						method.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
						
						try {
							 res=inputStreamToString(method.getEntity().getContent());
							 
						} catch (IllegalStateException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}catch(UnsupportedEncodingException e){
						Log.d(TAG,"UnsupportedEncodingException");
					}
					
				}
				System.out.println("res="+res);
				resp= execute(client,method);
				HttpParams paramsH=client.getParams();

				Header [] head= method.getAllHeaders();
				
				
				
				Log.d(TAG, "HEaders inspecting");
				for (Header header : head) {
					Log.d(TAG,header.getName() + "=" + header.getValue());
				}
				

				
				//Log.d(TAG, "HttpClient "+ client.);
				Log.d(TAG, "HttpMethod "+ method.toString());
				
				
				
				
			}else if(requestType==HttpRequestHelperImediate.PUT_TYPE){
				HttpPut method= new HttpPut(url);
				List<NameValuePair> nvps=null;
				if((params!=null) && (params.size()>0)){
					nvps= new ArrayList<NameValuePair>();
					for(String key: params.keySet()){
						nvps.add(new BasicNameValuePair(key, params.get(key)));
					}
				}
				if(nvps!= null){
					try{
						method.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
					}catch(UnsupportedEncodingException e){
						Log.d(TAG,"UnsupportedEncodingException");
					}
					
				}
				resp= execute(client,method);
				
			}else if(requestType == HttpRequestHelperImediate.GET_TYPE){
				HttpGet method = new HttpGet(url);
				resp= execute(client,method);
			}else if(requestType == HttpRequestHelperImediate.DELETE_TYPE){
				HttpDelete method=new HttpDelete(url);
				resp= execute(client, method);
			}

			
			return resp;
			
			
			
		}
		 private HttpResponse execute(HttpClient client, HttpRequestBase method) {
			// TODO Auto-generated method stub
			BasicHttpResponse errorResponse = new BasicHttpResponse(new ProtocolVersion("HTTP_ERROR", 1, 1), 500, "ERROR");
			HttpResponse httpResponse=errorResponse;
			
			try {
				
				httpResponse=client.execute(method);
				Log.d(TAG,"Executing httpRequest");
				
				
			} catch (Exception e) {
				errorResponse.setReasonPhrase(e.getMessage());
				try {
					httpResponse=errorResponse;
				} catch (Exception ex) {
					Log.d(TAG,"HandleResponseException:" + e.getMessage());
				}
			}
			return httpResponse;
		}

		private String getB64Auth (String login, String pass) {
			   String source=login+":"+pass;
			   String ret="Basic "+Base64.encodeToString(source.getBytes(),Base64.URL_SAFE|Base64.NO_WRAP);
			   //String ret=Base64.encodeToString(source.getBytes(),Base64.URL_SAFE|Base64.NO_WRAP);
			   return ret;
		}
		
		
		
		public static ResponseHandler<String>  getResponseHandlerInstance(final Handler handler) {
	        final ResponseHandler<String> responseHandler =  new ResponseHandler<String>() {
	            public String handleResponse(final HttpResponse response) {
	                Message message = handler.obtainMessage();
	                Bundle bundle = new Bundle();
	                StatusLine status = response.getStatusLine();
	                HttpEntity entity = response.getEntity();
	                String result = null;
	                if (entity != null) {
	                    try {                    	
	                    	result = inputStreamToString(
	                          entity.getContent());
	                        bundle.putString(
	                            "RESPONSE", result);
								message.setData(bundle); 
	                        handler.sendMessage(message);
	                    } catch (IOException e) {
	                        bundle.putString("RESPONSE", "Error - " + e.getMessage());
	                        message.setData(bundle);
	                        handler.sendMessage(message);
	                    }
	                } else {
	                    bundle.putString("RESPONSE", "Error - " 
	                      + response.getStatusLine().getReasonPhrase());
	                    message.setData(bundle);
	                    handler.sendMessage(message);
	                }
	                return result;
	            }
	        };
	        return responseHandler;
	    } 
		

	}


		

