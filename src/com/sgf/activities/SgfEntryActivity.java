package com.sgf.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.sgf.api.services.SynchService;

public class SgfEntryActivity extends Activity {
	private Button newMovement;
	private Button listMovement;
	private Button synchMovement;
	private Button reports;

	private OnClickListener newMovementListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(SgfEntryActivity.this,
					MovementInsertActivity.class);
			startActivity(intent);

		}
	};

	private OnClickListener listMovementListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(SgfEntryActivity.this,
					ListMovementsActivity.class);
			startActivity(intent);

		}
	};

	private OnClickListener reportListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(SgfEntryActivity.this,
					ListAccountSummaryActivity.class);
			startActivity(intent);

		}
	};
	private OnClickListener launchSyncListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(SgfEntryActivity.this,
					SynchService.class);
			startService(intent);

		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sgf_entry_layout);

		newMovement = (Button) findViewById(R.id.imgbtn_new);
		listMovement = (Button) findViewById(R.id.imgbtn_list);
		synchMovement = (Button) findViewById(R.id.imgbtn_synch);
		reports = (Button) findViewById(R.id.imgbtn_report);

		newMovement.setOnClickListener(newMovementListener);
		listMovement.setOnClickListener(listMovementListener);
		synchMovement.setOnClickListener(launchSyncListener);

		reports.setOnClickListener(reportListener);

	}
	
	 @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	        getMenuInflater().inflate(R.menu.main_menu, menu);
	        return true;
	    }
	    
	    @Override
	    public boolean onOptionsItemSelected(MenuItem item) {
	    	switch (item.getItemId()) {
			case R.id.settingsMenuItem:
				//Lancar o menu de settings
				Intent intent=new Intent(getApplicationContext(), PreferencesActivity.class);
				startActivity(intent);
				break;
			case R.id.helpButton:
			default:
				break;
			}
	    	return super.onOptionsItemSelected(item);
	    }

}
