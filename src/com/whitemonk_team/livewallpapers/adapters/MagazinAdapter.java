package com.whitemonk_team.livewallpapers.adapters;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.whitemonk_team.livewallpapers.objects.MagazinDto;
import com.whitemonk_team.livewallpapers1.R;

public class MagazinAdapter extends BaseAdapter
{

	ArrayList<MagazinDto> list;
	Activity context;
	public MagazinAdapter(Activity context, ArrayList<MagazinDto> list)
	{
		this.context = context;
		this.list = list;
	}
	@Override
	public int getCount() 
	{
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		if (convertView == null) 
		{
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.magazin_item,
					parent, false);

			ViewHolder viewHolder = new ViewHolder();

			viewHolder.tvName = (TextView) convertView
					.findViewById(R.id.magazin_item_tvName);
			viewHolder.tvState = (TextView) convertView
					.findViewById(R.id.magazin_item_tvState);
			viewHolder.iv = (ImageView) convertView
					.findViewById(R.id.magazin_item_iv);

			convertView.setTag(viewHolder);
		}
		ViewHolder holder = (ViewHolder) convertView.getTag();
		holder.tvName.setText(list.get(position).Name);
		 holder.iv.setImageDrawable(
				   context.getResources().getDrawable(list.get(position).DrawableId)
				   );
		 if ( list.get(position).IsActivated)
		 {
			 holder.tvState.setText(R.string.magazin_item_on);
			 holder.tvState.setTextColor(Color.BLUE);
		 }
		 else
		 {
			 holder.tvState.setText(R.string.magazin_item_off);
			 holder.tvState.setTextColor(Color.RED);
		 }
		 
		 return convertView;
	}
	
	static class ViewHolder
	{
		public ImageView iv;
		public TextView tvName;
		public TextView tvState;
	}

}
