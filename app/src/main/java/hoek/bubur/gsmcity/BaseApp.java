package hoek.bubur.gsmcity;

import android.Manifest;
import android.app.Application;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import hoek.bubur.gsmcity.Interface.OnLocationLock;

/**
 * Created by dalbo on 5/20/2017.
 */

public class BaseApp extends Application implements LocationListener {
    static double lat, lng;
    static LatLng latLng;
    static OnLocationLock listener;
    LocationManager lm;

    @Override
    public void onCreate() {
        super.onCreate();
        listener = new OnLocationLock() {
            @Override
            public void onLock(LatLng latLng) {

            }
        };
        lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000*5, 0, this);
        Log.i("LOC_MY","Initializing");
    }

    @Override
    public void onLocationChanged(Location location) {
        lat = location.getLatitude();
        lng = location.getLongitude();
        Log.i("LOC_AUTO",lat+" "+lng);
        latLng = new LatLng(lat, lng);
        listener.onLock(latLng);
    }

    public static void setOnLockListener(OnLocationLock listener){
        BaseApp.listener = listener;
    }

    public static void removeOnLockListener(){
        BaseApp.listener = new OnLocationLock() {
            @Override
            public void onLock(LatLng latLng) {

            }
        };
    }


    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    public static double getLat() {
        return lat;
    }

    public static void setLat(double lat) {
        BaseApp.lat = lat;
    }

    public static double getLng() {
        return lng;
    }

    public static void setLng(double lng) {
        BaseApp.lng = lng;
    }

    public static LatLng getLatLng() {
        return latLng;
    }

    public static void setLatLng(LatLng latLng) {
        BaseApp.latLng = latLng;
    }
}
