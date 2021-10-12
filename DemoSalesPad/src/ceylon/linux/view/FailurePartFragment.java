package ceylon.linux.view;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import com.example.dimosales.R;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import ceylon.linux.db.Dbworker;
import ceylon.linux.utility.Utility;

public class FailurePartFragment extends Fragment {
	private static final int CAPTURE_IMAGE_FULLSIZE_ACTIVITY_REQUEST_CODE = 1777;
	public String image_name;
	OnClickListener capture = new OnClickListener() {

		@Override
		public void onClick(View v) {

			Intent cameraIntent = new Intent(
				android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

			File file = new File(Environment.getExternalStorageDirectory() + File.separator + "FailureImages/" + new Date().getTime() + ".jpg");
			cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
			image_name = file.getName();

			startActivityForResult(cameraIntent,
				CAPTURE_IMAGE_FULLSIZE_ACTIVITY_REQUEST_CODE);

		}
	};
	public String image_path;
	public String catogery_id;
	OnClickListener save = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Toast.makeText(getActivity(), image_name, Toast.LENGTH_LONG).show();
			db.save_failure_parts(catogery_id, outlet_id.toString(), part_no.getText().toString(), failure.getText().toString(), image_name);

		}
	};
	OnItemSelectedListener select = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
		                           long arg3) {
			// TODO Auto-generated method stub
			HashMap<String, String> s = (HashMap<String, String>) arg0.getItemAtPosition(arg2);

			if (s.get("id").equals("1")) {
				catogery_id = s.get("id");
				populate_dealer();
				// outlet_id.setVisibility(View.VISIBLE);

			} else if (s.get("id").equals("2")) {
				catogery_id = s.get("id");
				populate_garages();
				// outlet_id.setVisibility(View.GONE);
			} else if (s.get("id").equals("5")) {
				catogery_id = s.get("id");
				populate_vehicales("5");
				// outlet_id.setVisibility(View.GONE);

			} else if (s.get("id").equals("3")) {
				catogery_id = s.get("id");
				populate_vehicales("3");
			}

		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub*+9

		}
	};
	public String outlet_id;
	private OnItemClickListener autoItemSelectedListner2 = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
		                        long arg3) {
			HashMap<String, String> hm = (HashMap<String, String>) arg0.getAdapter().getItem(arg2);
			outlet_id = hm.get("ID");

		}

	};
	View rootView;
	EditText sales_officer_name, part_no, failure;
	Spinner catogery;
	Button image_capture, save_button, reset;
	SharedPreferences userdata;
	String[] from = {"NAME", "ID"};
	int[] to = {R.id.auto_id, R.id.auto_name};
	ArrayList<HashMap<String, String>> values = new ArrayList<HashMap<String, String>>();
	ArrayAdapter<HashMap<String, String>> catogery_dataAdapter;
	ArrayList<HashMap<String, String>> categories_details = new ArrayList<HashMap<String, String>>();
	ArrayList<HashMap<String, String>> itemCollection = new ArrayList<HashMap<String, String>>();
	CustomAutoCompleteTextView outlet, description;
	Utility u;
	Dbworker db;
	OnClickListener reset_data = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			outlet.setText("");
			part_no.setText("");
			description.setText("");
			failure.setText("");
		}
	};
	private OnItemClickListener autoItemSelectedListner1 = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
		                        long arg3) {
			HashMap<String, String> hm = (HashMap<String, String>) arg0
				.getAdapter().getItem(arg2);
			part_no.setText(hm.get("ID"));

		}

	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		db = new Dbworker(getActivity());
		userdata = getActivity().getSharedPreferences("USERDATA",
			Context.MODE_PRIVATE);

	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {

		rootView = inflater
			.inflate(R.layout.failure_fragment, container, false);
		sales_officer_name = (EditText) rootView
			.findViewById(R.id.sales_officer);
		catogery = (Spinner) rootView.findViewById(R.id.catogery);
		description = (CustomAutoCompleteTextView) rootView
			.findViewById(R.id.part_no);
		part_no = (EditText) rootView.findViewById(R.id.part_description);
		failure = (EditText) rootView.findViewById(R.id.failure);
		reset = (Button) rootView.findViewById(R.id.reset);
		outlet = (CustomAutoCompleteTextView) rootView.findViewById(R.id.outlet);
		save_button = (Button) rootView.findViewById(R.id.save_data);
		u = new Utility(getActivity());
		image_capture = (Button) rootView.findViewById(R.id.captute_image);
		Utility.createDirectoryIfNeeded("FailureImages");

		populate_get_visit_categories_details();
		populate_item_list();

		catogery_dataAdapter = new ArrayAdapter<HashMap<String, String>>(getActivity(), android.R.layout.simple_spinner_item, categories_details);
		sales_officer_name.setText(userdata.getString("name", ""));
		catogery.setAdapter(catogery_dataAdapter);
		catogery.setOnItemSelectedListener(select);

		SimpleAdapter part_no_adapter = new SimpleAdapter(getActivity(), itemCollection, R.layout.row, from, to);
		description.setAdapter(part_no_adapter);
		outlet.setOnItemClickListener(autoItemSelectedListner2);
		description.setOnItemClickListener(autoItemSelectedListner1);
		image_capture.setOnClickListener(capture);
		save_button.setOnClickListener(save);
		reset.setOnClickListener(reset_data);


		return rootView;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == CAPTURE_IMAGE_FULLSIZE_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
			//	Bitmap photo = (Bitmap) data.getExtras().get("data");

			// CALL THIS METHOD TO GET THE URI FROM THE BITMAP
			//	Uri tempUri = u.getImageUri(getActivity().getApplicationContext(),photo);

			// CALL THIS METHOD TO GET THE ACTUAL PATH
			//	File finalFile = new File(u.getRealPathFromURI(tempUri));
			// Log.i("FILE NAME IS", finalFile.getName()+"");
			//image_name = finalFile.getName();
			//image_path = u.getRealPathFromURI(tempUri);
			//Utility.copy(image_path, "Brand", image_name);
			// image_button.setTag(image_path);
			// System.out.println(getRealPathFromURI(tempUri));

		}
	}

	public void populate_get_visit_categories_details() {
		Cursor cursor1 = db.get_visit_categories_details();
		if (cursor1 != null) {
			cursor1.moveToFirst();
			while (!cursor1.isAfterLast()) {
				HashMap<String, String> temp = new HashMap<String, String>() {
					@Override
					public String toString() {
						return get("catogery_name");
					}
				};
				temp.put("id", cursor1.getString(1));
				temp.put("catogery_name", String.valueOf(cursor1.getString(0)));

				categories_details.add(temp);
				cursor1.moveToNext();
			}

		}
		cursor1.close();
	}

	public void populate_item_list() {
		Cursor cursor = db.get_all_items();
		if (cursor != null) {
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				HashMap<String, String> temp = new HashMap<String, String>();

				temp.put("ID", cursor.getString(2).trim());
				temp.put("NAME", cursor.getString(1).trim());

				/*
				 * Log.i("item_id", cursor.getString(0).trim());
				 * Log.i("description", cursor.getString(1).trim());
				 * Log.i("item_part_no", cursor.getString(2).trim());
				 *
				 * Log.i("selling_price", cursor.getString(3).trim());
				 * Log.i("total_stock_qty", cursor.getString(4).trim());
				 * Log.i("status", cursor.getString(5).trim());
				 * Log.i("time_stamp", cursor.getString(6).trim());
				 */

				itemCollection.add(temp);
				cursor.moveToNext();
			}

		}
		db.close();
	}

	public void populate_dealer() {

		Cursor cursor1 = db.get_all_dealers();
		if (!values.isEmpty()) {
			values.clear();

		}

		if (cursor1 != null) {
			cursor1.moveToFirst();
			while (!cursor1.isAfterLast()) {
				HashMap<String, String> temp = new HashMap<String, String>();
				temp.put("NAME", cursor1.getString(3));
				temp.put("ID", String.valueOf(cursor1.getString(2)));
				values.add(temp);
				cursor1.moveToNext();
			}

		}

		SimpleAdapter adapter = new SimpleAdapter(getActivity(), values,R.layout.row, from, to);
		adapter.notifyDataSetChanged();
		outlet.setAdapter(adapter);
		cursor1.close();

	}

	// populate garages
	public void populate_garages() {

		Cursor cursor = db.get_all_newly_added_garages();
		if (!values.isEmpty()) {
			values.clear();

		}

		if (cursor != null) {
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				HashMap<String, String> temp = new HashMap<String, String>();

				temp.put("NAME", cursor.getString(1));
				temp.put("ID", String.valueOf(cursor.getString(6)));
				values.add(temp);
				cursor.moveToNext();
			}

		}

		SimpleAdapter adapter = new SimpleAdapter(getActivity(), values,R.layout.row, from, to);
		adapter.notifyDataSetChanged();
		outlet.setAdapter(adapter);
		cursor.close();

	}

	public void populate_vehicales(String type) {
		Cursor cursor;
		if (type.equals("3")) {
			cursor = db.get_all_fleet_vehicales();

		} else {
			cursor = db.get_all_vehicales();

		}

		if (!values.isEmpty()) {
			values.clear();

		}

		if (cursor != null) {
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				HashMap<String, String> temp = new HashMap<String, String>();
				temp.put("NAME", cursor.getString(1));
				temp.put("ID", cursor.getString(0));

				values.add(temp);
				cursor.moveToNext();
			}

		}

		SimpleAdapter adapter = new SimpleAdapter(getActivity(), values,
			R.layout.row, from, to);
		adapter.notifyDataSetChanged();
		outlet.setAdapter(adapter);
		cursor.close();

	}

}
