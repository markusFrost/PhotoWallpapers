package com.whitemonk_team.livewallpapers.activities;

import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;

import com.whitemonk_team.livewallpapers.draw.Draw;
import com.whitemonk_team.livewallpapers.objects.Constants.Extra;
import com.whitemonk_team.livewallpapers.utils.StringUtils;
import com.whitemonk_team.livewallpapers1.R;

public class CropActivity extends Activity
{

	private static boolean isBack = false;
	
	private Draw draw;
	static String path = null;
	static HashMap<String, Rect> mapImg;
	boolean wasRotation;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
// сделать вариант чтобы можно было показывать всю область
		
		wasRotation = getIntent().getBooleanExtra(Extra.WAS_ROTATION, false);
		if ( path == null)
		{
		  path = getIntent().getStringExtra(Extra.IMAGE_URL);
		}
		
		if ( mapImg == null)
		{
		   mapImg = (HashMap<String, Rect>) getIntent().getSerializableExtra(Extra.IMAGE_HM);
		}

		if (mapImg == null || mapImg.isEmpty())
		{
			draw = new Draw(this, path);
			mapImg = new HashMap<>();
		}
		else
		{
			String key = StringUtils.getOrientation(this);
			if ( mapImg.containsKey(key))
			{
				Rect rect = mapImg.get(key);
				draw = new Draw(this, path, rect,wasRotation);
			}
			else
			{
				draw = new Draw(this, path);
			}
			//
		}
		
		
		setContentView(draw);
		
		setScreenOrientation(false);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.crop, menu);
		return true;
	}
	
	MenuItem menuBack, menuNext, menuFinish;
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) 
	{
		menuBack = menu.findItem(R.id.menu_back);
		menuNext = menu.findItem(R.id.menu_next);
		menuFinish = menu.findItem(R.id.menu_finish);
		
		if ( isBack == false)
		{
			menuNext.setVisible(true);
			menuBack.setVisible(false);
			menuFinish.setVisible(false);
		}
		else
		{
			menuNext.setVisible(false);
			menuBack.setVisible(true);
			menuFinish.setVisible(true);
		}
		
		return true;
	}
	
	

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		
		clearAll();
		finish();
		
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		switch (id) 
		{
		
		case android.R.id.home:
		{
			clearAll();
			finish();
			return true;
		}
		
		
		
		    case R.id.menu_back: 
		      {
		    	 
		    	  isBack = false;
		    	  String key = StringUtils.getOrientation(this);
		  		
		  		mapImg.put(key, draw.getRectangle());
		  		 setScreenOrientation(true);
			       return true;
		      }
		    case R.id.menu_next: 
		      {
		    	  isBack = true;
		    	  String key = StringUtils.getOrientation(this);
		  		
		  		mapImg.put(key, draw.getRectangle());
		  		setScreenOrientation(true);
			       return true;
		      }
		    case R.id.menu_finish: 
		    {
		    	  sendAllData();
			      return true;
		    }
//		    case R.id.menu_rotate :
//		    {
//		    	draw.changeOrientation();
//		    	return true;
//		    }
		}

		return super.onOptionsItemSelected(item);
	}
	
	private void sendAllData() 
	{
		
		
		String key = StringUtils.getOrientation(this);
		
		mapImg.put(key, draw.getRectangle());
	
		Intent intent = new Intent();
		intent.putExtra(Extra.IMAGE_URL, path);
		intent.putExtra(Extra.IMAGE_HM, mapImg);
		intent.putExtra(Extra.WAS_ROTATION, draw.wasRotation);

		setResult(RESULT_OK, intent);
		clearAll();
		finish();

	}
	
	private void clearAll()
	{
		isBack = false;
		mapImg = null;
		path = null;
	}

	private void setScreenOrientation(boolean isChange)
	{
	    Display getOrient = getWindowManager().getDefaultDisplay();
	 
	    if(getOrient.getWidth()==getOrient.getHeight())
	    {
	       // orientation = Configuration.ORIENTATION_SQUARE;
	    } else
	    { 
	        if(getOrient.getWidth() < getOrient.getHeight())
	        {
	            //orientation = Configuration.ORIENTATION_PORTRAIT;
	        	if ( isChange) // нужно поменять
	        	{
	        		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
	        	}
	        	else
	        	{
	        		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	        	}
	        	
	        }else 
	        {
	            // orientation = Configuration.ORIENTATION_LANDSCAPE;
	        	if ( isChange) // нужно поменять
	        	{
	        		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	        	}
	        	else
	        	{
	        		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
	        	}
	        }
	    }
	    
	}
}
