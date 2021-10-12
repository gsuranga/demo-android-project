package ceylon.linux.view;

import com.example.dimosales.R;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import ceylon.linux.url.URLS;

public class InvoiceSummery extends Fragment {
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
		myWebView.getSettings().setJavaScriptEnabled(true);
		userdata = getActivity().getSharedPreferences("USERDATA",
				Context.MODE_PRIVATE);

		String username = userdata.getString("u_name", "");
		// Log.i("rtrtr", userdata.getString("pw", "").toString());
		// http://192.168.1.62/TATA/TATA/anroid_login/checkAuthentication?txt_user_name=sales&txt_password=123
		myWebView.loadUrl(URLS.invoice_web_view + "?username=" + username
				+ "&password=" + userdata.getString("pw", "").toString()
				+ "&k=0");

		myWebView.setWebViewClient(new WebViewClient() {

			@Override
			public boolean shouldOverrideUrlLoading(WebView view,String urlNewString) {
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
				// Toast.makeText(getActivity(), "Loading",
				// Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onPageFinished(WebView view, String url) {

				dialog.dismiss();

			}
		});

		return rootView;

	}

}
