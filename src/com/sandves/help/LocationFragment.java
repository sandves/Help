package com.sandves.help;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;

public class LocationFragment extends Fragment {

	private TextView mLatLng;
	private TextView mAddress;
	private TextView mAccuracy;
	private TextView mTimestamp;
	private ImageView mShowMap;
	private Button mCall;
	private ImageView mSMS;
	private LocationManager mLocationManager;
	private Handler mHandler;
	private boolean mGeocoderAvailable;
	private double latitude = 0;
	private double longtitude = 0;
	private int geoparsePref;
	private LinearLayout copyLocationToClipboard;
	private LinearLayout showHospitalsNearby;

	// UI handler codes.
	private static final int UPDATE_ADDRESS = 1;
	private static final int UPDATE_LATLNG = 2;
	private static final int UPDATE_ACCURACY = 3;
	private static final int UPDATE_TIMESTAMP = 4;

	private static final int FIVE_SECONDS = 5000;
	private static final int FIVE_METERS = 5;
	private static final int TWO_MINUTES = 1000 * 60 * 2;

	public LocationFragment() {

	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment

		return inflater.inflate(R.layout.location, container, false);
	}

	@SuppressLint("HandlerLeak")
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Receive location updates from the fine location provider (gps) only.

		//mFineProviderButton = (Button) findViewById(R.id.provider_fine);

		// Receive location updates from both the fine (gps) and coarse (network) location
		// providers.

		// The isPresent() helper method is only available on Gingerbread or above.
		mGeocoderAvailable = Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD && Geocoder.isPresent();

		// Handler for updating text fields on the UI like the lat/long and address.
		mHandler = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case UPDATE_ADDRESS:
					mAddress.setText((String) msg.obj);
					break;
				case UPDATE_LATLNG:
					mLatLng.setText((String) msg.obj);
					break;
				case UPDATE_ACCURACY:
					mAccuracy.setText((String) msg.obj);
					break;
				case UPDATE_TIMESTAMP:
					mTimestamp.setText((String) msg.obj);
					break;
				}
			}
		};
		// Get a reference to the LocationManager object.
		mLocationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

	}

	public void showMap(View shopMapButton) {
		if(latitude != 0 && longtitude != 0) {
			Intent myIntent = new Intent(android.content.Intent.ACTION_VIEW, 
					Uri.parse("geo:"+latitude+","+longtitude+"?z=14"));
			startActivity(myIntent);
		}
	}
	
	private void showHostpitalsNearbyInMaps() {
		Intent myIntent = new Intent(android.content.Intent.ACTION_VIEW, 
				Uri.parse("geo:"+latitude+","+longtitude+"?q=hospital"));
		startActivity(myIntent);
	}

	public void call(View callButton) {
		String latlng = mLatLng.getText().toString();
		if(!latlng.equals(getString(R.string.unknown)))
			StatusBar.notify(getActivity().getApplicationContext(), getString(R.string.title_section1), mLatLng.getText().toString());

		int emgNumber = com.sandves.help.Settings.getPref(getActivity(), SettingsFragment.KEY_PREF_CALL, 
				getString(R.string.pref_emg_number_default));
		Intent i = new Intent(android.content.Intent.ACTION_DIAL, Uri.parse("tel:"+emgNumber));
		startActivity(i);
	}

	private void sendSMS() {
		String smsText = mLatLng.getText().toString();
		int smsNumber = com.sandves.help.Settings.getPref(getActivity(), SettingsFragment.KEY_PREF_CALL, 
				getString(R.string.pref_emg_number_default));

		if(!smsText.equalsIgnoreCase(getString(R.string.unknown))) {
			Intent intent = new Intent(getActivity(), SMSActivity.class);
			intent.putExtra("sms_number", Integer.toString(smsNumber));
			intent.putExtra("sms_body", smsText);  
			startActivity(intent);
		}
	}

	// Restores UI states after rotation.
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		//outState.putBoolean(KEY_FINE, mUseFine);
	}

	@Override
	public void onResume() {
		super.onResume();
		setup();
	}

	@Override
	public void onStart() {
		super.onStart();

	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mLatLng = (TextView) getView().findViewById(R.id.latlng);
		mAddress = (TextView) getView().findViewById(R.id.address);
		mAccuracy = (TextView) getView().findViewById(R.id.accuracy);
		mTimestamp = (TextView) getView().findViewById(R.id.timestamp);
		mShowMap = (ImageView) getView().findViewById(R.id.btn_show_map);
		mShowMap.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(final View v) {
				showMap(mShowMap);
			}
		});
		mCall = (Button) getView().findViewById(R.id.btn_call);
		mCall.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(final View v) {
				call(mCall);
			}
		});
		mSMS = (ImageView) getView().findViewById(R.id.btn_send_sms);
		mSMS.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				sendSMS();
			}});
		copyLocationToClipboard = (LinearLayout) getView().findViewById(R.id.copylayout);
		copyLocationToClipboard.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(final View v) {
				copyToClipboard(mLatLng.getText().toString());
			}
		});
		showHospitalsNearby = (LinearLayout) getView().findViewById(R.id.showHospitals);
		showHospitalsNearby.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(final View v) {
				showHostpitalsNearbyInMaps();
			}
		});
	}
	
	//mCall.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFAA0000));

	private void gpsCheck() {
		// Check if the GPS setting is currently enabled on the device.
		// This verification should be done during onStart() because the system calls this method
		// when the user returns to the activity, which ensures the desired location provider is
		// enabled each time the activity resumes from the stopped state.
		LocationManager locationManager =
				(LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
		final boolean gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

		if (!gpsEnabled) {
			// Build an alert dialog here that requests that the user enable
			// the location services, then when the user clicks the "OK" button,
			// call enableLocationSettings()
			new EnableGpsDialogFragment().show(getActivity().getSupportFragmentManager(), "enableGpsDialog");
		}
	}
	
	private void copyToClipboard(String text) {
		Clipboard clipboard = new Clipboard();
		Bundle textToCopy = new Bundle();
		textToCopy.putString("textToCopy", text);
		clipboard.setArguments(textToCopy);
		clipboard.show(getActivity().getSupportFragmentManager(), "copyToClipboard");
	}

	// Method to launch Settings
	private void enableLocationSettings() {
		Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		startActivity(settingsIntent);
	}

	// Stop receiving location updates whenever the Activity becomes invisible.
	@Override
	public void onStop() {
		super.onStop();
		mLocationManager.removeUpdates(listener);
	}

	// Set up fine and/or coarse location providers depending on whether the fine provider or
	// both providers button is pressed.
	private void setup() {
		Location gpsLocation = null;
		Location networkLocation = null;
		geoparsePref = com.sandves.help.Settings.getPref(getActivity(), 
				SettingsFragment.KEY_PREF_GEOPARSE, getString(R.string.pref_geoparse_default));
		mLocationManager.removeUpdates(listener);
		mLatLng.setText(getString(R.string.unknown));
		mAddress.setText(getString(R.string.requires_internet));
		mAccuracy.setText(getString(R.string.unknown));
		mTimestamp.setText(getString(R.string.unknown));
		mCall.setText(getString(R.string.call) + " " + com.sandves.help.Settings.getPref(getActivity(), 
				SettingsFragment.KEY_PREF_CALL, getString(R.string.pref_emg_number_default)));
		mShowMap.setEnabled(false);
		//mSMS.setEnabled(false);
		gpsCheck();
		// Get fine location updates only.
		//mFineProviderButton.setBackgroundResource(R.drawable.button_active);
		// Request updates from just the fine (gps) provider.
		gpsLocation = requestUpdatesFromProvider(
				LocationManager.GPS_PROVIDER, R.string.not_support_gps);
		networkLocation = requestUpdatesFromProvider(
                LocationManager.NETWORK_PROVIDER, R.string.not_support_network);
		// Update the UI immediately if a location is obtained
		if (gpsLocation != null && networkLocation != null) {
            updateUILocation(getBetterLocation(gpsLocation, networkLocation));
        } else if (gpsLocation != null) {
            updateUILocation(gpsLocation);
        } else if (networkLocation != null) {
            updateUILocation(networkLocation);
        }
	}

	/**
	 * Method to register location updates with a desired location provider.  If the requested
	 * provider is not available on the device, the app displays a Toast with a message referenced
	 * by a resource id.
	 *
	 * @param provider Name of the requested provider.
	 * @param errorResId Resource id for the string message to be displayed if the provider does
	 *                   not exist on the device.
	 * @return A previously returned {@link android.location.Location} from the requested provider,
	 *         if exists.
	 */
	private Location requestUpdatesFromProvider(final String provider, final int errorResId) {
		Location location = null;
		if (mLocationManager.isProviderEnabled(provider)) {
			mLocationManager.requestLocationUpdates(provider, FIVE_SECONDS, FIVE_METERS, listener);
			location = mLocationManager.getLastKnownLocation(provider);
		} else {
			Toast.makeText(getActivity(), errorResId, Toast.LENGTH_LONG).show();
		}
		return location;
	}

	// Callback method for the "fine provider" button.
	/* public void useFineProvider(View v) {
        mUseFine = true;
        setup();
    }

    // Callback method for the "both providers" button.
    public void useCoarseFineProviders(View v) {
        mUseFine = false;
        setup();
    }*/



	private void doReverseGeocoding(Location location) {
		// Since the geocoding API is synchronous and may take a while.  You don't want to lock
		// up the UI thread.  Invoking reverse geocoding in an AsyncTask.
		(new ReverseGeocodingTask(getActivity())).execute(new Location[] {location});
	}

	@SuppressLint("HandlerLeak")
	private void updateUILocation(Location location) {
		// We're sending the update to a handler which then updates the UI with the new
		// location.
		latitude = location.getLatitude();
		longtitude = location.getLongitude();
		String coordinates;
		if(geoparsePref==0)
			coordinates = Geo.parseCoordinates(latitude, longtitude);
		else if(geoparsePref==1)
			coordinates = Geo.parseCoordinates2(latitude, longtitude);
		else if(geoparsePref==2)
			coordinates = Geo.parseDecimalLocation(latitude, longtitude);
		else
			coordinates = "Error!";

		Message.obtain(mHandler,
				UPDATE_LATLNG,
				coordinates).sendToTarget();
		mShowMap.setEnabled(true);
		//mSMS.setEnabled(true);

		Message.obtain(mHandler, UPDATE_ACCURACY, String.valueOf((int)location.getAccuracy())+"m").sendToTarget();
		Message.obtain(mHandler, UPDATE_TIMESTAMP, Geo.parseTime(location.getTime())).sendToTarget();

		// Bypass reverse-geocoding only if the Geocoder service is available on the device.
		if (mGeocoderAvailable) doReverseGeocoding(location);
	}

	@SuppressLint("DefaultLocale")
	private final LocationListener listener = new LocationListener() {

		@Override
		public void onLocationChanged(Location location) {
			// A new location update is received.  Do something useful with it.  Update the UI with
			// the location update.
			//Toast.makeText(getActivity(), location.getProvider(), Toast.LENGTH_SHORT).show();
			updateUILocation(location);
		}

		@Override
		public void onProviderDisabled(String provider) {
			Toast.makeText(getActivity(), provider.toUpperCase(Locale.US) + "disabled!", Toast.LENGTH_LONG).show();
		}

		@Override
		public void onProviderEnabled(String provider) {
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	};

	/** Determines whether one Location reading is better than the current Location fix.
	 * Code taken from
	 * http://developer.android.com/guide/topics/location/obtaining-user-location.html
	 *
	 * @param newLocation  The new Location that you want to evaluate
	 * @param currentBestLocation  The current Location fix, to which you want to compare the new
	 *        one
	 * @return The better Location object based on recency and accuracy.
	 */
	protected Location getBetterLocation(Location newLocation, Location currentBestLocation) {
		if (currentBestLocation == null) {
			// A new location is always better than no location
			return newLocation;
		}

		// Check whether the new location fix is newer or older
		long timeDelta = newLocation.getTime() - currentBestLocation.getTime();
		boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
		boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
		boolean isNewer = timeDelta > 0;

		// If it's been more than two minutes since the current location, use the new location
		// because the user has likely moved.
		if (isSignificantlyNewer) {
			return newLocation;
			// If the new location is more than two minutes older, it must be worse
		} else if (isSignificantlyOlder) {
			return currentBestLocation;
		}

		// Check whether the new location fix is more or less accurate
		int accuracyDelta = (int) (newLocation.getAccuracy() - currentBestLocation.getAccuracy());
		boolean isLessAccurate = accuracyDelta > 0;
		boolean isMoreAccurate = accuracyDelta < 0;
		boolean isSignificantlyLessAccurate = accuracyDelta > 200;

		// Check if the old and new location are from the same provider
		boolean isFromSameProvider = isSameProvider(newLocation.getProvider(),
				currentBestLocation.getProvider());

		// Determine location quality using a combination of timeliness and accuracy
		if (isMoreAccurate) {
			return newLocation;
		} else if (isNewer && !isLessAccurate) {
			return newLocation;
		} else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
			return newLocation;
		}
		return currentBestLocation;
	}

	/** Checks whether two providers are the same */
	private boolean isSameProvider(String provider1, String provider2) {
		if (provider1 == null) {
			return provider2 == null;
		}
		return provider1.equals(provider2);
	}

	// AsyncTask encapsulating the reverse-geocoding API.  Since the geocoder API is blocked,
	// we do not want to invoke it from the UI thread.
	private class ReverseGeocodingTask extends AsyncTask<Location, Void, Void> {
		Context mContext;

		public ReverseGeocodingTask(Context context) {
			super();
			mContext = context;
		}

		@Override
		protected Void doInBackground(Location... params) {
			Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());

			Location loc = params[0];
			List<Address> addresses = null;
			try {
				addresses = geocoder.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);
			} catch (IOException e) {
				e.printStackTrace();
				// Update address field with the exception.
				Message.obtain(mHandler, UPDATE_ADDRESS, getString(R.string.requires_internet)).sendToTarget();
			}
			if (addresses != null && addresses.size() > 0) {
				Address address = addresses.get(0);
				// Format the first line of address (if available), city, and country name.
				String addressText = String.format("%s\n%s %s",
						address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "",
								address.getPostalCode() != null ? address.getPostalCode() : "",
								address.getLocality() != null ? address.getLocality() : "");
				// Update address field on UI.
				Message.obtain(mHandler, UPDATE_ADDRESS, addressText).sendToTarget();
			}
			return null;
		}
	}

	/**
	 * Dialog to prompt users to enable GPS on the device.
	 */

	@SuppressLint("ValidFragment")
	private class EnableGpsDialogFragment extends DialogFragment {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			return new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_LIGHT)
			.setTitle(R.string.enable_gps)
			.setMessage(R.string.enable_gps_dialog)
			.setPositiveButton(R.string.enable_gps, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					enableLocationSettings();
				}
			})
			.create();
		}
	}
}
