package ceylon.linux.view;



import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import com.example.dimosales.R;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import ceylon.linux.db.Dbworker;

public class MapActivity extends FragmentActivity implements
OnMapLongClickListener, OnMapClickListener, OnMarkerDragListener,OnMarkerClickListener {
	
	
		private GoogleMap mMap;
		private LatLng newPosition;
		private Button btnUpdate;
		private TextView dealerTv;
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
						Toast.makeText(getApplicationContext(), "Updated",
								Toast.LENGTH_LONG).show();
						Double lat=newPosition.latitude;
						Double longt=newPosition.longitude;
						
						dbworker.update_dealer_loc(UpdateDealerLocation.dealer_id, lat+"", longt+"");
						
						Log.i("location", ""+UpdateDealerLocation.dealer_id+" "+lat+" "+longt);
						
						Intent intent = new Intent(MapActivity.this, MapViewActivty.class);
						startActivity(intent);
						finish();
						
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
		Toast.makeText(MapActivity.this, "Updated", Toast.LENGTH_LONG)
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
		
		
		Toast.makeText(MapActivity.this, "Selected", Toast.LENGTH_SHORT)
		.show();
		
	}

	@Override
	public boolean onMarkerClick(Marker arg0) {
		// TODO Auto-generated method stub
		arg0.remove();
		return false;
	}

	

	@Override
	public void onBackPressed() {
		Intent intent = new Intent(MapActivity.this, HomeActivity.class);
		startActivity(intent);
		finish();
	}

	@Override
	protected void onDestroy() {
	// TODO Auto-generated method stub
	super.onDestroy();
	dbworker.close();
	}
}
