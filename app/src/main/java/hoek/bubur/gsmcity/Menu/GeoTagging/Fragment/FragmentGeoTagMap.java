package hoek.bubur.gsmcity.Menu.GeoTagging.Fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import hoek.bubur.gsmcity.BaseApp;
import hoek.bubur.gsmcity.BaseFragment;
import hoek.bubur.gsmcity.Conf;
import hoek.bubur.gsmcity.Menu.GeoTagging.DialogGambar;
import hoek.bubur.gsmcity.Menu.Ide.Fragment.FragmentIde;
import hoek.bubur.gsmcity.Model.RTH;
import hoek.bubur.gsmcity.R;
import hoek.bubur.gsmcity.WebService.API;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by dalbo on 5/20/2017.
 */

public class FragmentGeoTagMap extends BaseFragment implements OnMapReadyCallback {
    public static final float MAP_ZOOM = 10;

    SupportMapFragment mapFragment;
    GoogleMap gmap;

    boolean useMyLocation;

    @BindView(R.id.checkMyLoc)
    CheckBox checkMyLoc;
    @BindView(R.id.vMyLoc)
    View vMyLoc;
    @BindView(R.id.refreshLoc)
    ImageView refreshLoc;
    @BindView(R.id.bSegar)
    Button bSegar;
    @BindView(R.id.fab)
    FloatingActionButton fab;

    double lat;
    double lng;
    String wsUrl;
    AlertDialog imageDialog;

    LocationManager lm;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_map_geotag, container, false);
        ButterKnife.bind(this, v);
        wsUrl = new Conf(getContext()).getConf(Conf.CONF_WSADDR);
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        refreshLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchLocation();
            }
        });

        useMyLocation = checkMyLoc.isChecked();
        initView();
        checkMyLoc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                useMyLocation = b;
                initView();
                if (b) {
                    fetchLocation();
                }
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fContainer, new FragmentIde()).commit();
            }
        });

        return v;
    }

    private void initView() {
        if (useMyLocation) {
            refreshLoc.setVisibility(View.VISIBLE);
        } else {
            refreshLoc.setVisibility(View.GONE);
        }
    }

    private void fetchLocation() {
        if (BaseApp.getLatLng() != null) {
            lat = BaseApp.getLat();
            lng = BaseApp.getLng();
            initData(lat, lng);
            if (gmap != null) {
//                markAndPan(BaseApp.getLatLng(), "Lokasi Saya");
            }
        } else {
            Toast.makeText(getContext(), "Lokasi anda belum ditemukan", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gmap = googleMap;
        bSegar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initData(lat, lng);
            }
        });
        fetchLocation();
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
        gmap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (!useMyLocation) {
                    Log.i("LOC_SELECTED", latLng.latitude + " " + latLng.longitude);
                    markAndPan(new LatLng(latLng.latitude, latLng.longitude), "Lokasi Saya");
                    lat = latLng.latitude;
                    lng = latLng.longitude;
                    initData(lat, lng);
                } else {
                    Toast.makeText(getContext(), "Hilangi centang \"Gunakan lokasi saya\"", Toast.LENGTH_SHORT).show();
                }
            }
        });
        gmap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                DialogGambar dg = new DialogGambar(getContext(), mapMarkerRTH.get(marker));
                dg.show();
            }
        });
        initData(0, 0);
    }

    Marker marker;

    private void markAndPan(LatLng latlng, String title) {
        if (marker != null) {
            marker.remove();
        }
//        marker = gmap.addMarker(new MarkerOptions().position(latlng).title(title).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        if (useMyLocation) {
            gmap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng,MAP_ZOOM));
        }
    }


    List<RTH> dataRTH;
    List<Marker> markers;
    HashMap<Marker, RTH> mapMarkerRTH = new HashMap<>();

    public void initData(double lat, double lng) {
        API api = new API(getContext());
        api.getGeotagList(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (isActive()) {
                    try{

                        dataRTH = new Gson().fromJson(response.body().string(), new TypeToken<List<RTH>>() {
                        }.getType());
                    } catch (JsonSyntaxException e){
                        e.printStackTrace();
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (response.isSuccessful()) {
                                try {

                                    gmap.clear();
                                    markers = new ArrayList<Marker>();
                                    if(dataRTH != null) {
                                        for (int i = 0; i < dataRTH.size(); i++) {
                                            String snippet = dataRTH.get(i).getNamaLokasi() + "\n" + dataRTH.get(i).getFasilitas() + "\n(" + dataRTH.get(i).getLuas() + ")";
                                            Marker temp = gmap.addMarker(new MarkerOptions().position(dataRTH.get(i).getLatLng()).title(dataRTH.get(i).getAlamat()).snippet(snippet));
                                            mapMarkerRTH.put(temp, dataRTH.get(i));
                                            markers.add(temp);
                                        }
                                        markAndPan(new LatLng(FragmentGeoTagMap.this.lat, FragmentGeoTagMap.this.lng), "Lokasi saya");
                                    }
                                } catch (JsonSyntaxException e) {
                                    e.printStackTrace();
                                    Toast.makeText(getContext(), "Gagal parsing data", Toast.LENGTH_SHORT).show();
                                }

                            }
                        }
                    });
                }
            }
        });
    }
}
