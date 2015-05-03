/*******************************************************************************
 * Copyright 2011-2013 Sergey Tarasevich
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.whitemonk_team.livewallpapers.objects;

import java.util.HashMap;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.StrictMode;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.whitemonk_team.livewallpapers.databases.DbAdapter;
import com.whitemonk_team.livewallpapers.enums.TrackerName;
import com.whitemonk_team.livewallpapers.objects.Constants.Config;
import com.whitemonk_team.livewallpapers.objects.StarFieldsShow.StarFieldsEngine;
import com.whitemonk_team.livewallpapers.utils.ShPreferencesUtils;
import com.whitemonk_team.livewallpapers1.R;

/**
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 */
public class UILApplication extends Application {
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@SuppressWarnings("unused")
	@Override
	public void onCreate() {
		if (Config.DEVELOPER_MODE && Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyDialog().build());
			StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyDeath().build());
		}

		super.onCreate();
		
		dbAdapter = new DbAdapter(getApplicationContext());

		initImageLoader(getApplicationContext());
	}
	
	 HashMap<TrackerName, Tracker> mTrackers = new HashMap<TrackerName, Tracker>();
	
	 private static final String USER_TIME = "user_time";
	 private static final String FILE_NAME = "time_user";
	 
	 public static final int GALLERY_TYPE = 0;
	 public static final int CAMERA_TYPE = 1;

	private static final String PROPERTY_ID = "UA-55930871-3";
	
	 public int getWallTime()
	   {
		   
		   SharedPreferences sd = getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
		   int time = sd.getInt(USER_TIME, 5000); 
		   return time;
	   }
	 
	public synchronized Tracker getTracker(TrackerName trackerId) {
		    if (!mTrackers.containsKey(trackerId)) {

		      GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
		      Tracker t = (trackerId == TrackerName.APP_TRACKER) ? analytics.newTracker(PROPERTY_ID)
		          : (trackerId == TrackerName.GLOBAL_TRACKER) ? analytics.newTracker(R.xml.global_tracker)
		              : analytics.newTracker(R.xml.ecommerce_tracker);
		      mTrackers.put(trackerId, t);

		    }
		    return mTrackers.get(trackerId);
		  }
	 
	 public String getTableName()
	 {
		 return ShPreferencesUtils.getTableName(getApplicationContext());
	 }
	 
	 public void saveChoosedTime(int time) // ��������� ����� ������ ����������
	   {
		   SharedPreferences sd = getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
		   SharedPreferences.Editor  editor = sd.edit();
		   editor.putInt(USER_TIME, time);
		   editor.commit();
		   
		   if ( sE != null)
		   {
			   sE.refreshData();
		   }
	   }
	
	public static  StarFieldsEngine sE;
	
	public DbAdapter dbAdapter;
	public static int _Id = 0;

	public static void initImageLoader(Context context) {
		// This configuration tuning is custom. You can tune every option, you may tune some of them,
		// or you can create default configuration by
		//  ImageLoaderConfiguration.createDefault(this);
		// method.
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.writeDebugLogs() // Remove for release app
				.build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
	}
}