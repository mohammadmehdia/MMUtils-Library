package ir.alizadeh.mmui.utils;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Locale;

public class GeocodingAddressFinderTask extends AsyncTask<Double, Void, GeocodingAddressFinderTask.ResultModel> {
    private WeakReference<Context> contextRef;
    private Callback callback;
    private static GeocodingAddressFinderTask instance;
    private long startDelay = 0;

    public GeocodingAddressFinderTask(Context context, Callback callback) {
        this.contextRef = new WeakReference<>(context);
        this.callback = callback;
    }

    public void get(double latitude, double longitude) {
        this.execute(latitude, longitude);
    }

    @Override
    protected ResultModel doInBackground(Double... params) {
        Context context = contextRef.get();
        if(context == null || params == null || params.length < 2) return null;
        double latitude = params[0];
        double longitude = params[1];
        if(startDelay > 0 ) {
            try {
                Thread.sleep(startDelay);
            } catch (Exception e1){}
        }
        try {
            Geocoder geocoder = new Geocoder(context, Locale.US);
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if(addresses == null || addresses.isEmpty())
                return null;

            ResultModel result = new ResultModel();
            result.address = addresses.get(0).getAddressLine(0);
            result.city = addresses.get(0).getLocality();
            result.state = addresses.get(0).getAdminArea();
            result.country = addresses.get(0).getCountryName();
            result.postalCode = addresses.get(0).getPostalCode();
            result.knownName = addresses.get(0).getFeatureName();
            return result;
        } catch (Exception e){
            e.printStackTrace();
            Log.e("Geocoding", "exception : " + e.getMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(ResultModel resultModel) {
        super.onPostExecute(resultModel);
        if(callback != null) {
            if(resultModel == null) {
                callback.onAddress(false, null, null, null, null, null, null);
            } else {
                callback.onAddress(true, resultModel.country, resultModel.state, resultModel.city, resultModel.postalCode, resultModel.address, resultModel.knownName);
            }
        }
    }

    public void setStartDelay(long delay) {
        this.startDelay = delay;
    }

    public static final class ResultModel {
        public String city;
        public String country;
        public String state;
        public String address;
        public String knownName;
        public String postalCode;
    }

    public interface Callback {
        void onAddress(boolean success, String country, String state, String city, String postalCode, String address, String knownName);
    }
}
