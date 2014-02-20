package com.sgf.api.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

/**
 * Servico que correr numa thread dedicada operação de GET das Contas existentes
 * na SGFAPP e persistir as novas na BD Local
 * 
 * @author wgovindji
 * 
 */
public class SynchService extends IntentService {
	private Handler handleResult;

	private final String TAG = "SGF_SynchService";
	private Context ctx;

	public SynchService() {
		super("SynchService");




		handleResult = new Handler() {

			@Override
			public void handleMessage(Message msg) {

			Toast.makeText(ctx, msg.getData().getString("RESPONSE"), Toast.LENGTH_LONG);

			}

		};

	}

	// este codigo corre em thread Alternativa
	@Override
	protected void onHandleIntent(Intent arg0) {
		
			
		ctx = getApplicationContext();	
		Log.d(TAG, "STARTING SERVICE from " + SynchService.class.getName() + GetAccountsService.class.getName());
		ctx.startService(new Intent(ctx, GetAccountsService.class));
		try {
		Thread.currentThread().sleep(1200);
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		}
		
		
		Log.d(TAG, "STARTING SERVICE from " + SynchService.class.getName()  + GetMovementsService.class.getName());
		ctx.startService(new Intent(ctx, GetMovementsService.class));
		try {
		Thread.currentThread().sleep(1200);
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		}
		
		
		Log.d(TAG, "STARTING SERVICE from " + SynchService.class.getName()  + PostMovementsService.class.getName());
		ctx.startService(new Intent(ctx, PostMovementsService.class));
		try {
		Thread.currentThread().sleep(1200);
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		}
		
		Message msg=handleResult.obtainMessage();
		Bundle bundle= new Bundle();
		bundle.putString("RESPONSE", "SGF-Synchronization on progress");
		msg.setData(bundle);
	 handleResult.handleMessage(msg);

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub

		Toast.makeText(this, "SGF-Synchronization Starting", Toast.LENGTH_SHORT).show();
		return super.onStartCommand(intent, flags, startId);
	}

}
