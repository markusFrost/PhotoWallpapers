package com.whitemonk_team.livewallpapers.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class ShPreferencesUtils 
{
	private static String FILE_NAME = "WasCopy";
	public static final String DB_FLAG = "com.nostra13.example.universalimageloader.DBfLAG";
	public static final String DB_TABLE_NAME = "com.nostra13.example.universalimageloader.DB_NAME";
	public static final String DB_TABLE_POSITION = "com.nostra13.example.universalimageloader.DB_POSITION";
	public static final String DO_NOT_SHOW_FLAG = "DO_NOT_SHOW_FLAG";
	public static final String TIME_ACTIVE = "timeActive";
	public static final String CAMERA_ACTIVE = "cameraActive";
	public static final String CLICK_COUTNT = "CLICK_COUTNT";
	public static final String IS_SHOW_GUIDE_LIVE_WALLPAPERS = "com.whitemonk_team.livewallpapers.utils.ShPreferencesUtils.IS_SHOW_GUIDE_LIVE_WALLPAPERS";
	public static final String DEVICE_NAME = "com.whitemonk_team.livewallpapers.utils.ShPreferencesUtils.DEVICE_NAME";
	public static void save(Context context, boolean wasCopy)
	{
		
		 SharedPreferences sd = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
		   SharedPreferences.Editor  editor = sd.edit();
		   editor.putBoolean(DB_FLAG, wasCopy);
		   editor.commit();
	}
	
	public static boolean getFlag(Context context)
	{
		SharedPreferences sd = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
		 boolean wasCopy =  sd.getBoolean(DB_FLAG, false);
		   return wasCopy;
	}
	
	public static void setTablePosition(Context context, int pos)
	{
		
		 SharedPreferences sd = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
		   SharedPreferences.Editor  editor = sd.edit();
		   editor.putInt(DB_TABLE_POSITION, pos);
		   editor.commit();
	}
	
	public static int getTablePosition(Context context)
	{
		SharedPreferences sd = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        return sd.getInt(DB_TABLE_POSITION, 0);
	}
	public static void setTableName(Context context, String tableName)
	{
		
		 SharedPreferences sd = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
		   SharedPreferences.Editor  editor = sd.edit();
		   editor.putString(DB_TABLE_NAME, tableName);
		   editor.commit();
	}
	
	public static String getTableName(Context context)
	{
		SharedPreferences sd = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
		 String tableName =  sd.getString(DB_TABLE_NAME, "");
		   return tableName;
	}
	
	public static  void setNotiDoNotShowFlag(Context context, boolean fl)
	{
		 SharedPreferences sd = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
		   SharedPreferences.Editor  editor = sd.edit();
		   editor.putBoolean(DO_NOT_SHOW_FLAG, fl);
		   editor.commit();
	}
	
	public static  boolean getNotiDoNotShowFlag(Context context)
	{
		SharedPreferences sd = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
		return sd.getBoolean(DO_NOT_SHOW_FLAG, true);
	
	}
	
	public static  void setTimeActive(Context context, boolean fl)
	{
		 SharedPreferences sd = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
		   SharedPreferences.Editor  editor = sd.edit();
		   editor.putBoolean(TIME_ACTIVE, fl);
		   editor.commit();
	}
	
	public static  boolean getTimeActive(Context context)
	{
		SharedPreferences sd = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
		return sd.getBoolean(TIME_ACTIVE, false);
	
	}
	
	public static  void setCameraActive(Context context, boolean fl)
	{
		 SharedPreferences sd = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
		   SharedPreferences.Editor  editor = sd.edit();
		   editor.putBoolean(CAMERA_ACTIVE, fl);
		   editor.commit();
	}
	
	public static  boolean getCameraActive(Context context)
	{
		SharedPreferences sd = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
		return sd.getBoolean(CAMERA_ACTIVE, false);
	}
	
	public static  void setClickCount(Context context,  int count)
	{
		 SharedPreferences sd = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
		   SharedPreferences.Editor  editor = sd.edit();
		   editor.putInt(CLICK_COUTNT, count);
		   editor.commit();
	}
	
	public static  int getClickCount(Context context)
	{
		SharedPreferences sd = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
		return sd.getInt(CLICK_COUTNT, 0);
	}
	
	public static void setNotifLiveWallpapers(Context context, boolean fl)
	{
		 SharedPreferences sd = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
		   SharedPreferences.Editor  editor = sd.edit();
		   editor.putBoolean(IS_SHOW_GUIDE_LIVE_WALLPAPERS, fl);
		   editor.commit();
	}
	public static  boolean getNotifLiveWallpapers(Context context)
	{
		SharedPreferences sd = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
		return sd.getBoolean(IS_SHOW_GUIDE_LIVE_WALLPAPERS, true);
	}
	
	public static void setDeviceName(Context context, String name)
	{
		 SharedPreferences sd = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
		   SharedPreferences.Editor  editor = sd.edit();
		   editor.putString(DEVICE_NAME, name);
		   editor.commit();
	}
	public static  String getDeviceName(Context context)
	{
		SharedPreferences sd = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
		return sd.getString(DEVICE_NAME, "");
	}
	

}
