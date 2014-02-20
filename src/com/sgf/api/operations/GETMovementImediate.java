package com.sgf.api.operations;

import org.apache.http.HttpResponse;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.sgf.http.HttpRequestHelperImediate;


/**Classe Wrapper para realizar o pedido à API abstraindo que usa da criacao do URL e criacao do pedido HTTP.
 * @author wgovindji
 *
 */
public class GETMovementImediate extends APIOperations {
	String HttpURL;
	private static final String TAG= "GETMovement";
	private static final String CONTENT_TYPE = "/application/json";
	private final String user;
	private final String pass;
	private final Handler handler;
	
	/*Exemplo:
	 * 
	 * http://sgf.luisduarte.net/api/accounts/16/movements/
	 * */
	public GETMovementImediate(String user, String password, String accountId , Handler handler, Context ctx){
		super(ctx);
		this.method="GET";
		this.operation="/api/accounts/{accountId}/movements/";
		this.operation=this.operation.replace("{accountId}", accountId);
		this.user=user;
		this.pass=password;
		this.handler=handler;
	}
	
	/** Executa de modo sincrono a operacao à API
	 * @return Message contendo informação da resposta à API
	 */
	public Message run() {
			String _url=getURL()+operation;
			HttpRequestHelperImediate helper= new HttpRequestHelperImediate();
			HttpResponse response=helper.performGet(_url,user,pass,null);
			Message msg = API_utils.getMessageFromHttp(response);
			//if(handler!=null){handler.sendMessage(msg);}
			return msg;

			
		}
		


	}
