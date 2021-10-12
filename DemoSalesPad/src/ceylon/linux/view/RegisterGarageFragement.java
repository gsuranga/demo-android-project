package ceylon.linux.view;

import java.util.ArrayList;
import java.util.HashMap;

import com.example.dimosales.R;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import ceylon.linux.db.Dbworker;
import ceylon.linux.url.URLS;

@SuppressLint("NewApi")
public class RegisterGarageFragement extends Fragment {

	EditText g_name;
	EditText g_address;
	EditText g_contact_no;
	EditText remarks;
	EditText g_officer;
	Button save_garage;
	
	protected ProgressDialog dialog;
	boolean loadingFinished = true;
	boolean redirect = false;
	SharedPreferences userdata;

	TableLayout tl;
	OnClickListener save = new OnClickListener() {

		@Override
		public void onClick(View v) {

			Log.i("g_name.getText().toString(),", g_name.getText().toString());
			Log.i("g_address.getText().toString(),", g_address.getText()
				.toString());
			Log.i("g_contact_no.getText().toString()", g_contact_no.getText()
				.toString());
			Log.i("g_name.getText().toString(),", g_name.getText().toString());
			Log.i("selectdealer.getText().toString(),", selectdealer.getText().toString());
			Log.i("remarks.getText().toString(),", remarks.getText().toString());
			dbworker.save_garages

				(g_name.getText().toString(), g_address
						.getText().toString(), g_contact_no.getText().toString(),
					selectdealer.getText().toString(), remarks.getText()
						.toString()
				);

			if (!(tl == null)) {

				if (!(tl.getChildCount() == 0)) {

					tl.removeAllViews();

				}
			}
			populate_Garage();
		}

	};
	ArrayList<HashMap<String, String>> values = new ArrayList<HashMap<String, String>>();
	AutoCompleteTextView selectdealer;
	Dbworker dbworker;
	OnClickListener clear_row = new OnClickListener() {

		@Override
		public void onClick(View v) {
			ViewGroup group = (ViewGroup) v.getParent();

			for (int i = 0; i < group.getChildCount(); i++) {
				View vv = group.getChildAt(i);
				if (vv instanceof TextView) {

					if (vv.getTag().equals("ID")) {
						// Log.i("ID is",((TextView) vv).getText().toString());
						dbworker.delete_newly_added_garage(((TextView) vv)
							.getText().toString());

					}

				}

			}
			group.removeAllViews();

		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		//dbworker = new Dbworker(getActivity());
		View rootView = inflater.inflate(R.layout.garage_fragement, container,
			false);
		getActivity().setRequestedOrientation(
				ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

		WebView myWebView = (WebView) rootView.findViewById(R.id.webview);
		myWebView.getSettings().setJavaScriptEnabled(true);
		userdata = getActivity().getSharedPreferences("USERDATA",
				Context.MODE_PRIVATE);
	
		String username =  userdata.getString("u_name", "");
	    myWebView.loadUrl(URLS.manage_garage_webview+"?txt_user_name="+ username+ "&txt_password="+ userdata.getString("pw", "").toString()+"&type=gr&dealer_id=1");
	  //  Log.i("URL",URL.manage_order_webview+"?txt_user_name="+ username+ "&txt_password="+ userdata.getString("pw", "").toString()+"&type=gp");
	    myWebView.getSettings().setBuiltInZoomControls(true);
	    myWebView.getSettings().setDisplayZoomControls(false);
	    
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

	/*	save_garage = (Button) rootView.findViewById(R.id.save_garage);
		save_garage.setOnClickListener(save);
		populate();

		tl = (TableLayout) rootView.findViewById(R.id.newly_added_garages);

		String[] from = {"NAME", "ID"};

		int[] to = {R.id.auto_id, R.id.auto_name};

		selectdealer = (AutoCompleteTextView) rootView
			.findViewById(R.id.n_dealer);
		SimpleAdapter adapter = new SimpleAdapter(getActivity(), values,
			R.layout.row, from, to);
		selectdealer.setThreshold(1);
		selectdealer.setAdapter(adapter);

		g_name = (EditText) rootView.findViewById(R.id.g_name);
		g_address = (EditText) rootView.findViewById(R.id.g_address);
		g_contact_no = (EditText) rootView.findViewById(R.id.g_contact_no);
		remarks = (EditText) rootView.findViewById(R.id.g_remark);
		g_officer = (EditText) rootView.findViewById(R.id.g_officer);*/
		
		
		
		return rootView;
	}

	public void populate() {

		Cursor cursor1 = dbworker.get_all_dealers();

		if (cursor1 != null) {
			cursor1.moveToFirst();
			while (!cursor1.isAfterLast()) {
				HashMap<String, String> temp = new HashMap<String, String>();
				temp.put("NAME", cursor1.getString(3));

				temp.put("ID", String.valueOf(cursor1.getString(2)));
				Log.i("tag", cursor1.getString(2));
				values.add(temp);
				cursor1.moveToNext();
			}

		}
		cursor1.close();

	}

	public void populate_Garage() {

		TableRow tr_head = new TableRow(getActivity());
		tr_head.setId(10);
		tr_head.setBackgroundColor(Color.GRAY);
		tr_head.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
			LayoutParams.WRAP_CONTENT));

		TextView label_id = new TextView(getActivity());
		label_id.setId(20);
		label_id.setText("Id");
		label_id.setTextColor(Color.RED);
		label_id.setGravity(Gravity.CENTER);
		label_id.setPadding(2, 2, 2, 2);
		tr_head.addView(label_id);

		TextView label_g_name = new TextView(getActivity());
		label_g_name.setId(20);
		label_g_name.setText("Garage Name");
		label_g_name.setTextColor(Color.RED);
		label_g_name.setGravity(Gravity.CENTER);
		label_g_name.setPadding(2, 2, 2, 2);
		tr_head.addView(label_g_name);// add the column to the table row
		// here

		TextView label_g_address = new TextView(getActivity());
		label_g_address.setId(21);// define id that must be unique
		label_g_address.setText("Garage Address "); // set the text for the
		label_g_address.setTextColor(Color.RED); // set the color
		label_g_address.setGravity(Gravity.CENTER);
		label_g_address.setPadding(2, 2, 2, 2);

		// set the padding (if
		tr_head.addView(label_g_address); // add the column to the table
		// row

		TextView label_g_contact_no = new TextView(getActivity());
		label_g_contact_no.setId(21);// define id that must be unique
		label_g_contact_no.setText("Contact No"); // set the text for the
		label_g_contact_no.setTextColor(Color.RED); // set the color
		label_g_contact_no.setPadding(2, 2, 2, 2);
		label_g_contact_no.setGravity(Gravity.CENTER);

		; // set the padding (if
		tr_head.addView(label_g_contact_no);

		TextView label_n_dealer = new TextView(getActivity());
		label_n_dealer.setId(21);// define id that must be unique
		label_n_dealer.setText("Nearest Dealer"); // set the text for the
		label_n_dealer.setTextColor(Color.RED); // set the color
		label_n_dealer.setPadding(2, 2, 2, 2);
		label_n_dealer.setGravity(Gravity.CENTER);

		; // set the padding (if
		tr_head.addView(label_n_dealer);

		TextView label_remark = new TextView(getActivity());
		label_remark.setId(21);// define id that must be unique
		label_remark.setText("Nearest Dealer"); // set the text for the
		label_remark.setTextColor(Color.RED); // set the color
		label_remark.setPadding(2, 2, 2, 2);
		label_remark.setGravity(Gravity.CENTER);

		; // set the padding (if
		tr_head.addView(label_remark);

		tl.addView(tr_head, new TableLayout.LayoutParams(
			LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));

		Integer count = 0;
		Cursor cursor = dbworker.get_all_newly_added_garages();

		if (cursor != null) {
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {

				TableRow tr = new TableRow(getActivity());

				if (count % 2 != 0)
					tr.setBackgroundColor(Color.rgb(28, 77, 103));
				tr.setId(100 + count);
				tr.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
					LayoutParams.WRAP_CONTENT));

				TextView id = new TextView(getActivity());
				id.setTag("ID");
				id.setId(200 + count);
				id.setText(cursor.getString(0));
				id.setTextColor(Color.BLACK);
				id.setGravity(Gravity.CENTER);
				tr.addView(id);

				TextView g_name = new TextView(getActivity());
				g_name.setTag("g_name");
				g_name.setId(200 + count);
				g_name.setText(cursor.getString(1));
				g_name.setTextColor(Color.BLACK);
				g_name.setGravity(Gravity.CENTER);
				tr.addView(g_name);

				TextView g_adress = new TextView(getActivity());
				g_adress.setId(200 + count);
				g_adress.setText(cursor.getString(2));
				g_adress.setTag("adress");
				g_adress.setTextColor(Color.BLACK);
				g_adress.setGravity(Gravity.CENTER);
				tr.addView(g_adress);

				TextView g_contac_no = new TextView(getActivity());
				g_contac_no.setId(200 + count);
				g_contac_no.setText(cursor.getString(3));
				g_contac_no.setTag("contactno");
				g_contac_no.setTextColor(Color.BLACK);
				g_contac_no.setGravity(Gravity.CENTER);
				tr.addView(g_contac_no);

				TextView n_dealer = new TextView(getActivity());
				n_dealer.setId(200 + count);
				n_dealer.setText(cursor.getString(4));
				n_dealer.setTag("n_dealer");
				n_dealer.setTextColor(Color.BLACK);
				n_dealer.setGravity(Gravity.CENTER);
				tr.addView(n_dealer);

				TextView remarks = new TextView(getActivity());
				remarks.setId(200 + count);
				remarks.setText(cursor.getString(5));
				remarks.setTag("remarks");
				remarks.setTextColor(Color.BLACK);
				remarks.setGravity(Gravity.CENTER);
				tr.addView(remarks);

				Button clear = new Button(getActivity());
				clear.setId(200 + count);
				clear.setText("Clear");
				clear.setTag("clear");
				clear.setOnClickListener(clear_row);
				clear.setTextColor(Color.BLACK);
				clear.setGravity(Gravity.CENTER);
				tr.addView(clear);

				// finally add this to the table row
				tl.addView(tr, new TableLayout.LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
				count++;
				cursor.moveToNext();

			}
			cursor.close();
		}

	}

}
