package com.sgf.activities;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ListActivity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;
import android.widget.Toast;

import com.sgf.api.services.GetMovementsService;
import com.sgf.custom.MovementAdapter;
import com.sgf.db.MovementsDAO;
import com.sgf.db.SGFDataModelSQLiteHelper;
import com.sgf.entities.Movement;


/**
 * @author wgovindji
 * 
 */

public class ListMovementsActivity extends ListActivity implements
		OnItemSelectedListener {
	ArrayList<Movement> _arr;
	private long movToDeleteId = -1;

	private Spinner spinnerAccount;
	
	private Handler handleGetMovementsResult;
	private OnItemClickListener onItemListener = new OnItemClickListener() {

		// ////
		// LISTENERS
		// ////
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long id) {
			// TODO Auto-generated method stub

			ContentValues cv = MovementsDAO.toContentValues(_arr.get(position));

//			Toast.makeText(getApplicationContext(),
//					"Position=" + position + " Id=" + id, Toast.LENGTH_SHORT)
//					.show();

			Intent intent = new Intent(getApplicationContext(),
					MovementEditActivity.class);
			intent.putExtra("Movement", cv);
			startActivity(intent);

		}
	};
	protected OnClickListener alertDeleteNoListener = new OnClickListener() {

		@Override
		public void onClick(DialogInterface arg0, int arg1) {
			// TODO Auto-generated method stub
//			Toast.makeText(getApplicationContext(),
//					" nNao Apagado o movimento " + movToDeleteId,
//					Toast.LENGTH_SHORT).show();

		}
	};
	private OnClickListener alertDeleteYesListener = new OnClickListener() {

		@Override
		public void onClick(DialogInterface arg0, int arg1) {
			// TODO Auto-generated method stub
			int res=-100;
			MovementsDAO mdao = new MovementsDAO(getApplicationContext());
			mdao.open();
			Movement movementToDelete=mdao.retrive(movToDeleteId);
			String TAG= "DELETE_LOCALLY";
			String nextState= mdao.getNextState(movementToDelete.getMov_state(), TAG);
			//decidir se é um movimento novo ou sincronizado de modo a decidir se é apagado da BD ou update para dirty
			if(nextState.equals("X")){
				res = mdao.delete(movToDeleteId);
			}else if(nextState.equals("E")){
				movementToDelete.setMov_state(nextState);
				res = mdao.update(movementToDelete);
			}
			
			
			
			
		

			mdao.close();
			movToDeleteId = -1;
			
//			Toast.makeText(getApplicationContext(),
//					"Apagado o movimento " + movToDeleteId, Toast.LENGTH_SHORT)
//					.show();
			if (res == 1) {

				//startActivity(new Intent(getApplicationContext(),	MainActivity.class));
				getMovementsByAccountID();
			}

		}
	};
	private OnItemLongClickListener onItemLongListener = new OnItemLongClickListener() {

		@Override
		public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
				int position, long id) {
			// TODO Auto-generated method stub

//			Toast.makeText(getApplicationContext(),
//					"OnItemLongClick : Position=" + position + " Id=" + id,
//					Toast.LENGTH_SHORT).show();

			/*
			 * AlertDialog deleteDialog= new
			 * AlertDialog.Builder(ListMovementsActivity.this).create();
			 * deleteDialog.setTitle("Delete Movement");
			 * deleteDialog.setMessage(
			 * "Do you really want to delete this movement? There is no way back after this... :"
			 * ); deleteDialog.show();
			 */

			movToDeleteId = id;
			Builder builder = new AlertDialog.Builder(
					ListMovementsActivity.this);
			builder.setTitle("Delete Movement");
			builder.setMessage("Do you really want to delete this movement?");
			builder.setCancelable(true);
			builder.setPositiveButton("Yes", alertDeleteYesListener);
			builder.setNegativeButton("No", alertDeleteNoListener);
			AlertDialog dialog = builder.create();
			dialog.show();
			return false;
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.list_movements_layout);
		spinnerAccount = (Spinner) findViewById(R.id.spinner1);

		this.getListView().setBackgroundColor(Color.BLACK);

		// when the user click over an item in the list
		this.getListView().setOnItemClickListener(onItemListener);

		// when user long click over an item in the list
		this.getListView().setOnItemLongClickListener(onItemLongListener);

		fillSpinnerAccount();
		
		handleGetMovementsResult= new Handler(){
			
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				
			}
		};
		

	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Log.d("SgfApp", "OnStart called");

		Log.d("SGFAPP", "All Movements from DB");
		getMovementsByAccountID();

	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		Log.d("SGFAPP", "OnRestartCalled");

	}

	/**
	 * Este metodo carrega a lista de contas de modo a popular o spinner,
	 * prepara o spinner fornecendo o adapter para apresentar os valores quando
	 * seleccionado
	 */
	private void fillSpinnerAccount() {
		// TODO Auto-generated method stub
		spinnerAccount = (Spinner) findViewById(R.id.spinner1);
		SGFDataModelSQLiteHelper lite = new SGFDataModelSQLiteHelper(this,
				"teste", null, 1);
		String[] cols = new String[] {
				SGFDataModelSQLiteHelper.COLUMN_ACCOUNT_ID,
				SGFDataModelSQLiteHelper.COLUMN_ACCOUNT_NAME,
				SGFDataModelSQLiteHelper.COLUMN_ACCOUNT_SGF_ID };
		Cursor c = lite.getReadableDatabase().query(
				SGFDataModelSQLiteHelper.TABLE_ACCOUNTS, cols, null, null,
				null, null, null);

		String[] columns = new String[] {
				SGFDataModelSQLiteHelper.COLUMN_ACCOUNT_NAME,
				SGFDataModelSQLiteHelper.COLUMN_ACCOUNT_SGF_ID };
		int[] to = new int[] { android.R.id.text1 };
		SimpleCursorAdapter accountAdapter = new SimpleCursorAdapter(this,
				android.R.layout.simple_spinner_item, c, columns, to, 0);
		accountAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerAccount.setAdapter(accountAdapter);
		spinnerAccount.setOnItemSelectedListener(this);
		lite.close();

	}

	/**
	 * Este metodo faz com que a listView seja actualizada com base no
	 * account_id seleccionado
	 */
	private void getMovementsByAccountID() {
		Long accountId = spinnerAccount.getSelectedItemId();
		String selection = "and "
				+ SGFDataModelSQLiteHelper.COLUMN_MOVEMENT_ACCOUNT_ID + "='"
				+ accountId + "' and "+ SGFDataModelSQLiteHelper.COLUMN_MOVEMENT_STATE + " in ('N','S','D') ";
		_arr = (ArrayList<Movement>) new MovementsDAO(getApplicationContext())
				.getAllMovements(selection);
		MovementAdapter adapter = new MovementAdapter(getApplicationContext(),
				_arr);
		setListAdapter(adapter);

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.d("SgfApp", "OnResume called");
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int pos,
			long id) {
		// TODO Auto-generated method stub
		switch (parent.getId()) {
		case (R.id.spinner1):
			Log.d("ListMovementsActivity",
					"Spinner de ACCOUNT_ID seleccionado e alterado o valor");
			getMovementsByAccountID();
			break;

		default:
			break;
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.list_movements_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.api_get_movements:
			// Ler qual a conta seleccionada e mandar executar service
			Long accountId = spinnerAccount.getSelectedItemId();
			if (accountId == 1) {
				Toast.makeText(getApplicationContext(),
						"Local Account can't be synched", Toast.LENGTH_SHORT)
						.show();
			} else {
				
				Intent intent = new Intent(getApplicationContext(),
						GetMovementsService.class);
				
				intent.putExtra("localAccountID", accountId.toString());
				Toast.makeText(getApplicationContext(),
						"Movements downloading in progress....", Toast.LENGTH_SHORT)
						.show();
				startService(intent);
			}
			break;
		case R.id.helpButton:
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	
}
