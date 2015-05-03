package com.whitemonk_team.livewallpapers.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;

import com.whitemonk_team.livewallpapers1.R;

public class AnimationActivity extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}
	
	
	
/*	@Override
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
	}*/
	
	
	protected void closeActivity() {
		finish();
		overridePendingTransition(R.anim.pull_in_left,
				R.anim.push_out_right);		
	}



	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			closeActivity();
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}
	


}
