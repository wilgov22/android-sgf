package com.sgf.api.services;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.sgf.activities.ListMovementsActivity;
import com.sgf.api.operations.API_utils;
import com.sgf.api.operations.GetAccountImediate;
import com.sgf.db.AccountsDAO;
import com.sgf.db.MovementsDAO;
import com.sgf.entities.Account;
import com.sgf.activities.R;

/**
 * Servico que correr numa thread dedicada operação de GET das Contas existentes
 * na SGFAPP e persistir as novas na BD Local
 * 
 * @author wgovindji
 * 
 */
public class GetAccountsService extends IntentService {
	private Handler handleResult;
	private final String TAG = "SGF_GetAccountsService";

	public GetAccountsService() {
		super("AccountService");

		handleResult = new Handler() {

			@Override
			public void handleMessage(Message msg) {

				CharSequence contentTitle = "SGFAPP Accounts";
				CharSequence contentText = "";

				Bundle bundle = msg.getData();
				if (bundle != null && bundle.containsKey("RESPONSE")) {
					contentText = bundle.getString("RESPONSE");
				}
				Log.d(TAG,"Respose:"+contentText);

				if (!contentText.toString().contains("Total of 0 Accounts")) {
					// criação de notificação
					String ns = Context.NOTIFICATION_SERVICE;
					NotificationManager mNotificationManager = (NotificationManager) getSystemService(ns);

					int icon = R.drawable.logo;
					CharSequence tickerText = "SGF Mobile";
					long when = System.currentTimeMillis();

					Notification notification = new Notification(icon,
							tickerText, when);
					notification.flags |= Notification.FLAG_AUTO_CANCEL;
					Context context = getApplicationContext();

					// este intent é lançado quando a notificação é clicada pelo
					// utilizador, pode abrir uma activity, e para isso é
					// embrulhado
					// num pendentIntent
					Intent notificationIntent = new Intent(
							getApplicationContext(),
							ListMovementsActivity.class);
					PendingIntent contentIntent = PendingIntent.getActivity(
							getApplicationContext(), 0, notificationIntent, 0);

					notification.setLatestEventInfo(context, contentTitle,
							contentText, contentIntent);

					mNotificationManager.notify(1, notification);
				}

			}

		};

	}

	// este codigo corre em thread Alternativa
	@Override
	protected void onHandleIntent(Intent arg0) {
		// TODO Auto-generated method stub
		MovementsDAO mDAO = new MovementsDAO(getApplicationContext());

		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		String username = preferences.getString("sgfUser", "1");
		String password = preferences.getString("sgfPassword", "1");

		GetAccountImediate getAccount = new GetAccountImediate(username,
				password, "", null, null, getApplicationContext());
		Message msgFromHTTP = getAccount.run();
		Message msgFinal = persistDataOnBD(msgFromHTTP);
		handleResult.handleMessage(msgFinal);

	}

	private Message persistDataOnBD(Message msg) {
		Message messageFinal = handleResult.obtainMessage();
		Bundle bundle = new Bundle();
		int accountsInserted = 0;
		AccountsDAO accDAO = new AccountsDAO(getApplicationContext());
		// TODO Auto-generated method stub
		String responseInJson = msg.getData().getString("RESPONSE");

		// Forçar mensagem
		// SUCESSO
		// responseInJson =
		// "{\"account\":[{\"account_id\":4,\"account_number\":\"100000000\",\"account_name\":\"Conta Principal\",\"bank_name\":\"BES\",\"balance\":0.00,\"other_info\":\"1\"},{\"account_id\":5,\"account_number\":\"200000000\",\"account_name\":\"Conta Extra\",\"bank_name\":\"CGD\",\"balance\":0.00,\"other_info\":\"CGD\"},{\"account_id\":7,\"account_number\":\"40000000\",\"account_name\":null,\"bank_name\":null,\"balance\":100.00,\"other_info\":null}],\"status\":{\"status_code\":\"C000\"}}";
		// ERRO C999
		// responseInJson =
		// "{\"account\":[{\"account_id\":4,\"account_number\":\"100000000\",\"account_name\":\"Conta Principal\",\"bank_name\":\"BES\",\"balance\":0.00,\"other_info\":\"1\"},{\"account_id\":5,\"account_number\":\"200000000\",\"account_name\":\"Conta Extra\",\"bank_name\":\"CGD\",\"balance\":0.00,\"other_info\":\"CGD\"},{\"account_id\":7,\"account_number\":\"40000000\",\"account_name\":null,\"bank_name\":null,\"balance\":100.00,\"other_info\":null}],\"status\":{\"status_code\":\"C999\"}}";
		// ERRO - status CODE invalido
		// responseInJson =
		// "{\"account\":[{\"account_id\":4,\"account_number\":\"100000000\",\"account_name\":\"Conta Principal\",\"bank_name\":\"BES\",\"balance\":0.00,\"other_info\":\"1\"},{\"account_id\":5,\"account_number\":\"200000000\",\"account_name\":\"Conta Extra\",\"bank_name\":\"CGD\",\"balance\":0.00,\"other_info\":\"CGD\"},{\"account_id\":7,\"account_number\":\"40000000\",\"account_name\":null,\"bank_name\":null,\"balance\":100.00,\"other_info\":null}],\"status\":{\"status_code\":\"C998\"}}";

		if (responseInJson != null && responseInJson.length() > 0) {
			JSONObject objJson;
			try {
				// Obter objecto JSON a partir da resposta
				objJson = new JSONObject(responseInJson);
				String code = objJson.getJSONObject("status").getString(
						"status_code");
				if (code.equals("C000")) {
					// obter array JSON, iterar sobre cada um deles e
					// inserir na BD caso seja novo
					JSONArray accountArr = objJson.getJSONArray("account");

					for (int i = 0; i < accountArr.length(); i++) {
						JSONObject jsonObject = accountArr.getJSONObject(i);
						Account acc = API_utils.AccountFromJson(jsonObject);
						if (!accDAO.accountExists(acc.getSgf_account_id())) {
							// nao existe entao insere na tabela
							accDAO.create(acc);
							accountsInserted++;

						}

					}

				} else if (code.equals("C999")) {

					Log.d(TAG, "Error - C999 : Authentication Invalid");

					bundle.putString("RESPONSE",
							"Error - C999 : Authentication Invalid");
				} else {
					Log.d(TAG, "Abnormal Error - No status Code found");
					bundle.putString("RESPONSE",
							"Abnormal Error - No status Code found");
				}
			} catch (JSONException e1) {
				Log.d(TAG, "No JSON object");
				bundle.putString("RESPONSE",
						"ERRO - Data not as Expected, check Internet Connection");
				e1.printStackTrace();
			}

		}
		if (!bundle.containsKey("RESPONSE")) {
			bundle.putString("RESPONSE", "Total of "+accountsInserted
					+ " Accounts Retrived");
		}
		messageFinal.setData(bundle);
		return messageFinal;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub

		// Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();
		Log.d(TAG, "OnStartCommand");
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		Log.d(TAG, "OnDestroy");
		super.onDestroy();
	}

}
