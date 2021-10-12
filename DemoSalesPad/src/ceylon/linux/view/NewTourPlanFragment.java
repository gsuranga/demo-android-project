package ceylon.linux.view;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import ceylon.linux.url.URLS;

import com.example.dimosales.R;

public class NewTourPlanFragment extends Fragment {

	private WebView webview;
	private static final String TAG = "Main";
	private ProgressDialog progressBar;
	SharedPreferences userdata;

	@Override
	public View onCreateView(final LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_register_dealer,
				container, false);

		this.webview = (WebView) rootView.findViewById(R.id.webView1);

		userdata = getActivity().getSharedPreferences("USERDATA",
				Context.MODE_PRIVATE);

		WebSettings settings = webview.getSettings();
		settings.setJavaScriptEnabled(true);
		webview.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);

		final AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
				.create();

		progressBar = ProgressDialog.show(getActivity(), "Dealer Registration",
				"Loading...");

		webview.setWebViewClient(new WebViewClient() {
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				Log.i(TAG, "Processing webview url click...");
				view.loadUrl(url);
				return true;
			}

			public void onPageFinished(WebView view, String url) {
				Log.i(TAG, "Finished loading URL: " + url);
				if (progressBar.isShowing()) {
					progressBar.dismiss();
				}
			}

			@SuppressWarnings("deprecation")
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				Log.e(TAG, "Error: " + description);
				Toast.makeText(getActivity(), "Oh no! " + description,
						Toast.LENGTH_SHORT).show();
				alertDialog.setTitle("Error");
				alertDialog.setMessage(description);
				alertDialog.setButton("OK",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								return;
							}
						});
				alertDialog.show();
			}
		});
		// webview.loadUrl("http://www.google.com");

		String username = userdata.getString("u_name", "");
		webview.loadUrl(URLS.manage_order_webview + "?txt_user_name=" + username
				+ "&txt_password=" + userdata.getString("pw", "").toString()
				+ "&type=atp&dealer_id=0");

		Log.i("URL", URLS.manage_order_webview + "?txt_user_name=" + username
				+ "&txt_password=" + userdata.getString("pw", "").toString()
				+ "&type=atp&dealer_id=0");

		return rootView;
	}

}
