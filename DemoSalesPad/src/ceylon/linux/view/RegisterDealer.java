package ceylon.linux.view;

import java.util.ArrayList;
import java.util.HashMap;

import com.example.dimosales.R;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import ceylon.linux.db.Dbworker;

@SuppressLint("NewApi")
public class RegisterDealer extends Fragment {
	// EditText dealer_search;
	EditText address;
	EditText shop_name;
	Button save_dealers;
	Button save2;
	EditText officer_name;
	EditText branch_name, dealer_name;
	Dbworker dbworker;
	TableLayout tl;
	OnClickListener o = new OnClickListener() {

		@Override
		public void onClick(View v) {
			dbworker.save_dealer(dealer_name.getText().toString(), address
				.getText().toString(), shop_name.getText().toString());
			if (!(tl == null)) {

				if (!(tl.getChildCount() == 0)) {

					tl.removeAllViews();

				}
			}
			populate();
		}
	};
	Spinner newdealer;
	CustomAutoCompleteTextView2 dealer_search;
	ArrayList<HashMap<String, String>> values = new ArrayList<HashMap<String, String>>();
	ArrayList<HashMap<String, String>> values1 = new ArrayList<HashMap<String, String>>();
	SimpleAdapter adapter;
	SimpleAdapter adapter1;
	ArrayAdapter<HashMap<String, String>> dataAdapter;
	SharedPreferences userdata;
	OnClickListener clear_row = new OnClickListener() {

		@Override
		public void onClick(View v) {
			ViewGroup group = (ViewGroup) v.getParent();

			for (int i = 0; i < group.getChildCount(); i++) {
				View vv = group.getChildAt(i);
				if (vv instanceof TextView) {

					if (vv.getTag().equals("ID")) {
						// Log.i("ID is",((TextView) vv).getText().toString());
						dbworker.delete_newly_added_dealer(((TextView) vv)
							.getText().toString());

					}

				}

			}
			group.removeAllViews();

		}
	};
	private OnItemSelectedListener itemSelectedListener = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
		                           long arg3) {
			HashMap<String, String> s = (HashMap<String, String>) arg0
				.getAdapter().getItem(arg2);
			shop_name.setText(s.get("shop_name").toString());
			address.setText(s.get("address").toString());
			dealer_name.setText(s.get("NAME").toString());

		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
		}
	};
	private OnItemClickListener autoItemSelectedListner = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
		                        long arg3) {
			HashMap<String, String> s = (HashMap<String, String>) arg0
				.getAdapter().getItem(arg2);
			shop_name.setText(s.get("shop_name").toString());
			address.setText(s.get("address").toString());
			dealer_name.setText(s.get("NAME").toString());

		}

	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		dbworker = new Dbworker(getActivity());

		View rootView = inflater.inflate(R.layout.registerdealerfragment,
			container, false);
		userdata = getActivity().getSharedPreferences("USERDATA",
			getActivity().MODE_PRIVATE);
		populate_Dealers();
		populate_newDealer();
		String[] from = {"NAME", "ID"};
		int[] to = {R.id.auto_id, R.id.auto_name};
		adapter = new SimpleAdapter(getActivity(), values, R.layout.row, from,
			to);
		dealer_search = (CustomAutoCompleteTextView2) rootView
			.findViewById(R.id.d_name);
		dealer_search.setOnItemClickListener(autoItemSelectedListner);
		dealer_search.setAdapter(adapter);
		dealer_search.setThreshold(1);
		officer_name = (EditText) rootView.findViewById(R.id.contact_no);
		branch_name = (EditText) rootView.findViewById(R.id.branch_name);
		dealer_name = (EditText) rootView.findViewById(R.id.account_number);
		tl = (TableLayout) rootView.findViewById(R.id.newly_added_dealers);

		address = (EditText) rootView.findViewById(R.id.c_address);
		shop_name = (EditText) rootView.findViewById(R.id.officer);
		save_dealers = (Button) rootView.findViewById(R.id.save_dealer);

		dataAdapter = new ArrayAdapter<HashMap<String, String>>(getActivity(), android.R.layout.simple_spinner_item, values1);
		newdealer = (Spinner) rootView.findViewById(R.id.new_dealer);
		newdealer.setOnItemSelectedListener(itemSelectedListener);
		dataAdapter
			.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		newdealer.setAdapter(dataAdapter);

		save_dealers.setOnClickListener(o);
		branch_name.setText(userdata.getString("branch_name", ""));
		officer_name.setText(userdata.getString("name", ""));
		populate();
		return rootView;

	}

	public void populate_Dealers() {

		Cursor cursor1 = dbworker.get_all_dealers();

		if (cursor1 != null) {
			cursor1.moveToFirst();
			while (!cursor1.isAfterLast()) {
				HashMap<String, String> temp = new HashMap<String, String>();
				temp.put("NAME", cursor1.getString(3));
				temp.put("ID", String.valueOf(cursor1.getString(2)));
				temp.put("shop_name", String.valueOf(cursor1.getString(9)));
				temp.put("address", String.valueOf(cursor1.getString(4)));
				values.add(temp);
				cursor1.moveToNext();
			}

		}
		cursor1.close();

	}

	public void populate_newDealer() {

		Cursor cursor1 = dbworker.get_all_new_dealers();

		if (cursor1 != null) {
			cursor1.moveToFirst();
			while (!cursor1.isAfterLast()) {
				HashMap<String, String> temp = new HashMap<String, String>() {
					@Override
					public String toString() {
						return get("shop_name");
					}
				};
				temp.put("NAME", cursor1.getString(3));
				temp.put("ID", String.valueOf(cursor1.getString(2)));
				temp.put("shop_name", String.valueOf(cursor1.getString(9)));
				temp.put("address", String.valueOf(cursor1.getString(4)));
				values1.add(temp);
				cursor1.moveToNext();
			}

		}
		cursor1.close();

	}

	public void populate() {

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

		TextView label_delar_name = new TextView(getActivity());
		label_delar_name.setId(20);
		label_delar_name.setText("DealerName");
		label_delar_name.setTextColor(Color.RED);
		label_delar_name.setGravity(Gravity.CENTER);
		label_delar_name.setPadding(2, 2, 2, 2);
		tr_head.addView(label_delar_name);// add the column to the table row
		// here

		TextView label_address = new TextView(getActivity());
		label_address.setId(21);// define id that must be unique
		label_address.setText("Delar Address"); // set the text for the
		label_address.setTextColor(Color.RED); // set the color
		label_address.setGravity(Gravity.CENTER);
		label_address.setPadding(2, 2, 2, 2);

		// set the padding (if
		tr_head.addView(label_address); // add the column to the table
		// row

		TextView label_shop_name = new TextView(getActivity());
		label_shop_name.setId(21);// define id that must be unique
		label_shop_name.setText("Shop Name"); // set the text for the
		label_shop_name.setTextColor(Color.RED); // set the color
		label_shop_name.setPadding(2, 2, 2, 2);
		label_shop_name.setGravity(Gravity.CENTER);

		; // set the padding (if
		tr_head.addView(label_shop_name);

		tl.addView(tr_head, new TableLayout.LayoutParams(
			LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));

		Integer count = 0;
		Cursor cursor = dbworker.get_all_newly_added_dealers();

		if (cursor.getCount() != 0) {
			if (cursor != null) {
				cursor.moveToFirst();
				while (!cursor.isAfterLast()) {

					TableRow tr = new TableRow(getActivity());

					if (count % 2 != 0)
						tr.setBackgroundColor(Color.rgb(28, 77, 103));
					tr.setId(100 + count);
					tr.setLayoutParams(new LayoutParams(
						LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));

					TextView id = new TextView(getActivity());
					id.setTag("ID");
					id.setId(200 + count);
					id.setText(cursor.getString(0));
					id.setTextColor(Color.BLACK);
					id.setGravity(Gravity.CENTER);
					tr.addView(id);

					TextView dealer_search = new TextView(getActivity());
					dealer_search.setTag("dealer_search");
					dealer_search.setId(200 + count);
					dealer_search.setText(cursor.getString(1));
					dealer_search.setTextColor(Color.BLACK);
					dealer_search.setGravity(Gravity.CENTER);
					tr.addView(dealer_search);

					TextView n_adress = new TextView(getActivity());
					n_adress.setId(200 + count);
					n_adress.setText(cursor.getString(2));
					n_adress.setTag("adress");
					n_adress.setTextColor(Color.BLACK);
					n_adress.setGravity(Gravity.CENTER);
					tr.addView(n_adress);

					TextView shopname = new TextView(getActivity());
					shopname.setId(200 + count);
					shopname.setText(cursor.getString(3));
					shopname.setTag("shopname");
					shopname.setTextColor(Color.BLACK);
					shopname.setGravity(Gravity.CENTER);
					tr.addView(shopname);

					Button clear = new Button(getActivity());
					clear.setId(200 + count);
					clear.setText("Clear");
					clear.setTag("clear");
					clear.setOnClickListener(clear_row);
					clear.setTextColor(Color.BLACK);
					clear.setGravity(Gravity.CENTER);
					tr.addView(clear);

					// finally add this to the table row
					tl.addView(tr,
						new TableLayout.LayoutParams(
							LayoutParams.FILL_PARENT,
							LayoutParams.WRAP_CONTENT));
					count++;
					cursor.moveToNext();

				}
				cursor.close();

			}
		}

	}

}
