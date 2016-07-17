package com.bidchat.crop;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;

import com.yalantis.ucrop.UCrop;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

public class CropPlugin extends CordovaPlugin {

    private CallbackContext callbackContext;
    private Uri inputUri;
    private Uri outputUri;

    @Override
    public boolean execute(String action, JSONArray args, final CallbackContext callbackContext) throws JSONException {
        if (action.equals("cropImage")) {
            int width = getDeviceWidth(), height = getDeviceWidth();
            int quality = 0;
            if (args.length() > 1) {
                quality = args.getJSONObject(1).getInt("quality");
                height = args.getJSONObject(1).getInt("imageHeight");
            }
            String imagePath = args.getString(0);

            height = height == 0 ? width : height;
            quality = quality == 0 ? 100 : quality;

            this.inputUri = Uri.parse("file://" + imagePath);
            this.outputUri = Uri.fromFile(new File(getTempDirectoryPath() + "/" + System.currentTimeMillis() + "-cropped.jpg"));

            PluginResult pr = new PluginResult(PluginResult.Status.NO_RESULT);
            pr.setKeepCallback(true);
            callbackContext.sendPluginResult(pr);
            this.callbackContext = callbackContext;
            Log.i("Reached here", "Before start");
            cordova.setActivityResultCallback(this);
            UCrop.Options options = new UCrop.Options();
            options.setHideBottomControls(true);
            options.withMaxResultSize(width, height);
            options.setCompressionQuality(quality);
            UCrop.of(this.inputUri, this.outputUri)
                    .withOptions(options)
                    .withAspectRatio(width, height)
                    .withMaxResultSize(width, height)
                    .start(cordova.getActivity());
            return true;
        }
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == UCrop.REQUEST_CROP) {
            if (resultCode == Activity.RESULT_OK) {
                Uri imageUri = UCrop.getOutput(intent);
                if (imageUri != null) {
                    this.callbackContext.success("file://" + imageUri.getPath());
                } else {
                    this.callbackContext.error("Image not found");
                }
                this.callbackContext = null;
            } else if (resultCode == UCrop.RESULT_ERROR) {
                try {
                    JSONObject err = new JSONObject();
                    err.put("message", "Error on cropping");
                    err.put("code", String.valueOf(resultCode));
                    this.callbackContext.error(err);
                    this.callbackContext = null;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                try {
                    JSONObject err = new JSONObject();
                    err.put("message", "User cancelled");
                    err.put("code", "userCancelled");
                    this.callbackContext.error(err);
                    this.callbackContext = null;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, intent);
    }

    int getDeviceWidth() {
        DisplayMetrics metrics = new DisplayMetrics();
        cordova.getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics.widthPixels;
    }

    private String getTempDirectoryPath() {
        File cache;

        // SD Card Mounted
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            cache = new File(Environment.getExternalStorageDirectory().getAbsolutePath() +
                    "/Android/data/" + cordova.getActivity().getPackageName() + "/cache/");
        }
        // Use internal storage
        else {
            cache = cordova.getActivity().getCacheDir();
        }

        // Create the cache directory if it doesn't exist
        //noinspection ResultOfMethodCallIgnored
        cache.mkdirs();
        return cache.getAbsolutePath();

    }

    public Bundle onSaveInstanceState() {
        Bundle state = new Bundle();

        if (this.inputUri != null) {
            state.putString("inputUri", this.inputUri.toString());
        }

        if (this.outputUri != null) {
            state.putString("outputUri", this.outputUri.toString());
        }

        return state;
    }

    public void onRestoreStateForActivityResult(Bundle state, CallbackContext callbackContext) {

        if (state.containsKey("inputUri")) {
            this.inputUri = Uri.parse(state.getString("inputUri"));
        }

        if (state.containsKey("outputUri")) {
            this.inputUri = Uri.parse(state.getString("outputUri"));
        }

        this.callbackContext = callbackContext;
    }

}
