package com.whitemonk_team.livewallpapers.activities;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.whitemonk_team.livewallpapers.adapters.MagazinAdapter;
import com.whitemonk_team.livewallpapers.enums.TrackerName;
import com.whitemonk_team.livewallpapers.objects.MagazinDto;
import com.whitemonk_team.livewallpapers.objects.UILApplication;
import com.whitemonk_team.livewallpapers1.R;

public class MagazinActivity extends Activity 
{
	ArrayList<MagazinDto> list = new ArrayList<>();
	MagazinAdapter adapter;
	ActionBar actionBar;
	ListView lv;
	
	  private static AdRequest adRequest;
		
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_magazin);
		actionBar = getActionBar();
		actionBar.setTitle(R.string.str_settings);
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		String pathAnalytic = "com.whitemonk_team.livewallpapers.activities.MagazinActivity";
		// Get tracker.
        Tracker t = ((UILApplication)this.getApplicationContext()).getTracker(
            TrackerName.APP_TRACKER);

        // Set screen name.
        // Where path is a String representing the screen name.
        t.setScreenName(pathAnalytic);

        // Send a screen view.
        t.send(new HitBuilders.AppViewBuilder().build());
		 AdView adView = (AdView)this.findViewById(R.id.adView);
		 
		 if (isNetworkAvailable())
		 {
			 adView.setVisibility(View.VISIBLE);
		 }
		 else
		 {
			 adView.setVisibility(View.GONE);
		 }
		 if (adRequest == null)
		 {
	         adRequest = new AdRequest.Builder().build();
		 }
	        adView.loadAd(adRequest);
	        
	      
	    
	     
		
		lv = (ListView) findViewById(R.id.lvMagazin);
		
		//getListView().setBackgroundResource(R.drawable.congruent_pentagon);
//		MagazinDto md1 = new MagazinDto();
//		md1.DrawableId = R.drawable.ic_settings;
//		md1.Name = getResources().getString(R.string.magazin_item_add_time);
//		md1.IsActivated = ShPreferencesUtils.getTimeActive(getApplicationContext());
//		list.add(md1);
		
//		MagazinDto md2 = new MagazinDto();
//		md2.DrawableId = R.drawable.ic_camera;
//		md2.Name = getResources().getString(R.string.magazin_item_add_from_camera);
//		md2.IsActivated = ShPreferencesUtils.getCameraActive(getApplicationContext());
//		list.add(md2);
		
		MagazinDto md3 = new MagazinDto();
		md3.DrawableId = R.drawable.ic_background;
		md3.Name = getResources().getString(R.string.Choose_other);
		md3.IsActivated = true;
		
		
		
		
		list.add(md3);
		
		adapter = new MagazinAdapter(MagazinActivity.this, list);
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				//if ( position == 0)
//					{
//						ShPreferencesUtils.setTimeActive(getApplicationContext(), list.get(position).IsActivated);
//						list.get(position).IsActivated = !list.get(position).IsActivated;
//						adapter.notifyDataSetChanged();
//					}
//					else if ( position == 1)
//					{
//						ShPreferencesUtils.setCameraActive(getApplicationContext(), list.get(position).IsActivated);
//						list.get(position).IsActivated = !list.get(position).IsActivated;
//						adapter.notifyDataSetChanged();
//					}
					 if (position == 0)
					{
						 Intent intent = new Intent(Intent.ACTION_SET_WALLPAPER);
				    	 startActivityForResult(Intent.createChooser(intent, "Select Wallpaper"), 10);
					}
				
			}
		});
		
	}
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.magazin, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == android.R.id.home) 
		{
			close();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onBackPressed() {
		
		super.onBackPressed();
		close();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	
		close();
		
		return super.onKeyDown(keyCode, event);
	}
	
	private void close()
	{
		finish();
		overridePendingTransition(R.anim.pull_in_left,
				R.anim.push_out_right);	
	}
	
	private boolean isNetworkAvailable() {
	    ConnectivityManager connectivityManager 
	          = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}
	
	

}
