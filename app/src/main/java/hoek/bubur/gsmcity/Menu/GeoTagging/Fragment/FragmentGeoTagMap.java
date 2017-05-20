package hoek.bubur.gsmcity.Menu.GeoTagging.Fragment;

import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import hoek.bubur.gsmcity.BaseApp;
import hoek.bubur.gsmcity.BaseFragment;
import hoek.bubur.gsmcity.Model.RTH;
import hoek.bubur.gsmcity.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by dalbo on 5/20/2017.
 */

public class FragmentGeoTagMap extends BaseFragment implements OnMapReadyCallback {
    public static final float MAP_ZOOM = 10;

    SupportMapFragment mapFragment;
    GoogleMap gmap;
    SearchView searchView;
    Location myLocation;


    boolean useMyLocation;

    @BindView(R.id.checkMyLoc)
    CheckBox checkMyLoc;
    @BindView(R.id.vMyLoc)
    View vMyLoc;
    @BindView(R.id.refreshLoc)
    ImageView refreshLoc;
    @BindView(R.id.bSegar)
    Button bSegar;

    double lat;
    double lng;

    LocationManager lm;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_map_geotag, container, false);
        ButterKnife.bind(this, v);
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
                markAndPan(BaseApp.getLatLng());
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
        gmap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (!useMyLocation) {
                    Log.i("LOC_SELECTED", latLng.latitude + " " + latLng.longitude);
                    markAndPan(new LatLng(latLng.latitude, latLng.longitude));
                    lat = latLng.latitude;
                    lng = latLng.longitude;
                    initData(lat, lng);
                } else {
                    Toast.makeText(getContext(), "Hilangi centang \"Gunakan lokasi saya\"", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    Marker marker;

    private void markAndPan(LatLng latlng) {
        if (marker != null) {
            marker.remove();
        }
        marker = gmap.addMarker(new MarkerOptions().position(latlng));
        if (useMyLocation) {
            gmap.animateCamera(CameraUpdateFactory.newLatLng(latlng));
        }
    }
//
//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        super.onCreateOptionsMenu(menu, inflater);
//        inflater.inflate(R.menu.menu_search, menu);
//        searchView = (SearchView) menu.findItem(R.id.menuSearch).getActionView();
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                Toast.makeText(getContext(), query, Toast.LENGTH_SHORT).show();
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                return false;
//            }
//        });
//    }

    List<RTH> dataRTH;
    List<Marker> markers;

    public void initData(double lat, double lng) {
        OkHttpClient ok = new OkHttpClient();
        ok.newCall(new Request.Builder()
                .url("http://ww.google.com")
                .post(new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("lat", Double.toString(lat))
                        .addFormDataPart("lng", Double.toString(lng))
                        .build()
                )
                .build()
        ).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        dataRTH = new Gson().fromJson(response.body().string(), new TypeToken<List<RTH>>() {
                        }.getType());
                        markers = new ArrayList<Marker>();
                        for (int i = 0; i < dataRTH.size(); i++) {
                            markers.add(gmap.addMarker(new MarkerOptions().position(dataRTH.get(i).getLatLng())));
                        }
                    } catch (JsonSyntaxException e){
                        e.printStackTrace();
                        if(isActive()) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getContext(), "Gagal parsing data", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                    }
                }
            }
        });
    }
}
