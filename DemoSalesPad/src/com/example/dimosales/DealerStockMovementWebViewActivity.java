package com.example.dimosales;

import ceylon.linux.url.URLS;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class DealerStockMovementWebViewActivity extends Activity {

	protected ProgressDialog dialog;
	boolean loadingFinished = true;
	boolean redirect = false;
	SharedPreferences userdata;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dealer_stock_movement);
		
		//Activity
		DealerStockMovementWebViewActivity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		//xml web view
		WebView myWebView = (WebView) findViewById(R.id.stock_movement_report);
		
		myWebView.getSettings().setJavaScriptEnabled(true);
		
		userdata = DealerStockMovementWebViewActivity.this.getSharedPreferences("USERDATA",Context.MODE_PRIVATE);
	
		String username =  userdata.getString("u_name", "");
	   
		myWebView.loadUrl(URLS.stock_movement_report+"?txt_user_name="+ username+ "&txt_password="+ userdata.getString("pw", "").toString()+"&type=dsm&dealer_id=1");
		//myWebView.loadUrl(URLS.stock_movement_report+"?txt_user_name="+ username+ "&txt_password="+ userdata.getString("pw", "").toString());
	    Log.e("URL",URLS.stock_movement_report+"?txt_user_name="+ username+ "&txt_password="+ userdata.getString("pw", "").toString()+"&type=dsm&dealer_id=1");
        
	    myWebView.setWebViewClient(new WebViewClient() {
           
			@Override
			public boolean shouldOverrideUrlLoading(WebView view,
					String urlNewString) {
				if (!loadingFinished) {
					redirect = true;
				}

				loadingFinished = false;
				view.loadUrl(urlNewString);
				return false;
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap facIcon) {
				loadingFinished = false;
				dialog = new ProgressDialog(DealerStockMovementWebViewActivity.this);
				dialog.setMessage("Loading Data........");
				dialog.setCanceledOnTouchOutside(false);
				dialog.show();

			}

			@Override
			public void onPageFinished(WebView view, String url) {
				dialog.dismiss();
			}
		});
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.dealer_stock_movement, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
