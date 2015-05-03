package com.whitemonk_team.livewallpapers.draw;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.whitemonk_team.livewallpapers.utils.BitmapUtils;

public class Draw extends View 
{
   public Draw( Context ctx)
   {
	   super(ctx);
	   this.context = ctx;
   }
   
   public Draw( Context ctx, String path)
   {
	   super(ctx);
	   this.context = ctx;
	  this.path = path.replace("file:/", "");
	   paintBmp = new Paint();
	   paintRect = new Paint();
	   wasRotation = false;
   }
   
   
   
   public Draw( Context ctx, String path, Rect rect, boolean wasRotation)
   {
	   super(ctx);
	   this.context = ctx;
	   this.path = path.replace("file:/", "");
	   this.rect = rect;
	   this.wasRotation = wasRotation;
	   paintBmp = new Paint();
	   paintRect = new Paint();
   }

   
   Context context;
   Canvas canvas;
   String path;
   int wScreen;
   int hScreen;

   
   Paint paintBmp;
   Paint paintRect;
   
   int wImage = 0;
   int hImage = 0;
   
   Rect rect = null;
   
   @Override
	protected void onDraw(final Canvas canvas)
    {
		super.onDraw(canvas);
		
		this.canvas = canvas;
		
		
		
		wScreen = canvas.getWidth();
		hScreen = canvas.getHeight();
				
		 final Paint paint = new Paint();
		 paint.setStyle(Paint.Style.FILL);

		 paint.setColor(Color.BLACK);
         canvas.drawPaint(paint);
         
         if ( fl == false)
         {
        drawBitmap();
        drawRect();
        
        defineMoveType();
         }
         else
         {
//        	 Bitmap bmp = BitmapUtils.getSizedBitmap(path, wScreen, hScreen, isFromCamera);
//             
//             Rect dest = new Rect(0,0, wScreen, hScreen);
//             
//           //  src = new Rect(361, 0 ,363, 800);
//            
//             paintBmp.setFilterBitmap(true);
//             canvas.drawBitmap(bmp, src, dest, paintBmp);
         }
 
	}
   
   int moveType = -1;
   static final int MOVE_X = 1;
   static final int MOVE_Y = 2;
   
   
   private void defineMoveType()
   {
	   int dw =  wImage - wR;
	   int dh =  hImage - hR;
	   
	   if ( dw > dh)
	   {
		   moveType = MOVE_X;
	   }
	   else
	   {
		   moveType = MOVE_Y;
	   }
   }
   
   private void drawBitmap()
   {
	   

		int w = canvas.getWidth();
		int h = canvas.getHeight();
		// boolean wasRotation = true;
		
			final BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			 BitmapFactory.decodeFile(path, options);
			 
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
		
		// будем считать что если попросил перевернуть
		//поворот нужен если съёмка в портретной
		
		 
		
		 
	   Bundle bundle = BitmapUtils.getBitmap(path, wasRotation, wScreen, hScreen);
	   
//	   if (isFromCamera)
//	   {
//		    wImage = bundle.getInt(BitmapUtils.WIDTH);
//	        hImage = bundle.getInt(BitmapUtils.HIDTH);
//	   }
//	   else
//	   {
//       
//        wImage = bundle.getInt(BitmapUtils.WIDTH);
//        hImage = bundle.getInt(BitmapUtils.HIDTH);
//	   }
	   wImage = bundle.getInt(BitmapUtils.WIDTH);
       hImage = bundle.getInt(BitmapUtils.HIDTH);
       Bitmap bmp = BitmapUtils.getSizedBitmap(path, wImage, hImage, wasRotation);
       
//       hImage = bundle.getInt(BitmapUtils.HIDTH);
//       Bitmap bmp = BitmapUtils.getSizedBitmap(path, wImage, hImage, false);
       
       int left = ( wScreen - wImage) / 2;
       int top = (hScreen - hImage) / 2;
       
       Rect dest = new Rect(left, top, wImage + left, hImage + top);
      
       paintBmp.setFilterBitmap(true);
       canvas.drawBitmap(bmp, null, dest, paintBmp);
       
       
   }
   
   int dx = 0;
   int dy = 0;
   int h = 20;
   
   int xCropStart, xCropEnd, yCropStart, yCropEnd;
   int wR,hR;
   private void drawRect()
   { 
	   Bundle bundle = BitmapUtils.getCropSize(canvas, wImage, hImage);
       wR = bundle.getInt(BitmapUtils.WIDTH);
       hR = bundle.getInt(BitmapUtils.HIDTH);
      if ( rect == null)
      {
        xCropStart = ( wScreen - wR) / 2 + dx;
        yCropStart = (hScreen - hR) / 2 + dy;
        
        xCropEnd = wR + xCropStart;
        yCropEnd = hR + yCropStart;
        
       
      }
      else
      {
    	  defineMoveType();
    	  
    	  if ( moveType == MOVE_X)
    	  {
    		  yCropStart = (hScreen - hR) / 2 + dy;
    		  yCropEnd = hR + yCropStart;
    		  
    		  double k = (double) ((double) hReal / (double) hImage);
    		  
    		  xCropStart = (int) (rect.left / k) + dx;
    		  xCropEnd = wR + xCropStart;
    	  }
    	  else
    	  {
    		  xCropStart = ( wScreen - wR) / 2 + dx;
    		  xCropEnd = wR + xCropStart;
    		  double k = (double) ((double) wReal / (double) wImage);
    		  yCropStart = (int) (rect.top / k) + dy;
    		  yCropEnd = hR + yCropStart;
    	  }
 
      }
      
      paintRect.setStyle(Paint.Style.STROKE);
      paintRect.setStrokeWidth(3);
      paintRect.setColor(Color.RED);
      Rect r = new Rect(xCropStart, yCropStart, xCropEnd, yCropEnd);
      canvas.drawRect(r, paintRect);
   }
   int xStart = 0,yStart = 0,xEnd = 0,yEnd = 0;
   @Override
   public boolean onTouchEvent(MotionEvent event) 
   {
	    int eventaction = event.getAction();

	   
	    switch (eventaction) 
	    {
	        case MotionEvent.ACTION_DOWN: 
	            // finger touches the screen
	        	
	        	xStart = (int) event.getX();
	        	yStart = (int) event.getY();
	        	
	            break;

	        case MotionEvent.ACTION_MOVE:
	            // finger moves on the screen
	        	
	        	
	        	
	        	
	            break;

	        case MotionEvent.ACTION_UP:   
	            // finger leaves the screen
	        	
	        	xEnd = (int) event.getX();
	        	yEnd = (int) event.getY();
	        	
	        	 if (moveType == MOVE_X)
	     	    {
	     	    	if ( (xEnd - xStart) > 0)
	     	    	{
	     	    		// вправо
	     	    		dx += h;
	     	    		if ( (xCropEnd + h) > wImage)
	     	    		{
	     	    			dx -= h;
	     	    		}
	     	    	}
	     	    	else
	     	    	{
	     	    		//влево
	     	    		dx -= h;
	     	    		if ((xCropStart - h) < ( ( wScreen - wImage) / 2)  )
	     	    		{
	     	    			dx += h;
	     	    		}
	     	    		
	     	    	}
	     	    }
	     	    else
	     	    {
	     	    	if ( (yEnd - yStart) > 0)
	     	    	{
	     	    		// вниз
	     	    		dy += h;
	     	    		if ( yCropEnd + h > hImage)
	     	    		{
	     	    			dy -= h;
	     	    		}
	     	    	}
	     	    	else
	     	    	{
	     	    		//вверх
	     	    		dy -= h;
	     	    		if (yCropStart - h < ( ( hScreen - hImage) / 2)  )
	     	    		{
	     	    			dy += h;
	     	    		}
	     	    		
	     	    	}
	     	    }
	     	    
	     	    invalidate();
	        	
	            break;
	    }
	    

	    
	   
	    
	  

	    // tell the system that we handled the event and no further processing is required
	    return true; 
	}
   
//   public void showImage()
//   {
//	   int xleft, xtop, yright, ybottom;
//	   final BitmapFactory.Options options = new BitmapFactory.Options();
//		options.inJustDecodeBounds = true;
//		BitmapFactory.decodeFile(path, options);
//
//		 wReal = options.outWidth;
//		 hReal = options.outHeight;
//	   
//		if (moveType == MOVE_X)
//		{
//			xleft = 0; // отступ слева релального изображения
//			yright = 0; // отступ права реального изображения
//
//			int left = xCropStart;
//			int right = wImage - xCropEnd;
//
//			double k = (double) ((double) hReal / (double) hImage);
//			xleft = (int) (left * k);
//			yright = wReal - (int) (right * k);
//
//			xtop = 0;
//			ybottom = hReal;
//		}
//		else
//		{
//			xleft = 0;
//			yright = wReal;
//			
//			int top = yCropStart;
//			int bottom = hImage - yCropEnd;
//			
//			double k = (double) ((double)wReal / (double)wImage);
//			
//			xtop = (int) (top * k);
//			ybottom = hReal - (int) (bottom * k);
//		}
//	   
//	   fl = true;
//	   
//	   src = new Rect(xleft, xtop, yright, ybottom);
//	   
//	   
//	   invalidate();
//	   
//	    
//   }
   
  public boolean wasRotation;
   
   public void changeOrientation()
   {
	   wasRotation = !wasRotation;
	   rect = null;
	   invalidate();
   }
   
   
   public Rect getRectangle()
   {
	   int xleft, xtop, yright, ybottom;
	   
	   
		if (moveType == MOVE_X)
		{
			xleft = 0; // отступ слева релального изображения
			yright = 0; // отступ права реального изображения

			int left = xCropStart;
			int right = wImage - xCropEnd;

			double k = (double) ((double) hReal / (double) hImage);
			xleft = (int) (left * k);
			yright = wReal - (int) (right * k);

			xtop = 0;
			ybottom = hReal;
		}
		else
		{
			xleft = 0;
			yright = wReal;
			
			int top = yCropStart;
			int bottom = hImage - yCropEnd;
			
			double k = (double) ((double)wReal / (double)wImage);
			
			xtop = (int) (top * k);
			ybottom = hReal - (int) (bottom * k);
		}
	   

	   
	   return  new Rect(xleft, xtop, yright, ybottom);
	   

	   
	    
   }
   
   int wReal, hReal;
   
   Rect src ;
   
   boolean fl = false;
   
  


   
}
