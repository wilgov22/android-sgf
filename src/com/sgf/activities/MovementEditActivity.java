package com.sgf.activities;

import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.sgf.db.MovementsDAO;
import com.sgf.db.SGFDataModelSQLiteHelper;
import com.sgf.entities.Movement;
import com.sgf.activities.R;


public class MovementEditActivity extends Activity implements
		OnItemSelectedListener {
	private EditText etxt;
	private EditText amount;
	private EditText description;

	private int _year;
	private int _month;
	private int _day;

	private Spinner spinnerMov;
	private Spinner spinnerCat;
	private Spinner spinnerSubCat;
	private Spinner spinnerAccount;

	private EditText date;
	private TextView movementID;

	private int spinnerCount = 3;
	private volatile int selectedListenerCount = 0;

	// Contem os dados do MovimentoApresentado
	private ContentValues currentMovement = null;

	private OnItemSelectedListener spinnerListener = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int pos,
				long id) {
			if (selectedListenerCount == 3) {
				switch (parent.getId()) {
				case (R.id.spinner3):

					Log.d("MovementInsertActivity",
							"Spinner de Mov_TYPE seleccionado");
					setSpinnerCatType(id);
					break;
				case (R.id.spinner4):
					Log.d("MovementInsertActivity",
							"Spinner de CAT_TYPE seleccionado");

					setSpinnerSubCatType(id);
					break;

				}
				// TODO Auto-generated method stub
			} else {
				selectedListenerCount++;

			}

		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub

		}
	};
	private OnItemSelectedListener spinnerCatListener;
	private OnItemSelectedListener spinnerSubCatListener;

	private volatile boolean userSelecting = true;

	private OnClickListener saveMovement = new OnClickListener() {

		@Override
		public void onClick(View v) {
			String TAG = "UPDATE_LOCALLY";
			Movement mv = new Movement();
			// read values
			Long idMov = spinnerMov.getSelectedItemId();
			Long idCat = spinnerCat.getSelectedItemId();
			Long idSubCat = spinnerSubCat.getSelectedItemId();
			Long idAccount = spinnerAccount.getSelectedItemId();
			Double _amount = Double.parseDouble(amount.getText().toString());
			String _description = description.getText().toString();
			etxt = (EditText) findViewById(R.id.etxt_date);
			if (idCat < 0 || (idSubCat > 0 && idCat < 0)) {
				Log.d("SGFAPP", "ERRO INSERTING Mov_id=" + idMov + " cat_id="
						+ idCat + " subcat_id=" + idSubCat);
			} else {
				Log.d("SGFAPP", "INSERTING Mov_id=" + idMov + " cat_id="
						+ idCat + " subcat_id=" + idSubCat);
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


			mv.setMov_amount(_amount);
			mv.setMov_date(etxt.getText().toString());
			mv.setMov_desc(_description);
			mv.set_id(movementID.getText().toString());
			mv.setMov_account_id(idAccount.toString());

			mv.setMov_state(MovementsDAO.getNextState(
					currentMovement
							.getAsString(SGFDataModelSQLiteHelper.COLUMN_MOVEMENT_STATE),
					TAG));

			MovementsDAO mdao = new MovementsDAO(getApplicationContext());
			long res = mdao.update(mv);
			if (res == -1) {
				Log.d("SGFAPP", "ERRO Writing to DB Mov_id=" + idMov
						+ " cat_id=" + idCat + " subcat_id=" + idSubCat);
				Toast toast = Toast.makeText(getApplicationContext(),
						"ERROR: Movement Not Saved", Toast.LENGTH_SHORT);
				toast.show();
			} else {

				Log.d("SGFAPP", "SUCESS Writing to DB Mov_id=" + idMov
						+ " cat_id=" + idCat + " subcat_id=" + idSubCat);
				Toast toast = Toast.makeText(getApplicationContext(),
						"Movement Saved", Toast.LENGTH_SHORT);
				toast.show();
				// startActivity(new Intent(getApplicationContext(),
				// MainActivity.class));

			}

		}
	};

	static final int DATE_DIALOG_ID = 111;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.expense_edit_layout);

		// getting the intent
		Intent int_ = getIntent();
		Bundle extras = int_.getExtras();
		currentMovement = (ContentValues) extras.get("Movement");

		// loading widgets
		amount = (EditText) findViewById(R.id.etAmount);
		description = (EditText) findViewById(R.id.etDescription);
		date = (EditText) findViewById(R.id.etxt_date);
		Button saveButton = (Button) findViewById(R.id.button1);
		spinnerAccount = (Spinner) findViewById(R.id.spinner1);
		spinnerMov = (Spinner) findViewById(R.id.spinner3);
		spinnerCat = (Spinner) findViewById(R.id.spinner4);
		spinnerSubCat = (Spinner) findViewById(R.id.spinner5);
		movementID = (TextView) findViewById(R.id.mov_id_tv);

		// preparing widgets
		setCurrentDate();
		setDatePickerListener();
		saveButton.setOnClickListener(saveMovement);

		int mov_id = currentMovement
				.getAsInteger(SGFDataModelSQLiteHelper.COLUMN_MOV_TYPE_ID_FK);
		// spinnerMov.setSelection(mov_id-1);

		setSpinnerMovType();

		int cat_id = currentMovement
				.getAsInteger(SGFDataModelSQLiteHelper.COLUMN_MOVEMENT_CATEGORY);
		// spinnerCat.setSelection(cat_id-1);

		setSpinnerCatType(mov_id);

		int sub_cat_id = -1;
		try {
			sub_cat_id = currentMovement
					.getAsInteger(SGFDataModelSQLiteHelper.COLUMN_MOVEMENT_SUB_CATEGORY);
		} catch (Exception e) {
			// Só para não estoirar, assim fica sub_cat_id=-1
		}
		// spinnersubCat.setSelection(sub_cat_id-1);

		setSpinnerSubCatType(cat_id);

		fillSpinnerAccount();
		int account_id = currentMovement
				.getAsInteger(SGFDataModelSQLiteHelper.COLUMN_MOVEMENT_ACCOUNT_ID);

		spinnerAccount
				.setSelection(getPosition(((SimpleCursorAdapter) spinnerAccount
						.getAdapter()).getCursor(),
						SGFDataModelSQLiteHelper.COLUMN_ACCOUNT_ID, account_id));

		spinnerMov.setSelection(getPosition(
				((SimpleCursorAdapter) spinnerMov.getAdapter()).getCursor(),
				SGFDataModelSQLiteHelper.COLUMN_MOVEMENT_ID, mov_id));
		spinnerCat.setSelection(getPosition(
				((SimpleCursorAdapter) spinnerCat.getAdapter()).getCursor(),
				SGFDataModelSQLiteHelper.COLUMN_CATEGORY_ID, cat_id));
		spinnerSubCat.setSelection(getPosition(
				((SimpleCursorAdapter) spinnerSubCat.getAdapter()).getCursor(),
				SGFDataModelSQLiteHelper.COLUMN_SUB_CATEGORY_ID, sub_cat_id));

		// spinnerMov.setOnItemSelectedListener(spinnerListener);
		// spinnerCat.setOnItemSelectedListener(spinnerListener);
		// spinnerSubCat.setOnItemSelectedListener(spinnerListener);

		spinnerMov.setOnItemSelectedListener(this);
		spinnerCat.setOnItemSelectedListener(this);
		spinnerSubCat.setOnItemSelectedListener(this);
		spinnerAccount.setOnItemSelectedListener(this);

		// setting values into widgets
		date.setText(currentMovement
				.getAsString(SGFDataModelSQLiteHelper.COLUMN_MOVEMENT_DATE));
		movementID.setText(currentMovement
				.getAsString(SGFDataModelSQLiteHelper.COLUMN_MOVEMENT_ID));
		description.setText(currentMovement
				.getAsString(SGFDataModelSQLiteHelper.COLUMN_MOVEMENT_DESC));
		amount.setText((currentMovement
				.getAsString(SGFDataModelSQLiteHelper.COLUMN_MOVEMENT_AMOUNT)));

	}

	// metodo helper para devolver a posição correcta do spinner tendo em conta
	// o cursor do spinner, o nome da coluna, e o ID
	private int getPosition(Cursor c, String columnName, int columnId) {
		int position = 0;
		String tmp;
		c.moveToFirst();
		for (int i = 0; i < c.getCount(); i++) {
			tmp = c.getString(c.getColumnIndex(columnName));
			if (c.getString(c.getColumnIndex(columnName)).equals(
					Integer.toString(columnId))) {
				position = i;
			}
			c.moveToNext();

		}
		return position;
	}

	private void fillSpinnerAccount() {
		// TODO Auto-generated method stub

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

	private void setSpinnerMovType() {
		Spinner spinner = (Spinner) findViewById(R.id.spinner3);
		SGFDataModelSQLiteHelper lite = new SGFDataModelSQLiteHelper(this,
				"teste", null, 1);
		String[] cols = new String[] {
				SGFDataModelSQLiteHelper.COLUMN_MOVEMENT_ID,
				SGFDataModelSQLiteHelper.COLUMN_MOVEMENT_ID,
				SGFDataModelSQLiteHelper.COLUMN_MOVEMENT_DESCRIPTION };
		Cursor c = lite.getReadableDatabase().query(
				SGFDataModelSQLiteHelper.TABLE_MOVEMENT_TYPE, cols, null, null,
				null, null, null);

		String[] columns = new String[] { SGFDataModelSQLiteHelper.COLUMN_MOVEMENT_DESCRIPTION };
		int[] to = new int[] { android.R.id.text1 };
		SimpleCursorAdapter movementsAdapter = new SimpleCursorAdapter(this,
				android.R.layout.simple_spinner_item, c, columns, to, 0);
		movementsAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(movementsAdapter);
		lite.close();
		// spinner.setOnItemSelectedListener(this);
		// c.close();

	}

	private void setSpinnerCatType(long mov_type_id) {
		Spinner spinner = (Spinner) findViewById(R.id.spinner4);
		SGFDataModelSQLiteHelper lite = new SGFDataModelSQLiteHelper(this,
				"teste", null, 1);
		String[] cols = new String[] {
				SGFDataModelSQLiteHelper.COLUMN_CATEGORY_ID,
				SGFDataModelSQLiteHelper.COLUMN_CATEGORY_ID,
				SGFDataModelSQLiteHelper.COLUMN_CATEGORT_DESCRIPTION };
		String _where = SGFDataModelSQLiteHelper.COLUMN_MOV_TYPE_ID_FK + "="
				+ mov_type_id;
		Cursor c = lite.getReadableDatabase().query(
				SGFDataModelSQLiteHelper.TABLE_CATEGORY, cols, _where, null,
				null, null, null);

		String[] columns = new String[] { SGFDataModelSQLiteHelper.COLUMN_CATEGORT_DESCRIPTION };
		int[] to = new int[] { android.R.id.text1 };
		SimpleCursorAdapter categoriesAdapter = new SimpleCursorAdapter(this,
				android.R.layout.simple_spinner_item, c, columns, to, 0);
		categoriesAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(categoriesAdapter);
		lite.close();

		// spinner.setOnItemSelectedListener(this);
		// c.close();

	}

	private void setSpinnerSubCatType(long cat_type_id) {
		Spinner spinner = (Spinner) findViewById(R.id.spinner5);
		SGFDataModelSQLiteHelper lite = new SGFDataModelSQLiteHelper(this,
				"teste", null, 1);
		String[] cols = new String[] {
				SGFDataModelSQLiteHelper.COLUMN_SUB_CATEGORY_ID,
				SGFDataModelSQLiteHelper.COLUMN_SUB_CATEGORY_ID,
				SGFDataModelSQLiteHelper.COLUMN_SUB_CATEGORT_DESCRIPTION };
		String _where = SGFDataModelSQLiteHelper.COLUMN_CATEGORY_TYPE_ID_FK
				+ "=" + cat_type_id;
		Cursor c = lite.getReadableDatabase().query(
				SGFDataModelSQLiteHelper.TABLE_SUB_CATEGORY, cols, _where,
				null, null, null, null);

		String[] columns = new String[] { SGFDataModelSQLiteHelper.COLUMN_SUB_CATEGORT_DESCRIPTION };
		int[] to = new int[] { android.R.id.text1 };
		SimpleCursorAdapter categoriesAdapter = new SimpleCursorAdapter(this,
				android.R.layout.simple_spinner_item, c, columns, to, 0);
		categoriesAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(categoriesAdapter);
		lite.close();

		// spinner.setOnItemSelectedListener(this);
		// c.close();

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
				.append(_month + 1).append("-").append(_year));

	}

	private void setDatePickerListener() {

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
			_dialog = new DatePickerDialog(this, datePickerListener, _year,
					_month, _day);

			break;

		default:
			_dialog = null;
			break;
		}
		return _dialog;
	}

	private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
			// TODO Auto-generated method stub
			_year = arg1;
			_month = arg2;
			_day = arg3;

			etxt.setText(new StringBuilder().append(_day).append("-")
					.append(_month + 1).append("-").append(_year));

		}
	};

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int pos,
			long id) {
		if (selectedListenerCount == 3) {
			switch (parent.getId()) {
			case (R.id.spinner3):

				Log.d("MovementInsertActivity",
						"Spinner de Mov_TYPE seleccionado");
				setSpinnerCatType(id);
				break;
			case (R.id.spinner4):
				Log.d("MovementInsertActivity",
						"Spinner de CAT_TYPE seleccionado");

				setSpinnerSubCatType(id);
				break;

			}
			// TODO Auto-generated method stub
		} else {
			selectedListenerCount++;

		}
		// TODO Auto-generated method stub

	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}

}
