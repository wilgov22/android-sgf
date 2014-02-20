package com.sgf.api.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.sgf.api.operations.GETMovementImediate;
import com.sgf.api.operations.POSTMovement;
import com.sgf.db.AccountsDAO;
import com.sgf.db.MovementsDAO;
import com.sgf.db.SGFDataModelSQLiteHelper;
import com.sgf.entities.Account;
import com.sgf.entities.Movement;
import com.sgf.activities.R;

/*
 * Este servico serve para obter os movimentos de um dada conta do utilizador e persistir localmente apenas as novas contas
 * */

/**
 * Servico que corre em Thread dedicada a operacao de GET de Movimentos da
 * SGFAPP e persistir os movimentos nao existentes na BD local
 * 
 * @author wgovindji
 * 
 */
public class GetMovementsService extends IntentService {
	private Handler handleResult;
	private final String TAG = "SGF_GetMovementsService";

	public GetMovementsService() {
		super("GetMovementService");

		handleResult = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				Bundle bundle = msg.getData();
				CharSequence contentTitle = "SGFAPP Movements";
				CharSequence contentText = "";
				if (bundle != null && bundle.containsKey("RESPONSE")) {
					contentText = bundle.getString("RESPONSE");
				}
				Log.d(TAG, "Respose:" + contentText);
				if (!contentText.toString().contains("Total of 0 ")) {

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
					// utilizador, pode abrir uma activity
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
		// TODO Auto-generated constructor stub
	}

	/**
	 * este codigo corre em thread alternativa, com base no account_id recebido
	 * no Intent, é invocada a operação getMovement da API
	 * 
	 * @see android.app.IntentService#onHandleIntent(android.content.Intent)
	 */
	@Override
	protected void onHandleIntent(Intent intent) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		String username = preferences.getString("sgfUser", "test");
		String password = preferences.getString("sgfPassword", "testpass");

		Bundle myBundle = intent.getExtras();
		// String accountID=myBundle.getString("localAccountID");

		String accountID = "";
		String filter = " where " + SGFDataModelSQLiteHelper.COLUMN_ACCOUNT_ID
				+ " not in (1) ";

		if (myBundle != null && myBundle.containsKey("localAccountID")) {
			accountID = myBundle.getString("localAccountID");
			filter += "and " + SGFDataModelSQLiteHelper.COLUMN_ACCOUNT_ID + "='"
					+ accountID + "' ";
		}

		List<com.sgf.entities.Account> accounts = new AccountsDAO(
				getApplicationContext()).getAllAccounts(filter);

		for (com.sgf.entities.Account account : accounts) {

			if (!account.get_id().equals("1")) {

				String sgfAccountId = account.getSgf_account_id();
				accountID = account.get_id();
				GETMovementImediate getMovs = new GETMovementImediate(username,
						password, sgfAccountId, null, getApplicationContext());

				Message msg = getMovs.run();
				Message MessageFinal = persistDataOnBD(msg, accountID);
				handleResult.handleMessage(MessageFinal);
			}

		}
	}

	/**
	 * Este metodo trata da mensgem que contem a resposta ao pedido HTTP, recebe
	 * em JSON os movimentos e insere os novos na tabela sgt_t_movements no
	 * estado S
	 * 
	 * @param msg
	 */
	private Message persistDataOnBD(Message msg, String localAccountId) {
		Message messageFinal = handleResult.obtainMessage();
		Bundle bundle = new Bundle();
		int countInsertedMovements = 0;
		MovementsDAO movDAO = new MovementsDAO(getApplicationContext());
		// TODO Auto-generated method stub
		String responseInJson = msg.getData().getString("RESPONSE");

		// Forçar mensagem
		// SUCESSO
		// responseInJson =
		// "{\"movement\":[{\"_id\":null,\"mov_id\":25,\"mov_account_id\":4,\"mov_type_id\":1,\"mov_cat_id\":1,\"mov_sub_cat_id\":null,\"mov_amount\":100.00,\"mov_desc\":\"\",\"mov_date\":\"22-08-2012\",\"status_code\":null},{\"_id\":null,\"mov_id\":13,\"mov_account_id\":4,\"mov_type_id\":2,\"mov_cat_id\":6,\"mov_sub_cat_id\":null,\"mov_amount\":150.00,\"mov_desc\":\"we\",\"mov_date\":\"15-08-2012\",\"status_code\":null}],\"status\":{\"status_code\":\"C000\"}}";
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
					JSONArray movementsArr = objJson.getJSONArray("movement");

					for (int i = 0; i <= movementsArr.length() - 1; i++) {
						JSONObject jsonObject = movementsArr.getJSONObject(i);
						Movement mov = API_utils.MovementFromJson(jsonObject);
						mov.setMov_account_id(localAccountId);
						if (!movDAO.movementExists((mov.getSgf_mov_id()))) {
							// nao existe entao insere na tabela, como estado a
							// S - Synchronized
							mov.setMov_state("S");
							movDAO.create(mov);
							countInsertedMovements++;

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
			bundle.putString("RESPONSE", "Total of " + countInsertedMovements
					+ " Movements Inserted");
		}
		messageFinal.setData(bundle);

		return messageFinal;

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub

		Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();
		return super.onStartCommand(intent, flags, startId);
	}

}
