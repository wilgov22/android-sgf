package com.sgf.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * @author wgovindji
 * Esta classe é responsavel pela criação da BD SQLite quando ela não existe
 * ou efectuar a sua actualização quando a versão é incrementada.
 *
 */
public class SGFDataModelSQLiteHelper extends SQLiteOpenHelper {
	
	public static final String DATABASE_NAME="sgf.db";
	private static final int DATABASE_VERSION=15;


	
	
	
	//Table
	public static final String TABLE_MOVEMENT_TYPE="sgf_r_movement_type";
	//Columns
	public static final String COLUMN_MOVEMENT_ID="_id";
	//public static final String COLUMN_MOVEMENT_TYPE_ID="movement_type_id";
	public static final String COLUMN_MOVEMENT_DESCRIPTION="movement_description";
	public static final String COLUMN_MOVEMENT_ACTIVE="movement_active";
	//Table Create statement
	private static final String TABLE_MOVEMENT_TYPE_CREATE="create table "
			+ TABLE_MOVEMENT_TYPE +  " ("+ COLUMN_MOVEMENT_ID
			+ " integer primary key , "  + COLUMN_MOVEMENT_DESCRIPTION
			+ " text not null," + COLUMN_MOVEMENT_ACTIVE
			+ " text not null"
			+ ");";
	
	
	//Table
	public static final String TABLE_CATEGORY="sgf_r_category";
	//Columns
	public static final String COLUMN_CATEGORY_ID="_id";
	public static final String COLUMN_MOV_TYPE_ID_FK="mov_type_id";
	//public static final String COLUMN_CATEGORY_TYPE_ID="cat_type_id";
	public static final String COLUMN_CATEGORT_DESCRIPTION="cat_description";
	public static final String COLUMN_CATEGORT_ACTIVE="cat_active";
	//Table Create statement
	private static final String TABLE_CATEGORY_CREATE="create table "
			+ TABLE_CATEGORY +  " ("+ COLUMN_CATEGORY_ID
			+ " integer primary key , "  + COLUMN_CATEGORT_DESCRIPTION
			+ " text not null," + COLUMN_MOV_TYPE_ID_FK
			+ " text not null," + COLUMN_CATEGORT_ACTIVE
			+ " text not null,"  + "FOREIGN KEY(" + COLUMN_MOV_TYPE_ID_FK + ") REFERENCES " + TABLE_MOVEMENT_TYPE + "(" +  COLUMN_MOVEMENT_ID + ")"
			+ ");";
	
	//Table
	public static final String TABLE_SUB_CATEGORY="sgf_r_sub_category";
	//Columns
	public static final String COLUMN_SUB_CATEGORY_ID="_id";
	//public static final String COLUMN_SUB_CATEGORY_TYPE_ID="sub_cat_type_id";
	public static final String COLUMN_CATEGORY_TYPE_ID_FK="cat_type_id";
	public static final String COLUMN_SUB_CATEGORT_DESCRIPTION="sub_cat_description";
	public static final String COLUMN_SUB_CATEGORY_ACTIVE="sub_cat_active";
	//Table Create statement
	private static final String TABLE_SUB_CATEGORY_CREATE="create table "
			+ TABLE_SUB_CATEGORY +  " ("+ COLUMN_SUB_CATEGORY_ID
			+ " integer primary key , "  + COLUMN_CATEGORY_TYPE_ID_FK
			+ " text not null," + COLUMN_SUB_CATEGORT_DESCRIPTION
			+ " text not null," + COLUMN_SUB_CATEGORY_ACTIVE
			+ " text not null," + "FOREIGN KEY(" + COLUMN_CATEGORY_TYPE_ID_FK + ") REFERENCES " + TABLE_CATEGORY + "(" +  COLUMN_CATEGORY_ID + ")"
			+ ");";
	
	//Table
	public static final String TABLE_MOVEMENTS="sgf_t_movements";
	//Columns
	public static final String COLUMN_MOVEMENTS_ID="_id";
	public static final String COLUMN_MOVEMENTS_SGF_ID="sgf_movement_id";
	public static final String COLUMN_MOVEMENT_TYPE="mov_type_id";
	public static final String COLUMN_MOVEMENT_CATEGORY="mov_cat_id";
	public static final String COLUMN_MOVEMENT_SUB_CATEGORY="mov_sub_cat_id";
	public static final String COLUMN_MOVEMENT_ACCOUNT_ID="mov_account_id";
	public static final String COLUMN_MOVEMENT_AMOUNT="mov_amount";
	public static final String COLUMN_MOVEMENT_DESC="mov_desc";
	public static final String COLUMN_MOVEMENT_DATE="mov_date";
	public static final String COLUMN_MOVEMENT_STATE="mov_state";
	//Table Create statement
	private static final String TABLE_MOVEMENTS_CREATE="create table "
			+ TABLE_MOVEMENTS +  " ("+ COLUMN_MOVEMENTS_ID
			+ " integer primary key autoincrement, " + COLUMN_MOVEMENTS_SGF_ID
			+ " integer ," +COLUMN_MOVEMENT_TYPE
			+ " text ," + COLUMN_MOVEMENT_CATEGORY
			+ " text ," + COLUMN_MOVEMENT_SUB_CATEGORY
			+ " text ," + COLUMN_MOVEMENT_ACCOUNT_ID
			+ " text ," + COLUMN_MOVEMENT_STATE
			+ " text not null," + COLUMN_MOVEMENT_DESC
			+ " text ," + COLUMN_MOVEMENT_AMOUNT
			+ " real ," + COLUMN_MOVEMENT_DATE 
			+ " text not null" 
			//+ "FOREIGN KEY(" + COLUMN_MOVEMENTS_ID + ") REFERENCES " + TABLE_MOVEMENT_TYPE + "(" +  COLUMN_MOVEMENT_ID + ")"
			//+ "FOREIGN KEY(" + COLUMN_MOVEMENT_CATEGORY + ") REFERENCES " + TABLE_CATEGORY + "(" +  COLUMN_CATEGORY_ID + ")"
			//+ "FOREIGN KEY(" + COLUMN_MOVEMENT_SUB_CATEGORY + ") REFERENCES " + TABLE_SUB_CATEGORY + "(" +  COLUMN_SUB_CATEGORY_ID + ")"
			+ ");";

	
	
		//Table Accounts
		public static final String TABLE_ACCOUNTS="sgf_t_accounts";
		//Columns
		public static final String COLUMN_ACCOUNT_ID="_id";
		//sgf_account_id é o ID na aplicacao SGFWEBAPP
		public static final String COLUMN_ACCOUNT_SGF_ID="sgf_account_id";
		public static final String COLUMN_ACCOUNT_NUMBER="account_number";
		public static final String COLUMN_ACCOUNT_NAME="account_name";
		public static final String COLUMN_ACCOUNT_BANK_NAME="account_bank_name";
		public static final String COLUMN_ACCOUNT_BALANCE="account_balance";
		public static final String COLUMN_ACCOUNT_OTHER_INFO="account_other_info";
		public static final String COLUMN_ACCOUNT_ACTIVE="account_active";
		//Table Create statement
		private static final String TABLE_ACCOUNTS_CREATE="create table "
				+ TABLE_ACCOUNTS +  " ("+ COLUMN_ACCOUNT_ID
				+ " integer primary key autoincrement, " + COLUMN_ACCOUNT_SGF_ID
				+ " integer, " + COLUMN_ACCOUNT_NUMBER
				+ " text ," + COLUMN_ACCOUNT_NAME
				+ " text ," + COLUMN_ACCOUNT_BANK_NAME
				+ " text ," + COLUMN_ACCOUNT_OTHER_INFO
				+ " text ," + COLUMN_ACCOUNT_BALANCE
				+ " real ," + COLUMN_ACCOUNT_ACTIVE 
				+ " text " 
				+ ");";


	public SGFDataModelSQLiteHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	/** Metodo chamado pelo Android para criacao da BD
	 * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
	 */
	@Override
	public void onCreate(SQLiteDatabase database) {
		// TODO Auto-generated method stub
		
		database.execSQL(TABLE_MOVEMENT_TYPE_CREATE);
		database.execSQL(TABLE_CATEGORY_CREATE);
		database.execSQL(TABLE_SUB_CATEGORY_CREATE);
		database.execSQL(TABLE_MOVEMENTS_CREATE);
		database.execSQL(TABLE_ACCOUNTS_CREATE);
		insertData(database);

	}

	/** Metodo chamado pelo Android para actualizacao da BD
	 * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase, int, int)
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		Log.w(SGFDataModelSQLiteHelper.class.getName(), "Upgrading database from version " + oldVersion + " to " + newVersion);
		db.execSQL("DROP TABLE IF EXISTS "+ TABLE_MOVEMENTS);
		db.execSQL("DROP TABLE IF EXISTS "+ TABLE_MOVEMENT_TYPE);
		db.execSQL("DROP TABLE IF EXISTS "+ TABLE_CATEGORY);
		db.execSQL("DROP TABLE IF EXISTS "+ TABLE_SUB_CATEGORY);
		db.execSQL("DROP TABLE IF EXISTS "+ TABLE_ACCOUNTS);
		onCreate(db);

	}
		
	/** Metodo executado aquando criacao da BD, para inserir dados de referencia (Conta local, Tipos de Movimentos,
	 * Categorias e Sub-Categorias)
	 * @param database
	 */
	private void insertData(SQLiteDatabase database){
		database.execSQL("INSERT INTO " + TABLE_MOVEMENT_TYPE + "(" + COLUMN_MOVEMENT_ID + "," + COLUMN_MOVEMENT_DESCRIPTION + "," + COLUMN_MOVEMENT_ACTIVE + ") VALUES (1, 'INCOME','Y');");
		database.execSQL("INSERT INTO " + TABLE_MOVEMENT_TYPE + "(" + COLUMN_MOVEMENT_ID + "," + COLUMN_MOVEMENT_DESCRIPTION + "," + COLUMN_MOVEMENT_ACTIVE + ") VALUES (2, 'EXPENSES','Y');");
		database.execSQL("INSERT INTO " + TABLE_MOVEMENT_TYPE + "(" + COLUMN_MOVEMENT_ID + "," + COLUMN_MOVEMENT_DESCRIPTION + "," + COLUMN_MOVEMENT_ACTIVE + ") VALUES (3, 'INCOME TAXES WITHHELD','Y');");

		database.execSQL("INSERT INTO " + TABLE_CATEGORY + "(" + COLUMN_CATEGORY_ID + "," + COLUMN_MOV_TYPE_ID_FK + "," + COLUMN_CATEGORT_DESCRIPTION + "," + COLUMN_CATEGORT_ACTIVE + ") VALUES('1'	,'1',	'Wages and Bonuses', 'Y');");
		database.execSQL("INSERT INTO " + TABLE_CATEGORY + "(" + COLUMN_CATEGORY_ID + "," + COLUMN_MOV_TYPE_ID_FK + "," + COLUMN_CATEGORT_DESCRIPTION + "," + COLUMN_CATEGORT_ACTIVE + ") VALUES('2'	,'1',	'Interest Income', 'Y');");
		database.execSQL("INSERT INTO " + TABLE_CATEGORY + "(" + COLUMN_CATEGORY_ID + "," + COLUMN_MOV_TYPE_ID_FK + "," + COLUMN_CATEGORT_DESCRIPTION + "," + COLUMN_CATEGORT_ACTIVE + ") VALUES('3'	,'1',	'Investment Income', 'Y');");
		database.execSQL("INSERT INTO " + TABLE_CATEGORY + "(" + COLUMN_CATEGORY_ID + "," + COLUMN_MOV_TYPE_ID_FK + "," + COLUMN_CATEGORT_DESCRIPTION + "," + COLUMN_CATEGORT_ACTIVE + ") VALUES('4'	,'1',	'Miscellaneous Income', 'Y');");
		database.execSQL("INSERT INTO " + TABLE_CATEGORY + "(" + COLUMN_CATEGORY_ID + "," + COLUMN_MOV_TYPE_ID_FK + "," + COLUMN_CATEGORT_DESCRIPTION + "," + COLUMN_CATEGORT_ACTIVE + ") VALUES('5'	,'2',	'HOME', 'Y');");
		database.execSQL("INSERT INTO " + TABLE_CATEGORY + "(" + COLUMN_CATEGORY_ID + "," + COLUMN_MOV_TYPE_ID_FK + "," + COLUMN_CATEGORT_DESCRIPTION + "," + COLUMN_CATEGORT_ACTIVE + ") VALUES('6'	,'2',	'UTILITIES', 'Y');");
		database.execSQL("INSERT INTO " + TABLE_CATEGORY + "(" + COLUMN_CATEGORY_ID + "," + COLUMN_MOV_TYPE_ID_FK + "," + COLUMN_CATEGORT_DESCRIPTION + "," + COLUMN_CATEGORT_ACTIVE + ") VALUES('7'	,'2',	'FOOD', 'Y');");
		database.execSQL("INSERT INTO " + TABLE_CATEGORY + "(" + COLUMN_CATEGORY_ID + "," + COLUMN_MOV_TYPE_ID_FK + "," + COLUMN_CATEGORT_DESCRIPTION + "," + COLUMN_CATEGORT_ACTIVE + ") VALUES('8'	,'2',	'FAMILY OBLIGATIONS', 'Y');");
		database.execSQL("INSERT INTO " + TABLE_CATEGORY + "(" + COLUMN_CATEGORY_ID + "," + COLUMN_MOV_TYPE_ID_FK + "," + COLUMN_CATEGORT_DESCRIPTION + "," + COLUMN_CATEGORT_ACTIVE + ") VALUES('9'	,'2',	'HEALTH AND MEDICAL', 'Y');");
		database.execSQL("INSERT INTO " + TABLE_CATEGORY + "(" + COLUMN_CATEGORY_ID + "," + COLUMN_MOV_TYPE_ID_FK + "," + COLUMN_CATEGORT_DESCRIPTION + "," + COLUMN_CATEGORT_ACTIVE + ") VALUES('10'	,'2',	'TRANSPORTATION', 'Y');");
		database.execSQL("INSERT INTO " + TABLE_CATEGORY + "(" + COLUMN_CATEGORY_ID + "," + COLUMN_MOV_TYPE_ID_FK + "," + COLUMN_CATEGORT_DESCRIPTION + "," + COLUMN_CATEGORT_ACTIVE + ") VALUES('11'	,'2',	'DEBT PAYMENTS', 'Y');");
		database.execSQL("INSERT INTO " + TABLE_CATEGORY + "(" + COLUMN_CATEGORY_ID + "," + COLUMN_MOV_TYPE_ID_FK + "," + COLUMN_CATEGORT_DESCRIPTION + "," + COLUMN_CATEGORT_ACTIVE + ") VALUES('12'	,'2',	'ENTERTAINMENT/RECREATION', 'Y');");
		database.execSQL("INSERT INTO " + TABLE_CATEGORY + "(" + COLUMN_CATEGORY_ID + "," + COLUMN_MOV_TYPE_ID_FK + "," + COLUMN_CATEGORT_DESCRIPTION + "," + COLUMN_CATEGORT_ACTIVE + ") VALUES('13'	,'2',	'PETS', 'Y');");
		database.execSQL("INSERT INTO " + TABLE_CATEGORY + "(" + COLUMN_CATEGORY_ID + "," + COLUMN_MOV_TYPE_ID_FK + "," + COLUMN_CATEGORT_DESCRIPTION + "," + COLUMN_CATEGORT_ACTIVE + ") VALUES('14'	,'2',	'CLOTHING', 'Y');");
		database.execSQL("INSERT INTO " + TABLE_CATEGORY + "(" + COLUMN_CATEGORY_ID + "," + COLUMN_MOV_TYPE_ID_FK + "," + COLUMN_CATEGORT_DESCRIPTION + "," + COLUMN_CATEGORT_ACTIVE + ") VALUES('15'	,'2',	'INVESTMENTS AND SAVINGS', 'Y');");
		database.execSQL("INSERT INTO " + TABLE_CATEGORY + "(" + COLUMN_CATEGORY_ID + "," + COLUMN_MOV_TYPE_ID_FK + "," + COLUMN_CATEGORT_DESCRIPTION + "," + COLUMN_CATEGORT_ACTIVE + ") VALUES('16'	,'2',	'MISCELLANEOUS', 'Y');");
		database.execSQL("INSERT INTO " + TABLE_CATEGORY + "(" + COLUMN_CATEGORY_ID + "," + COLUMN_MOV_TYPE_ID_FK + "," + COLUMN_CATEGORT_DESCRIPTION + "," + COLUMN_CATEGORT_ACTIVE + ") VALUES('17'	,'2',	'EDUCATION', 'Y');");

		database.execSQL("INSERT INTO " + TABLE_SUB_CATEGORY + "(" + COLUMN_SUB_CATEGORY_ID + "," + COLUMN_CATEGORY_TYPE_ID_FK + "," + COLUMN_SUB_CATEGORT_DESCRIPTION + "," + COLUMN_SUB_CATEGORY_ACTIVE + ") VALUES('1', + '5', 'Mortgage or Rent', 'Y');");
		database.execSQL("INSERT INTO " + TABLE_SUB_CATEGORY + "(" + COLUMN_SUB_CATEGORY_ID + "," + COLUMN_CATEGORY_TYPE_ID_FK + "," + COLUMN_SUB_CATEGORT_DESCRIPTION + "," + COLUMN_SUB_CATEGORY_ACTIVE + ") VALUES('2', + '5', 'Homeowners/Renters Insurance', 'Y');");
		database.execSQL("INSERT INTO " + TABLE_SUB_CATEGORY + "(" + COLUMN_SUB_CATEGORY_ID + "," + COLUMN_CATEGORY_TYPE_ID_FK + "," + COLUMN_SUB_CATEGORT_DESCRIPTION + "," + COLUMN_SUB_CATEGORY_ACTIVE + ") VALUES('3', + '5', 'Property Taxes', 'Y');");
		database.execSQL("INSERT INTO " + TABLE_SUB_CATEGORY + "(" + COLUMN_SUB_CATEGORY_ID + "," + COLUMN_CATEGORY_TYPE_ID_FK + "," + COLUMN_SUB_CATEGORT_DESCRIPTION + "," + COLUMN_SUB_CATEGORY_ACTIVE + ") VALUES('4', + '5', 'Home Repairs/Maintenance/HOA Dues', 'Y');");
		database.execSQL("INSERT INTO " + TABLE_SUB_CATEGORY + "(" + COLUMN_SUB_CATEGORY_ID + "," + COLUMN_CATEGORY_TYPE_ID_FK + "," + COLUMN_SUB_CATEGORT_DESCRIPTION + "," + COLUMN_SUB_CATEGORY_ACTIVE + ") VALUES('5', + '5', 'Home Improvements', 'Y');");
		database.execSQL("INSERT INTO " + TABLE_SUB_CATEGORY + "(" + COLUMN_SUB_CATEGORY_ID + "," + COLUMN_CATEGORY_TYPE_ID_FK + "," + COLUMN_SUB_CATEGORT_DESCRIPTION + "," + COLUMN_SUB_CATEGORY_ACTIVE + ") VALUES('6', + '6', 'Electricity', 'Y');");
		database.execSQL("INSERT INTO " + TABLE_SUB_CATEGORY + "(" + COLUMN_SUB_CATEGORY_ID + "," + COLUMN_CATEGORY_TYPE_ID_FK + "," + COLUMN_SUB_CATEGORT_DESCRIPTION + "," + COLUMN_SUB_CATEGORY_ACTIVE + ") VALUES('7', + '6', 'Water and Sewer', 'Y');");
		database.execSQL("INSERT INTO " + TABLE_SUB_CATEGORY + "(" + COLUMN_SUB_CATEGORY_ID + "," + COLUMN_CATEGORY_TYPE_ID_FK + "," + COLUMN_SUB_CATEGORT_DESCRIPTION + "," + COLUMN_SUB_CATEGORY_ACTIVE + ") VALUES('8', + '6', 'Natural Gas or Oil', 'Y');");
		database.execSQL("INSERT INTO " + TABLE_SUB_CATEGORY + "(" + COLUMN_SUB_CATEGORY_ID + "," + COLUMN_CATEGORY_TYPE_ID_FK + "," + COLUMN_SUB_CATEGORT_DESCRIPTION + "," + COLUMN_SUB_CATEGORY_ACTIVE + ") VALUES('9', + '6', 'Telephone (Land Line, Cell)', 'Y');");
		database.execSQL("INSERT INTO " + TABLE_SUB_CATEGORY + "(" + COLUMN_SUB_CATEGORY_ID + "," + COLUMN_CATEGORY_TYPE_ID_FK + "," + COLUMN_SUB_CATEGORT_DESCRIPTION + "," + COLUMN_SUB_CATEGORY_ACTIVE + ") VALUES('10', + '7', 'Groceries', 'Y');");
		database.execSQL("INSERT INTO " + TABLE_SUB_CATEGORY + "(" + COLUMN_SUB_CATEGORY_ID + "," + COLUMN_CATEGORY_TYPE_ID_FK + "," + COLUMN_SUB_CATEGORT_DESCRIPTION + "," + COLUMN_SUB_CATEGORY_ACTIVE + ") VALUES('11', + '7', 'Eating Out, Lunches, Snacks', 'Y');");
		database.execSQL("INSERT INTO " + TABLE_SUB_CATEGORY + "(" + COLUMN_SUB_CATEGORY_ID + "," + COLUMN_CATEGORY_TYPE_ID_FK + "," + COLUMN_SUB_CATEGORT_DESCRIPTION + "," + COLUMN_SUB_CATEGORY_ACTIVE + ") VALUES('12', + '8', 'Child Support', 'Y');");
		database.execSQL("INSERT INTO " + TABLE_SUB_CATEGORY + "(" + COLUMN_SUB_CATEGORY_ID + "," + COLUMN_CATEGORY_TYPE_ID_FK + "," + COLUMN_SUB_CATEGORT_DESCRIPTION + "," + COLUMN_SUB_CATEGORY_ACTIVE + ") VALUES('13', + '8', 'Alimony', 'Y');");
		database.execSQL("INSERT INTO " + TABLE_SUB_CATEGORY + "(" + COLUMN_SUB_CATEGORY_ID + "," + COLUMN_CATEGORY_TYPE_ID_FK + "," + COLUMN_SUB_CATEGORT_DESCRIPTION + "," + COLUMN_SUB_CATEGORY_ACTIVE + ") VALUES('14', + '8', 'Day Care, Babysitting', 'Y');");
		database.execSQL("INSERT INTO " + TABLE_SUB_CATEGORY + "(" + COLUMN_SUB_CATEGORY_ID + "," + COLUMN_CATEGORY_TYPE_ID_FK + "," + COLUMN_SUB_CATEGORT_DESCRIPTION + "," + COLUMN_SUB_CATEGORY_ACTIVE + ") VALUES('15', + '9', 'Insurance (medical,dental,vision)', 'Y');");
		database.execSQL("INSERT INTO " + TABLE_SUB_CATEGORY + "(" + COLUMN_SUB_CATEGORY_ID + "," + COLUMN_CATEGORY_TYPE_ID_FK + "," + COLUMN_SUB_CATEGORT_DESCRIPTION + "," + COLUMN_SUB_CATEGORY_ACTIVE + ") VALUES('16', + '9', 'Unreimbursed Medical Expenses, Copays', 'Y');");
		database.execSQL("INSERT INTO " + TABLE_SUB_CATEGORY + "(" + COLUMN_SUB_CATEGORY_ID + "," + COLUMN_CATEGORY_TYPE_ID_FK + "," + COLUMN_SUB_CATEGORT_DESCRIPTION + "," + COLUMN_SUB_CATEGORY_ACTIVE + ") VALUES('17', + '9', 'Fitness (Yoga,Massage,Gym)', 'Y');");
		database.execSQL("INSERT INTO " + TABLE_SUB_CATEGORY + "(" + COLUMN_SUB_CATEGORY_ID + "," + COLUMN_CATEGORY_TYPE_ID_FK + "," + COLUMN_SUB_CATEGORT_DESCRIPTION + "," + COLUMN_SUB_CATEGORY_ACTIVE + ") VALUES('18', + '10', 'Car Payments', 'Y');");
		database.execSQL("INSERT INTO " + TABLE_SUB_CATEGORY + "(" + COLUMN_SUB_CATEGORY_ID + "," + COLUMN_CATEGORY_TYPE_ID_FK + "," + COLUMN_SUB_CATEGORT_DESCRIPTION + "," + COLUMN_SUB_CATEGORY_ACTIVE + ") VALUES('19', + '10', 'Gasoline/Oil', 'Y');");
		database.execSQL("INSERT INTO " + TABLE_SUB_CATEGORY + "(" + COLUMN_SUB_CATEGORY_ID + "," + COLUMN_CATEGORY_TYPE_ID_FK + "," + COLUMN_SUB_CATEGORT_DESCRIPTION + "," + COLUMN_SUB_CATEGORY_ACTIVE + ") VALUES('20', + '10', 'Auto Repairs/Maintenance/Fees', 'Y');");
		database.execSQL("INSERT INTO " + TABLE_SUB_CATEGORY + "(" + COLUMN_SUB_CATEGORY_ID + "," + COLUMN_CATEGORY_TYPE_ID_FK + "," + COLUMN_SUB_CATEGORT_DESCRIPTION + "," + COLUMN_SUB_CATEGORY_ACTIVE + ") VALUES('21', + '10', 'Auto Insurance', 'Y');");
		database.execSQL("INSERT INTO " + TABLE_SUB_CATEGORY + "(" + COLUMN_SUB_CATEGORY_ID + "," + COLUMN_CATEGORY_TYPE_ID_FK + "," + COLUMN_SUB_CATEGORT_DESCRIPTION + "," + COLUMN_SUB_CATEGORY_ACTIVE + ") VALUES('22', + '10', 'Other Transportation (tolls, bus, subway, taxis)', 'Y');");
		database.execSQL("INSERT INTO " + TABLE_SUB_CATEGORY + "(" + COLUMN_SUB_CATEGORY_ID + "," + COLUMN_CATEGORY_TYPE_ID_FK + "," + COLUMN_SUB_CATEGORT_DESCRIPTION + "," + COLUMN_SUB_CATEGORY_ACTIVE + ") VALUES('23', + '11', 'Credit Cards', 'Y');");
		database.execSQL("INSERT INTO " + TABLE_SUB_CATEGORY + "(" + COLUMN_SUB_CATEGORY_ID + "," + COLUMN_CATEGORY_TYPE_ID_FK + "," + COLUMN_SUB_CATEGORT_DESCRIPTION + "," + COLUMN_SUB_CATEGORY_ACTIVE + ") VALUES('24', + '11', 'Student Loans', 'Y');");
		database.execSQL("INSERT INTO " + TABLE_SUB_CATEGORY + "(" + COLUMN_SUB_CATEGORY_ID + "," + COLUMN_CATEGORY_TYPE_ID_FK + "," + COLUMN_SUB_CATEGORT_DESCRIPTION + "," + COLUMN_SUB_CATEGORY_ACTIVE + ") VALUES('25', + '11', 'Other Loans', 'Y');");
		database.execSQL("INSERT INTO " + TABLE_SUB_CATEGORY + "(" + COLUMN_SUB_CATEGORY_ID + "," + COLUMN_CATEGORY_TYPE_ID_FK + "," + COLUMN_SUB_CATEGORT_DESCRIPTION + "," + COLUMN_SUB_CATEGORY_ACTIVE + ") VALUES('26', + '12', 'Cable TV/Videos/Movies', 'Y');");
		database.execSQL("INSERT INTO " + TABLE_SUB_CATEGORY + "(" + COLUMN_SUB_CATEGORY_ID + "," + COLUMN_CATEGORY_TYPE_ID_FK + "," + COLUMN_SUB_CATEGORT_DESCRIPTION + "," + COLUMN_SUB_CATEGORY_ACTIVE + ") VALUES('27', + '12', 'Computer Expense', 'Y');");
		database.execSQL("INSERT INTO " + TABLE_SUB_CATEGORY + "(" + COLUMN_SUB_CATEGORY_ID + "," + COLUMN_CATEGORY_TYPE_ID_FK + "," + COLUMN_SUB_CATEGORT_DESCRIPTION + "," + COLUMN_SUB_CATEGORY_ACTIVE + ") VALUES('28', + '12', 'Hobbies', 'Y');");
		database.execSQL("INSERT INTO " + TABLE_SUB_CATEGORY + "(" + COLUMN_SUB_CATEGORY_ID + "," + COLUMN_CATEGORY_TYPE_ID_FK + "," + COLUMN_SUB_CATEGORT_DESCRIPTION + "," + COLUMN_SUB_CATEGORY_ACTIVE + ") VALUES('29', + '12', 'Subscriptions and Dues', 'Y');");
		database.execSQL("INSERT INTO " + TABLE_SUB_CATEGORY + "(" + COLUMN_SUB_CATEGORY_ID + "," + COLUMN_CATEGORY_TYPE_ID_FK + "," + COLUMN_SUB_CATEGORT_DESCRIPTION + "," + COLUMN_SUB_CATEGORY_ACTIVE + ") VALUES('30', + '12', 'Vacations', 'Y');");
		database.execSQL("INSERT INTO " + TABLE_SUB_CATEGORY + "(" + COLUMN_SUB_CATEGORY_ID + "," + COLUMN_CATEGORY_TYPE_ID_FK + "," + COLUMN_SUB_CATEGORT_DESCRIPTION + "," + COLUMN_SUB_CATEGORY_ACTIVE + ") VALUES('31', + '13', 'Food', 'Y');");
		database.execSQL("INSERT INTO " + TABLE_SUB_CATEGORY + "(" + COLUMN_SUB_CATEGORY_ID + "," + COLUMN_CATEGORY_TYPE_ID_FK + "," + COLUMN_SUB_CATEGORT_DESCRIPTION + "," + COLUMN_SUB_CATEGORY_ACTIVE + ") VALUES('32', + '13', 'Grooming, Boarding, Vet', 'Y');");
		database.execSQL("INSERT INTO " + TABLE_SUB_CATEGORY + "(" + COLUMN_SUB_CATEGORY_ID + "," + COLUMN_CATEGORY_TYPE_ID_FK + "," + COLUMN_SUB_CATEGORT_DESCRIPTION + "," + COLUMN_SUB_CATEGORY_ACTIVE + ") VALUES('33', + '15', 'Stocks/Bonds/Mutual Funds', 'Y');");
		database.execSQL("INSERT INTO " + TABLE_SUB_CATEGORY + "(" + COLUMN_SUB_CATEGORY_ID + "," + COLUMN_CATEGORY_TYPE_ID_FK + "," + COLUMN_SUB_CATEGORT_DESCRIPTION + "," + COLUMN_SUB_CATEGORY_ACTIVE + ") VALUES('34', + '15', 'College Fund', 'Y');");
		database.execSQL("INSERT INTO " + TABLE_SUB_CATEGORY + "(" + COLUMN_SUB_CATEGORY_ID + "," + COLUMN_CATEGORY_TYPE_ID_FK + "," + COLUMN_SUB_CATEGORT_DESCRIPTION + "," + COLUMN_SUB_CATEGORY_ACTIVE + ") VALUES('35', + '15', 'Savings', 'Y');");
		database.execSQL("INSERT INTO " + TABLE_SUB_CATEGORY + "(" + COLUMN_SUB_CATEGORY_ID + "," + COLUMN_CATEGORY_TYPE_ID_FK + "," + COLUMN_SUB_CATEGORT_DESCRIPTION + "," + COLUMN_SUB_CATEGORY_ACTIVE + ") VALUES('36', + '15', 'Emergency Fund', 'Y');");
		database.execSQL("INSERT INTO " + TABLE_SUB_CATEGORY + "(" + COLUMN_SUB_CATEGORY_ID + "," + COLUMN_CATEGORY_TYPE_ID_FK + "," + COLUMN_SUB_CATEGORT_DESCRIPTION + "," + COLUMN_SUB_CATEGORY_ACTIVE + ") VALUES('37', + '16', 'Toiletries, Household Products', 'Y');");
		database.execSQL("INSERT INTO " + TABLE_SUB_CATEGORY + "(" + COLUMN_SUB_CATEGORY_ID + "," + COLUMN_CATEGORY_TYPE_ID_FK + "," + COLUMN_SUB_CATEGORT_DESCRIPTION + "," + COLUMN_SUB_CATEGORY_ACTIVE + ") VALUES('38', + '16', 'Gifts/Donations', 'Y');");
		database.execSQL("INSERT INTO " + TABLE_SUB_CATEGORY + "(" + COLUMN_SUB_CATEGORY_ID + "," + COLUMN_CATEGORY_TYPE_ID_FK + "," + COLUMN_SUB_CATEGORT_DESCRIPTION + "," + COLUMN_SUB_CATEGORY_ACTIVE + ") VALUES('39', + '16', 'Grooming (Hair, Make-up, Other)', 'Y');");
		database.execSQL("INSERT INTO " + TABLE_SUB_CATEGORY + "(" + COLUMN_SUB_CATEGORY_ID + "," + COLUMN_CATEGORY_TYPE_ID_FK + "," + COLUMN_SUB_CATEGORT_DESCRIPTION + "," + COLUMN_SUB_CATEGORY_ACTIVE + ") VALUES('40', + '16', 'Miscellaneous Expense', 'Y');");
		database.execSQL("INSERT INTO " + TABLE_SUB_CATEGORY + "(" + COLUMN_SUB_CATEGORY_ID + "," + COLUMN_CATEGORY_TYPE_ID_FK + "," + COLUMN_SUB_CATEGORT_DESCRIPTION + "," + COLUMN_SUB_CATEGORY_ACTIVE + ") VALUES('41', + '17', 'Fees', 'Y');");
		database.execSQL("INSERT INTO " + TABLE_SUB_CATEGORY + "(" + COLUMN_SUB_CATEGORY_ID + "," + COLUMN_CATEGORY_TYPE_ID_FK + "," + COLUMN_SUB_CATEGORT_DESCRIPTION + "," + COLUMN_SUB_CATEGORY_ACTIVE + ") VALUES('42', + '17', 'Materials', 'Y');");
		
		
		database.execSQL("INSERT INTO " + TABLE_ACCOUNTS + "(" + COLUMN_ACCOUNT_SGF_ID + "," + COLUMN_ACCOUNT_NUMBER + "," + COLUMN_ACCOUNT_NAME + "," + COLUMN_ACCOUNT_BANK_NAME + "," + COLUMN_ACCOUNT_OTHER_INFO + "," + COLUMN_ACCOUNT_BALANCE + "," + COLUMN_ACCOUNT_ACTIVE +") VALUES(null,'111',  'Private Account','Private Bank', 'Private Account', 0,'Y');");
		//database.execSQL("INSERT INTO " + TABLE_ACCOUNTS + "(" + COLUMN_ACCOUNT_SGF_ID + "," + COLUMN_ACCOUNT_NUMBER + "," + COLUMN_ACCOUNT_NAME + "," + COLUMN_ACCOUNT_BANK_NAME + "," + COLUMN_ACCOUNT_OTHER_INFO + "," + COLUMN_ACCOUNT_BALANCE + "," + COLUMN_ACCOUNT_ACTIVE +") VALUES(2,'223',  'Conta BPI','BPI', 'Conta Poupança', 0,'Y');");

	}

}
