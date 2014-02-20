package com.sgf.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.sgf.entities.Account;

/** Classe que oferece uma interface para a BD SQLite, funcionando também como um mecanismo de persistencia
 * da entidade Accounts possibilitando operacoes CRUD sobre as mesmas
 * @author wgovindji
 * 
 */
public  class AccountsDAO {
	private SQLiteDatabase _database;
	private SGFDataModelSQLiteHelper _dbHelper;
	private String [] _ALL_COLUMNS = {	SGFDataModelSQLiteHelper.COLUMN_ACCOUNT_ID,
										SGFDataModelSQLiteHelper.COLUMN_ACCOUNT_SGF_ID,
										SGFDataModelSQLiteHelper.COLUMN_ACCOUNT_NUMBER,
										SGFDataModelSQLiteHelper.COLUMN_ACCOUNT_NAME,
										SGFDataModelSQLiteHelper.COLUMN_ACCOUNT_BANK_NAME,
										SGFDataModelSQLiteHelper.COLUMN_ACCOUNT_BALANCE,
										SGFDataModelSQLiteHelper.COLUMN_ACCOUNT_OTHER_INFO,
										SGFDataModelSQLiteHelper.COLUMN_ACCOUNT_ACTIVE};
	
	public AccountsDAO(Context ctx){
		
		_dbHelper=new SGFDataModelSQLiteHelper(ctx, null, null, '1');
	}
	
	/**
	 * Inicializa o objecto _database para escrita e leitura
	 */
	public void open(){
		
		_database=_dbHelper.getWritableDatabase();
	}
	/**
	 * Liberta os recursos inicializados pelo metodo open
	 */
	public void close(){
		_dbHelper.close();
	}
	
	/** Retorna todos as Contas existentes na BD, opcionalmente podendo passando um filtro
	 * @param filter filtro para limitar de alguma forma a seleccao exemplo "and account_id=1" 
	 * @return Lista de Contas
	 */
	public List<Account> getAllAccounts(String filter){
		List<Account> _list=new ArrayList<Account>();
		String selection=null;
		String sqlStatement;
		this.open();
		
		if(filter.length()>1){
			selection=filter;
			sqlStatement="select 														"+			
					"acc._id as ACC_ID,                                             "+
					"acc.sgf_account_id as ACC_SGF_ID,                                "+
					"acc.account_number as ACC_NUMBER,               "+
					"acc.account_name AS ACC_NAME,                                  "+
					"acc.account_bank_name AS ACC_BANK_NAME,                    "+
					"acc.account_balance AS ACC_BALANCE,                          "+
					"acc.account_other_info AS ACC_OTHER_INFO,         "+
					"acc.account_active AS ACC_ACTIVE                               "+
					"from                                                           "+
					"sgf_t_accounts acc                                           "+
					selection 														
					;
		}else{
			sqlStatement="select 														"+			
					"acc._id as ACC_ID,                                             "+
					"acc.sgf_account_id as ACC_SGF_ID,                                "+
					"acc.account_number as ACC_NUMBER,               "+
					"acc.account_name AS ACC_NAME,                                  "+
					"acc.account_bank_name AS ACC_BANK_NAME,                    "+
					"acc.account_balance AS ACC_BALANCE,                          "+
					"acc.account_other_info AS ACC_OTHER_INFO,         "+
					"acc.account_active AS ACC_ACTIVE                               "+
					"from                                                           "+
					"sgf_t_accounts acc                                           "
					 ;
		}
		
		
		
		
		Cursor cursor=_database.rawQuery(sqlStatement, null);
				//_database.query(MovementSQLiteHelper.TABLE_MOVEMENTS, _ALL_COLUMNS, selection, null, null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()){
			Account account=cursorToAccount(cursor);
			_list.add(account);
			cursor.moveToNext();
			
		}
		cursor.close();
		this.close();
		return _list;
		
	} 
	
	/**Metodo helper que como input recebe um cursor apontando a um registo da tabela Accounts e retorna um Objecto Account 
	 * @param cursor
	 * @return Retorna um Objecto Movement
	 */
	private Account cursorToAccount(Cursor cursor) {
		Account ac=new Account();
	
		ac.set_id(cursor.getString(cursor.getColumnIndex("ACC_ID")));
		ac.setSgf_account_id(cursor.getString(cursor.getColumnIndex("ACC_SGF_ID")));
		ac.setAccount_number(cursor.getString(cursor.getColumnIndex("ACC_NUMBER")));
		ac.setAccount_name(cursor.getString(cursor.getColumnIndex("ACC_NAME")));
		ac.setAccount_bank_name(cursor.getString(cursor.getColumnIndex("ACC_BANK_NAME")));
		ac.setAccount_balance(cursor.getDouble(cursor.getColumnIndex("ACC_BALANCE")));
		ac.setAccount_other_info(cursor.getString(cursor.getColumnIndex("ACC_OTHER_INFO")));
		ac.setAccount_active(cursor.getString(cursor.getColumnIndex("ACC_ACTIVE")));

		
		return ac;
	}


	/**Operacao CREATE - Cria um conta nova na BD 
	 * @param account Objecto Account 
	 * @return
	 */
	public long create(Account account){
		ContentValues cv=new ContentValues();
		cv.put(SGFDataModelSQLiteHelper.COLUMN_ACCOUNT_SGF_ID, account.getSgf_account_id());
		cv.put(SGFDataModelSQLiteHelper.COLUMN_ACCOUNT_NUMBER, account.getAccount_number());
		cv.put(SGFDataModelSQLiteHelper.COLUMN_ACCOUNT_NAME, account.getAccount_name());
		cv.put(SGFDataModelSQLiteHelper.COLUMN_ACCOUNT_BANK_NAME, account.getAccount_bank_name());
		cv.put(SGFDataModelSQLiteHelper.COLUMN_ACCOUNT_BALANCE, account.getAccount_balance());
		cv.put(SGFDataModelSQLiteHelper.COLUMN_ACCOUNT_OTHER_INFO, account.getAccount_other_info());
		cv.put(SGFDataModelSQLiteHelper.COLUMN_ACCOUNT_ACTIVE, account.getAccount_active());
	
		
		this.open(); 
		Long idinse= _database.insert(SGFDataModelSQLiteHelper.TABLE_ACCOUNTS, null, cv);
		this.close();
		return idinse;

	}
	
	


//UPDATE
/** Operacao DELETE - Apaga a conta passada por parametro de entrada, da BD
 * @param account
 * @return
 */
public int update(Account account){
	ContentValues cv=new ContentValues();
	cv.put(SGFDataModelSQLiteHelper.COLUMN_ACCOUNT_SGF_ID, account.getSgf_account_id());
	cv.put(SGFDataModelSQLiteHelper.COLUMN_ACCOUNT_NUMBER, account.getAccount_number());
	cv.put(SGFDataModelSQLiteHelper.COLUMN_ACCOUNT_NAME, account.getAccount_name());
	cv.put(SGFDataModelSQLiteHelper.COLUMN_ACCOUNT_BANK_NAME, account.getAccount_bank_name());
	cv.put(SGFDataModelSQLiteHelper.COLUMN_ACCOUNT_BALANCE, account.getAccount_balance());
	cv.put(SGFDataModelSQLiteHelper.COLUMN_ACCOUNT_OTHER_INFO, account.getAccount_other_info());
	cv.put(SGFDataModelSQLiteHelper.COLUMN_ACCOUNT_ACTIVE, account.getAccount_active());
	
	this.open();
	 
			
	int res =_database.update(SGFDataModelSQLiteHelper.TABLE_ACCOUNTS, 
			cv,
			SGFDataModelSQLiteHelper.COLUMN_ACCOUNT_ID + "=?", 
			new String [] {account.get_id()});
	this.close();
	return res;
}


//DELETE
public int delete(long id){
	
	this.open();
	String _id=new Long(id).toString();
	 int res=_database.delete(SGFDataModelSQLiteHelper.TABLE_ACCOUNTS, 
			SGFDataModelSQLiteHelper.COLUMN_ACCOUNT_ID + "=?",
			new String [] {_id});
	 this.close();
	 return res;
}


/** Metodo Helper que com base num Account passado como parametro de input,
 *  retorna um ContentValue com os dados do objecto Account 
 * @param acc
 * @return
 */
public static ContentValues toContentValues(Account acc){
	ContentValues cv=new ContentValues();
	String acc_id;
	String sgf_account_id;
	String account_number;
	String account_name;
	String account_bank_name;
	String account_other_info;
	String account_active;
	Double account_balance;
	
	if (acc.get_id() != null){ acc_id = acc.get_id();
		cv.put(SGFDataModelSQLiteHelper.COLUMN_ACCOUNT_ID, acc_id);}
	
	if (acc.getSgf_account_id() !=null){ sgf_account_id=acc.getSgf_account_id();
		cv.put(SGFDataModelSQLiteHelper.COLUMN_ACCOUNT_SGF_ID,sgf_account_id);}
	
	if (acc.getAccount_number() !=null){ account_number=acc.getAccount_number();
		cv.put(SGFDataModelSQLiteHelper.COLUMN_ACCOUNT_NUMBER,account_number);}
	
	if(acc.getAccount_name() !=null) {account_name=acc.getAccount_name();
		cv.put(SGFDataModelSQLiteHelper.COLUMN_ACCOUNT_NAME, account_name);}
	
	if (acc.getAccount_bank_name() !=null){ account_bank_name=acc.getAccount_bank_name();
		cv.put(SGFDataModelSQLiteHelper.COLUMN_ACCOUNT_BANK_NAME, account_bank_name);}
	
	if (acc.getAccount_other_info() !=null){ account_other_info=acc.getAccount_other_info();
		cv.put(SGFDataModelSQLiteHelper.COLUMN_ACCOUNT_OTHER_INFO, account_other_info);}
	
	if (acc.getAccount_active() !=null){ account_active=acc.getAccount_active();
		cv.put(SGFDataModelSQLiteHelper.COLUMN_ACCOUNT_ACTIVE, account_active);}
	
	if (acc.getAccount_balance() !=null){ account_balance=acc.getAccount_balance();
		cv.put(SGFDataModelSQLiteHelper.COLUMN_ACCOUNT_BALANCE, account_balance);}

	

	return cv;
	

	
}


/**
 * Este metodo verifica para um dado ID se a conta existe ou nao na BD Local do android
 * @param id identificador da accountId existente no SGFWEBAPP
 * @return true ou false consoante a conta existe ou não
 */
public boolean accountExists(String id){
	this.open();
	String sql = "select count(1) from sgf_t_accounts  where sgf_account_id = ? ";
	Cursor cur=_database.rawQuery(sql, new String [] {id});
	cur.moveToFirst();
	int res=cur.getInt(0);
	cur.close();
	this.close();
	
	return res==1;

}




}