package com.sgf.entities;

/**Classe Account com respectivos getters e setters
 * @author wgovindji
 * 
 */
public class Account {
	private String _id;
	private String sgf_account_id;
	private String account_number;
	private String account_name;
	private Double account_balance;
	private String account_other_info;
	private String account_active;
	private String account_bank_name;
	public String getAccount_bank_name() {
		return account_bank_name;
	}
	public void setAccount_bank_name(String account_bank_name) {
		this.account_bank_name = account_bank_name;
	}
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	public String getSgf_account_id() {
		return sgf_account_id;
	}
	public void setSgf_account_id(String sgf_account_id) {
		this.sgf_account_id = sgf_account_id;
	}
	public String getAccount_number() {
		return account_number;
	}
	public void setAccount_number(String account_number) {
		this.account_number = account_number;
	}
	public String getAccount_name() {
		return account_name;
	}
	public void setAccount_name(String account_name) {
		this.account_name = account_name;
	}
	public Double getAccount_balance() {
		return account_balance;
	}
	public void setAccount_balance(Double account_balance) {
		this.account_balance = account_balance;
	}
	public String getAccount_other_info() {
		return account_other_info;
	}
	public void setAccount_other_info(String account_other_info) {
		this.account_other_info = account_other_info;
	}
	public String getAccount_active() {
		return account_active;
	}
	public void setAccount_active(String account_active) {
		this.account_active = account_active;
	}
	
	
}
