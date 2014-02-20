package com.sgf.api.operations;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.sgf.http.HttpRequestHelperImediate;


/**Classe Wrapper para realizar o pedido à API abstraindo que usa da criacao do URL e criacao do pedido HTTP.
 * @author wgovindji
 *
 */
public class POSTMovement extends APIOperations {
	String HttpURL;
	private static final String TAG= "POSTMovement";
	private static final String CONTENT_TYPE = "/application/json";
	private final String user;
	private final String pass;
	private final Handler handler;
	private Map<String, String> headers;
	private Map<String, String> params;
	
	public POSTMovement(String user, String password, String accountId , Handler handler, Map<String, String> headers,Map<String,String> params, Context ctx){
		super(ctx);
		this.method="POST";
		this.operation="/api/accounts/{accountId}/movements/";
		this.operation=this.operation.replace("{accountId}", accountId);
		this.user=user;
		this.pass=password;
		this.handler=handler;
		this.params=params;
	}

	/** Executa de modo sincrono a operacao à API
	 * @return Message contendo informação da resposta à API
	 */
	public Message run() {
		// TODO Auto-generated method stub
		String _url=getURL()+operation;

			
			Map<String, String> map= new HashMap<String, String>();
			//map.put("content", "ah e tal");
			
			HttpRequestHelperImediate helper= new HttpRequestHelperImediate();
			HttpResponse response= helper.performPost(CONTENT_TYPE, _url, user, pass, headers, this.params);
			Message msg=API_utils.getMessageFromHttp(response);
			if(handler!=null){handler.sendMessage(msg);}
			return msg;
		}
		


	}
