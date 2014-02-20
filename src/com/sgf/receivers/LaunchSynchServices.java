package com.sgf.receivers;

import com.sgf.api.services.GetAccountsService;
import com.sgf.api.services.SynchService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

public class LaunchSynchServices extends BroadcastReceiver {
	private final String TAG = "SGF_BroadcastReceiver";
	private  Context myContext = null;

	@Override
	public void onReceive(final Context ctx, Intent intent) {
		// TODO Auto-generated method stub

		myContext = ctx;
		Bundle bundle = intent.getExtras();

		Log.d(ConnectivityManager.class.getSimpleName(),
				"action: " + intent.getAction());

		boolean noConnectivity = intent.getBooleanExtra(
				ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
		String reason = intent.getStringExtra(ConnectivityManager.EXTRA_REASON);
		boolean isFailover = intent.getBooleanExtra(
				ConnectivityManager.EXTRA_IS_FAILOVER, false);

		NetworkInfo currentNetworkInfo = (NetworkInfo) intent
				.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
		NetworkInfo otherNetworkInfo = (NetworkInfo) intent
				.getParcelableExtra(ConnectivityManager.EXTRA_OTHER_NETWORK_INFO);

		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(ctx);
		Log.d(TAG, "Conectivity:" + noConnectivity + "Type:"
				+ currentNetworkInfo.getTypeName());

		boolean autosynch = sp.getBoolean("autosync", false);

		if (!noConnectivity && autosynch) {
			Log.d(TAG, "Starting SynchService");
			new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					myContext.startService(new Intent(myContext, SynchService.class));
				}
			}).start();

			Log.d(TAG, "Exiting.... bye bye");

		}

	}

}
