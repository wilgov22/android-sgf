package com.sgf.activities;

import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.sgf.db.MovementsDAO;
import com.sgf.db.SGFDataModelSQLiteHelper;
import com.sgf.entities.Movement;
import com.sgf.activities.R;


public class MovementInsertActivity extends Activity implements OnItemSelectedListener {
	private EditText etxt;
	private EditText amount;
	private EditText description;
	
	private int _year;
	private int _month;
	private int _day;
	
	private Spinner accountSpinner=null;

	private OnClickListener saveMovement =new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Movement mv=new Movement();
			//read values
			Spinner spinnerMov=(Spinner) findViewById(R.id.spinner3);
			Spinner spinnerCat=(Spinner) findViewById(R.id.spinner4);
			Spinner spinnerSubCat=(Spinner) findViewById(R.id.spinner5);
			Long idMov= spinnerMov.getSelectedItemId();
			Long idCat= spinnerCat.getSelectedItemId();
			
			Long idSubCat= spinnerSubCat.getSelectedItemId();
			
			Long idAccount = accountSpinner.getSelectedItemId();
			
			Double _amount=Double.parseDouble(amount.getText().toString());
			String _description=description.getText().toString();
			etxt = (EditText) findViewById(R.id.etxt_date);
			if(idCat < 0 || (idSubCat>0 && idCat <0 )){
				Log.d("SGFAPP","ERRO INSERTING Mov_id=" + idMov + " cat_id=" + idCat + " subcat_id=" + idSubCat);
			}else {
				Log.d("SGFAPP","INSERTING Mov_id=" + idMov + " cat_id=" + idCat + " subcat_id=" + idSubCat);
			}
			if(idCat<0) {
				idCat=null;
				mv.setMov_cat_id(null);
			}else {mv.setMov_cat_id(String.valueOf(idCat));}

			
			if(idSubCat<0) {
				idSubCat=null;
				mv.setMov_sub_cat_id(null);
			}else {mv.setMov_sub_cat_id(String.valueOf(idSubCat));}
			
			mv.setMov_type_id(String.valueOf(idMov));
			mv.setMov_state("N");
			mv.setMov_amount(_amount);
			mv.setMov_date(etxt.getText().toString());
			mv.setMov_desc(_description);
			mv.setMov_account_id(String.valueOf(idAccount));
			
			MovementsDAO mdao=new MovementsDAO(getApplicationContext());
			long res=mdao.create(mv);
			if (res == -1){
				Log.d("SGFAPP","ERRO Writing to DB Mov_id=" + idMov + " cat_id=" + idCat + " subcat_id=" + idSubCat);
				Toast toast= Toast.makeText(getApplicationContext(), "ERROR: Movement Not Saved", Toast.LENGTH_SHORT);
				toast.show();
			}else {
				
				Log.d("SGFAPP","SUCESS Writing to DB Mov_id=" + idMov + " cat_id=" + idCat + " subcat_id=" + idSubCat);
				Toast toast= Toast.makeText(getApplicationContext(), "Movement Saved", Toast.LENGTH_SHORT);
				toast.show();
				
			}
			
		}
	};
	
	static final int DATE_DIALOG_ID = 111;



	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.expense_layout);
		amount=(EditText) findViewById(R.id.etAmount);
		description=(EditText) findViewById(R.id.etDescription);
		setCurrentDate();
		setDatePickerListener();
		Button saveButton= (Button) findViewById(R.id.button1);
		saveButton.setOnClickListener(saveMovement);
		setSpinnerMovType();
		fillSpinnerAccount();
	}
		
		
		
	

	private void fillSpinnerAccount() {
		// TODO Auto-generated method stub
		accountSpinner = (Spinner) findViewById(R.id.spinner1);
		SGFDataModelSQLiteHelper lite=new SGFDataModelSQLiteHelper(this,"teste", null, 1);
        String [] cols=new String []{SGFDataModelSQLiteHelper.COLUMN_ACCOUNT_ID,SGFDataModelSQLiteHelper.COLUMN_ACCOUNT_NAME,SGFDataModelSQLiteHelper.COLUMN_ACCOUNT_SGF_ID};
        Cursor c= lite.getReadableDatabase().query(SGFDataModelSQLiteHelper.TABLE_ACCOUNTS, cols, null, null, null, null, null);
        
        String [] columns= new String [] { SGFDataModelSQLiteHelper.COLUMN_ACCOUNT_NAME ,SGFDataModelSQLiteHelper.COLUMN_ACCOUNT_SGF_ID };
        int [] to = new int[] {android.R.id.text1};
        SimpleCursorAdapter accountAdapter=new SimpleCursorAdapter(this, android.R.layout.simple_spinner_item, c, columns, to,0);
        accountAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        accountSpinner.setAdapter(accountAdapter);
        accountSpinner.setOnItemSelectedListener(this);
        lite.close();
		
	}





	private void setSpinnerMovType(){
		Spinner spinner=(Spinner) findViewById(R.id.spinner3);
        SGFDataModelSQLiteHelper lite=new SGFDataModelSQLiteHelper(this,"teste", null, 1);
        String [] cols=new String []{SGFDataModelSQLiteHelper.COLUMN_MOVEMENT_ID,SGFDataModelSQLiteHelper.COLUMN_MOVEMENT_ID,SGFDataModelSQLiteHelper.COLUMN_MOVEMENT_DESCRIPTION};
        Cursor c= lite.getReadableDatabase().query(SGFDataModelSQLiteHelper.TABLE_MOVEMENT_TYPE, cols, null, null, null, null, null);
        
        String [] columns= new String [] { SGFDataModelSQLiteHelper.COLUMN_MOVEMENT_DESCRIPTION};
        int [] to = new int[] {android.R.id.text1};
        SimpleCursorAdapter movementsAdapter=new SimpleCursorAdapter(this, android.R.layout.simple_spinner_item, c, columns, to,0);
        movementsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(movementsAdapter);
        spinner.setOnItemSelectedListener(this);
        lite.close();
        //c.close();
        
		
	}
	private void setSpinnerCatType(long mov_type_id){
		Spinner spinner=(Spinner) findViewById(R.id.spinner4);
        SGFDataModelSQLiteHelper lite=new SGFDataModelSQLiteHelper(this,"teste", null, 1);
        String [] cols=new String []{SGFDataModelSQLiteHelper.COLUMN_CATEGORY_ID,SGFDataModelSQLiteHelper.COLUMN_CATEGORY_ID,SGFDataModelSQLiteHelper.COLUMN_CATEGORT_DESCRIPTION};
        String _where= SGFDataModelSQLiteHelper.COLUMN_MOV_TYPE_ID_FK+"="+mov_type_id;
        Cursor c= lite.getReadableDatabase().query(SGFDataModelSQLiteHelper.TABLE_CATEGORY, cols, _where, null, null, null, null);
        
        String [] columns= new String [] { SGFDataModelSQLiteHelper.COLUMN_CATEGORT_DESCRIPTION};
        int [] to = new int[] {android.R.id.text1};
        SimpleCursorAdapter categoriesAdapter=new SimpleCursorAdapter(this, android.R.layout.simple_spinner_item, c, columns, to,0);
        categoriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(categoriesAdapter);
        spinner.setOnItemSelectedListener(this);
        lite.close();
       // c.close();
        
		
	}
	
	private void setSpinnerSubCatType(long cat_type_id){
		Spinner spinner=(Spinner) findViewById(R.id.spinner5);
        SGFDataModelSQLiteHelper lite=new SGFDataModelSQLiteHelper(this,"teste", null, 1);
        String [] cols=new String []{SGFDataModelSQLiteHelper.COLUMN_SUB_CATEGORY_ID,SGFDataModelSQLiteHelper.COLUMN_SUB_CATEGORY_ID,SGFDataModelSQLiteHelper.COLUMN_SUB_CATEGORT_DESCRIPTION};
        String _where= SGFDataModelSQLiteHelper.COLUMN_CATEGORY_TYPE_ID_FK+"="+cat_type_id;
        Cursor c= lite.getReadableDatabase().query(SGFDataModelSQLiteHelper.TABLE_SUB_CATEGORY, cols, _where, null, null, null, null);
        
        String [] columns= new String [] { SGFDataModelSQLiteHelper.COLUMN_SUB_CATEGORT_DESCRIPTION};
        int [] to = new int[] {android.R.id.text1};
        SimpleCursorAdapter categoriesAdapter=new SimpleCursorAdapter(this, android.R.layout.simple_spinner_item, c, columns, to,0);
        categoriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(categoriesAdapter);
        spinner.setOnItemSelectedListener(this);
        lite.close();
        //c.close();
		
	}
	

	private void setCurrentDate() {
		// get current date
		etxt = (EditText) findViewById(R.id.etxt_date);
		Calendar c = Calendar.getInstance();
		_year = c.get(Calendar.YEAR);
		_month = c.get(Calendar.MONTH);
		_day = c.get(Calendar.DAY_OF_MONTH);

		// set current date on edit text
		etxt.setText(new StringBuilder().append(_day).append("-")
				.append(_month+1).append("-").append(_year));

	}
	private void setDatePickerListener(){
		
		etxt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showDialog(DATE_DIALOG_ID);
			}
		});
	}
	
	
	@Override
	protected Dialog onCreateDialog(int id) {
		Dialog _dialog;
		switch (id) {
		case DATE_DIALOG_ID:
			_dialog=new DatePickerDialog(this, datePickerListener , _year, _month, _day);
			
			break;

		default:
			_dialog=null;
			break;
		}
		return _dialog;
	}
	
	private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
		
		@Override
		public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
			// TODO Auto-generated method stub
			_year=arg1;
			_month=arg2;
			_day=arg3;
			
			etxt.setText(new StringBuilder().append(_day).append("-")
					.append(_month+1).append("-").append(_year));

			
		}
	};




	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int pos,
			long id) {
		// TODO Auto-generated method stub
		switch (parent.getId()){
		case (R.id.spinner3):
			Log.d("MovementInsertActivity", "Spinner de Mov_TYPE seleccionado");
			setSpinnerCatType(id);
			break;
		case (R.id.spinner4):
			Log.d("MovementInsertActivity", "Spinner de CAT_TYPE seleccionado");
			
			setSpinnerSubCatType(id);
			break;
			
		}

		
		Object c= parent.getItemAtPosition(pos).toString();
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
}
