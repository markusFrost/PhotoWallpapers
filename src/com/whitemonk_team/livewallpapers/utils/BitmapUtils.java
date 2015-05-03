package com.whitemonk_team.livewallpapers.utils;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.whitemonk_team.livewallpapers.enums.TrackerName;
import com.whitemonk_team.livewallpapers.objects.UILApplication;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Point;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Display;

public class BitmapUtils
{
	
	public static final String WIDTH = "width";
	public static final String HIDTH = "hidth";
	
	
	public static Bundle getCropSize(Canvas c, int wImage, int hImage)
	{
		int wR = 0;
		int hR = 0;
		
		int wScreen = c.getWidth();
		int hScreen = c.getHeight();
		
		double k = (double) ( (double) wScreen / (double) hScreen );
		
		wR = wImage;
		hR = (int) (wR / k);
		
		if ( hR > hImage)
		{
			hR = hImage;
			wR = (int) (hR * k);
		}
		
		Bundle bundle = new Bundle();
		bundle.putInt(WIDTH, wR);
		bundle.putInt(HIDTH, hR);
		
		return bundle;
	}
	
	
	private static Bundle getSizedBitmap(String imagePath) 
	{
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(imagePath, options);

		int width = options.outWidth;
		int hight = options.outHeight;
		
		Bundle bundle = new Bundle();
		bundle.putInt(WIDTH, width);
		bundle.putInt(HIDTH, hight);
		
		return bundle;
		
	}
	
	public static Bundle getBitmap(String path, boolean wasRotation, int wScreen, int hScreen)
	{
		//path = path.substring(6);
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);

		int wReal,hReal;
		
		if ( wasRotation)
		{
		 hReal = options.outWidth;
		 wReal = options.outHeight;
		}
		else
		{
			wReal = options.outWidth;
			 hReal = options.outHeight;
		}

//		 wReal = options.outWidth;
//		 hReal = options.outHeight;
		
		
		int wImage = 0;
		int hImage = 0;
		
		wImage = wScreen;
		
		double k = (double) ((double) hReal / (double) wReal);
		hImage = (int) (  wImage * k );
		
		if ( hImage > hScreen)
		{
			k = (double)  ((double) wReal/ (double)hReal);
			hImage = hScreen;
			wImage = (int) ( hImage * k );
		}
		 Bundle bundle = new  Bundle();
		 bundle.putInt(WIDTH, wImage);
		 bundle.putInt(HIDTH, hImage);
		 
		
		return bundle;
	}
	
	
	public static Bundle getBitmap(Context ctx, int resId, int wScreen, int hScreen)
	{
		//path = path.substring(6);
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(ctx.getResources(), resId, options);

		int wReal = options.outWidth;
		int hReal = options.outHeight;
		
		int wImage = 0;
		int hImage = 0;
		
		wImage = wScreen;
		
		double k = (double) ((double) hReal / (double) wReal);
		hImage = (int) (  wImage * k );
		
		if ( hImage > hScreen)
		{
			k = (double)  ((double) wReal/ (double)hReal);
			hImage = hScreen;
			wImage = (int) ( hImage * k );
		}
		 Bundle bundle = new  Bundle();
		 bundle.putInt(WIDTH, wImage);
		 bundle.putInt(HIDTH, hImage);
		 
		
		return bundle;
	}
	
	public static Bitmap getSizedBitmap(String imagePath,int reqWidth, int reqHeight, boolean fixOrientation) {

		//imagePath = imagePath.substring(6);
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(imagePath, options);

		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

		options.inJustDecodeBounds = false;
		
	if (fixOrientation)
		{
			return fixOrientation(BitmapFactory.decodeFile(imagePath, options));
		}
		Bitmap bmResult = null;
//		try
//		{
//			bmResult =  BitmapFactory.decodeFile(imagePath, options);
//		}
//		catch (OutOfMemoryError ex)
//		{
//			bmResult = null;
//		}
		bmResult =  BitmapFactory.decodeFile(imagePath, options);
		return bmResult;
		
		
		
	}
	
	public static Bitmap getSizedBitmap(Context ctx, int resId,int reqWidth, int reqHeight, boolean fixOrientation) {

		//imagePath = imagePath.substring(6);
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(ctx.getResources(), resId, options);

		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

		options.inJustDecodeBounds = false;
		
		if (fixOrientation){
			return fixOrientation(BitmapFactory.decodeResource(ctx.getResources(), resId, options));
		}
		
		return BitmapFactory.decodeResource(ctx.getResources(), resId, options);
	}

	private static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {

		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);

			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}

		return inSampleSize;
	}
	
	private static Bitmap fixOrientation(Bitmap bitmap) {
		
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
	    if (bitmap.getWidth() > bitmap.getHeight()) 
	    {
	        Matrix matrix = new Matrix();
	        matrix.postRotate(90);
//	       
//	        
//	        Runtime.getRuntime().freeMemory();
//	        Runtime.getRuntime().totalMemory();
	        try
	        {
	        bitmap = Bitmap.createBitmap(bitmap , 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
	        } catch (Exception e){}
	        
	    }
	    
	    return bitmap;
	}
	
	public static  boolean provOnOutOfMemmoryError(Activity activity, String p)
	{
		//String value = "";
		boolean fl;
		int wScreen = 0;
		int hScreen = 0;
		Display display = activity.getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		wScreen = size.x;
		hScreen = size.y;
		
		final String path = p.replace("file:/", "");
		
		//value += "Путь равен = " + path + " _ " + p +"\n";
		Bitmap bmp = null;
		try
		{
			 bmp = BitmapUtils.getSizedBitmap(path, wScreen, hScreen, false);
		     fl = true;
		    
		}
		catch (OutOfMemoryError e)
		{
			//value += "OutOfMemoryError при первой попытке" + "\t" + formError(e) + "\n";
		  fl = false;	
		}
		
		catch (Exception e1) {
			fl = false;
			//value += "Какая то ошибка при первой попытке" + "\t" + formError(e1)+ "\n";
		}
		if ( fl)
		{
			try
			{
			  bmp.recycle();
			  bmp = BitmapUtils.getSizedBitmap(path, hScreen, wScreen, false);
		      fl = true;
		      
			}
			catch (OutOfMemoryError e)
			{
				//value += "OutOfMemoryError при второй попытке" + "\t" + formError(e) + "\n";
			  fl = false;	
			}
			catch (Exception e1) {
				fl = false;
				//value += "Какая то ошибка при второй попытке" + "\t" +formError(e1) + "\n";
			}
			
			if ( fl)
			{
				try
				{
					bmp.recycle();
				final BitmapFactory.Options options = new BitmapFactory.Options();
				options.inJustDecodeBounds = true;
				BitmapFactory.decodeFile(path, options);

				 int w,h;
				 
				  w = options.outWidth;
				  h = options.outHeight;
				 
				 bmp = BitmapUtils.getSizedBitmap(path, w, h, false);
				 fl = true;
				} catch (OutOfMemoryError e)
				{
					fl = false;
					//value += "OutOfMemoryError при третьей попытке" + "\t" + formError(e) + "\n";
				}
				catch (Exception e1) {
					fl = false;
					//value += "Какая то ошибка при третьей попытке" + "\t" + formError(e1)+ "\n";
				}
			}
		}
		
//		if ( !value.equals(""))
//		{
//			value += "path = " + path + "\n";
//			Tracker t = ((UILApplication)activity.getApplicationContext()).getTracker(
//		            TrackerName.APP_TRACKER);
//		        // Build and send an Event.
//		        t.send(new HitBuilders.EventBuilder()
//		            .setCategory(CATEGORY_APPLICATION_ERROR)
//		            .setAction(ACTION_CLICK_ERROR)
//		            .setLabel(value)
//		            .build());
//		}
		
		
		if ( bmp != null) { bmp.recycle();}
		
		return fl;
		
		
	}
	
	 public static final String CATEGORY_ON_MENU_ITEM_CLICK = "Выбор пункта меню в Action Bar";
	    public static final String CATEGORY_APPLICATION_ERROR = "Категория ошибки в приложении";
	    public static final String ACTION_CLICK = "Одиночное нажатие на выбранный элемент";
	    public static final String ACTION_CLICK_ERROR = "Ошибка при клике";
	    public static final String LABEL_ADD_IMAGE = "Добавление изображения";
	    public static final String LABEL_CALL_WALLPAPERS = "Установка живых обоев";
	    public static final String LABEL_SET_TIME = "Установка времени показа изображений";
	    public static final String LABEL_GO_TO_MAGAZIN = "Переход в настройки";
	    public static final String LABEL_EXIT = "Был осуществлён выход из приложения";
	    public static final String LABEL_ERROR_ADD_IMAGE = "Не удалось добавить изображение";
	    
	    private static String formError(Exception e)
	    {
	    	String value = "";
	    	StackTraceElement []  array = e.getStackTrace();
	    	if ( array.length > 0)
	    	{
	    		value += "Имя класса : " + array[0].getClassName() + "\n";
	    		value += "Имя файла : " + array[0].getFileName() + "\n";
	    		value += "Имя метода : " + array[0].getMethodName() + "\n";
	    		value += "Строка ошибки : " + array[0].getLineNumber() + "\n";
	    		
	    	}
	    	value += "Сообщение : " +  e.getMessage() + "\n";
	    	return value;
	    }
	    
	    private static String formError(OutOfMemoryError e)
	    {
	    	String value = "";
	    	StackTraceElement []  array = e.getStackTrace();
	    	if ( array.length > 0)
	    	{
	    		value += "Имя класса : " + array[0].getClassName() + "\n";
	    		value += "Имя файла : " + array[0].getFileName() + "\n";
	    		value += "Имя метода : " + array[0].getMethodName() + "\n";
	    		value += "Строка ошибки : " + array[0].getLineNumber() + "\n";
	    		
	    	}
	    	value += "Сообщение : " +  e.getMessage() + "\n";
	    	return value;
	    }
	
	
	

}
