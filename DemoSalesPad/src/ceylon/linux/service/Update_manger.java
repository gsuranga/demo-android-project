package ceylon.linux.service;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import ceylon.linux.controller.Jsonhelper;
import ceylon.linux.url.URLS;
import ceylon.linux.utility.Utility;
import ceylon.linux.view.Update_activity;


public class Update_manger extends Service {

	private Timer timer;
	static boolean status = false;
	static int num = 0;

	String path = Environment.getExternalStorageDirectory() + "/"; // Path to
																	// where you
																	// want to
																	// save the
																	// file
	
	String inet = "http://123.231.13.186/dimo_lanka/apk/DemoSalesPad.apk"; // Internet
																	// path to
																	// the file
	String cachedir = "";
	String filename = "TMC.apk";

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub

		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Utility utility = new Utility(Update_manger.this);
		
		if(utility.isNetworkAvailable()){
		
		timer = new Timer("Updater");
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
																
				String version = "";
				try {
					version = getPackageManager().getPackageInfo(
							getPackageName(), 0).versionCode
							+ "";
				} catch (NameNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				Jsonhelper jh = new Jsonhelper();
				ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
						2);
				nameValuePairs.add(new BasicNameValuePair("position_id", ""));// et_username.getText().toString().trim()
				nameValuePairs.add(new BasicNameValuePair("version", version));
				JSONObject jsonObject = jh
						.JsonObjectSendToServerPostWithNameValuePare(
								URLS.UPDATER, nameValuePairs);

				try {
					if(jsonObject!=null){
					if (jsonObject.getString("result").trim().equals("true")) {
						status = true;

						inet = jsonObject.getString("url").trim();
						Log.i(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>", "updating");

					} else {
						status = false;
						Log.w("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<", "not updating");
					}
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if (status) {

					
					//
					Intent intent = new Intent(Update_manger.this,
							Update_activity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent);

				} else {
					Log.i("", "thanks jim it's working");
				}

			}

			}, 0, 1200000 );//3600000
		}
	}


}
