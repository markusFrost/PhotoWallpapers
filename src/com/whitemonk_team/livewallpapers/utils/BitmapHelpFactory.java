package com.whitemonk_team.livewallpapers.utils;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class BitmapHelpFactory 
{
	private final boolean useHack;
	private static final VMRuntimeHack runtime = new VMRuntimeHack();
	private Set<Bitmap> allocatedBitmaps = new HashSet<Bitmap>(); 
	private Set<Bitmap> hackedBitmaps = new HashSet<Bitmap>(); 
	public BitmapHelpFactory(boolean useHack) 
	{
		this.useHack = useHack;
		
	}
	
// создать картинку
	public Bitmap alloc(boolean useHack, String imagePath,int reqWidth, int reqHeight, boolean fixOrientation) 
	{
        Bitmap bmp = getSizedBitmap(imagePath, reqWidth, reqHeight, fixOrientation);
        if (useHack) 
        {
            runtime.trackFree(bmp.getRowBytes() * bmp.getHeight());
            hackedBitmaps.add(bmp);
        }
        allocatedBitmaps.add(bmp);
        return bmp;
	}
	
	public static Bitmap getSizedBitmap(String imagePath,int reqWidth, int reqHeight, boolean fixOrientation) {

		//imagePath = imagePath.substring(6);
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(imagePath, options);

		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

		options.inJustDecodeBounds = false;

		Bitmap bmResult = null;
		try
		{
			bmResult =  BitmapFactory.decodeFile(imagePath, options);
		}
		catch (OutOfMemoryError ex)
		{
			bmResult = null;
		}
		
		return bmResult;
		
		
		
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

// освободить картинку
	public void free(Bitmap bmp) {
			bmp.recycle();
        if (hackedBitmaps.contains(bmp)) {
            runtime.trackAlloc(bmp.getRowBytes() * bmp.getHeight());
            hackedBitmaps.remove(bmp);
        }
        allocatedBitmaps.remove(bmp);
	}

// освоболить все картинки (удобно для тестирования)    	
	public void freeAll() {
		for (Bitmap bmp : new LinkedList<Bitmap>(allocatedBitmaps))
			free(bmp);
	}
	

}
