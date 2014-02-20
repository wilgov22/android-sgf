package com.sgf.entities;

/** Classe Movement com getters e setters
 * @author wgovindji
 * 
 */
public class Movement {

	private String _id;
	private String sgf_mov_id;
	private String mov_type_id;
	private String mov_cat_id;
	private String mov_sub_cat_id;
	private Double mov_amount;
	private String mov_date;
	private String mov_desc;
	private String mov_state;
	private String mov_cat_desc;
	private String mov_sub_cat_desc;
	private String mov_account_id;

	public String getMov_account_id() {
		return mov_account_id;
	}

	public void setMov_account_id(String mov_account_id) {
		this.mov_account_id = mov_account_id;
	}

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String getMov_type_id() {
		return mov_type_id;
	}

	public void setMov_type_id(String mov_type_id) {
		this.mov_type_id = mov_type_id;
	}

	public String getMov_cat_id() {
		return mov_cat_id;
	}

	public void setMov_cat_id(String mov_cat_id) {
		this.mov_cat_id = mov_cat_id;
	}

	public String getMov_sub_cat_id() {
		return mov_sub_cat_id;
	}

	public void setMov_sub_cat_id(String mov_sub_cat_id) {
		this.mov_sub_cat_id = mov_sub_cat_id;
	}

	public Double getMov_amount() {
		return mov_amount;
	}

	public void setMov_amount(Double mov_amount) {
		this.mov_amount = mov_amount;
	}

	public String getMov_date() {
		return mov_date;
	}

	public void setMov_date(String mov_date) {
		this.mov_date = mov_date;
	}

	public String getMov_desc() {
		return mov_desc;
	}

	public void setMov_desc(String mov_desc) {
		this.mov_desc = mov_desc;
	}

	public String getMov_state() {
		return mov_state;
	}

	public void setMov_state(String mov_state) {
		this.mov_state = mov_state;
	}

	@Override
	public String toString() {

		return "Mov_id=" + get_id();
	}

	public String getMov_cat_desc() {
		return mov_cat_desc;
	}

	public void setMov_cat_desc(String mov_cat_desc) {
		this.mov_cat_desc = mov_cat_desc;
	}

	public String getMov_sub_cat_desc() {
		return mov_sub_cat_desc;
	}

	public void setMov_sub_cat_desc(String mov_sub_cat_desc) {
		this.mov_sub_cat_desc = mov_sub_cat_desc;
	}

	public String getSgf_mov_id() {
		return sgf_mov_id;
	}

	public void setSgf_mov_id(String sgf_mov_id) {
		this.sgf_mov_id = sgf_mov_id;
	}
}

/*
 * public static final String COLUMN_MOVEMENTS_ID="_id"; public static final
 * String COLUMN_MOVEMENT_TYPE="mov_type_id"; public static final String
 * COLUMN_MOVEMENT_CATEGORY="mov_cat_id"; public static final String
 * COLUMN_MOVEMENT_SUB_CATEGORY="mov_sub_cat_id"; public static final String
 * COLUMN_MOVEMENT_AMOUNT="mov_amount"; public static final String
 * COLUMN_MOVEMENT_DESC="mov_desc"; public static final String
 * COLUMN_MOVEMENT_DATE="mov_date"; public static final String
 * COLUMN_MOVEMENT_STATE="mov_state";
 */