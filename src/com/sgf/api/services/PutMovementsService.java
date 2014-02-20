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

import com.sgf.activities.MainActivity;
import com.sgf.api.operations.API_utils;
import com.sgf.api.operations.POSTMovement;
import com.sgf.api.operations.PUTMovement;
import com.sgf.db.AccountsDAO;
import com.sgf.db.MovementsDAO;
import com.sgf.db.SGFDataModelSQLiteHelper;
import com.sgf.entities.Movement;
import com.sgf.activities.R;

/*
 * Este servico serve para obter os movimentos de um dada conta do utilizador e persistir localmente apenas as novas contas
 * */

/**
 * Esta classe verifica quais os movimentos que estão por sincronizar Status = N
 * à excepcao do account_id=1 porque é uma conta local
 * 
 * @author wgovindji
 * 
 */
public class PutMovementsService extends IntentService {
	private Handler handleResult;
	private final String TAG = "PutMovementsService";

	public PutMovementsService() {
		super("PutMovementService");

		handleResult = new Handler() {

			@Override
			public void handleMessage(Message msg) {

				// criação de notificação
				String ns = Context.NOTIFICATION_SERVICE;
				NotificationManager mNotificationManager = (NotificationManager) getSystemService(ns);

				int icon = R.drawable.logo;
				CharSequence tickerText = "SGF Mobile";
				long when = System.currentTimeMillis();

				Notification notification = new Notification(icon, tickerText,
						when);
				notification.flags |= Notification.FLAG_AUTO_CANCEL;
				Context context = getApplicationContext();
				CharSequence contentTitle = "SGFAPP Movements";
				CharSequence contentText = "Movements Putted to Server";
				if(msg.getData().containsKey("message")){
					contentText= msg.getData().getString("message");
				}

				// este intent é lançado quando a notificação é clicada pelo
				// utilizador, pode abrir uma activity
				Intent notificationIntent = new Intent(getApplicationContext(),
						MainActivity.class);
				PendingIntent contentIntent = PendingIntent.getActivity(
						getApplicationContext(), 0, notificationIntent, 0);

				notification.setLatestEventInfo(context, contentTitle,
						contentText, contentIntent);

				mNotificationManager.notify(1, notification);

			}

		};
		// TODO Auto-generated constructor stub
	}

	/**
	 * este codigo corre em thread alternativa, com base no account_id recebido
	 * no Intent, caso contrario realiza para todas as contas é invocada a
	 * operação PutMovement da API
	 * 
	 * @see android.app.IntentService#onHandleIntent(android.content.Intent)
	 */
	@Override
	protected void onHandleIntent(Intent intent) {
		MovementsDAO mdao = new MovementsDAO(getApplicationContext());
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		String username = preferences.getString("sgfUser", "test");
		String password = preferences.getString("sgfPassword", "testpass");
		Boolean movementsSynched= false;
		Bundle myBundle = intent.getExtras();

		String accountID = "";
		String filter = " where "+ SGFDataModelSQLiteHelper.COLUMN_ACCOUNT_ID + " not in (1) ";

		if (myBundle != null && myBundle.containsKey("localAccountID")) {
			accountID = myBundle.getString("localAccountID");
			filter = "and " + SGFDataModelSQLiteHelper.COLUMN_ACCOUNT_ID + "='"
					+ accountID + "' ";
		}

		// Se exitir uma localAccountId no bundle, então obter o SGF_ACCOUNT_ID
		// real para invocar a API,
		// Caso contrario, iterar por todas as contas (à excepcao da
		// account_id=1) e obter o SGF_ACCOUNT_ID e invocar a API por todas as
		// contas
		List<com.sgf.entities.Account> accounts = new AccountsDAO(
				getApplicationContext()).getAllAccounts(filter);
		for (com.sgf.entities.Account account : accounts) {
			JSONObject object = new JSONObject();
			JSONArray jsonArray = new JSONArray();
			if (!account.get_id().equals("1")) {
				List<Movement> movementsTOSynch = mdao.getAllMovements("and "
						+ SGFDataModelSQLiteHelper.COLUMN_MOVEMENT_ACCOUNT_ID
						+ " ='" + account.get_id() + "' and "
						+ SGFDataModelSQLiteHelper.COLUMN_MOVEMENT_STATE
						+ " in ('D') ");
				for (Movement mov : movementsTOSynch) {
					jsonArray.put(API_utils.JsonFromMovement(mov));
				}
				try {
					object.put("movement", jsonArray);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (movementsTOSynch.size() > 0) {
					movementsSynched=true;
					Log.d(TAG,
							"INVOKE PUT_API WITH MovementArray ="
									+ object.toString());
					Map<String, String> params=new HashMap<String, String>();
					params.put("content", object.toString());
					PUTMovement putmovs = new PUTMovement(username, password,
							account.getSgf_account_id(), null, null, params,getApplicationContext());
					Message msg = putmovs.run();
					persistDataOnBD(msg);
				}

			}
		}
		
		if(movementsSynched && this.handleResult!=null){
			Bundle bundle=new Bundle();
			bundle.putString("message", "Your new movements were synched");
			Message msg=handleResult.obtainMessage();
			msg.setData(bundle);
			handleResult.sendMessage(msg);
		}

		
		
	}

	/**
	 * Este metodo trata da mensgem que contem a resposta ao pedido HTTP, recebe
	 * em JSON os movimentos e insere os novos na tabela sgt_t_movements no
	 * estado S
	 * 
	 * @param msg
	 */
	private void persistDataOnBD(Message msg) {

		MovementsDAO movDAO = new MovementsDAO(getApplicationContext());
		// TODO Auto-generated method stub
		String responseInJson = msg.getData().getString("RESPONSE");

		// Forçar mensagem
		// SUCESSO
		//responseInJson = "{\"movement\":[{\"_id\":13,\"mov_id\":31,\"mov_account_id\":4,\"mov_type_id\":2,\"mov_cat_id\":6,\"mov_sub_cat_id\":null,\"mov_amount\":150.0,\"mov_desc\":\"we\",\"mov_date\":\"15-08-2012\",\"status_code\":\"C000\"}], \"status\":{\"status_code\":\"C000\"}}";
		// ERRO C999

		// ERRO - status CODE invalido

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

					for (int i = 0; i <= movementsArr.length()-1; i++) {
						JSONObject jsonObject = movementsArr.getJSONObject(i);
						Long localId = jsonObject.getLong("_id");
						Long sgfId = jsonObject.getLong("mov_id");

						Movement movToUpdate = movDAO.retrive(localId);
						String MovCurrentState = movToUpdate.getMov_state();
						String MovNextState = MovementsDAO.getNextState(
								MovCurrentState, "POST_API");
						if (MovNextState.equals("S")) {
							movToUpdate.setMov_state(MovNextState);
							movToUpdate.setSgf_mov_id(sgfId.toString());
							movDAO.update(movToUpdate);
						}

					}

				} else if (code.equals("C999")) {

					Log.d(TAG, "Error - C999 : Authentication Invalid");
				} else {
					Log.d(TAG, "Abnormal Error - No status Code found");
				}
			} catch (JSONException e1) {
				Log.d(TAG, "No JSON object");
				e1.printStackTrace();
			}

		}

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub

		Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();
		return super.onStartCommand(intent, flags, startId);
	}

}
