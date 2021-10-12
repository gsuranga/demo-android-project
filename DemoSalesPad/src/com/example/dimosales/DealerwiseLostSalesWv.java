package com.example.dimosales;

import ceylon.linux.url.URLS;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class DealerwiseLostSalesWv extends Activity {

	protected ProgressDialog dialog;
	boolean loadingFinished = true;
	boolean redirect = false;
	SharedPreferences userdata;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dealerwise_lost_sales_wv);
		
		DealerwiseLostSalesWv.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		
		WebView myWebView = (WebView) findViewById(R.id.dwls);
		myWebView.getSettings().setJavaScriptEnabled(true);
		userdata = DealerwiseLostSalesWv.this.getSharedPreferences("USERDATA",Context.MODE_PRIVATE);
	
		String username =  userdata.getString("u_name", "");
		//String name = userdata.getString("name", "");
		//myWebView.loadUrl(URLS.dealerwise_lost_sales +"?txt_user_name="+ username+ "&txt_password="+ userdata.getString("pw", "").toString()+"&txt_name="+ name+ "&type=dwls&dealer_id=1");
		
		
		
	    myWebView.loadUrl(URLS.dealerwise_lost_sales+"?txt_user_name="+ username+ "&txt_password="+ userdata.getString("pw", "").toString()+"&type=dwls&dealer_id=1");
	  //  Log.e("URL",URLS.dealerwise_lost_sales+"?txt_user_name="+ username+ "&txt_password="+ userdata.getString("pw", "").toString()+"&type=dwls&dealer_id=1");
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
				dialog = new ProgressDialog(DealerwiseLostSalesWv.this);
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
		getMenuInflater().inflate(R.menu.dealerwise_lost_sales_wv, menu);
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
