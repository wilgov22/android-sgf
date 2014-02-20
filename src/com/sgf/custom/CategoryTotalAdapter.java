package com.sgf.custom;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sgf.entities.CategoryTotal;
import com.sgf.entities.Movement;

/**
 * Classe CategoryTotalAdapter para alimentar um ListAdapter com rows personalizadas
 * 
 * @author wgovindji
 * 
 */
public class CategoryTotalAdapter extends BaseAdapter {
	private final Context context;
	private final ArrayList<CategoryTotal> categoryTotalArr;

	public CategoryTotalAdapter(Context ctx, ArrayList<CategoryTotal> movs) {
		this.context = ctx;
		this.categoryTotalArr = movs;

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return this.categoryTotalArr.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return this.categoryTotalArr.get(position);
	}

	@Override
	public long getItemId(int position) {
		return Long.parseLong(this.categoryTotalArr.get(position)
				.getCategory_id());
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		CategoryTotal ct = categoryTotalArr.get(position);
		return new CategoryTotalListView(context, ct, parent);
	}

	private final class CategoryTotalListView extends LinearLayout {

		private TextView category;
		private TextView categoryAmount;

		public CategoryTotalListView(Context ctx, CategoryTotal ct,
				ViewGroup parent) {
			super(ctx);
			boolean isExpense = false;

			View.inflate(
					ctx,
					com.sgf.activities.R.layout.single_row_category_total_layout,
					this);

			if (ct.getMov_type().equals("2")) {
				isExpense = true;
			}

			category = (TextView) findViewById(com.sgf.activities.R.id.categoryTv);
			categoryAmount = (TextView) findViewById(com.sgf.activities.R.id.categoryAmountTv);

			category.setText(ct.getCategory_description());
			categoryAmount
					.setText(String.valueOf(ct.getCategory_total_amount()));

			if (isExpense) {
				categoryAmount.setTextColor(android.graphics.Color.RED);
			} else {
				categoryAmount.setTextColor(android.graphics.Color.GREEN);
			}

		}
	}

}
