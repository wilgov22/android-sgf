package com.sgf.api.operations;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import com.sgf.entities.Account;
import com.sgf.entities.Movement;
import com.sgf.http.StringUtils;

public class API_utils {

	// Este metodo recebe um Account em formato JsonObject e retorna um objecto
	// Account
	public static Account AccountFromJson(JSONObject _obj) {

		Account acc = new Account();
		try {
			if (!_obj.isNull("account_id")) {
				acc.setSgf_account_id(_obj.getString("account_id"));
			}

			if (!_obj.isNull("bank_name")) {
				acc.setAccount_bank_name(_obj.getString("bank_name"));
			}
			if (!_obj.isNull("other_info")) {
				acc.setAccount_other_info(_obj.getString("other_info"));
			}
			if (!_obj.isNull("balance")) {
				acc.setAccount_balance(_obj.getDouble("balance"));
			}
			if (!_obj.isNull("account_number")) {
				acc.setAccount_number(_obj.getString("account_number"));
			}
			if (!_obj.isNull("account_name")) {
				acc.setAccount_name(_obj.getString("account_name"));
			} else {
				acc.setAccount_name("No Name - " + acc.getAccount_number());
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			Log.d(API_utils.class.getName(),
					"Something went wrong while parsing JSON");
			e.printStackTrace();
		}
		return acc;
	}

	// Este metodo recebe um Movement em formato JsonObject e retorna um objecto
	// Movement
	public static Movement MovementFromJson(JSONObject _obj) {

		Movement mov = new Movement();
		try {
			if (!_obj.isNull("mov_id")) {
				mov.setSgf_mov_id(_obj.getString("mov_id"));
			}

			if (!_obj.isNull("mov_account_id")) {
				mov.setMov_account_id(_obj.getString("mov_account_id"));
			}
			if (!_obj.isNull("mov_type_id")) {
				mov.setMov_type_id(_obj.getString("mov_type_id"));
			}
			if (!_obj.isNull("mov_cat_id")) {
				mov.setMov_cat_id(_obj.getString("mov_cat_id"));
			}
			if (!_obj.isNull("mov_sub_cat_id")) {
				mov.setMov_sub_cat_id(_obj.getString("mov_sub_cat_id"));
			}
			if (!_obj.isNull("mov_amount")) {
				mov.setMov_amount(_obj.getDouble("mov_amount"));
			}
			if (!_obj.isNull("mov_desc")) {
				mov.setMov_desc(_obj.getString("mov_desc"));
			}
			if (!_obj.isNull("mov_date")) {
				mov.setMov_date(_obj.getString("mov_date"));
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			Log.d(API_utils.class.getName(),
					"Something went wrong while parsing JSON");
			e.printStackTrace();
		}
		return mov;
	}

	// Este metodo recebe um Movement e retorna no formato JSON
	// Movement
	public static JSONObject JsonFromMovement(Movement mov) {

		/*
		 * {"_id":999, -> ponho "mov_id":null, -> ponho "mov_account_id":4, ->
		 * ponho "mov_type_id":2, "mov_cat_id":6, "mov_sub_cat_id":null,
		 * "mov_amount":150.00, "mov_desc":"we", "mov_date":"15-08-2012",
		 * "status_code":null}
		 */

		JSONObject movJson = new JSONObject();
		try {

			movJson.put("_id", Integer.parseInt(mov.get_id()));

			if (mov.getSgf_mov_id() != null && mov.getSgf_mov_id().length() > 0) {
				String mov_id = mov.getSgf_mov_id();
				movJson.put("mov_id", Integer.parseInt(mov.getSgf_mov_id()));
			} else {
				movJson.put("mov_id", null);
			}
			movJson.put("mov_account_id",
					Integer.parseInt(mov.getMov_account_id()));
			movJson.put("mov_type_id", Integer.parseInt(mov.getMov_type_id()));
			movJson.put("mov_cat_id", Integer.parseInt(mov.getMov_cat_id()));
			if (mov.getMov_sub_cat_id() != null && mov.getMov_sub_cat_id().length()>0) {
				movJson.put("mov_sub_cat_id",
						Integer.parseInt(mov.getMov_sub_cat_id()));
			} else {
				movJson.put("mov_sub_cat_id", null);
			}
			movJson.put("mov_amount", mov.getMov_amount());
			movJson.put("mov_desc", mov.getMov_desc());
			movJson.put("mov_date", mov.getMov_date());
			;
			movJson.put("status_code", mov.getMov_state());

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			Log.d(API_utils.class.getName(),
					"Something went wrong while parsing JSON");
			e.printStackTrace();
		}
		return movJson;
	}

	public static Message getMessageFromHttp(HttpResponse response) {
		Message message = new Message();
		Bundle bundle = new Bundle();
		StatusLine status = response.getStatusLine();
		HttpEntity entity = response.getEntity();
		String result = null;
		if (entity != null) {
			try {
				result = StringUtils.inputStreamToString(entity.getContent());
				bundle.putString("RESPONSE", result);
				message.setData(bundle);

			} catch (IOException e) {
				bundle.putString("RESPONSE", "Error - " + e.getMessage());
				message.setData(bundle);

			}
		} else {
			bundle.putString("RESPONSE", "Error - "
					+ response.getStatusLine().getReasonPhrase());
			message.setData(bundle);

		}
		return message;
	}

}
