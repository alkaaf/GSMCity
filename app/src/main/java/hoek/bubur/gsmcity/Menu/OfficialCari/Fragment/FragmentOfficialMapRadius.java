package hoek.bubur.gsmcity.Menu.OfficialCari.Fragment;

import android.graphics.Color;
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
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.google.maps.android.heatmaps.HeatmapTileProvider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import hoek.bubur.gsmcity.BaseApp;
import hoek.bubur.gsmcity.BaseFragment;
import hoek.bubur.gsmcity.Model.RTH;
import hoek.bubur.gsmcity.R;
import hoek.bubur.gsmcity.WebService.API;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by dalbo on 5/20/2017.
 */

public class FragmentOfficialMapRadius extends BaseFragment implements OnMapReadyCallback {
    public static final float MAP_ZOOM = 10;

    SupportMapFragment mapFragment;
    GoogleMap gmap;
    SearchView searchView;

    boolean useMyLocation;

    @BindView(R.id.checkMyLoc)
    CheckBox checkMyLoc;
    @BindView(R.id.vMyLoc)
    View vMyLoc;
    @BindView(R.id.refreshLoc)
    ImageView refreshLoc;
    @BindView(R.id.seekJarak)
    SeekBar seekJarak;
    @BindView(R.id.tvJarak)
    TextView tvJarak;
    @BindView(R.id.bSegar)
    Button bSegar;


    double lat;
    double lng;
    String cari = "";
    int jarak = 1;

    LocationManager lm;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_map_official_rth, container, false);
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
        seekJarak.setProgress(jarak);
        tvJarak.setText(jarak + " KM");
        seekJarak.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                jarak = i;
                tvJarak.setText(jarak + " KM");
                markAndPan(new LatLng(lat,lng));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        bSegar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initData(lat, lng, jarak, cari, cari);
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
        fetchLocation();
        gmap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (!useMyLocation) {
                    Log.i("LOC_SELECTED", latLng.latitude + " " + latLng.longitude);
                    lat = latLng.latitude;
                    lng = latLng.longitude;
                    markAndPan(new LatLng(latLng.latitude, latLng.longitude));
                } else {
                    Toast.makeText(getContext(), "Hilangi centang \"Gunakan lokasi saya\"", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    Marker marker;
    Circle circle;
    private void markAndPan(LatLng latlng) {
        if (marker != null) {
            marker.remove();
            circle.remove();
        }
        marker = gmap.addMarker(new MarkerOptions().position(latlng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        circle = gmap.addCircle(new CircleOptions().center(latlng).fillColor(0x5552e069).radius(jarak*1000).strokeColor(0x5552e069).strokeWidth(2));
        if (useMyLocation) {
            gmap.animateCamera(CameraUpdateFactory.newLatLng(latlng));
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_search, menu);
        searchView = (SearchView) menu.findItem(R.id.menuSearch).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                cari = query;
                initData(lat, lng, jarak, cari, cari);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                cari = newText;
                return false;
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                cari = "";
                return false;
            }
        });
    }

    List<RTH> dataRTH;
    List<Marker> markers;

    public void initData(double lat, double lng, double jarak, String alamat, String nama) {
        API api = new API(getContext());
        api.getRadiusRTHOfficial(lat, lng, alamat, nama, jarak, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (isActive()) {
                    if (response.isSuccessful()) {
                        try {
                            // parse data
                            dataRTH = new Gson().fromJson(response.body().string(), new TypeToken<List<RTH>>() {
                            }.getType());
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    // save marker
                                    markers = new ArrayList<Marker>();

                                    // clear maps marker
                                    gmap.clear();

                                    // put marker to list and map
                                    if(dataRTH != null) {
                                        for (int i = 0; i < dataRTH.size(); i++) {
                                            markers.add(gmap.addMarker(new MarkerOptions().position(dataRTH.get(i).getLatLng()).title(dataRTH.get(i).getAlamat()).snippet(dataRTH.get(i).getNamaLokasi())));
                                        }
                                    }

                                    markAndPan(new LatLng(FragmentOfficialMapRadius.this.lat, FragmentOfficialMapRadius.this.lng));
                                }
                            });

                        } catch (JsonSyntaxException e) {
                            e.printStackTrace();
                            // catch when cannot parse json response
//                            Toast.makeText(getContext(), "Gagal parsing data", Toast.LENGTH_SHORT).show();
                        }

                    }

                }
            }
        });
    }
}
