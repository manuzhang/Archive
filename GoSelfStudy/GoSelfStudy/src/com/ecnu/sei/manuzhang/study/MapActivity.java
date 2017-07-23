package com.ecnu.sei.manuzhang.study;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

public class MapActivity extends Activity implements LocationListener {

	private MapFragment mMapFragment;
	private GoogleMap mMap;
	private boolean showHomeAsUp = true;

	private static LocationManager mLocationManager;
	private static LocationProvider mLocationProvider;
	private static String mProviderName = LocationManager.GPS_PROVIDER;	
	private double mLatitude = 31.23;
	private double mLongitude = 121.40;
	private float mCameraZoom = 13;
	private float mCameraTilt = 0;
    private float mCameraBearing = 0;
    
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map);

		mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		if (!mLocationManager.isProviderEnabled(mProviderName)) {
			//TODO display a dialog and suggest to go to the settings
		}
		mLocationProvider = mLocationManager.getProvider(mProviderName);
		GoogleMapOptions options = new GoogleMapOptions();
		options.mapType(GoogleMap.MAP_TYPE_NORMAL)
		       .camera(new CameraPosition(new LatLng(mLatitude, mLongitude), mCameraZoom, mCameraTilt, mCameraBearing))
		       .compassEnabled(true);
		mMapFragment = MapFragment.newInstance(options);
		getFragmentManager().beginTransaction().add(R.id.map, mMapFragment).commit();

		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(showHomeAsUp);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			Intent intent = new Intent(this, Main.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public LocationManager getLocationManager() {
		return mLocationManager;
	}

	public LocationProvider getLocationProvider() {
		return mLocationProvider;
	}

	public void requestUpdate() { 
		mLocationManager.requestLocationUpdates(mProviderName, 10000, 10, this);
	}

	public void removeUpdate() {
		mLocationManager.removeUpdates(this);
	}

	@Override
	public void onLocationChanged(Location loc) 
	{
		mLatitude = loc.getLatitude();
		mLongitude = loc.getLongitude();
	}


	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) 
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) 
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderDisabled(String provider)
	{
		// TODO Auto-generated method stub

	}

}
