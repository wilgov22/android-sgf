package com.sgf.entities;

public class CategoryTotal{
	public String getAccount_id() {
		return account_id;
	}

	public void setAccount_id(String account_id) {
		this.account_id = account_id;
	}

	public String getCategory_id() {
		return category_id;
	}

	public void setCategory_id(String category_id) {
		this.category_id = category_id;
	}

	public String getCategory_description() {
		return category_description;
	}

	public void setCategory_description(String category_description) {
		this.category_description = category_description;
	}

	public double getCategory_total_amount() {
		return category_total_amount;
	}

	public void setCategory_total_amount(double category_total_amount) {
		this.category_total_amount = category_total_amount;
	}
	
	public String getMov_type() {
		return mov_type;
	}

	public void setMov_type(String mov_type) {
		this.mov_type = mov_type;
	}


	private String account_id;
	private String category_id;
	private String category_description;
	private double category_total_amount;

	
	
	private String mov_type;
	
	public CategoryTotal(){
		
		
	}
	
	
	
	
} 

