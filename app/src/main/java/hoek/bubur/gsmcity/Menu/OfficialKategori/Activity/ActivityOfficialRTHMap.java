package hoek.bubur.gsmcity.Menu.OfficialKategori.Activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.widget.SeekBar;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.MarkerOptions;

import butterknife.BindView;
import butterknife.ButterKnife;
import hoek.bubur.gsmcity.BaseActivity;
import hoek.bubur.gsmcity.Custom.RTHInfoWindow;
import hoek.bubur.gsmcity.Model.RTH;
import hoek.bubur.gsmcity.R;

/**
 * Created by dalbo on 5/20/2017.
 */

public class ActivityOfficialRTHMap extends BaseActivity implements OnMapReadyCallback {
    public static final String INTENT_DATA = "data.map.official";
    public static final float MAP_ZOOM = 10;
    RTH rth;
    GoogleMap gmap;
    SupportMapFragment mapFragment;
    RTHInfoWindow infoWindow;

    @BindView(R.id.seekZoom)
    SeekBar seekZoom;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_official_rth);
        ButterKnife.bind(this);

        rth = getIntent().getParcelableExtra(INTENT_DATA);
        if (rth == null) {
            finish();
        }
        infoWindow = new RTHInfoWindow(this, rth);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        seekZoom.setProgress((int) MAP_ZOOM);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        gmap = googleMap;
        gmap.addMarker(new MarkerOptions().position(rth.getLatLng()).title(rth.getNamaLokasi()));
        gmap.setInfoWindowAdapter(infoWindow);
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
        gmap.setMyLocationEnabled(true);
        moveCamera(MAP_ZOOM);
        seekZoom.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                moveCamera(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void moveCamera(float zoom){
        gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(rth.getLatLng(),zoom));
    }
}
