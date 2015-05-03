package com.whitemonk_team.livewallpapers.adapters;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.whitemonk_team.livewallpapers1.R;

public class ImageAdapter extends BaseAdapter 
{
	Activity context;
	ArrayList<String> imageUrls;
	ImageLoader imageLoader;
	DisplayImageOptions options;
	
	public ImageAdapter ( Activity context, ArrayList<String> imgUrls,	
			ImageLoader imageLoader, DisplayImageOptions options
			)
	{
		this.context = context;
		this.imageUrls = imgUrls;
		this.imageLoader = imageLoader;
		this.options = options;
	}
	@Override
	public int getCount() {
		return imageUrls.size();
	}

	@Override
	public Object getItem(int position) {
		return imageUrls.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	public ArrayList<Integer> getAdaptersIds(String path)
	{
		ArrayList<Integer> list = new ArrayList<>();
		for ( int i = 0; i < imageUrls.size(); i++)
		{
			if ( imageUrls.get(i).equals(path)  )
			{
				list.add(i);
			}
		}
		
		return list;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		View view = convertView;
		if (view == null) 
		{
			view = context.getLayoutInflater().inflate(R.layout.item_grid_image, parent, false);
			holder = new ViewHolder();
			assert view != null;
			holder.imageView = (ImageView) view.findViewById(R.id.image);
			holder.progressBar = (ProgressBar) view.findViewById(R.id.progress);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		imageLoader.displayImage(imageUrls.get(position), holder.imageView, options, new SimpleImageLoadingListener() {
									 @Override
									 public void onLoadingStarted(String imageUri, View view) {
										 holder.progressBar.setProgress(0);
										 holder.progressBar.setVisibility(View.VISIBLE);
									 }

									 @Override
									 public void onLoadingFailed(String imageUri, View view,
											 FailReason failReason) {
										 holder.progressBar.setVisibility(View.GONE);
									 }

									 @Override
									 public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
										 holder.progressBar.setVisibility(View.GONE);
									 }
								 }, new ImageLoadingProgressListener() {
									 @Override
									 public void onProgressUpdate(String imageUri, View view, int current,
											 int total) {
										 holder.progressBar.setProgress(Math.round(100.0f * current / total));
									 }
								 }
		);

		return view;
	}

	class ViewHolder {
		ImageView imageView;
		ProgressBar progressBar;
	}
}