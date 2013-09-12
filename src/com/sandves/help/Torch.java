package com.sandves.help;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

public class Torch {

	boolean mIsLightOn;
	Camera mCamera;
	MenuItem mTorchButton;
	Context mContext;

	public Torch(Context c, MenuItem i) {
		mIsLightOn = false;
		mTorchButton = i;
		mContext = c;
		mCamera = null;
	}

	public void light() {

		if(mCamera==null) {
			PackageManager pm = mContext.getPackageManager();

			// if device support camera?
			if (!pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
				Log.e("err", "Device has no camera!");
				Toast.makeText(mContext, R.string.no_camera, Toast.LENGTH_LONG).show();
				return;
			}

			mCamera = getCameraInstance();
		}

		final Camera.Parameters p = mCamera.getParameters();

		if (mIsLightOn) {

			Log.i("info", "torch is turn off!");

			p.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
			mCamera.setParameters(p);
			mCamera.stopPreview();
			//mTorchButton.setTitle(mContext.getString(R.string.torch_on));
			releaseCamera();
			mIsLightOn = false;

		} else {

			Log.i("info", "torch is turn on!");

			p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
			mCamera.setParameters(p);
			mCamera.startPreview();
			//mTorchButton.setTitle(mContext.getString(R.string.torch_off));
			mIsLightOn = true;
		}
	}
	
	/** A safe way to get an instance of the Camera object. */
	public static Camera getCameraInstance(){
	    Camera c = null;
	    try {
	        c = Camera.open(); // attempt to get a Camera instance
	    }
	    catch (Exception e){
	        // Camera is not available (in use or does not exist)
	    }
	    return c; // returns null if camera is unavailable
	}
	
	public void releaseCamera() {
		if (mCamera != null) {
			mCamera.release(); // release the camera for other applications
			mCamera = null;
		}
	}

}
