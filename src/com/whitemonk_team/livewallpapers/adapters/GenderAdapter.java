package com.whitemonk_team.livewallpapers.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.whitemonk_team.livewallpapers.objects.UILApplication;
import com.whitemonk_team.livewallpapers1.R;

public class GenderAdapter extends BaseAdapter {

	Activity context;
	String[] list;
	public GenderAdapter (Activity context, String[] list)
	{
		this.context = context;
		this.list = list;
	}
	
	@Override
	public int getCount() {
		return list.length;
	}

	@Override
	public Object getItem(int position)
	{
		return list[position];
	}

	@Override
	public long getItemId(int position) 
	{
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
			convertView = inflater.inflate(R.layout.gender_item,
					parent, false);

			ViewHolder viewHolder = new ViewHolder();

			viewHolder.tv = (TextView) convertView
					.findViewById(R.id.gender_item_tv);
			viewHolder.iv = (ImageView) convertView
					.findViewById(R.id.gender_item_iv);

			convertView.setTag(viewHolder);
		}
			ViewHolder holder = (ViewHolder) convertView.getTag();
			holder.tv.setText(list[position]);
			switch(position)
			{
			   case UILApplication.GALLERY_TYPE :
			    {
				   holder.iv.setImageDrawable(
						   context.getResources().getDrawable(R.drawable.ic_galery)
						   );
			    }break;
			   case UILApplication.CAMERA_TYPE :
			    {
			    	holder.iv.setImageDrawable(
							   context.getResources().getDrawable(R.drawable.ic_camera)
							   );
			    }break;
			}


		
		
		return convertView;
	}
	
	static class ViewHolder
	{
		public TextView tv;
		public ImageView iv;
	}

}
