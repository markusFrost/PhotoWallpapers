package com.whitemonk_team.livewallpapers.databases;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.whitemonk_team.livewallpapers.objects.ImageDto;
import com.whitemonk_team.livewallpapers.objects.UILApplication;
import com.whitemonk_team.livewallpapers1.R;

public class DbAdapter {

	public static final String DB_NAME = "photo_wallp.db";
	public static final int DB_VERSION = 1;

	private DbHelper dbHelper;
	private Context context;
	private SQLiteDatabase db;
	
	public static String IMAGES_PATH = "path";
	//public static String TABLE_NAME = "bmpTable";
	public static String IMAGES_ID = "_id";
	public static String IMAGES_JSON = "json";
	public static String WAS_ROTATION = "isFromCamera";
	
	
	
	public DbAdapter(Context context){
		this.context = context;
		dbHelper = new DbHelper(context);
		db = dbHelper.getReadableDatabase();
	}
	
	
	public void addImage(String tableName,  ImageDto imageDto)
	{
		String json = new Gson().toJson(imageDto);
		ContentValues cv = new ContentValues();
		cv.put(IMAGES_PATH, imageDto.Path);
		cv.put(WAS_ROTATION, imageDto.WasRotation );
		
		cv.put(IMAGES_JSON, json);
		
	long id = 	db.insert(tableName, null, cv);

	
	//Toast.makeText(context, id + "", Toast.LENGTH_SHORT).show();
	}
	
	public void updateImage(String tableName, ImageDto imageDto)
	{
//		String sql = "Select " + IMAGES_ID + " , " +  IMAGES_IS_FROM_CAMERA +
//		" from " + tableName + " where " + IMAGES_ID + " = " + UILApplication._Id;
//		 
//		Cursor c = db.rawQuery(sql, null);
//		imageDto.IsFromCamera = false;
//		if ( c.moveToFirst())
//		{
//			imageDto.IsFromCamera = c.getInt(c.getColumnIndex(IMAGES_IS_FROM_CAMERA)) > 0;
//		}
		String json = new Gson().toJson(imageDto);
		ContentValues cv = new ContentValues();
		cv.put(IMAGES_JSON, json);
		cv.put(WAS_ROTATION, imageDto.WasRotation);
		
		long l = db.update(tableName, cv, IMAGES_ID + " = ?",
		          new String[] { UILApplication._Id + "" });

	
	//Toast.makeText(context, l + "", Toast.LENGTH_SHORT).show();
	}
	
	public  int getCount(String tableName)
	{
		String sql = "Select " + IMAGES_ID + " from " + tableName;
		int count =  -1;
		try
		{
		   Cursor c = db.rawQuery(sql, null);
		   count = c.getCount();
		} catch ( Exception e)
		{
			
		}
		
		return count;
		
		
	}
	
	public ImageDto getImageDto(String tableName, int position)
	{
		String sql = "Select * from " +  tableName + 
				" limit " + position  + " , " + 1;
		Cursor c = db.rawQuery(sql, null);
		
		c.moveToFirst();
		
		String json = c.getString(c.getColumnIndex(IMAGES_JSON));
		
		
		ImageDto imageDto = new Gson().fromJson(json, 
	    		new TypeToken<ImageDto>() {
	          }.getType()
	          
		);
		
		UILApplication._Id = c.getInt(c.getColumnIndex(IMAGES_ID));
		return imageDto;
	}
	
	public ImageDto getImageDto(String tableName, int position, int count)
	{
		String sql = "Select * from " +  tableName + 
				" limit " + position  + " , " + 1;
		Cursor c = db.rawQuery(sql, null);
		
		c.moveToFirst();
		
        String json = c.getString(c.getColumnIndex(IMAGES_JSON));
		
		
		ImageDto imageDto = new Gson().fromJson(json, 
	    		new TypeToken<ImageDto>() {
	          }.getType()
	          
		);
		
		return imageDto;
	}
	
	
	
//	public ArrayList<String> getList(String tableName)
//	{
//		ArrayList<String> listPaths = new ArrayList<>();
//		
//		String sql = "Select * from " +  tableName;
//		
//		Cursor c = db.rawQuery(sql, null);
//		
//		if ( c.moveToFirst())
//		{
//			do
//			{
//				int id = c.getInt(c.getColumnIndex(IMAGES_ID));
//				//System.out.println("id = " + id);
//				String path = c.getString(c.getColumnIndex(IMAGES_PATH));
//				listPaths.add(path);
//			}while ( c.moveToNext());
//		}
//		
//		return listPaths;
//	}
/*	public void deleteItem(String tableName, int id)
	{
		
		db.delete(tableName, IMAGES_ID + " = " + id, null);
	}
	
	public void deleteItem(String tableName, String path)
	{
		if ( !path.contains("file:/"))
		{
		   path = "file:/" + path;
		}
		path = "'" + path + "'";
		long id = db.delete(tableName, IMAGES_PATH + " = " + path , null);
	
		System.out.println("ID = " + id);
	}*/
	
	public void deleteImage(String tableName, int pos)
	{
		String sql = "Select * from " +  tableName + 
				" limit " + pos  + " , " + 1;

		Cursor c = db.rawQuery(sql, null);
		c.moveToFirst();
		db.delete(tableName, IMAGES_ID + " = " + 
		c.getInt(c.getColumnIndex(IMAGES_ID)), 
		null);
	}
	
	public ArrayList<String> getTableNameList()
	{
		ArrayList<String> list = new ArrayList<>();
		String sql = "select * from sqlite_master where type = 'table'";
		Cursor c = db.rawQuery(sql, null);
		
		if ( c.moveToFirst())
		{
			do
			{
				String name = c.getString(c.getColumnIndex("name"));
				if ( name.equals("sqlite_sequence") || name.equals("android_metadata"))
				{
					
				}
				else
				{
				   list.add(name);
				}
			}while(c.moveToNext());
		}
		
		
		return list;
	}
	
	public ArrayList<String> getList(String tableName)
	{
		ArrayList<String> listPaths = new ArrayList<>();
		
		String sql = "Select * from " + tableName;
		
		Cursor c = db.rawQuery(sql, null);
		
		if ( c.moveToFirst())
		{
			do
			{
				int id = c.getInt(c.getColumnIndex(IMAGES_ID));
				//System.out.println("id = " + id);
				String path = c.getString(c.getColumnIndex(IMAGES_PATH));
				listPaths.add(path);
			}while ( c.moveToNext());
		}
		
		return listPaths;
	}
	
	public void createTable(String tableName)
	{
		String sql = "CREATE TABLE [" + tableName + "] ( " + 
				  "  [_id] INTEGER NOT NULL ON CONFLICT IGNORE PRIMARY KEY ON CONFLICT ABORT AUTOINCREMENT, " + 
				  " [path] VARCHAR NOT NULL ON CONFLICT FAIL, " +  
				  " [isFromCamera] BOOLEAN NOT NULL ON CONFLICT FAIL," + 
				 " [json] VARCHAR NOT NULL ON CONFLICT FAIL); ";
        db.execSQL(sql);
	}
	
	public void renameTable ( String oldname, String newName)
	{
		if ( oldname.equals(newName)) { return; }
		String sql = "ALTER TABLE " + 
	oldname + " RENAME TO " + newName;
		db.execSQL(sql);
		
	}
	
	public ArrayList<Integer> getDataBasesIds(String tableName, String path)
	{
		String sql = "Select " + IMAGES_ID + " , " + IMAGES_PATH + 
				" from " +  tableName + " Where " + IMAGES_PATH + " = " + 
				"'" + path + "'";
		
		Cursor c = db.rawQuery(sql, null);
		 int count = c.getCount();
		 
		 if ( count == 1)
		 {
			 return null;
		 }
		 else
		 {
			 ArrayList<Integer> list = new ArrayList<>();
			 
			 if ( c.moveToFirst())
			 {
				 do
				 {
					 list.add(
							 c.getInt(c.getColumnIndex(IMAGES_ID))
							 );
				 }while ( c.moveToNext());
			 }
			 
			 return list; // получаем массив всех ид
		 }
		 
	}
	
	

	private static class DbHelper extends SQLiteOpenHelper 
	{

		public static final String DB_NAME = "images.db";
		public static final int DB_VERSION = 1;
		private String dbPath;
		private Context context;
		
		public DbHelper(Context context) {
			super(context, DB_NAME, null, DB_VERSION);
			
			dbPath = context.getApplicationInfo().dataDir + "/" + "databases/"
					+ DB_NAME;
			this.context = context;
			
//			if (!ShPreferencesUtils.getFlag(context)) 
//			{
//				
//					copyDataBase();
//					ShPreferencesUtils.save(context, true);
//				
//			}
			

		}

		
		@Override
		public void onCreate(SQLiteDatabase db) 
		{
			String tableName = context.getResources().getString(R.string.database_name );
                String sql = "CREATE TABLE [" + tableName + "] ( " + 
  "  [_id] INTEGER NOT NULL ON CONFLICT IGNORE PRIMARY KEY ON CONFLICT ABORT AUTOINCREMENT, " + 
  " [path] VARCHAR NOT NULL ON CONFLICT FAIL, " + 
  " [isFromCamera] BOOLEAN NOT NULL ON CONFLICT FAIL," + 
 " [json] VARCHAR NOT NULL ON CONFLICT FAIL); ";


                db.execSQL(sql);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		}

	}



	public void deleteTable(String tableName)
	{
		String sql = "Drop table " + tableName;
		db.execSQL(sql);
	}
}
