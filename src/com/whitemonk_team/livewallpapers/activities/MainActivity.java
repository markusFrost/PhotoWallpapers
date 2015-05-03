package com.whitemonk_team.livewallpapers.activities;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.widget.DrawerLayout;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.whitemonk_team.livewallpapers.adapters.GenderAdapter;
import com.whitemonk_team.livewallpapers.adapters.ImageAdapter;
import com.whitemonk_team.livewallpapers.databases.DbAdapter;
import com.whitemonk_team.livewallpapers.enums.TrackerName;
import com.whitemonk_team.livewallpapers.listners.OnGridViewClickListner;
import com.whitemonk_team.livewallpapers.listners.OnGridViewLongClickListner;
import com.whitemonk_team.livewallpapers.objects.Constants.Extra;
import com.whitemonk_team.livewallpapers.objects.ImageDto;
import com.whitemonk_team.livewallpapers.objects.UILApplication;
import com.whitemonk_team.livewallpapers.utils.BitmapUtils;
import com.whitemonk_team.livewallpapers.utils.ShPreferencesUtils;
import com.whitemonk_team.livewallpapers.utils.StorageUtils;
import com.whitemonk_team.livewallpapers.utils.StringUtils;
import com.whitemonk_team.livewallpapers1.R;

// c добавлением альбомов - показать ошибки тостером
public class MainActivity extends AbsListViewBaseActivity
{
	DisplayImageOptions options;
	public ImageAdapter imageAdapter;
	public ArrayList<String> imageUrls = new ArrayList<>();
	
	ArrayAdapter<String> navAdapter;
	private ListView menuList;
	private DrawerLayout navDrawer;
	static ArrayList<String> listTables = new ArrayList<>();
	
	RelativeLayout rl;
	TextView tvResult;
	
	OnGridViewClickListner onGridViewClickListner;
	OnGridViewLongClickListner onGridViewLongClickListner;
	 private InterstitialAd interstitial;
	 boolean wasClick = true;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		setDeviceName();
		
		String pathAnalytic = "com.whitemonk_team.livewallpapers.activities.MainActivity";
		// Get tracker.
        Tracker t = ((UILApplication)this.getApplicationContext()).getTracker(
            TrackerName.APP_TRACKER);

        // Set screen name.
        // Where path is a String representing the screen name.
        t.setScreenName(pathAnalytic);

        // Send a screen view.
        t.send(new HitBuilders.AppViewBuilder().build());
        
        
		menuList = (ListView) findViewById(R.id.left_menu);
	     
		listTables = getDbAdapter().getTableNameList();
		navAdapter = new ArrayAdapter<String>(this, R.layout.my_list_item, listTables);
        menuList.setAdapter(navAdapter);
        navDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
      // navDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
		rl = (RelativeLayout) findViewById(R.id.rlInfo);
		rl.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) 
				{
					
					sendDataForAnalytic(CATEGORY_ON_MENU_ITEM_CLICK, ACTION_CLICK, LABEL_ADD_IMAGE);
					
				if (wasClick)
				{
					wasClick = false;

					if (tvResult
							.getText()
							.toString()
							.equals(getResources().getString(
									R.string.str_all_empty))) {
						createAddAlertDialog(null, -1);
					} else {
						if (ShPreferencesUtils
								.getCameraActive(getApplicationContext())) {

							alertImage();
						} else {
							createGaleryIntent();
						}
					}
				}
					
					
				}
			});
		tvResult = (TextView) findViewById(R.id.tvInfo);

		StorageUtils.initializeAll(this);
		
		if ( listTables == null || listTables.isEmpty())
		{
			imageUrls = new ArrayList<>();
			tableName = "";
		}
		else
		{	
			/*tableName = ShPreferencesUtils.getTableName(this);
			if (tableName == null || tableName.isEmpty())
			{
				tableName = listTables.get(0);
			}*/
			
			int pos = ShPreferencesUtils.getTablePosition(MainActivity.this);
			tableName = listTables.get(pos);
		//	getActionBar().setTitle(tableName);
			ShPreferencesUtils.setTablePosition(getApplicationContext(), pos);
			//ShPreferencesUtils.setTableName(getApplicationContext(), tableName);
		   imageUrls = ((UILApplication)this.getApplicationContext()).dbAdapter.getList(tableName); 
		}
		options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.ic_stub)
		.showImageForEmptyUri(R.drawable.ic_empty)
		.showImageOnFail(R.drawable.ic_error)
		.cacheInMemory(true)
		.cacheOnDisc(true)
		.considerExifParams(true)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.build();
		
		
		imageAdapter = new ImageAdapter(this, imageUrls, imageLoader, options);
		onGridViewClickListner = new OnGridViewClickListner(this, imageUrls);
	    onGridViewLongClickListner = new OnGridViewLongClickListner(this, tableName);
		
		listView = (GridView) findViewById(R.id.gridview);
	((GridView) listView).setAdapter(imageAdapter);
	
	listView.setOnItemClickListener(onGridViewClickListner);
	listView.setOnItemLongClickListener(onGridViewLongClickListner);

	menuList.setOnItemClickListener(new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) 
		{
			createIntent(position);
			
			navDrawer.closeDrawers();
			
			
		}
	});
	
	menuList.setOnItemLongClickListener(new OnItemLongClickListener() {

		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view,
				final int position, long id)
		{
			
			PopupMenu popup = new PopupMenu(MainActivity.this, view);
			popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu()); 
			
			popup.setOnMenuItemClickListener(new OnMenuItemClickListener() {
				
				@Override
				public boolean onMenuItemClick(MenuItem item) 
				{
					int itemId = item.getItemId();
		        	 
		        	 if ( itemId == R.id.popup_menu_delete)
		        	 {
		        		 createNotifAlert(position);
		        		 return true;
		        	 }
		        	 else
		        	 {
		        		 createAddAlertDialog(listTables.get(position), position);
		        		 
		        		
		        		 return true;
		        	 }
		        	 
		        	 
				}
			});
			
			popup.show();
			
			
			return true;
		}
	});
	
	}
	
	
	



	public void createNotifAlert(final int position)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setTitle(R.string.app_name)
    			.setMessage(R.string.alertShureDelete)
    			.setIcon(R.drawable.ic_launcher)
    			.setCancelable(false)
    			.setNegativeButton("Cancel",
    					new DialogInterface.OnClickListener() {
    						public void onClick(DialogInterface dialog, int id) {
    							dialog.cancel();
    						}
    					})
    					.setPositiveButton("ОК",
    					new DialogInterface.OnClickListener() {
    						public void onClick(DialogInterface dialog, int id) 
    						{
    							
    							if ( listTables.size() < 2)
    							{
    								String tbName = listTables.get(position);
    								getDbAdapter().deleteTable(tbName);
        							listTables.remove(position);
        							navAdapter.notifyDataSetChanged();
        							navDrawer.closeDrawers();
        							provActivity();
        							if (tableName.equals(tbName) &&
        									ShPreferencesUtils.getTableName(MainActivity.this).equals(tbName)
        									)
        							{
        								ShPreferencesUtils.setTableName(getApplicationContext(), "");
        								if ( UILApplication.sE != null)
        								{
        								   UILApplication.sE.refreshData();
        								}
        							}
        							
    							}
    							else if ( position == 0)
    							{
    								String tbName = listTables.get(position);
    								getDbAdapter().deleteTable(tbName);
    								listTables.remove(position);
        							navAdapter.notifyDataSetChanged();
        							if (tableName.equals(tbName))
        							{
        							   navDrawer.closeDrawers();
        							   createIntent(position);
        							   if (ShPreferencesUtils.getTableName(MainActivity.this).equals(tbName))
        							   {
        							   ShPreferencesUtils.setTableName(getApplicationContext(), listTables.get(position));
        							   if ( UILApplication.sE != null)
       								{
       								   UILApplication.sE.refreshData();
       								}
        							   }
        							}
        							
    							}
    							else 
    							{
    								String tbName = listTables.get(position);
    								getDbAdapter().deleteTable(tbName);
    								listTables.remove(position);
        							navAdapter.notifyDataSetChanged();
        							if (tableName.equals(tbName))
        							{
        							   navDrawer.closeDrawers();
        							   createIntent(position - 1);
        							   if (ShPreferencesUtils.getTableName(MainActivity.this).equals(tbName))
        							   {
        							   ShPreferencesUtils.setTableName(getApplicationContext(), listTables.get(position - 1));
        							   if ( UILApplication.sE != null)
       								{
       								   UILApplication.sE.refreshData();
       								}
        							   }
        							}
        							
    							}
    								
    							
    							
    							
    							//provViews();
    						}
    					})
    					;

    	AlertDialog alert = builder.create();
    	
    	
    	alert.show();
		
	}
	
//	public void provOnEmpty()
//	{
//		if ( imageUrls == null || imageUrls.isEmpty())
//		{
//			menuAdd.setVisible(false);
//			menuBackground.setVisible(false);
//			menuWallp.setVisible(false);
//			menuSettings.setVisible(false);
//			rl.setVisibility(View.VISIBLE);
//            listView.setVisibility(View.GONE);
//		}
//		else
//		{
//			rl.setVisibility(View.GONE);
//            listView.setVisibility(View.VISIBLE);
//            menuAdd.setVisible(true);
//            menuBackground.setVisible(true);
//			menuWallp.setVisible(true);
//			menuSettings.setVisible(true);
//		}
//		
//	}
	
	MenuItem menuAdd = null, menuWallp = null,  menuSettings = null, menuMagazin;
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) 
	{
		menuAdd = menu.findItem(R.id.menuAdd);
		menuWallp = menu.findItem(R.id.menuWallp);
		
		menuSettings = menu.findItem(R.id.menu_setTimePhoto);
		menuMagazin = menu.findItem(R.id.menuMagazin);
		
		provActivity();
		//provOnEmpty();
		return true;
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
		imageLoader.clearDiscCache();
		imageLoader.clearMemoryCache();
		imageUrls.clear();
	}
	
	


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	long time = 0;
	long time1 = 0;
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		 showAdversting();
		switch ( id)
		{
		 case android.R.id.home: 
		 {
			 sendDataForAnalytic(CATEGORY_ON_MENU_ITEM_CLICK, ACTION_CLICK, 
	    			   LABEL_EXIT);
			closeActivity();

			return true;
		}
		 
		 case R.id.menu_setTimePhoto:
	       {
	    	   sendDataForAnalytic(CATEGORY_ON_MENU_ITEM_CLICK, ACTION_CLICK, 
	    			   LABEL_SET_TIME);
	    	   long t1 = 0;
	    	   if (time == 0)
	    	   {
	    		   time = System.currentTimeMillis();
	    	   }
	    	   else
	    	   {
	    		   t1 = System.currentTimeMillis();
	    	   }
	    	   
	    	   if (t1 - time > 1500 || t1 == 0)
	    	   {
	    		   showDialog();
	    		   
	    	   }
	    	   
//	    	   if (ShPreferencesUtils.getTimeActive(getApplicationContext()) ) 
//	    	   {
//	    		   
//	    	   }
//	    	   else
//	    	   {
//	    		  goToMagazin();
//	    	   }

	    	  return true;
	       }

		 
		
		 
		 case R.id.menuMagazin :
		 {
			 sendDataForAnalytic(CATEGORY_ON_MENU_ITEM_CLICK, ACTION_CLICK, LABEL_GO_TO_MAGAZIN);
			 displayInterstitial();
			goToMagazin();
				return true;
		 }
		 case R.id.menuWallp:
	     {
	    	 
	    	 sendDataForAnalytic(CATEGORY_ON_MENU_ITEM_CLICK, ACTION_CLICK, LABEL_CALL_WALLPAPERS + " from Action bar");
	    	 
//	    	 if (UILApplication.sE != null)
//	    	 {
//	    		 ShPreferencesUtils.setTableName(getApplicationContext(), tableName);
//	    		 UILApplication.sE.refreshData();
//	    		 
//	    		 Toast.makeText(getApplicationContext(), android.R.string.ok, Toast.LENGTH_SHORT).show();
//	    		 return true;
//	    	 }
	    	//   
	    	// createNotivLiveWallpapersDialog();
	    	 boolean isShow = ShPreferencesUtils
	    			 .getNotiDoNotShowFlag(getApplicationContext());
	    	 if ( isShow)
	    	 {
	    		 createNotifAlertDialog();
	    		 return true;
	    	 }
	    	 else
	    	 {    	 
	    	 ShPreferencesUtils.setTableName(getApplicationContext(), tableName);
	    	 Intent intent = new Intent();
	    	 intent.setAction(WallpaperManager.ACTION_LIVE_WALLPAPER_CHOOSER);
	    	// startActivityForResult(intent, 1000);
	    	startActivity(intent);
	    	 return true;
	    	 }


	     }
		   case R.id.menuAdd:
		     {
		    	 sendDataForAnalytic(CATEGORY_ON_MENU_ITEM_CLICK, ACTION_CLICK, LABEL_ADD_IMAGE);
			long t1 = 0;
			if (time1 == 0) {
				time1 = System.currentTimeMillis();
			} else {
				t1 = System.currentTimeMillis();
			}

			if (t1 - time1 > 1500 || t1 == 0) {
				
				if (ShPreferencesUtils.getCameraActive(getApplicationContext())) {

					alertImage();
				} else {
					createGaleryIntent();
				}
			}
		     }break;
		}
//		if (id == R.id.action_settings) {
//			return true;
//		}
		return super.onOptionsItemSelected(item);
	}
	
	private void goToMagazin()
	{
		 Intent intent = new Intent(MainActivity.this, MagazinActivity.class);
			startActivity(intent);
		overridePendingTransition(R.anim.pull_in_right,
				R.anim.push_out_left);
	}
	
	private void alertImage()
	{
		Integer selection = -1;

 		String [] items = getResources().getStringArray(R.array.images_array);
// 		ArrayAdapter<String>	genderAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, 
// 				items
// 				);
 		GenderAdapter genderAdapter = new GenderAdapter(MainActivity.this, items);

 		Builder genderBuilder = new AlertDialog.Builder(this)
         .setTitle(R.string.alert_str_choose)
         .setIcon(R.drawable.ic_launcher)
         .setSingleChoiceItems(genderAdapter, selection, new DialogInterface.OnClickListener() {
             public void onClick(DialogInterface dialog, int which) 
             {
                 
                switch ( which)
                {
                case UILApplication.GALLERY_TYPE :
                {
                	imageForCameraPath = null;
             	   createGaleryIntent();
                }break;
                case UILApplication.CAMERA_TYPE :
                {
             	   createCameraIntent();
                }break;
                }
                 dialog.cancel();
             }
         });
     AlertDialog genderAlert = genderBuilder.create();
     genderAlert.show();
		
	}
	
	
	

	
	
	String imageForCameraPath;
	
	private void createCameraIntent()
	{
		
		Intent intentAttachPhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		Uri uri = Uri.fromFile(getImagePath());

		imageForCameraPath = uri.getPath();

		intentAttachPhoto.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		startActivityForResult(intentAttachPhoto, CAPTURE_IMAGE_REQUEST);
	}
	
	private File getImagePath() {
		File directory = new File(
				Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
				this.getResources().getString(R.string.database_name)
				);
		if (!directory.exists()) {
			directory.mkdirs();
		}

		return new File(directory.getPath() + File.separator
				+ System.currentTimeMillis() + ".jpg");
	}
  


	private void createGaleryIntent()
	{
		if (tvResult.getText().toString()
				.equals(getResources().getString(R.string.str_all_empty)))
		{
			createAddAlertDialog(null, -1);
		} else 
		{
			try {
				final Intent galleryIntent = new Intent(
						Intent.ACTION_GET_CONTENT);

				galleryIntent.setType("*/*");
				startActivityForResult(galleryIntent, PICK_IMAGE);
			} catch (Exception e) {
			}
		}
		
	}

	private static int PICK_IMAGE = 1;
	private static int CROP_IMAGE = 2;
	public static int UPDATE_IMAGE = 3;
	private static final int CAPTURE_IMAGE_REQUEST = 4;
	
	
	private void getPhoto(String path)
	{
		if (path == null)
		{
		   createOtherApplicationAlertDialog();	
		   sendDataForAnalytic(CATEGORY_APPLICATION_ERROR, ACTION_CLICK_ERROR, 
					"path is null");
		   return;
		}
		boolean fl = BitmapUtils.provOnOutOfMemmoryError(this,  path);
		
		if (fl) 
		{

		ImageDto imageDto = new ImageDto();
		imageDto.Path = path;
		
		
		Intent intent = new Intent(this, CropActivity.class);
		intent.putExtra(Extra.IMAGE_URL, path);
		
		
		startActivityForResult(intent, CROP_IMAGE);
		}
		else
		{
			
			
			   createLogAlertDialog();
			
			sendDataForAnalytic(CATEGORY_APPLICATION_ERROR, ACTION_CLICK_ERROR, 
					LABEL_ERROR_ADD_IMAGE);
		}
	}
	 
	



	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		
		wasClick = true;
		if ( requestCode == PICK_IMAGE && resultCode == RESULT_OK)
		{
			String path = StringUtils.getPathFromUri(this, data);
			
//			Tracker t = ((UILApplication)MainActivity.this.getApplicationContext()).getTracker(
//		            TrackerName.APP_TRACKER);
//		        // Build and send an Event.
//		        t.send(new HitBuilders.EventBuilder()
//		            .setCategory(CATEGORY_APPLICATION_ERROR)
//		            .setAction(ACTION_CLICK_ERROR)
//		            .setLabel("Path = " + path)
//		            .build());
			getPhoto(path);
            
			
		}
		else	if (requestCode == CAPTURE_IMAGE_REQUEST && resultCode == RESULT_OK) 
		{
            imageForCameraPath = "file:/" + imageForCameraPath;   
			getPhoto(imageForCameraPath);
		}
		else if ( requestCode == CROP_IMAGE && resultCode == RESULT_OK)
		{
			String path = data.getStringExtra(Extra.IMAGE_URL);
			
			boolean wasRotation = data.getBooleanExtra(Extra.WAS_ROTATION, false);
				HashMap<String, Rect> map = (HashMap<String, Rect>) data
						.getSerializableExtra(Extra.IMAGE_HM);

				ImageDto imageDto = new ImageDto();
				imageDto.Path = path;
				imageDto.ImageMap = map;
				imageDto.WasRotation = wasRotation;
				
				
				imageUrls.add(imageDto.Path);

				
				((UILApplication) this.getApplicationContext()).dbAdapter
						.addImage(tableName, imageDto);
				

				imageAdapter.notifyDataSetChanged();

				provActivity();

				if (((UILApplication) this.getApplicationContext()).dbAdapter
						.getCount(tableName) == 1 
						&& ((UILApplication) this.getApplicationContext()).dbAdapter.getTableNameList().size()==1 
						&& UILApplication.sE != null) 
				{
					if (!ShPreferencesUtils.getTableName(getApplicationContext())
							.equals(tableName))
					{
						ShPreferencesUtils.setTableName(getApplicationContext(), tableName);
					}
							
					
					UILApplication.sE.refreshData();
				}
				
				if ( imageUrls.size() == 1)
				{
					createGuideLiveWallpapersAlert(); 
				}
				else if (ShPreferencesUtils.getNotifLiveWallpapers(getApplicationContext()))
				{
					createNotivLiveWallpapersDialog();
				}
			
   		 
		}
		else if ( requestCode == UPDATE_IMAGE && resultCode == RESULT_OK)
		{
			
			
			String path = data.getStringExtra(Extra.IMAGE_URL);
			boolean wasRotation = data.getBooleanExtra(Extra.WAS_ROTATION, false);
			HashMap<String, Rect> map = (HashMap<String, Rect>) data.getSerializableExtra(Extra.IMAGE_HM);
			ImageDto imageDto = new ImageDto();
			imageDto.Path = path;
			imageDto.ImageMap = map;
			imageDto.WasRotation = wasRotation;
			
			((UILApplication) this.getApplicationContext())
			.dbAdapter.updateImage(tableName, imageDto);
		
		}
		
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	
	
	
	@Override
	public void onBackPressed() 
	{
		sendDataForAnalytic(CATEGORY_ON_MENU_ITEM_CLICK, ACTION_CLICK, 
 			   LABEL_EXIT);
   showAdversting();
		    imageLoader.stop();
		
		super.onBackPressed();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		showAdversting();
		sendDataForAnalytic(CATEGORY_ON_MENU_ITEM_CLICK, ACTION_CLICK, 
 			   LABEL_EXIT);
		if (
		navDrawer.isDrawerOpen(Gravity.START))
		{
			
			navDrawer.closeDrawers();
			
			return true;
		}
		
		return super.onKeyDown(keyCode, event);
	}
	
//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
//			if ( navDrawer!= null && navDrawer.isDrawerOpen(navDrawer))
//			{
//				navDrawer.closeDrawer(navDrawer);
//			}
//			return true;
//		}
//		return super.onKeyDown(keyCode, event);
//	}
	
     int numb = 1000;
	private int time_dimension = 0;
	private String hint_time = "5";
	public void showDialog()
    {
		final String seconds = getResources().getString(R.string.seconds);
		final String minutes = getResources().getString(R.string.minutes);
		final String hours = getResources().getString(R.string.hours);
		final String days = getResources().getString(R.string.days);
		final String [] items = {seconds, minutes, hours, days};
    	AlertDialog.Builder builder=new AlertDialog.Builder(this);
    	builder.setTitle(R.string.str_choose_time);
    	
    	getLastSavedTime();
    	
    	
     	builder.setSingleChoiceItems(items,time_dimension, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
				switch ( which)
				{
				case 0:
                  {
            	   numb = 1000;  // ���
                  }break;
                case 1:
                {
          	          numb = 60 * 1000;  // ���
                }break;
                case 2:
                {
          	        numb = 60 * 60 * 1000; 
                }break;
                case 3:
                {
          	        numb =  24 * 60 * 60 * 1000;  
                }break;
				}
			
				
				
			}
		});
     	
     	final EditText input = new EditText(this);
		input.setInputType(InputType.TYPE_CLASS_NUMBER);
		input.setHint(hint_time);
		builder.setView(input);
		
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
				
				int time = 2147483640;
				try
				{
					 String value = input.getText().toString();
					 if ( value == null || value.isEmpty())
					 {
						 value = input.getHint().toString();
					 }
					time = Integer.parseInt(value) * numb;
					save(time);
				}catch ( Exception e) {}
				
				
			}
		});
		
		
		
    	builder.show();
    
    }
	
	public void onClick(View v)
	{
		createAddAlertDialog(null, -1);
		navDrawer.closeDrawers();
		/*Intent i = new Intent();
		 i.setAction(WallpaperManager.ACTION_LIVE_WALLPAPER_CHOOSER);
	  startActivityForResult(i, 10);*/
	}
	
	private void createLogAlertDialog()
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		 builder.setMessage(R.string.allertError)
		 .setTitle(R.string.str_ups)
			.setIcon(R.drawable.ic_notif_error)
			.setCancelable(false)
			.setNeutralButton(android.R.string.ok, null);
		 AlertDialog alert = builder.create();

	    	alert.show();
	}
	private void createOtherApplicationAlertDialog() 
	{
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		 builder.setMessage(R.string.allertOtherApplication)
		 .setTitle(R.string.str_ups)
			.setIcon(R.drawable.ic_notif_error)
			.setCancelable(false)
			.setNeutralButton(android.R.string.ok, new DialogInterface. OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					createGaleryIntent();
				}
			});
		 AlertDialog alert = builder.create();

	    	alert.show();
	}
	
	
	private void createGuideLiveWallpapersAlert()
	{
		String message = this.getResources().getString(R.string.str_guide_live_wallpapers) + "\n" + 
				this.getResources().getString(R.string.str_wallp) + "\n" + 
				this.getResources().getString(R.string.wallp_name).toUpperCase();
			
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			 builder.setMessage(message)
			 .setTitle(R.string.str_attention)
				.setIcon(R.drawable.ic_launcher)
				.setCancelable(false)
				.setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() 
				{
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						ShPreferencesUtils.setTableName(getApplicationContext(), tableName);
				    	 Intent intent = new Intent();
				    	 intent.setAction(WallpaperManager.ACTION_LIVE_WALLPAPER_CHOOSER);
				    	startActivity(intent);
						
					}
				});
			 
			 AlertDialog alert = builder.create();
			
			 

		    	alert.show();
	}
	
	private void createNotivLiveWallpapersDialog()
	{
		
			CheckBox chb = new CheckBox(getApplicationContext());
			chb.setText(R.string.str_show);
			chb.setTextColor(Color.BLACK);
			chb.setChecked(false);
			chb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) 
				{

						ShPreferencesUtils.setNotifLiveWallpapers(getApplicationContext(), !isChecked);
					
					
				}
			});
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			 builder.setMessage(R.string.str_notife_guide_live_wallpapers)
			 .setTitle(R.string.strNotifAdvise)
				.setIcon(R.drawable.ic_launcher)
				.setCancelable(false)
				.setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() 
				{
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						ShPreferencesUtils.setTableName(getApplicationContext(), tableName);
				    	 Intent intent = new Intent();
				    	 intent.setAction(WallpaperManager.ACTION_LIVE_WALLPAPER_CHOOSER);
				    	startActivity(intent);
				    	 sendDataForAnalytic(CATEGORY_ON_MENU_ITEM_CLICK, ACTION_CLICK, LABEL_CALL_WALLPAPERS);
						
					}
				});
			 
			 AlertDialog alert = builder.create();
			 alert.setView(chb);
			 

		    	alert.show();
	}
	private void createNotifAlertDialog()
	{
		String message = this.getResources().getString(R.string.str_wallp) + "\n" + 
			this.getResources().getString(R.string.wallp_name);
		CheckBox chb = new CheckBox(getApplicationContext());
		chb.setText(R.string.str_show);
		chb.setTextColor(Color.BLACK);
		chb.setChecked(false);
		chb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) 
			{

					ShPreferencesUtils.setNotiDoNotShowFlag(getApplicationContext(), !isChecked);
				
				
			}
		});
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		 builder.setMessage(message)
		 .setTitle(R.string.str_attention)
			.setIcon(R.drawable.ic_launcher)
			.setCancelable(false)
			.setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() 
			{
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					ShPreferencesUtils.setTableName(getApplicationContext(), tableName);
			    	 Intent intent = new Intent();
			    	 intent.setAction(WallpaperManager.ACTION_LIVE_WALLPAPER_CHOOSER);
			    	startActivity(intent);
			    	 sendDataForAnalytic(CATEGORY_ON_MENU_ITEM_CLICK, ACTION_CLICK, LABEL_CALL_WALLPAPERS);
					
				}
			});
		 
		 AlertDialog alert = builder.create();
		 alert.setView(chb);
		 

	    	alert.show();
	}
	
	private void createAddAlertDialog(final String tablename, final int position) 
	{
		final EditText edit = new EditText(this);
		edit.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
		
		if ( tablename != null && position >= 0)
		{
			edit.setText(tablename);
		}
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		if ( tablename != null && position >= 0)
		{
		  builder.setMessage(R.string.alertRename);
		}
		else
		{
			builder.setMessage(R.string.alertCreate);
		}
    	builder.setTitle(R.string.app_name)
    			.setIcon(R.drawable.ic_launcher)
    			.setCancelable(false)
    			.setNegativeButton(android.R.string.cancel,
    					new DialogInterface.OnClickListener() {
    						public void onClick(DialogInterface dialog, int id) {
    							dialog.cancel();
    						}
    					})
    					.setPositiveButton(android.R.string.ok,
    					new DialogInterface.OnClickListener() {
    						public void onClick(DialogInterface dialog, int id) {
    							
    							
    							String tableName = edit.getText().toString();
    							if ( isTableNameExist(tableName))
    							{
    								dialog.cancel();
    							}
    							else if ( tablename != null && position >= 0)
    							{
    								listTables.set(position, tableName);
    								getDbAdapter().renameTable(tablename, tableName);
    								navAdapter.notifyDataSetChanged();
    								createIntent(position);
    								navDrawer.closeDrawers();
    								ShPreferencesUtils.setTableName(
    										getApplicationContext(), tableName);
    							}
    							else
    							{
    							listTables.add(tableName);
    							getDbAdapter().createTable(tableName);
                                navAdapter.notifyDataSetChanged();
    							
    							createIntent(listTables.size() - 1);
    							}
    							
    							//provViews();
    						}
    					})
    					;
    	
    	
    	
    	builder.setView(edit);
    	AlertDialog alert = builder.create();
    	
    	
    	alert.show();
		
		
	}
	
	protected void createIntent(int position) 
	{
		Intent intent = new Intent(this, MainActivity.class);
		intent.putExtra(Extra.TABLE_NAME, listTables.get(position));
		startActivity(intent);
	}
	
	String tableName = null;
	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		imageLoader.clearDiscCache();
		imageLoader.clearMemoryCache();
		 tableName = intent.getStringExtra(Extra.TABLE_NAME);
		
			if ( tableName != null && !tableName.isEmpty())
			{
				
			//	getActionBar().setTitle(tableName);
				
				for ( int i = 0; i < listTables.size(); i++)
				{
					if ( listTables.get(i).equals(tableName))
					{
						ShPreferencesUtils.setTablePosition(getApplicationContext(), i);
					}
				}
				
				//ShPreferencesUtils.setTableName(getApplicationContext(), tableName);
				imageUrls = ((UILApplication)this.getApplicationContext()).dbAdapter.getList(tableName); 

				imageAdapter = new ImageAdapter(this, imageUrls, imageLoader, options);
				((GridView) listView).setAdapter(imageAdapter);
				
				onGridViewClickListner = new OnGridViewClickListner(this, imageUrls);
			    onGridViewLongClickListner = new OnGridViewLongClickListner(this, tableName);
				
				listView.setOnItemClickListener(onGridViewClickListner);
				listView.setOnItemLongClickListener(onGridViewLongClickListner);
		     	provActivity();
			
			}
			
	

		
	}
	
	private boolean isTableNameExist(String name)
	{
	  ArrayList<String> list = 	getDbAdapter().getTableNameList();
	  for ( String str : list)
	  {
		  if ( str.equals(name))
		  {
			  return true;
		  }
		  
	  }
	  return false;
	}
	
//	public boolean provUrlList()
//	{
//		imageUrls = ((UILApplication)this.getApplicationContext()).dbAdapter.getList(tableName); 
//		if (imageUrls.isEmpty())
//		{
//			listView.setVisibility(View.GONE);
//			rl.setVisibility(View.VISIBLE);
//			TextView tv = (TextView) findViewById(R.id.tvInfo);
//			tv.setText(R.string.str_albom_empty);
//			
//			rl.setOnClickListener(new OnClickListener() {
//				
//				@Override
//				public void onClick(View v) 
//				{
//					final Intent galleryIntent = new Intent(
//				            Intent.ACTION_GET_CONTENT);
//		
//		               galleryIntent.setType("*/*");
//		               startActivityForResult(galleryIntent, PICK_IMAGE);
//				}
//			});
//			
//			return false;
//		}
//		else
//		{
//			listView.setVisibility(View.VISIBLE);
//			rl.setVisibility(View.GONE);
//			
//			return true;
//		}
//
//	}
//	

	
	private void getLastSavedTime() 
	{
		int time =  ( (UILApplication)  this.getApplicationContext() ).getWallTime();
		
		time /= 1000;
		if ( time < 60) // ���� ��� �������
		{
			time_dimension = 0;
			hint_time = time + "";
		}
		else if ((time = time / 60) < 60) // ���� ������
			{
				time_dimension = 1;
				hint_time = time + "";
			}
		else if ((time = time / 60) < 24) // ���� ����
		{
			time_dimension = 2;
			hint_time = time + "";
		}
		else
		{
			time /= 24;
			time_dimension = 3;
			hint_time = time + "";
		}


		
	}
	
	public void provActivity()
	{
		ArrayList<String> list = getDbAdapter().getTableNameList();
		
		
		
		if ( list == null || list.isEmpty())
		{
			//listView.setVisibility(View.GONE);
			//rl.setVisibility(View.VISIBLE);
			tvResult.setText(R.string.str_all_empty);
			menuAdd.setVisible(false);
			menuMagazin.setVisible(false);
			//menuBackground.setVisible(false);
			menuWallp.setVisible(false);
			menuSettings.setVisible(false);
			//getActionBar().setTitle("Нет Альбомов");
		}
		else if (getDbAdapter().getList(tableName).isEmpty())
		{
			//listView.setVisibility(View.GONE);
			//rl.setVisibility(View.VISIBLE);
			tvResult.setText(R.string.str_albom_empty);
			menuAdd.setVisible(false);
			//menuBackground.setVisible(false);
			menuMagazin.setVisible(false);
			menuWallp.setVisible(false);
			menuSettings.setVisible(false);
		}
		else
		{
			//listView.setVisibility(View.VISIBLE);
			//rl.setVisibility(View.GONE);
			//tvResult.setText("Invisible");
			tvResult.setText(R.string.str_albom_empty);
			menuAdd.setVisible(true);
			//menuBackground.setVisible(true);
			menuWallp.setVisible(true);
			menuSettings.setVisible(true);
			menuMagazin.setVisible(true);
		}
	}

	private void save(int time)
	{
		 ( (UILApplication)  this.getApplicationContext() ).saveChoosedTime(time);
	}
	
	private DbAdapter getDbAdapter()
	{
		return ((UILApplication)this.getApplicationContext()).dbAdapter;
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		 // Создание межстраничного объявления.
        interstitial = new InterstitialAd(this);
        interstitial.setAdUnitId(getResources().getString(R.string.interstitialAd_1));

        // Создание запроса объявления.
        AdRequest adRequest1 = new AdRequest.Builder().build();

        // Запуск загрузки межстраничного объявления.
        interstitial.loadAd(adRequest1);
	}
	
	// Вызовите displayInterstitial(), когда будете готовы показать межстраничное объявление.
    public void displayInterstitial() {
      if (interstitial.isLoaded())
      {
        interstitial.show();
      }
    }
    
    private boolean isNetworkAvailable() {
	    ConnectivityManager connectivityManager 
	          = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}
    
    int count = 0;
    private void showAdversting()
    {
    	
    	count = ShPreferencesUtils.getClickCount(getApplicationContext());
    	count++;
    	ShPreferencesUtils.setClickCount(getApplicationContext(), count);
    	if (count % 7 == 0)
    	{
    		displayInterstitial();
    		count = 0;
        	ShPreferencesUtils.setClickCount(getApplicationContext(), count);
    	}
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
    
    private void sendDataForAnalytic(String category, String action, String label)
    {
    	Tracker t = ((UILApplication)this.getApplicationContext()).getTracker(
	            TrackerName.APP_TRACKER);
	        // Build and send an Event.
	        t.send(new HitBuilders.EventBuilder()
	            .setCategory(category)
	            .setAction(action)
	            .setLabel(label)
	            .build());
	        
	        Tracker t1 = ((UILApplication)this.getApplicationContext()).getTracker(
		            TrackerName.APP_TRACKER);
		        // Build and send an Event.
		        t1.send(new HitBuilders.EventBuilder()
		            .setCategory(getDevName())
		            .setAction(action)
		            .setLabel(label)
		            .build());
	        
	        
    }
    

	private void setDeviceName() 
	{
		if (TextUtils.isEmpty(ShPreferencesUtils.getDeviceName(getApplicationContext())))
		{
		   String name = getDeviceName() + " [ " + System.currentTimeMillis() + " ]";
		   ShPreferencesUtils.setDeviceName(getApplicationContext(), name);
		}
		
		//11-28 11:34:22.262: I/System.out(11710): dev name = Samsung GT-P5100 [ 1417160062274 ]
//System.out.println(getDevName());
	}
	
	private String getDevName()
	{
		String name = ShPreferencesUtils.getDeviceName(getApplicationContext());
		if (TextUtils.isEmpty(name))
		{
			setDeviceName();
			name = ShPreferencesUtils.getDeviceName(getApplicationContext());
		}
		
		return name;
		
	}
	
	public String getDeviceName() {
		  String manufacturer = Build.MANUFACTURER;
		  String model = Build.MODEL;
		  if (model.startsWith(manufacturer)) {
		    return capitalize(model);
		  } else {
		    return capitalize(manufacturer) + " " + model;
		  }
		}


		private String capitalize(String s) {
		  if (s == null || s.length() == 0) {
		    return "";
		  }
		  char first = s.charAt(0);
		  if (Character.isUpperCase(first)) {
		    return s;
		  } else {
		    return Character.toUpperCase(first) + s.substring(1);
		  }
		} 
	
	
}
