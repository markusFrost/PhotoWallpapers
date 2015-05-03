package com.whitemonk_team.livewallpapers.listners;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.PopupMenu;

import com.whitemonk_team.livewallpapers.activities.CropActivity;
import com.whitemonk_team.livewallpapers.activities.MainActivity;
import com.whitemonk_team.livewallpapers.databases.DbAdapter;
import com.whitemonk_team.livewallpapers.objects.Constants.Extra;
import com.whitemonk_team.livewallpapers.objects.ImageDto;
import com.whitemonk_team.livewallpapers.objects.UILApplication;
import com.whitemonk_team.livewallpapers.utils.StringUtils;
import com.whitemonk_team.livewallpapers1.R;
public class OnGridViewLongClickListner implements GridView.OnItemLongClickListener
{
	
	Activity activity;
	String tableName;
	
	public OnGridViewLongClickListner(){}
	
	public OnGridViewLongClickListner(Activity activity, String tableName)
	{
		this.activity = activity;
		this.tableName = tableName;
	}


	

	@Override
	public boolean onItemLongClick(final AdapterView<?> parent, View view,
			final int position, long id0) 
	{
		PopupMenu popup = new PopupMenu(activity, view);
		popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu()); 
		
		
		
		
		
		//registering popup with OnMenuItemClickListener  
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() 
        {  
         public boolean onMenuItemClick(MenuItem item) {  
        //  Toast.makeText(activity,"You Clicked : " + title,Toast.LENGTH_SHORT).show();  
        

        	 final String  path = (String) parent.getAdapter().getItem(position);
     		
        	 Integer id = StringUtils.getNeedId(tableName, parent, position, path, activity);
        	 
        	 int itemId = item.getItemId();
        	 
        	 if ( itemId == R.id.popup_menu_delete)
        	 {
        		createNotifAlert(id, path, position);
        		 return true;
        	 }
        	 
        	 else
        	 {
        		 ImageDto imageDto = getDbAdapter().getImageDto(tableName, position);
     			
     			Intent intent = new Intent(activity, CropActivity.class);
     			intent.putExtra(Extra.IMAGE_URL, path  );
     			intent.putExtra(Extra.IMAGE_HM, imageDto.ImageMap);
     			intent.putExtra(Extra.WAS_ROTATION, imageDto.WasRotation);
     			activity.startActivityForResult(intent, MainActivity.UPDATE_IMAGE);
        	
     			return true;
        	 
        	 }
        	
        	 
        	// return true;  
         }  
        });  

        popup.show();//showing popup menu 
        return true;
	}
	
	
	private DbAdapter getDbAdapter()
	{
		return ((UILApplication)activity.getApplicationContext()).dbAdapter;
	}
	
	public void createNotifAlert(final Integer id, final String path, final int position)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
    	builder.setTitle(R.string.app_name)
    			.setMessage(R.string.alertShure)
    			.setIcon(R.drawable.ic_launcher)
    			.setCancelable(false)
    			.setNegativeButton("Cancel",
    					new DialogInterface.OnClickListener() {
    						public void onClick(DialogInterface dialog, int id0) {
    							dialog.cancel();
    						}
    					})
    					.setPositiveButton("ОК",
    					new DialogInterface.OnClickListener() {
    						public void onClick(DialogInterface dialog, int id0) 
    						{
//    							 if ( id == null)
//    			            	 {
//    			            		 getDbAdapter().deleteItem(tableName, path);
//    			            	 }
//    			            	 else
//    			            	 {
//    			            		 getDbAdapter().deleteItem(tableName, id);
//    			            	 }
    							getDbAdapter().deleteImage(tableName, position);
    							 
    							 if ( UILApplication.sE != null)
    							 {
    								 UILApplication.sE.refreshData();
    							 }
    			        		 
    			        		 ((MainActivity)activity).imageAdapter.notifyDataSetChanged();
    			          		((MainActivity)activity).imageUrls.remove(position);
    			          		((MainActivity)activity).provActivity();
    						}
    					})
    					;

    	AlertDialog alert = builder.create();
    	
    	
    	alert.show();
		
	}
	
//	Кратко об алгоритме удаления фотографий
//	если в бд с такми адресом только одна то удаляем её по адресу
//	если несколько берём позиции этих путей в адаптере и бд
//	далее в массиве адаптеров ищем позицию выбранного элемента
//	далее в массиве бд ищем элемент по найденной позиции
//	по получившемуся ид и будет удалён элемент в бд

}
