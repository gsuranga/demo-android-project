package ceylon.linux.view;

import java.util.ArrayList;
import java.util.HashMap;

import com.example.dimosales.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.TextView;
import ceylon.linux.db.Dbworker;
import ceylon.linux.utility.NumbeFormater;

/**
 * This shows how to create a simple activity with a map and a marker on the
 * map.
 * <p>
 * Notice how we deal with the possibility that the Google Play services APK is
 * not installed/enabled/updated on a user's device.
 */
public class MapViewActivty extends FragmentActivity implements
		OnMarkerClickListener {

	NumbeFormater numbeFormater = new NumbeFormater();
	Dbworker db;
	static final ArrayList<HashMap<String, String>> LocationList = new ArrayList<HashMap<String, String>>();

	private GoogleMap mMap;
	private TextView text_dealerName, text_dealer_address, text_overdue_amount,
			text_outstanding_amount, text_credit_limit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map_view);
		text_dealerName = (TextView) findViewById(R.id.text_dealerName);
		text_dealer_address = (TextView) findViewById(R.id.text_dealer_address);
		text_overdue_amount = (TextView) findViewById(R.id.text_overdue_amount);
		text_outstanding_amount = (TextView) findViewById(R.id.text_outstanding_amount);
		text_credit_limit = (TextView) findViewById(R.id.text_credit_limit);

		load_data();
		mMap.setOnMarkerClickListener(this);

	}

	private void setUpMapIfNeeded() {
	
			// Try to obtain the map from the SupportMapFragment.
			mMap = ((SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map)).getMap();
			// Check if we were successful in obtaining the map.
		
	//			setUpMap();
			
		
	}

	
	

	private void getMapLocationsFromDB() {

		db = new Dbworker(MapViewActivty.this);
		if (LocationList.isEmpty()) {
			Cursor cursor = db.get_all_dealers();
			String dump = DatabaseUtils.dumpCursorToString(cursor);
			if (cursor != null) {
				cursor.moveToFirst();
				while (!cursor.isAfterLast()) {
					HashMap<String, String> temp = new HashMap<String, String>();
					temp.put("dealer_id", cursor.getString(1));
					temp.put("dealer_name", cursor.getString(3));
					temp.put("Lat", cursor.getString(15));
					temp.put("Long", cursor.getString(16));
					temp.put("overdue_amount", cursor.getString(14));
					temp.put("outstanding_amount", cursor.getString(13));
					temp.put("credit_limit", cursor.getString(12));
					temp.put("discount", cursor.getString(6));

					LocationList.add(temp);

					cursor.moveToNext();
				}
				cursor.close();
			}

		}

	}

	private void addMarkersToMap() {
		mMap.clear();
		for (HashMap<String, String> hashMap : LocationList) {

			float marker_color=0.0f;
			
			if(hashMap.get("discount").trim().equals("15"))
			{
				marker_color = 0.0f;
			}
			else if(hashMap.get("discount").trim().equals("17"))
			{
				marker_color = 60.0f;
			}
			else if(hashMap.get("discount").trim().equals("20"))
			{
				marker_color = 240.0f;
			}
			else if(hashMap.get("discount").trim().equals("24"))
			{
				marker_color = 120.0f;
			}
			else 
			{
				marker_color = 0.0f;
			}
			
			MarkerOptions a = new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble(hashMap.get("Lat")),
									Double.parseDouble(hashMap.get("Long"))))
					.title(hashMap.get("dealer_name"))
					.icon(BitmapDescriptorFactory.defaultMarker(marker_color));

			mMap.addMarker(a);

		}

	}

	@Override
	public boolean onMarkerClick(Marker arg0) {
		arg0.getTitle();

		Cursor cursor = db.get_dealer_by_deler_name(arg0.getTitle());
		if (cursor.moveToFirst()) {
			text_dealerName.setText(cursor.getString(3));
			text_dealer_address.setText(cursor.getString(4));
			text_overdue_amount.setText(numbeFormater
					.format_double_val_to_decimal_Strng(Double
							.parseDouble(cursor.getString(14))));
			text_outstanding_amount.setText(numbeFormater
					.format_double_val_to_decimal_Strng(Double
							.parseDouble(cursor.getString(13))));
			text_credit_limit.setText(numbeFormater
					.format_double_val_to_decimal_Strng(Double
							.parseDouble(cursor.getString(12))));
			
			
			
			
		}
		cursor.close();

		return false;
	}

	@Override
	public void onBackPressed() {
		Intent intent = new Intent(MapViewActivty.this, HomeActivity.class);
		startActivity(intent);
		finish();
	}

	@Override
	protected void onDestroy() {
		db.close();
		super.onDestroy();
	}
	
	
	private void load_data()
	{
		String[] a ={""};
		new AsyncTask<String, String, String>() {
			
			
			@Override
			protected void onPreExecute() {
				
				super.onPreExecute();
				setUpMapIfNeeded();
				
			}

			@Override
			protected String doInBackground(String... params) {
				getMapLocationsFromDB();
				return null;
			}
			
			 @Override
			protected void onPostExecute(String result) {
				
				super.onPostExecute(result);
				addMarkersToMap();
				
				
			}
			
			
		}.execute(a);
	}
}
