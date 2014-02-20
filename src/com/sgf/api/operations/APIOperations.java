package com.sgf.api.operations;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceManager;


/** Classe Base para as operacoes na API
 * @author wgovindji
 *
 */
public  class APIOperations {
	private  String host;
	private String port;
	public String operation;
	public String method;
	private Context context=null;
	
	
	public APIOperations(Context ctx){
		this.host="lx3433.com";
		this.port="8080";
		this.context=ctx;
		
	}
	
	/** 
	 * @return host onde se encontra a API
	 */
	public  String getHost() {
		return host;
	}

	/** 
	 * @return porto que está a escuta no host para os pedidos HTTP
	 */
	public  String getPort() {
		return port;
	}
	
	/**
	 * @return String contendo o url com base no host e o porto
	 */
	public String getURL(){
		SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(context);
		if(preferences.contains("sgf_api_host")){
			return preferences.getString("sgf_api", "http://sgf.luisduarte.net");
		}else{
		
		
		
		return "http://" + host + ":" + port + "";
		}
		
		
	}


	


	
	
	
}
