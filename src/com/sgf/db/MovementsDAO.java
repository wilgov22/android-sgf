package com.sgf.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.sgf.entities.CategoryTotal;
import com.sgf.entities.Movement;

/**
 * Classe que oferece uma interface para a BD SQLite, funcionando também como um
 * mecanismo de persistencia da entidade Movements possibilitando operacoes CRUD
 * sobre as mesmas
 * 
 * @author wgovindji
 * 
 */

public class MovementsDAO {
	private SQLiteDatabase _database;
	private SGFDataModelSQLiteHelper _dbHelper;
	private String[] _ALL_COLUMNS = {
			SGFDataModelSQLiteHelper.COLUMN_MOVEMENTS_ID,
			SGFDataModelSQLiteHelper.COLUMN_MOVEMENT_TYPE,
			SGFDataModelSQLiteHelper.COLUMN_MOVEMENT_CATEGORY,
			SGFDataModelSQLiteHelper.COLUMN_MOVEMENT_SUB_CATEGORY,
			SGFDataModelSQLiteHelper.COLUMN_MOVEMENT_AMOUNT,
			SGFDataModelSQLiteHelper.COLUMN_MOVEMENT_DESC,
			SGFDataModelSQLiteHelper.COLUMN_MOVEMENT_DATE,
			SGFDataModelSQLiteHelper.COLUMN_MOVEMENT_STATE,
			SGFDataModelSQLiteHelper.COLUMN_MOVEMENT_ACCOUNT_ID,
			SGFDataModelSQLiteHelper.COLUMN_MOVEMENTS_SGF_ID };

	public MovementsDAO(Context ctx) {

		_dbHelper = new SGFDataModelSQLiteHelper(ctx, null, null, '1');
	}

	/**
	 * Inicializa o objecto _database para escrita e leitura
	 */
	public void open() {

		_database = _dbHelper.getWritableDatabase();
	}

	/**
	 * Fecho do objecto _database
	 */
	public void close() {
		_dbHelper.close();
	}

	/**
	 * Retorna todos os movimentos contidos na BD, opcionalmente podendo
	 * passando um filtro
	 * 
	 * @param filter
	 *            filtro para limitar de alguma forma a seleccao exemplo
	 *            "and mov.account_id=1"
	 * @return Lista de Movements
	 */
	public List<Movement> getAllMovements(String filter) {
		List<Movement> _list = new ArrayList<Movement>();

		String selection = null;
		String sqlStatement;
		this.open();

		if (filter.length() > 1) {
			selection = filter;
			sqlStatement = "select 														"
					+ "mov._id as MOV_ID,                                             "
					+ "mov.mov_type_id as MOV_TYPE_ID,                                "
					+ "movType.movement_description as MOV_DESCRIPTION,               "
					+ "mov.mov_cat_id AS MOV_CAT_ID,                                  "
					+ "cat.cat_description AS MOV_CAT_DESCRIPTION,                    "
					+ "mov.mov_sub_cat_id AS MOV_SUB_CAT_ID,                          "
					+ "subcat.sub_cat_description AS MOV_SUB_CAT_DESCRIPTION,         "
					+ "mov.mov_desc AS MOV_DESCRIPTION,                               "
					+ "mov.mov_amount AS MOV_AMOUNT,                                  "
					+ "mov.mov_date AS MOV_DATE,                                      "
					+ "mov.mov_state AS MOV_STATE,                                     "
					+ "mov.mov_account_id AS MOV_ACCOUNT_ID,                           "
					+ "mov.sgf_movement_id AS MOV_SGF_ID                           "
					+ "from                                                           "
					+ "sgf_t_movements mov,                                           "
					+ "sgf_r_movement_type movType,                                   "
					+ "sgf_r_category cat,                                            "
					+ "sgf_r_sub_category subcat                                      "
					+ "where mov.mov_type_id=movType._id                              "
					+ "and mov.mov_cat_id=cat._id                                     "
					+ "and mov.mov_sub_cat_id=subcat._id                              "
					+ selection
					+ "union all                                                      "
					+ "select                                                         "
					+ "mov._id as MOV_ID,                                             "
					+ "mov.mov_type_id as MOV_TYPE_ID,                                "
					+ "movType.movement_description as MOV_DESCRIPTION,               "
					+ "'' AS MOV_CAT_ID,                                              "
					+ "'' AS MOV_CAT_DESCRIPTION,                                     "
					+ "'' AS MOV_SUB_CAT_ID,                                          "
					+ "'' AS MOV_SUB_CAT_DESCRIPTION,                                 "
					+ "mov.mov_desc AS MOV_DESCRIPTION,                               "
					+ "mov.mov_amount AS MOV_AMOUNT,                                  "
					+ "mov.mov_date AS MOV_DATE,                                      "
					+ "mov.mov_state AS MOV_STATE,                                     "
					+ "mov.mov_account_id AS MOV_ACCOUNT_ID,                           "
					+ "mov.sgf_movement_id AS MOV_SGF_ID                           "
					+

					"from                                                           "
					+ "sgf_t_movements mov,                                           "
					+ "sgf_r_movement_type movType                                    "
					+ "where mov.mov_type_id=movType._id                              "
					+ "and mov.mov_cat_id is null                                     "
					+ selection
					+ "union all                                                      "
					+ "                                                               "
					+ "select                                                         "
					+ "mov._id as MOV_ID,                                             "
					+ "mov.mov_type_id as MOV_TYPE_ID,                                "
					+ "movType.movement_description as MOV_DESCRIPTION,               "
					+ "mov.mov_cat_id AS MOV_CAT_ID,                                  "
					+ "cat.cat_description AS MOV_CAT_DESCRIPTION,                    "
					+ "'' AS MOV_SUB_CAT_ID,                                          "
					+ "'' AS MOV_SUB_CAT_DESCRIPTION,                                 "
					+ "mov.mov_desc AS MOV_DESCRIPTION,                               "
					+ "mov.mov_amount AS MOV_AMOUNT,                                  "
					+ "mov.mov_date AS MOV_DATE,                                      "
					+ "mov.mov_state AS MOV_STATE,                                     "
					+ "mov.mov_account_id AS MOV_ACCOUNT_ID,                           "
					+ "mov.sgf_movement_id AS MOV_SGF_ID                           "
					+ "from                                                           "
					+ "sgf_t_movements mov,                                           "
					+ "sgf_r_movement_type movType,                                   "
					+ "sgf_r_category cat                                             "
					+ "where mov.mov_type_id=movType._id                              "
					+ "and mov.mov_cat_id=cat._id                                     "
					+ "and mov.mov_sub_cat_id is null                                 "
					+ selection + "order by mov._id desc";
		} else {
			sqlStatement = "select 														"
					+ "mov._id as MOV_ID,                                             "
					+ "mov.mov_type_id as MOV_TYPE_ID,                                "
					+ "movType.movement_description as MOV_DESCRIPTION,               "
					+ "mov.mov_cat_id AS MOV_CAT_ID,                                  "
					+ "cat.cat_description AS MOV_CAT_DESCRIPTION,                    "
					+ "mov.mov_sub_cat_id AS MOV_SUB_CAT_ID,                          "
					+ "subcat.sub_cat_description AS MOV_SUB_CAT_DESCRIPTION,         "
					+ "mov.mov_desc AS MOV_DESCRIPTION,                               "
					+ "mov.mov_amount AS MOV_AMOUNT,                                  "
					+ "mov.mov_date AS MOV_DATE,                                      "
					+ "mov.mov_state AS MOV_STATE,                                     "
					+ "mov.mov_account_id AS MOV_ACCOUNT_ID,                           "
					+ "mov.sgf_movement_id AS MOV_SGF_ID                           "
					+ "from                                                           "
					+ "sgf_t_movements mov,                                           "
					+ "sgf_r_movement_type movType,                                   "
					+ "sgf_r_category cat,                                            "
					+ "sgf_r_sub_category subcat                                      "
					+ "where mov.mov_type_id=movType._id                              "
					+ "and mov.mov_cat_id=cat._id                                     "
					+ "and mov.mov_sub_cat_id=subcat._id                              "
					+ "union all                                                      "
					+ "select                                                         "
					+ "mov._id as MOV_ID,                                             "
					+ "mov.mov_type_id as MOV_TYPE_ID,                                "
					+ "movType.movement_description as MOV_DESCRIPTION,               "
					+ "'' AS MOV_CAT_ID,                                              "
					+ "'' AS MOV_CAT_DESCRIPTION,                                     "
					+ "'' AS MOV_SUB_CAT_ID,                                          "
					+ "'' AS MOV_SUB_CAT_DESCRIPTION,                                 "
					+ "mov.mov_desc AS MOV_DESCRIPTION,                               "
					+ "mov.mov_amount AS MOV_AMOUNT,                                  "
					+ "mov.mov_date AS MOV_DATE,                                      "
					+ "mov.mov_state AS MOV_STATE,                                     "
					+ "mov.mov_account_id AS MOV_ACCOUNT_ID,                           "
					+ "mov.sgf_movement_id AS MOV_SGF_ID                           "
					+ "from                                                           "
					+ "sgf_t_movements mov,                                           "
					+ "sgf_r_movement_type movType                                    "
					+ "where mov.mov_type_id=movType._id                              "
					+ "and mov.mov_cat_id is null                                     "
					+ "union all                                                      "
					+ "                                                               "
					+ "select                                                         "
					+ "mov._id as MOV_ID,                                             "
					+ "mov.mov_type_id as MOV_TYPE_ID,                                "
					+ "movType.movement_description as MOV_DESCRIPTION,               "
					+ "mov.mov_cat_id AS MOV_CAT_ID,                                  "
					+ "cat.cat_description AS MOV_CAT_DESCRIPTION,                    "
					+ "'' AS MOV_SUB_CAT_ID,                                          "
					+ "'' AS MOV_SUB_CAT_DESCRIPTION,                                 "
					+ "mov.mov_desc AS MOV_DESCRIPTION,                               "
					+ "mov.mov_amount AS MOV_AMOUNT,                                  "
					+ "mov.mov_date AS MOV_DATE,                                      "
					+ "mov.mov_state AS MOV_STATE,                                     "
					+ "mov.mov_account_id AS MOV_ACCOUNT_ID,                           "
					+ "mov.sgf_movement_id AS MOV_SGF_ID                           "
					+ "from                                                           "
					+ "sgf_t_movements mov,                                           "
					+ "sgf_r_movement_type movType,                                   "
					+ "sgf_r_category cat                                             "
					+ "where mov.mov_type_id=movType._id                              "
					+ "and mov.mov_cat_id=cat._id                                     "
					+ "and mov.mov_sub_cat_id is null                                 "
					+ "order by mov._id desc";
		}

		Cursor cursor = _database.rawQuery(sqlStatement, null);
		// _database.query(MovementSQLiteHelper.TABLE_MOVEMENTS, _ALL_COLUMNS,
		// selection, null, null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Movement movement = cursorToMovement(cursor);
			_list.add(movement);
			cursor.moveToNext();

		}
		cursor.close();
		this.close();
		return _list;

	}

	/**
	 * Metodo helper que como input recebe um cursor apontando a um registo da
	 * tabela movimento e retorna um Objecto Movement
	 * 
	 * @param cursor
	 *            Cursor de movimento
	 * @return Retorna um objecto Movement
	 */
	private Movement cursorToMovement(Cursor cursor) {
		Movement mv = new Movement();

		mv.set_id(cursor.getString(cursor.getColumnIndex("MOV_ID")));
		mv.setMov_type_id(cursor.getString(cursor.getColumnIndex("MOV_TYPE_ID")));
		mv.setMov_amount(cursor.getDouble(cursor.getColumnIndex("MOV_AMOUNT")));
		mv.setMov_cat_id(cursor.getString(cursor.getColumnIndex("MOV_CAT_ID")));
		mv.setMov_sub_cat_id(cursor.getString(cursor
				.getColumnIndex("MOV_SUB_CAT_ID")));
		mv.setMov_desc(cursor.getString(cursor
				.getColumnIndex("MOV_DESCRIPTION")));
		mv.setMov_date(cursor.getString(cursor.getColumnIndex("MOV_DATE")));
		mv.setMov_state(cursor.getString(cursor.getColumnIndex("MOV_STATE")));
		mv.setMov_cat_desc(cursor.getString(cursor
				.getColumnIndex("MOV_CAT_DESCRIPTION")));
		mv.setMov_sub_cat_desc(cursor.getString(cursor
				.getColumnIndex("MOV_SUB_CAT_DESCRIPTION")));
		mv.setMov_account_id(cursor.getString(cursor
				.getColumnIndex("MOV_ACCOUNT_ID")));
		mv.setSgf_mov_id(cursor.getString(cursor.getColumnIndex("MOV_SGF_ID")));

		return mv;
	}

	/**
	 * Operacao CREATE - Cria um movimento novo na BD
	 * 
	 * @param movement
	 *            Objecto Movement
	 * @return retorna o ID (chave primaria)
	 */
	public long create(Movement movement) {
		ContentValues cv = new ContentValues();
		cv.put(SGFDataModelSQLiteHelper.COLUMN_MOVEMENT_TYPE,
				movement.getMov_type_id());
		cv.put(SGFDataModelSQLiteHelper.COLUMN_MOVEMENT_CATEGORY,
				movement.getMov_cat_id());
		cv.put(SGFDataModelSQLiteHelper.COLUMN_MOVEMENT_SUB_CATEGORY,
				movement.getMov_sub_cat_id());
		cv.put(SGFDataModelSQLiteHelper.COLUMN_MOVEMENT_AMOUNT,
				movement.getMov_amount());
		cv.put(SGFDataModelSQLiteHelper.COLUMN_MOVEMENT_DATE,
				movement.getMov_date());
		cv.put(SGFDataModelSQLiteHelper.COLUMN_MOVEMENT_DESC,
				movement.getMov_desc());
		cv.put(SGFDataModelSQLiteHelper.COLUMN_MOVEMENT_STATE,
				movement.getMov_state());
		cv.put(SGFDataModelSQLiteHelper.COLUMN_MOVEMENT_ACCOUNT_ID,
				movement.getMov_account_id());
		if (movement.getSgf_mov_id() != null
				&& movement.getSgf_mov_id().length() > 0) {

			cv.put(SGFDataModelSQLiteHelper.COLUMN_MOVEMENTS_SGF_ID,
					movement.getSgf_mov_id());
		}

		this.open();
		Long idinse = _database.insert(
				SGFDataModelSQLiteHelper.TABLE_MOVEMENTS, null, cv);
		this.close();
		return idinse;
	}

	//
	/**
	 * Operacao RETRIVE - Retorna o objecto Movement passando como parametro de
	 * entrada a chave primaria
	 * 
	 * @param id
	 *            Chave primaria - identificador do ID
	 * @return Objecto do tipo Movement
	 */
	public Movement retrive(long id) {
		String selection = "and " + "mov" + "."
				+ SGFDataModelSQLiteHelper.COLUMN_MOVEMENT_ID + "="
				+ Long.toString(id) + " ";
		Movement mv = getAllMovements(selection).get(0);
		return mv;

	}

	// UPDATE
	/**
	 * Operacao UPDATE - Actualiza um movimento existente na BD com os dados do
	 * movimento recebido como parametro de entrada
	 * 
	 * @param movement
	 *            Objecto Movement com os dados actualizados a persistir na BD
	 * @return Numero de registos actualizados (Deverá ser 1)
	 */
	public int update(Movement movement) {
		ContentValues cv = new ContentValues();
		cv.put(SGFDataModelSQLiteHelper.COLUMN_MOVEMENT_TYPE,
				movement.getMov_type_id());
		cv.put(SGFDataModelSQLiteHelper.COLUMN_MOVEMENT_CATEGORY,
				movement.getMov_cat_id());
		cv.put(SGFDataModelSQLiteHelper.COLUMN_MOVEMENT_SUB_CATEGORY,
				movement.getMov_sub_cat_id());
		cv.put(SGFDataModelSQLiteHelper.COLUMN_MOVEMENT_AMOUNT,
				movement.getMov_amount());
		cv.put(SGFDataModelSQLiteHelper.COLUMN_MOVEMENT_DATE,
				movement.getMov_date());
		cv.put(SGFDataModelSQLiteHelper.COLUMN_MOVEMENT_DESC,
				movement.getMov_desc());
		cv.put(SGFDataModelSQLiteHelper.COLUMN_MOVEMENT_ACCOUNT_ID,
				movement.getMov_account_id());

		/*
		 * String nextState = getNextState(movement.getMov_state(),
		 * "UPDATE_LOCALLY");
		 * cv.put(SGFDataModelSQLiteHelper.COLUMN_MOVEMENT_STATE, nextState);
		 */

		cv.put(SGFDataModelSQLiteHelper.COLUMN_MOVEMENT_STATE,
				movement.getMov_state());

		if (movement.getSgf_mov_id() != null) {
			cv.put(SGFDataModelSQLiteHelper.COLUMN_MOVEMENTS_SGF_ID,
					movement.getSgf_mov_id());
		}

		this.open();

		int res = _database.update(SGFDataModelSQLiteHelper.TABLE_MOVEMENTS,
				cv, SGFDataModelSQLiteHelper.COLUMN_MOVEMENTS_ID + "=?",
				new String[] { movement.get_id() });
		this.close();
		return res;
	}

	// DELETE
	/**
	 * Operacao DELETE - Apaga um movimento identificado pelo parametro de
	 * entrada
	 * 
	 * @param id
	 *            ID do moviemento a eliminar
	 * @return Numero de registos eliminados (Deverá ser 1)
	 */
	public int delete(long id) {
		this.open();
		String _id = Long.toString(id);
		int res = _database.delete(SGFDataModelSQLiteHelper.TABLE_MOVEMENTS,
				SGFDataModelSQLiteHelper.COLUMN_MOVEMENTS_ID + "=?",
				new String[] { _id });
		this.close();
		return res;
	}

	/**
	 * Metodo Helper que com base num Movement passado como parametro de input,
	 * retorna um ContentValue com os dados do movimento
	 * 
	 * @param mv
	 *            Objecto Movement
	 * @return ContentValue contendo a informacao do Movement
	 */
	public static ContentValues toContentValues(Movement mv) {
		ContentValues cv = new ContentValues();
		String mov_id;
		String mov_type_id;
		String mov_cat_id;
		String mov_cat_desc;
		String mov_sub_cat_id;
		String mov_sub_cat_desc;
		String mov_date;
		String mov_desc;
		String mov_state;
		String mov_account_id;
		Double mov_amount;

		if (mv.get_id() != null) {
			mov_id = mv.get_id();
			cv.put(SGFDataModelSQLiteHelper.COLUMN_MOVEMENT_ID, mov_id);
		}
		if (mv.getMov_type_id() != null) {
			mov_type_id = mv.getMov_type_id();
			cv.put(SGFDataModelSQLiteHelper.COLUMN_MOVEMENT_TYPE, mov_type_id);
		}
		if (mv.getMov_cat_id() != null) {
			mov_cat_id = mv.getMov_cat_id();
			cv.put(SGFDataModelSQLiteHelper.COLUMN_MOVEMENT_CATEGORY,
					mov_cat_id);
		}
		if (mv.getMov_cat_desc() != null) {
			mov_cat_desc = mv.getMov_cat_desc();
			cv.put("mov_cat_desc", mov_cat_desc);
		}
		if (mv.getMov_sub_cat_id() != null) {
			mov_sub_cat_id = mv.getMov_sub_cat_id();
			cv.put(SGFDataModelSQLiteHelper.COLUMN_MOVEMENT_SUB_CATEGORY,
					mov_sub_cat_id);
		}
		if (mv.getMov_sub_cat_desc() != null) {
			mov_sub_cat_desc = mv.getMov_sub_cat_desc();
			cv.put("mov_sub_cat_desc", mov_sub_cat_desc);
		}
		if (mv.getMov_date() != null) {
			mov_date = mv.getMov_date();
			cv.put(SGFDataModelSQLiteHelper.COLUMN_MOVEMENT_DATE, mov_date);
		}
		if (mv.getMov_desc() != null) {
			mov_desc = mv.getMov_desc();
			cv.put(SGFDataModelSQLiteHelper.COLUMN_MOVEMENT_DESC, mov_desc);
		}
		if (mv.getMov_state() != null) {
			mov_state = mv.getMov_state();
			cv.put(SGFDataModelSQLiteHelper.COLUMN_MOVEMENT_STATE, mov_state);
		}
		if (mv.getMov_amount() != null) {
			mov_amount = mv.getMov_amount();
			cv.put(SGFDataModelSQLiteHelper.COLUMN_MOVEMENT_AMOUNT, mov_amount);
		}
		if (mv.getMov_account_id() != null) {
			mov_account_id = mv.getMov_account_id();
			cv.put(SGFDataModelSQLiteHelper.COLUMN_MOVEMENT_ACCOUNT_ID,
					mov_account_id);
		}

		return cv;

	}

	/**
	 * Este metodo verifica para um dado ID se o movimento existe ou nao na BD
	 * Local do android
	 * 
	 * @param id
	 *            identificador do movimento existente no SGFWEBAPP
	 * @return true ou false consoante o movimento existe ou não
	 */
	public boolean movementExists(String id) {
		this.open();
		String sql = "select count(1) from "
				+ SGFDataModelSQLiteHelper.TABLE_MOVEMENTS + " where "
				+ SGFDataModelSQLiteHelper.COLUMN_MOVEMENTS_SGF_ID + "= ? ";
		Cursor cur = _database.rawQuery(sql, new String[] { id });
		cur.moveToFirst();
		int res = cur.getInt(0);
		cur.close();
		this.close();

		return res == 1;

	}

	/**
	 * Este metodo verifica para um dado SGF_ID se o movimento existe ou nao na
	 * BD Local do android
	 * 
	 * @param id
	 * @return
	 */
	public boolean LocalMovementExists(String id) {
		this.open();
		String sql = "select count(1) from "
				+ SGFDataModelSQLiteHelper.TABLE_MOVEMENTS + " where "
				+ SGFDataModelSQLiteHelper.COLUMN_MOVEMENT_ID + "= ? ";
		Cursor cur = _database.rawQuery(sql, new String[] { id });
		cur.moveToFirst();
		int res = cur.getInt(0);
		cur.close();
		this.close();

		return res == 1;

	}

	public Map<String, Double> getTotalsOfMovementTypesByAccountId(String accountId) {
		Map<String, Double> map = new HashMap<String, Double>();
		//map.put("Expense", 0.0);
		//map.put("Income", 0.0);

		this.open();
		String sqlStatement = "select  "
				+ SGFDataModelSQLiteHelper.COLUMN_MOVEMENT_TYPE
				+ " as MOV_TYPE," + "		sum( "
				+ SGFDataModelSQLiteHelper.COLUMN_MOVEMENT_AMOUNT
				+ ")    as TOTAL       " + "from "
				+ SGFDataModelSQLiteHelper.TABLE_MOVEMENTS + " where "
				+ SGFDataModelSQLiteHelper.COLUMN_MOVEMENT_ACCOUNT_ID + "='"
				+ accountId + "' group by "
				+ SGFDataModelSQLiteHelper.COLUMN_MOVEMENT_TYPE;

		Cursor cursor = _database.rawQuery(sqlStatement, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Double TOTAL = cursor.getDouble(cursor.getColumnIndex("TOTAL"));
			String MOV_TYPE = cursor.getString(cursor
					.getColumnIndex("MOV_TYPE"));
			if (MOV_TYPE.equals("1")) {
				map.put("Income", TOTAL);
			} else if (MOV_TYPE.equals("2")) {
				map.put("Expense", TOTAL);
			}

			cursor.moveToNext();

		}
		cursor.close();

		this.close();
		return map;
	}

	public ArrayList<CategoryTotal> getCategoryTotal(String accountid) {

		ArrayList<CategoryTotal> arrCatTotal = new ArrayList<CategoryTotal>();

		this.open();
		String sqlStatement = "select                                      		"
				+ "mov."
				+ SGFDataModelSQLiteHelper.COLUMN_MOVEMENT_TYPE
				+ " as MOV_TYPE,"
				+ SGFDataModelSQLiteHelper.COLUMN_MOVEMENT_CATEGORY
				+ " as MOV_CAT_ID,"
				+ SGFDataModelSQLiteHelper.COLUMN_CATEGORT_DESCRIPTION
				+ " CAT_DESC,"
				+ "sum("
				+ SGFDataModelSQLiteHelper.COLUMN_MOVEMENT_AMOUNT
				+ ")"
				+ " as TOTAL "

				+ "from "
				+ SGFDataModelSQLiteHelper.TABLE_MOVEMENTS
				+ " mov, "
				+ SGFDataModelSQLiteHelper.TABLE_CATEGORY
				+ " cat "
				+ " where mov."
				+ SGFDataModelSQLiteHelper.COLUMN_MOVEMENT_CATEGORY
				+ "= cat."
				+ SGFDataModelSQLiteHelper.COLUMN_CATEGORY_ID
				+ " and mov."
				+ SGFDataModelSQLiteHelper.COLUMN_MOVEMENT_STATE
				+ " not in ('E') "
				+ " and mov."
				+ SGFDataModelSQLiteHelper.COLUMN_MOVEMENT_ACCOUNT_ID
				+ "='"
				+ accountid
				+ "' "
				+ "group by mov."
				+ SGFDataModelSQLiteHelper.COLUMN_MOVEMENT_TYPE
				+ ","
				+ SGFDataModelSQLiteHelper.COLUMN_MOVEMENT_CATEGORY
				+ " order by TOTAL desc";

		Cursor cursor = _database.rawQuery(sqlStatement, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			CategoryTotal catTotal = cursorToCategoryTotal(cursor, accountid);
			arrCatTotal.add(catTotal);
			cursor.moveToNext();

		}
		cursor.close();
		this.close();

		return arrCatTotal;

	}

	private CategoryTotal cursorToCategoryTotal(Cursor cursor, String accountId) {
		CategoryTotal catTotal = new CategoryTotal();

		catTotal.setAccount_id(accountId);
		catTotal.setCategory_description(cursor.getString(cursor
				.getColumnIndex("CAT_DESC")));
		catTotal.setCategory_id(cursor.getString(cursor
				.getColumnIndex("MOV_CAT_ID")));
		catTotal.setCategory_total_amount(cursor.getDouble(cursor
				.getColumnIndex("TOTAL")));
		catTotal.setMov_type(cursor.getString(cursor.getColumnIndex("MOV_TYPE")));
		return catTotal;
	}

	/**
	 * Este metodo suporta um conceito de maquina de estados de movimentos, com
	 * base no estado actual e na operacao a efectuar devolve o novo estado
	 * final
	 * 
	 * @param currentState
	 *            Estado actual do Movimento
	 * @param TAG
	 *            TAG idenficadora da operacao corrente a ser efectuada ao
	 *            movimento
	 * @return Retorna novo estado
	 */
	public static String getNextState(String currentState, String TAG) {
		String finalState = currentState;

		if (currentState.equals("N")) {
			if (TAG.equals("POST_API")) {
				finalState = "S"; // synchronized
			}
			if (TAG.equals("DELETE_LOCALLY")) {
				finalState = "X"; // to delete from bd
			}
			if (TAG.equals("UPDATE_LOCALLY")) {
				finalState = "N"; // new - terá que ser invocado POST Movement
									// da API
			}
		} else if (currentState.equals("S")) {
			if (TAG.equals("UPDATE_LOCALLY")) {
				finalState = "D"; // dirty - terá que ser invocado PUT Movement
									// da API
			}
			if (TAG.equals("DELETE_LOCALLY")) {
				finalState = "E"; // erased - terá que ser invocado DELETE
									// Movement da API
			}
		} else if (currentState.equals("D")) {
			if (TAG.equals("PUT_API")) {
				finalState = "S"; // synchronized
			}
			if (TAG.equals("UPDATE_LOCALLY")) {
				finalState = "D"; // dirty - terá que ser invocado PUT Movement
									// da API
			}
			if (TAG.equals("DELETE_LOCALLY")) {
				finalState = "E"; // erased - terá que ser invocado DELETE
									// Movement da API
			}
		}
		return finalState;
	}

}