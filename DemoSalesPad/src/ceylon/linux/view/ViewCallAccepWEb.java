package ceylon.linux.view;

import com.example.dimosales.R;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import ceylon.linux.url.URLS;

@SuppressLint("SetJavaScriptEnabled")
public class ViewCallAccepWEb extends Fragment {
	protected ProgressDialog dialog;
	boolean loadingFinished = true;
	boolean redirect = false;
	SharedPreferences userdata;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(
				R.layout.webview_for_manage_purchase_order, container, false);
		getActivity().setRequestedOrientation(
				ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

		WebView myWebView = (WebView) rootView.findViewById(R.id.webview);

		userdata = getActivity().getSharedPreferences("USERDATA",
				Context.MODE_PRIVATE);
	
		String username =  userdata.getString("u_name", "");
	    myWebView.loadUrl(URLS.manage_order_webview+"?txt_user_name="+ Call_Order_Accepting.userName+ "&txt_password="+ Call_Order_Accepting.password+"&type=call&dealer_id=1");
	    Log.i("URL",URLS.manage_order_webview+"?txt_user_name="+ username+ "&txt_password="+ userdata.getString("pw", "").toString()+"&type=mo");
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
				dialog = new ProgressDialog(getActivity());
				dialog.setMessage("Loading Data........");
				dialog.setCanceledOnTouchOutside(false);
				dialog.show();
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				dialog.dismiss();
			}
		});

		myWebView.getSettings().setJavaScriptEnabled(true);
		myWebView.getSettings().setBuiltInZoomControls(true);
		myWebView.getSettings().setDisplayZoomControls(false);
        
		return rootView;
	}
}
