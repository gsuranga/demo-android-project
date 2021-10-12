package com.example.dimosales;

import java.util.ArrayList;
import java.util.List;

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
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import ceylon.linux.db.Dbworker;
import ceylon.linux.view.HomeActivity;
import ceylon.linux.view.UpdateDealerLocation;

public class MapActivity extends FragmentActivity implements
OnMapLongClickListener, OnMapClickListener, OnMarkerDragListener,OnMarkerClickListener {
	
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
		
		private LatLng newPosition;
		
		
		private Button btnUpdate;
		private TextView dealerTv;

		private final List<Marker> mMarkerRainbow = new ArrayList<Marker>();

		private Dbworker dbworker;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		dbworker = new Dbworker(MapActivity.this);
		
		setUpMapIfNeeded();
		

		CameraPosition INIT = new CameraPosition.Builder()
				.target(new LatLng(6.9, 81.033)).zoom(8).bearing(0) // orientation
				.tilt(0) // viewing angle
				.build();
		
		// use map to move camera into position
				mMap.moveCamera(CameraUpdateFactory.newCameraPosition(INIT));
				
				mMap.setOnMapLongClickListener(this);
				mMap.setOnMapClickListener(this);
				mMap.setOnMarkerDragListener(this);
				mMap.setOnMarkerClickListener(this);
				
				btnUpdate=(Button) findViewById(R.id.btn_update);
				dealerTv=(TextView) findViewById(R.id.dtv);
				
				dealerTv.setText(UpdateDealerLocation.dealer_name);
				
				btnUpdate.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Toast.makeText(getApplicationContext(), "Clicked..!",
								Toast.LENGTH_LONG).show();
						Double lat=newPosition.latitude;
						Double longt=newPosition.longitude;
						
						dbworker.update_dealer_loc(UpdateDealerLocation.dealer_id, lat+"", longt+"");
					}
				});				
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
		//addMarkersToMap();
	}

	private void addMarkersToMap() {
		// Uses a colored icon.
		mBrisbane = mMap.addMarker(new MarkerOptions()
				.position(BRISBANE)
				.title("Brisbane")
				.snippet("Population: 2,074,200")
				.icon(BitmapDescriptorFactory
						.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));

		// Uses a custom icon with the info window popping out of the center of
		// the icon.
		mSydney = mMap.addMarker(new MarkerOptions().position(SYDNEY)
				.title("Sydney").snippet("Population: 4,627,300")

				.infoWindowAnchor(0.5f, 0.5f));

		// Creates a draggable marker. Long press to drag.
		mMelbourne = mMap.addMarker(new MarkerOptions()
				.position(MELBOURNE)
				.title("Melbourne")
				.snippet("Population: 4,137,400")
				.icon(BitmapDescriptorFactory
						.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
				.draggable(true));
		mMelbourne.setSnippet(mMelbourne.getPosition().toString());

		// A few more markers for good measure.
		mPerth = mMap.addMarker(new MarkerOptions().position(PERTH)
				.title("Perth").snippet("Population: 1,738,800"));

		mAdelaide = mMap.addMarker(new MarkerOptions()
				.position(ADELAIDE)
				.title("Adelaide")
				.icon(BitmapDescriptorFactory
						.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
				.snippet("Population: 1,213,000"));
		mGalle = mMap.addMarker(new MarkerOptions()
				.position(GALLE)
				.title("galle")
				.icon(BitmapDescriptorFactory
						.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));

		mGalle.showInfoWindow();
		/*
		 * mGalle = mMap.addMarker(new MarkerOptions().position(new LatLng(7,
		 * 80)) .title("galle") .snippet("Population: 1,213,000"));
		 */
		/*
		 * MarkerOptions markerOptions2 = new MarkerOptions();
		 * markerOptions2.position(new LatLng(80.5, 6.7));
		 * markerOptions2.title("Location"); mMap.addMarker(markerOptions2);
		 */
	}

	@Override
	public void onMarkerDrag(Marker arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMarkerDragEnd(Marker arg0) {
		// TODO Auto-generated method stub
		 newPosition = arg0.getPosition();
		
	}

	@Override
	public void onMarkerDragStart(Marker arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMapClick(LatLng arg0) {
		// TODO Auto-generated method stub
		Toast.makeText(MapActivity.this, "clicked", Toast.LENGTH_LONG)
		.show();
	}

	@Override
	public void onMapLongClick(LatLng arg0) {
		
		// TODO Auto-generated method stub
		MarkerOptions newMarkerOption = new MarkerOptions().position(arg0).title("New Location").snippet("")
				.draggable(true);
		Marker newMarker = mMap.addMarker(newMarkerOption);
		
		newPosition = arg0;
		
		newMarker.showInfoWindow();
		
		
		Toast.makeText(MapActivity.this, "longclicked", Toast.LENGTH_LONG)
		.show();
		
	}

	@Override
	public boolean onMarkerClick(Marker arg0) {
		// TODO Auto-generated method stub
		arg0.remove();
		return false;
	}

	

	/*@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Toast.makeText(getApplicationContext(), "Clicked..!",
				Toast.LENGTH_LONG).show();
		
	}*/
	
	@Override
	public void onBackPressed() {
		Intent intent = new Intent(MapActivity.this, HomeActivity.class);
		startActivity(intent);
		finish();
		
	}

}
