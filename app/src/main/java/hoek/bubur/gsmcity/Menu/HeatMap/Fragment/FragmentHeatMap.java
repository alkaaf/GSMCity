package hoek.bubur.gsmcity.Menu.HeatMap.Fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.google.maps.android.heatmaps.Gradient;
import com.google.maps.android.heatmaps.HeatmapTileProvider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import hoek.bubur.gsmcity.BaseFragment;
import hoek.bubur.gsmcity.Model.RTH;
import hoek.bubur.gsmcity.R;
import hoek.bubur.gsmcity.WebService.API;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by dalbo on 5/23/2017.
 */

public class FragmentHeatMap extends BaseFragment implements OnMapReadyCallback {
    public static final int RADIUS_BLUR = 35;

    SupportMapFragment mapFragment;
    GoogleMap gMap;
    List<RTH> dataRTH;
    List<LatLng> dataLatLng;

    @BindView(R.id.checkOfficial)
    CheckBox checkOfficial;
    @BindView(R.id.pb)
    ProgressBar pb;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_heatmap, container, false);
        ButterKnife.bind(this, v);
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        return v;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
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
        gMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                initData();
                return false;
            }
        });
        gMap.setMyLocationEnabled(true);
        checkOfficial.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                initData();
            }
        });
        initData();
    }

    public void initData() {
        gMap.clear();
        pb.setVisibility(View.VISIBLE);
        dataLatLng = new ArrayList<>();
        API api = new API(getContext());
        api.getHeatMap(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                if (isActive()) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(), "Gagal koneksi", Toast.LENGTH_SHORT).show();
                            pb.setVisibility(View.GONE);
                        }
                    });
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        dataRTH = new Gson().fromJson(response.body().string(), new TypeToken<List<RTH>>() {
                        }.getType());
                        if (dataRTH != null) {
                            for (int i = 0; i < dataRTH.size(); i++) {
                                dataLatLng.add(dataRTH.get(i).getLatLng());
                            }
                            if (isActive()) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        initHeatMap();
                                    }
                                });
                            }
                        }
                    } catch (JsonSyntaxException e) {
                        e.printStackTrace();
                    }
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pb.setVisibility(View.GONE);
                    }
                });
            }
        },checkOfficial.isChecked());
    }
    TileOverlay tileOverlay;
    private void initHeatMap(){
        // Create the gradient.
        int[] colors = {
                Color.rgb(102, 225, 0), // green
                Color.rgb(255, 0, 0)    // red
        };

        float[] startPoints = {
                0.2f, 1f
        };
        Gradient gradient = new Gradient(colors, startPoints);
        HeatmapTileProvider provider = new HeatmapTileProvider.Builder()
                .data(dataLatLng)
                .gradient(gradient)
                .radius(RADIUS_BLUR)
                .build();
        tileOverlay = gMap.addTileOverlay(new TileOverlayOptions().tileProvider(provider));
    }
}
