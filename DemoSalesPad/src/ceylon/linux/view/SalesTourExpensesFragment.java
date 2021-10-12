package ceylon.linux.view;

import java.util.ArrayList;
import java.util.HashMap;

import com.example.dimosales.R;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import ceylon.linux.db.Dbworker;
import ceylon.linux.url.URLS;

public class SalesTourExpensesFragment extends Fragment {
	protected ProgressDialog dialog;
	boolean loadingFinished = true;
	boolean redirect = false;
	SharedPreferences userdata;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.sales_tour_itenary,container, false);
		
		getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);	
		WebView myWebView = (WebView) rootView.findViewById(R.id.wv_tourexpenses);
		userdata = getActivity().getSharedPreferences("USERDATA",Context.MODE_PRIVATE);

		String username =  userdata.getString("u_name", "");
	    myWebView.loadUrl(URLS.android_web_auth +"?txt_user_name="+ username+ "&txt_password="+ userdata.getString("pw", "").toString()+"&type=st");
	    Log.i("URL",URLS.android_web_auth +"?txt_user_name="+ username+ "&txt_password="+ userdata.getString("pw", "").toString()+"&type=st");
        
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
				
		return  rootView;
		};	
}
