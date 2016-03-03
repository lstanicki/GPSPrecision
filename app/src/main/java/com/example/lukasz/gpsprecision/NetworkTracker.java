package com.example.lukasz.gpsprecision;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.view.ContextThemeWrapper;
import android.widget.Toast;

public class NetworkTracker extends Service implements LocationListener {
	private final Context context;

	boolean isGPSEnabled = false;
	boolean isNetworkEnabled = false;
	boolean isWifiEnabled = false;
	boolean isMobileEnabled = false;
	boolean canGetLocation = false;

	private Location location;

	double latitude;
	double longitude;

	private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; //10 metrów
	private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; //1 minuta

	protected LocationManager locationManager;
	protected ConnectivityManager connectivityManager;

	public NetworkTracker(Context context) {
		this.context = context;
		getNetworkLocation();
	}

	public Location getNetworkLocation() {
		try {
			locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);

			isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

			isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

			if(!isNetworkEnabled) {

			} else {
				this.canGetLocation = true;

				if (isNetworkEnabled) {

					locationManager.requestLocationUpdates(
							LocationManager.NETWORK_PROVIDER,
							MIN_TIME_BW_UPDATES,
							MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

					if (locationManager != null) {
						location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

						if (location != null) {

							latitude = location.getLatitude();
							longitude = location.getLongitude();
						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return location;
	}


	public void stopUsingGPS() {
		if (locationManager != null) {

			locationManager.removeUpdates(NetworkTracker.this);
		}
	}

	public double getLatitude() {
		if (location != null) {
			latitude = location.getLatitude();
		}

		return latitude;
	}

	public double getLongitude() {
		if (location != null) {
			longitude = location.getLongitude();
		}

		return longitude;
	}

	public boolean canGetLocation() {
		return this.canGetLocation;
	}


	@Override
	public void onLocationChanged(Location location) {
		int lat = (int) (location.getLatitude());
		int lng = (int) (location.getLongitude());
	}

	@Override
	public void onProviderDisabled(String provider) {
		Toast.makeText(this, "Disabled provider " + provider,
				Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onProviderEnabled(String provider) {
		Toast.makeText(this, "Enabled new provider " + provider,
				Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		Toast.makeText(this, "Status changed " + status,
				Toast.LENGTH_SHORT).show();
	}


	@Override
	public IBinder onBind(Intent intent) {

		return null;
	}

}

