
package ceylon.linux.controller;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


public class InternetObserver {

	private static InternetObserver instance;
	private final ConnectivityManager connectivityManager;

	private InternetObserver(Context context) {
		connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	}

	public static synchronized boolean isConnectedToInternet(Context context) {
		if (instance == null) {
			instance = new InternetObserver(context);
		}
		NetworkInfo networkInfo = instance.connectivityManager.getActiveNetworkInfo();
		return networkInfo != null && networkInfo.isConnected();
	}

}
