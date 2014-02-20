package com.sgf.activities;

import java.util.ArrayList;
import java.util.Map;

import android.app.ListActivity;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;
import android.widget.TextView;

import com.sgf.custom.CategoryTotalAdapter;
import com.sgf.db.MovementsDAO;
import com.sgf.db.SGFDataModelSQLiteHelper;
import com.sgf.entities.CategoryTotal;

/**
 * @author wgovindji
 * 
 */

public class ListAccountSummaryActivity extends ListActivity implements
		OnItemSelectedListener {
	ArrayList<CategoryTotal> _arr;

	private Spinner spinnerAccount;
	private TextView totalIncomeAmount;
	private TextView totalExpenseAmount;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.list_account_summary_layout);
		spinnerAccount = (Spinner) findViewById(R.id.spinner1);
		totalIncomeAmount= (TextView) findViewById(R.id.totalIncomeAmount);
		totalExpenseAmount = (TextView) findViewById(R.id.totalExpenseAmount);

		this.getListView().setBackgroundColor(Color.BLACK);

		fillSpinnerAccount();

	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Log.d("SgfApp", "OnStart called");

		Log.d("SGFAPP", "All Movements from DB");
		getTotalsCategoryByAccountId();

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
	private void getTotalsCategoryByAccountId() {
		Long accountId = spinnerAccount.getSelectedItemId();
		MovementsDAO mdao = new MovementsDAO(getApplicationContext());
		boolean computeCategories=false;
		
		
		//First Compute Totals 
		Map<String, Double> totals=mdao.getTotalsOfMovementTypesByAccountId(accountId.toString());
		if(totals.containsKey("Income")){
			totalIncomeAmount.setText(totals.get("Income").toString());
			computeCategories=true;
		}else {
			totalIncomeAmount.setText("0");
		}
		
		if(totals.containsKey("Expense")){
			totalExpenseAmount.setText(totals.get("Expense").toString());
			computeCategories=true;
		}else {
			totalExpenseAmount.setText("0");
		}
		

		_arr=new ArrayList<CategoryTotal>();
		//Then compute each category
		if(computeCategories){
		_arr = mdao.getCategoryTotal(accountId.toString());
		}

		CategoryTotalAdapter adapter = new CategoryTotalAdapter(
				getApplicationContext(), _arr);
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
			getTotalsCategoryByAccountId();
			break;

		default:
			break;
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}

}
