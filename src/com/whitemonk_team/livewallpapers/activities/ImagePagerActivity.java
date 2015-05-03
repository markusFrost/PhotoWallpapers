package com.whitemonk_team.livewallpapers.activities;

import java.util.ArrayList;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.whitemonk_team.livewallpapers.adapters.ImagePagerAdapter;
import com.whitemonk_team.livewallpapers.objects.Constants.Extra;
import com.whitemonk_team.livewallpapers1.R;

public class ImagePagerActivity extends BaseActivity {

	private static final String STATE_POSITION = "STATE_POSITION";

	DisplayImageOptions options;

	ViewPager pager;
	
	ImagePagerAdapter imagePagerAdapter;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_pager);

		Bundle bundle = getIntent().getExtras();
		assert bundle != null;
		//list.toArray(new Foo[list.size()]);list.toArray(new Foo[list.size()]);
		
		ArrayList<String> list = bundle.getStringArrayList(Extra.IMAGES);
		String[] imageUrls  =  list.toArray(new String [list.size()]);
		
		
		int pagerPosition = bundle.getInt(Extra.IMAGE_POSITION, 0);

		if (savedInstanceState != null) {
			pagerPosition = savedInstanceState.getInt(STATE_POSITION);
		}

		options = new DisplayImageOptions.Builder()
			.showImageForEmptyUri(R.drawable.ic_empty)
			.showImageOnFail(R.drawable.ic_error)
			.resetViewBeforeLoading(true)
			.cacheOnDisc(true)
			.imageScaleType(ImageScaleType.EXACTLY)
			.bitmapConfig(Bitmap.Config.RGB_565)
			.considerExifParams(true)
			.displayer(new FadeInBitmapDisplayer(300))
			.build();

		imagePagerAdapter = new ImagePagerAdapter(this, imageUrls, imageLoader, options);
		pager = (ViewPager) findViewById(R.id.pager);
		pager.setAdapter(imagePagerAdapter);
		pager.setCurrentItem(pagerPosition);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.image_pager, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home: {

			closeActivity();

			return true;
		}

		default:
			break;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putInt(STATE_POSITION, pager.getCurrentItem());
	}

}