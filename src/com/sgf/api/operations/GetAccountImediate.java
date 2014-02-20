package com.sgf.api.operations;

import java.util.Map;

import org.apache.http.HttpResponse;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.sgf.http.HttpRequestHelperImediate;



/** Classe Wrapper para realizar o pedido à API abstraindo que usa da criacao do URL e criacao do pedido HTTP.
 * @author wgovindji
 *
 */
public class GetAccountImediate extends APIOperations {
	String HttpURL;
	private static final String TAG= "GETAccount";
	private static final String CONTENT_TYPE = "/application/json";
	private final String user;
	private final String pass;
	private final Handler handler;
	private final Map<String,String> additionalHeaders;
	
	/*Exemplo:
	 * http://sgf.luisduarte.net/api/accounts/
	 * */
	public GetAccountImediate(String user, String password, String accountId , Handler handler, Map<String, String> additionalHeaders,Context ctx){
		super(ctx);
		this.method="POST";
		this.operation="/api/accounts/";
		if(accountId !=null && !accountId.equals("")){
			this.operation="/accounts/{accountId}";
			this.operation=this.operation.replace("{accountId}", accountId);
		}
		this.user=user;
		this.pass=password;
		this.handler=handler;
		this.additionalHeaders=additionalHeaders;
	}
	
	/** Executa de modo sincrono a operacao à API
	 * @return Message contendo informação da resposta à API
	 */
	public Message run() {
		// TODO Auto-generated method stub
		String _url=getURL()+operation;
		_url="http://sgf.luisduarte.net"+operation;


			
			HttpRequestHelperImediate helper= new HttpRequestHelperImediate();
			HttpResponse resp=helper.performGet(_url, user, pass, additionalHeaders);
			Message msg= API_utils.getMessageFromHttp(resp);
			if(handler!=null){handler.sendMessage(msg);}
			return msg;
			
		}
	
			


	

}
