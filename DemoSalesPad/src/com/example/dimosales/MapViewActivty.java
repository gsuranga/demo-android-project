/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.dimosales;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.example.dimosales.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;
import ceylon.linux.db.Dbworker;

/**
 * This shows how to create a simple activity with a map and a marker on the
 * map.
 * <p>
 * Notice how we deal with the possibility that the Google Play services APK is
 * not installed/enabled/updated on a user's device.
 */
public class MapViewActivty extends FragmentActivity implements OnMarkerClickListener{

	Dbworker db;
	static final ArrayList<HashMap<String, String>> LocationList = new ArrayList<HashMap<String, String>>();

	// private static final LatLng BRISBANE = new LatLng(-27.47093, 153.0235);
	private static final LatLng BRISBANE = new LatLng(-27.47093, 153.0235);
	private static final LatLng MELBOURNE = new LatLng(-37.81319, 144.96298);
	private static final LatLng SYDNEY = new LatLng(-33.87365, 151.20689);
	private static final LatLng ADELAIDE = new LatLng(-34.92873, 138.59995);
	private static final LatLng PERTH = new LatLng(-31.952854, 115.857342);
	private static final LatLng GALLE = new LatLng(6, 80);
	private GoogleMap mMap;

	private Marker mPerth;
	private Marker mSydney;
	private Marker mBrisbane;
	private Marker mAdelaide;
	private Marker mMelbourne;
	private Marker mGalle;

	private final List<Marker> mMarkerRainbow = new ArrayList<Marker>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map_view);
		setUpMapIfNeeded();
		mMap.setOnMarkerClickListener(this);
		
	}

	

	private void setUpMapIfNeeded() {
		if (mMap == null) {
			// Try to obtain the map from the SupportMapFragment.
			mMap = ((SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map)).getMap();
			// Check if we were successful in obtaining the map.
			if (mMap != null) {
				setUpMap();
			}
		}
	}

	private void setUpMap() {

		// Add lots of markers to the map.
		getMapLocationsFromDB();
		addMarkersToMap();
	}

	private void getMapLocationsFromDB() {

		db=new Dbworker(MapViewActivty.this);
		if (LocationList.isEmpty()) {
			Cursor cursor = db.get_all_dealers();
			if (cursor != null) {
				cursor.moveToFirst();
				while (!cursor.isAfterLast()) {
					HashMap<String, String> temp = new HashMap<String, String>();
					temp.put("dealer_id", cursor.getString(1));
					temp.put("dealer_name", cursor.getString(3));
					temp.put("Lat", cursor.getString(15));
					temp.put("Long", cursor.getString(16));

					LocationList.add(temp);

					cursor.moveToNext();
				}
				cursor.close();
			}
			db.close();
		}

	}

	private void addMarkersToMap() {
	
		
	    for (HashMap<String, String> hashMap: LocationList) {
	    	
	    	MarkerOptions a=
	    	  new MarkerOptions().position(new LatLng(Double.parseDouble(hashMap.get("Lat")), Double.parseDouble(hashMap.get("Long")))).title(hashMap.get("dealer_name")).icon(BitmapDescriptorFactory
						.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
	    	
	    	mMap.addMarker(a);
	    	
	    	 
		}
		
	}



	@Override
	public boolean onMarkerClick(Marker arg0) {
		arg0.getTitle();
		
		Toast.makeText(MapViewActivty.this, arg0.getTitle(),Toast.LENGTH_SHORT).show();
		return false;
	}
	
	
}
