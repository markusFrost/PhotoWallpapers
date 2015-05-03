package com.whitemonk_team.livewallpapers.utils;

import java.util.ArrayList;

import com.whitemonk_team.livewallpapers.adapters.ImageAdapter;
import com.whitemonk_team.livewallpapers.databases.DbAdapter;
import com.whitemonk_team.livewallpapers.objects.UILApplication;
import com.whitemonk_team.livewallpapers.objects.Constants.Extra;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.net.Uri;
import android.view.Display;
import android.widget.AdapterView;


public class StringUtils 
{
     public static String getPathFromUri(Activity activity,Intent data)
     {
    	// получаем адрес фотографии
    				Uri _uri = data.getData();

    				// User had pick an image.
    				Cursor cursor = activity.getContentResolver()
    						.query(_uri,
    								new String[] { android.provider.MediaStore.Images.ImageColumns.DATA },
    								null, null, null);
    				cursor.moveToFirst();

    				// Link to the image
    				 
    				String  path =  "file:/" + cursor.getString(0);
    				
    				if ( cursor.getString(0) == null)
    				{
    					path = null;
    				}
    			
    				cursor.close();
    				
    				return path;
     }

	public static String getOrientation(Activity activity) 
	{
	    Display getOrient = activity.getWindowManager().getDefaultDisplay();
		 
	    if(getOrient.getWidth()==getOrient.getHeight())
	    {
	       // orientation = Configuration.ORIENTATION_SQUARE;
	    	return null;
	    } else
	    { 
	        if(getOrient.getWidth() < getOrient.getHeight())
	        {
	            //orientation = Configuration.ORIENTATION_PORTRAIT;
	        	return Extra.ORIENTATION_PORTRAT;	        	
	        }else 
	        {
	            // orientation = Configuration.ORIENTATION_LANDSCAPE;
	        	return Extra.ORIENTATION_LANDSCAPE;
	        }
	    }
	}
	
	public static Integer getNeedId(String tableName, AdapterView<?> parent, int position, String path, Activity activity)
	{
		ArrayList<Integer> listDB = getDbAdapter(activity).getDataBasesIds(tableName, path);
 		
 		if ( listDB == null)
 		{
 			// то есть удалить в базе данных только один такой путь
 			
 			return null;
 			
 		}
 		else
 		{
 			ArrayList<Integer> listAdapters = ((ImageAdapter) parent.getAdapter()).getAdaptersIds(path);
 		 // узнаём позицию элемента по которому мы нажали в адаптере
 			
 			int posInAdapter = findPosition(listAdapters, position);
 			
 			int _id = listDB.get(posInAdapter);
 			
 			return _id;
 		
 		}
	}
	
	private static DbAdapter getDbAdapter(Activity activity)
	{
		return ((UILApplication)activity.getApplicationContext()).dbAdapter;
	}
	
	private static int findPosition(ArrayList<Integer> list, Integer position)
	{
		for ( int i = 0; i < list.size(); i++)
		{
			if ( list.get(i).equals(position) )
			{
				return i;
			}
		}
		return -1;
	}
	
	public static String getNextNableName(DbAdapter db, String tableName)
	{
		ArrayList<String> list = db.getTableNameList();
		if ( list.size() < 2)
		{
			return null;
		}
		else
		{
			int pos = 0;
			for ( pos = 0; pos < list.size(); pos++)
			{
				if ( list.get(pos).equals(tableName))
				{
					break;
				}
			}
			
			if (pos == 0)
			{
				return list.get(pos + 1);
			}
			else
			{
				return list.get(pos - 1);
			}
		}
	}
}
