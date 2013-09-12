package com.sandves.help;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;

public class FirstAidContent extends Activity {

	private WebView mWebView;
	private Torch mTorch;
	private final String[] url = {
			"file:///android_asset/crash_site.htm",
			"file:///android_asset/heart_lung.htm",
			"file:///android_asset/poisoning.htm",
			"file:///android_asset/bleedings.htm",
			"file:///android_asset/fire.htm",
			"file:///android_asset/frostbite.htm",
			"file:///android_asset/bone_fracture.htm",
			"file:///android_asset/head_injury.htm",
			"file:///android_asset/back_neck.htm",
			"file:///android_asset/sudden_illness.htm" 
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.firstaid_content);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		mWebView = (WebView)findViewById(R.id.webview);
		
		Intent i = getIntent();
		// getting attached intent data
		int position = i.getIntExtra("position", 0);
		// displaying selected product name        
		mWebView.loadUrl(url[position]);

		mTorch = new Torch(getApplicationContext(), (MenuItem)findViewById(R.id.menu_torch));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);

		switch(item.getItemId()) {
		case android.R.id.home:
            // app icon in action bar clicked; go home
            finish();
            return true;
		case R.id.menu_settings: 
			this.closeOptionsMenu();
			startActivity(new Intent(this, SettingsActivity.class));
			return true;
		case R.id.menu_torch:
			mTorch.light();
			return true;
		case R.id.menu_call:
			call();
			return true;
		}

		return true;
	}

	@Override
	public void onPause() {
		super.onPause();
		mTorch.releaseCamera();
	}

	public void call() {
		int emgNumber = Settings.getPref(getApplicationContext(), 
				SettingsFragment.KEY_PREF_CALL, getString(R.string.pref_emg_number_default));
		Intent i = new Intent(android.content.Intent.ACTION_DIAL, Uri.parse("tel:"+emgNumber));
		startActivity(i);
	}

}
