package com.whitemonk_team.livewallpapers.listners;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.whitemonk_team.livewallpapers.activities.CropActivity;
import com.whitemonk_team.livewallpapers.activities.ImagePagerActivity;
import com.whitemonk_team.livewallpapers.databases.DbAdapter;
import com.whitemonk_team.livewallpapers.objects.ImageDto;
import com.whitemonk_team.livewallpapers.objects.UILApplication;
import com.whitemonk_team.livewallpapers.objects.Constants.Extra;
import com.whitemonk_team.livewallpapers.utils.StringUtils;

public class OnGridViewClickListner implements GridView.OnItemClickListener
{
	
	Activity activity;
	ArrayList<String> imageUrls;
	
	public OnGridViewClickListner() {}
	
	public OnGridViewClickListner(Activity activity, ArrayList<String> imageUrls)
	{
		this.activity = activity;
		this.imageUrls = imageUrls;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) 
	{
		startImagePagerActivity(position);

	}
	
	private void startImagePagerActivity(int position) {
		Intent intent = new Intent(activity, ImagePagerActivity.class);
		intent.putExtra(Extra.IMAGES, imageUrls);
		intent.putExtra(Extra.IMAGE_POSITION, position);
		activity.startActivity(intent);
	}
	
	private DbAdapter getDbAdapter()
	{
		return ((UILApplication)activity.getApplicationContext()).dbAdapter;
	}
	
	

}
