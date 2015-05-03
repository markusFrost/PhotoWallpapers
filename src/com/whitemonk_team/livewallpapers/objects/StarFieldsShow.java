package com.whitemonk_team.livewallpapers.objects;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.view.SurfaceHolder;

import com.whitemonk_team.livewallpapers.databases.DbAdapter;
import com.whitemonk_team.livewallpapers.objects.Constants.Extra;
import com.whitemonk_team.livewallpapers.utils.BitmapUtils;
import com.whitemonk_team.livewallpapers1.R;

public class StarFieldsShow extends WallpaperService
{
    private final Handler mHandler = new Handler();

    public int count = -1;
    public int position = -1;
    public Bitmap bmp = null;
    @Override
    public Engine onCreateEngine() {
        return new StarFieldsEngine();
    }

   public  class StarFieldsEngine extends Engine 
    {
        private boolean mVisible;
        private boolean isChanged = false;
        private final Runnable draw_frame = new Runnable() {
            public void run() 
            {
                drawFrame();
            }
        };
        // ���������� ���������� ������ � ������ ������
        private float screen_width = 0;
        private float screen_height = 0;         
        //Random rnd; 

      public  StarFieldsEngine() 
        {
        	//rnd = new Random();
    	  UILApplication.sE = this;
        }
      
      public void refreshData()
      {
    	  drawFrame();
      }

        @Override
        public void onDestroy() {
            super.onDestroy();
            print("OnDestroy");
            mHandler.removeCallbacks(draw_frame);
        }
        
       
        int visibilityCHangetCounter = 0;
        @Override
        public void onVisibilityChanged(boolean visible) 
        {
        	if ( visibilityCHangetCounter < 5)
      	  {
      	     visibilityCHangetCounter++;
      	  }
          mVisible = visible;
          if (visible) 
          {
          	setNewIndex();

              drawFrame();
          } else {
             // mHandler.removeCallbacks(draw_frame);
          }
        }
        
        void setNewIndex()
        {
        	if (visibilityCHangetCounter > 3)
            {
            	isChanged = true;
            }
        	 if ( position == 0)
             {
             	position = count;
             }
             else
             {
             	position--;
             }
        }
        

        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) 
        {
            super.onSurfaceChanged(holder, format, width, height);
          //  print("onSurfaceChanged");
            // ���������� ������ ������� ������� ��� ���������
            screen_width = width;
            screen_height = height;
            
           if ( position >= 0) { isChanged = true;}
            
           if ( position == 0)
           {
        	   position = count;
           }
           else
           {
             position--;
           }
            drawFrame();

        }

        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) 
        {
            super.onSurfaceDestroyed(holder);
            
            print("onSurfaceDestroyed");
            mVisible = false;
            mHandler.removeCallbacks(draw_frame); 
        }

        @Override
        public void onOffsetsChanged(float xOffset, float yOffset,
                float xStep, float yStep, int xPixels, int yPixels)
        {

            
        }

        // ����������� ������
		void drawFrame() 
		{
			UILApplication.sE = this;
			final SurfaceHolder holder = getSurfaceHolder();
			Canvas c = null;
			try 
			{
				c = holder.lockCanvas();
				if (c != null)
				  { 
					drawScreen(c);
				  }
			} finally 
			{
				try
				{
				if (c != null)
					holder.unlockCanvasAndPost(c);
				} catch ( Exception e){}
			}
			
			// ��������� �������� ����� ������� ��������
			if (isChanged == false)
			{
				int time = ((UILApplication) getApplicationContext()).getWallTime();
				print("TIME = " + time);
				mHandler.removeCallbacks(draw_frame);
				mHandler.postDelayed(draw_frame, time);
			}
			else
			{
				isChanged = false;
			}

		}
        
        // ����� ��������� ��� ������ �� ���������
         void drawScreen(Canvas c)
        {
            c.save();
            final Paint paint = new Paint();
   		 paint.setStyle(Paint.Style.FILL);

			paint.setColor(Color.BLACK);
			c.drawPaint(paint);
			Paint paintBmp = new Paint();

			String tableName = ((UILApplication) getApplicationContext()).getTableName();
			
			if ( tableName == null || tableName.isEmpty())
			{
				drawEmptyBitmap(c);
				return;
			}
			
			// здесь надо вытащить ее из xml
			count = getDbAdapter(getApplicationContext()).getCount(tableName) - 1;

			if (count < 0) 
			{
				drawEmptyBitmap(c);
			} else 
			{
				if ( position > count)
				{
					position = count;
				}
				else
				{
					if (position < 0)
					{
						position = 0;
					}
				}
				/*try {
					drawBitmap(tableName, c, paintBmp);
				} catch (Exception e) 
				{
					position++;
					drawScreen(c);

				}*/
				boolean fl = drawBitmap(tableName, c, paintBmp);
				if (!fl )
				{
					position++;
					drawScreen(c);
				}
				
			}
            
            
            c.restore(); 
        }    
    }
    
    private boolean drawBitmap(String tableName, Canvas c, Paint paintBmp)
    {
    	if ( position < 0)
    	{
    		position = 0;
    	}
    	 ImageDto imageDto = getDbAdapter(getApplicationContext()).getImageDto(tableName, position, 1);
    	String path = imageDto.Path.replace("file:/", "");
        
        final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);
        
		 boolean wasRotation = imageDto.WasRotation;
		 
		int w,h;
		
		if ( wasRotation)
		{
			  h = options.outWidth;
			  w = options.outHeight;
		}
		else
		{
		  w = options.outWidth;
		  h = options.outHeight;
		}

		  bmp = BitmapUtils.getSizedBitmap(path, w, h, wasRotation);
		 //09-06 12:22:13.112: I/dalvikvm-heap(24358): Grow heap (frag case) to 37.821MB for 12582928-byte allocation

		 /* if ( bmp == null)
		  {
			  getDbAdapter(getApplicationContext()).deleteImage(tableName, position);
			  return false;
		  }*/
		 
		 Rect dst = new Rect(0,0, c.getWidth(), c.getHeight());
		
		 String key = getOrientation(c);
		 Rect src = imageDto.ImageMap.get(key);
		 
		 paintBmp.setFilterBitmap(true);
         c.drawBitmap(bmp, src, dst, paintBmp);
         
         bmp.recycle();
         bmp = null;
         
         print("POSITION = " + position);
         //print("TIME = " + tim)
         
         position++;
         
         if ( position > count)
         {
        	 position = 0;
         }
         return true;
    }
    private String getOrientation (Canvas c)
    {
    	if(c.getWidth() == c.getHeight())
	    {
	       // orientation = Configuration.ORIENTATION_SQUARE;
	    	return null;
	    } else
	    { 
	        if(c.getWidth() < c.getHeight())
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
    
    private void print(Object obj)
    {
    //	System.out.println(obj.toString());
    }
    
    private DbAdapter getDbAdapter(Context context)
    {
    	return ((UILApplication)(context)).dbAdapter;
    }
    
    private void drawEmptyBitmap(Canvas c)
    {
    	Bundle bundle = BitmapUtils.getBitmap(getApplicationContext(),
    			R.drawable.no_image, c.getWidth(), c.getHeight());
        
        int wImage = bundle.getInt(BitmapUtils.WIDTH);
        int hImage = bundle.getInt(BitmapUtils.HIDTH);
        
       bmp = BitmapUtils.getSizedBitmap(getApplicationContext(), 
        		R.drawable.no_image, wImage, hImage, false);
        
        int left = ( c.getWidth() - wImage) / 2;
        int top = (c.getHeight() - hImage) / 2;
        
        Rect dest = new Rect(left, top, wImage + left, hImage + top);
       
        Paint paintBmp = new Paint();
        paintBmp.setFilterBitmap(true);
        c.drawBitmap(bmp, null, dest, paintBmp);
        
        bmp.recycle();
    }
}
