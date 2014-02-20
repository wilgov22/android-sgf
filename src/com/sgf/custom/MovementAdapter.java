package com.sgf.custom;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sgf.entities.Movement;

/**Classe MovementAdapter para alimentar um ListAdapter com rows personalizadas
 * @author wgovindji
 * 
 */
public class MovementAdapter extends BaseAdapter {
	private final Context context;
	private final List<Movement> movements;
	
	public MovementAdapter(Context ctx,List<Movement> movs){
		this.context=ctx;
		this.movements=movs;
		
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return this.movements.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return this.movements.get(position);
	}

	@Override
	public long getItemId(int position) {
		return Long.parseLong(this.movements.get(position).get_id());
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Movement mv=movements.get(position);
		return new MovementListView(context, mv, parent) ;
	}
	

	

	
	
	private final class MovementListView extends LinearLayout{
		
		private TextView amount;
		private TextView categories;
		private TextView description;
		private TextView date;
		private TextView state;
		
		public MovementListView(Context ctx, Movement movement,ViewGroup parent){
			super(ctx);
			boolean isExpense=false;
			
			View.inflate(ctx, com.sgf.activities.R.layout.single_row_movement_layout, this);
			
			if(movement.getMov_type_id().equals("2")){
				isExpense=true;
			}
			amount = (TextView) findViewById(com.sgf.activities.R.id.amountTv);
			categories = (TextView)findViewById(com.sgf.activities.R.id.categoriesTv);
			date= (TextView) findViewById(com.sgf.activities.R.id.dateTv);
			description=(TextView) findViewById(com.sgf.activities.R.id.descriptionTv);
			state= (TextView) findViewById(com.sgf.activities.R.id.stateTv);

			amount.setText(movement.getMov_amount().toString());
			categories.setText(movement.getMov_cat_desc() + ">" + movement.getMov_sub_cat_desc());
			date.setText(movement.getMov_date());
			description.setText(movement.getMov_desc());
			state.setText(movement.getMov_state());
			
			if(isExpense){
				amount.setTextColor(android.graphics.Color.RED);
			}else {
				amount.setTextColor(android.graphics.Color.GREEN);
			}

			
			
			
			
		}
	}


}
