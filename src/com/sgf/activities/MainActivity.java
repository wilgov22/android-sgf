package com.sgf.activities;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.sgf.api.services.GetAccountsService;
import com.sgf.api.services.GetMovementsService;
import com.sgf.api.services.PostMovementsService;
import com.sgf.api.services.PutMovementsService;
import com.sgf.db.SGFDataModelSQLiteHelper;
import com.sgf.activities.R;




public class MainActivity extends Activity {
	
	private EditText editText;
	private TextView textView;
	private TextView textViewTotal;

	
	private OnClickListener getMovementsListener= new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(getApplicationContext(), GetMovementsService.class);
			startService(intent);
		}
	};

    private OnClickListener buttonListener=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			

			//SynchMovementsService srv= new SynchMovementsService();
			//Intent intent_= new Intent(getApplicationContext(), SynchMovementsService.class);
			//startService(intent_);
			
			
//			Handler handler= new Handler(){
//				public void handleMessage(android.os.Message msg) {
//					String bundleResult= msg.getData().getString("RESPONSE");
//					Toast.makeText(getApplicationContext(), bundleResult, Toast.LENGTH_SHORT).show();
//				}};
//			GETMovement get= new GETMovement("wg", "wgpass", "222", handler);
//			get.run();
			
			Intent intent = new Intent(getApplicationContext(), GetAccountsService.class);
			startService(intent);
			
				
		}
	};
	private OnClickListener helpButtonListener= new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Intent intent=new Intent(MainActivity.this, MovementInsertActivity.class);
			startActivity(intent);
			
		}
	};
	private OnClickListener listButtonListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent=new Intent(MainActivity.this, ListMovementsActivity.class);
			startActivity(intent);
			
		}
	};
	private OnClickListener totalListener= new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			SGFDataModelSQLiteHelper helper=new SGFDataModelSQLiteHelper(getApplicationContext(), null, null, 1);
			SQLiteDatabase db=helper.getReadableDatabase();
			String sqlStatement = "select sum(mov_amount) as Total from sgf_t_movements where mov_type_id=2";
			Cursor cursor=db.rawQuery(sqlStatement,null);
			String total="";
			cursor.moveToFirst();
			while (!cursor.isAfterLast()){
				total=cursor.getString(cursor.getColumnIndex("Total"));
				
				cursor.moveToNext();
				
			}
			cursor.close();
			helper.close();
			textViewTotal.setText(total);
			
		}
	};
	private OnClickListener postMovementsListener= new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent=new Intent(MainActivity.this, PostMovementsService.class);
			startService(intent);
			
		}
	};
	
	private OnClickListener putMovementsListener= new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent=new Intent(MainActivity.this, PutMovementsService.class);
			startService(intent);
			
		}
	};
	private OnClickListener newActivityListener =new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Intent intent=new Intent(MainActivity.this,SgfEntryActivity.class);
			startActivity(intent);
			
		}
	};


	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        if(savedInstanceState!=null){
        	
        	Log.d("SGFMobile", savedInstanceState.getString("SGFLingua"));
        }
        

        textView= (TextView) findViewById(R.id.textViewMain);
        textViewTotal = (TextView) findViewById(R.id.textViewTotal);
        Button btn = (Button) findViewById(R.id.button1);
        Button btnTotal = (Button) findViewById(R.id.totalButton);
        
        btn.setOnClickListener(buttonListener);
        btnTotal.setOnClickListener(totalListener);
        
        Button helpButton= (Button) findViewById(R.id.helpButton);
        helpButton.setOnClickListener(helpButtonListener);

        Button listButton = (Button) findViewById(R.id.ListButton);
        listButton.setOnClickListener(listButtonListener);
        
        Button btnGetMovements= (Button) findViewById(R.id.button2);
        btnGetMovements.setOnClickListener(getMovementsListener);
        
        Button postMovBtn= (Button) findViewById(R.id.PostMovements);
        postMovBtn.setOnClickListener(postMovementsListener);
        
        Button btnMainEntry= (Button) findViewById(R.id.NewActivity);
        btnMainEntry.setOnClickListener(newActivityListener);
        
        
        Button putMovBtn= (Button) findViewById(R.id.PutMovements);
        putMovBtn.setOnClickListener(putMovementsListener);
        
 
        
        
        
    }
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putString("SGFLingua", "PT");
		super.onSaveInstanceState(outState);
		
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
