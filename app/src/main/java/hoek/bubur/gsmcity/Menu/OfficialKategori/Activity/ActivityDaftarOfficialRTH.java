package hoek.bubur.gsmcity.Menu.OfficialKategori.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import hoek.bubur.gsmcity.BaseActivity;
import hoek.bubur.gsmcity.Interface.OnItemClickListener;
import hoek.bubur.gsmcity.Menu.OfficialKategori.Adapter.AdapterDaftarOfficialRTH;
import hoek.bubur.gsmcity.Model.KategoriRTH;
import hoek.bubur.gsmcity.Model.RTH;
import hoek.bubur.gsmcity.R;
import hoek.bubur.gsmcity.WebService.API;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by dalbo on 5/20/2017.
 */

public class ActivityDaftarOfficialRTH extends BaseActivity {
    public static final String INTENT_DATA = "data.kategori";
    @BindView(R.id.swipe)
    SwipeRefreshLayout swipe;
    @BindView(R.id.rv)
    RecyclerView rv;

    AdapterDaftarOfficialRTH adapter;
    KategoriRTH kat;
    List<RTH> data = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar_official_rth);
        ButterKnife.bind(this);
        kat = getIntent().getParcelableExtra(INTENT_DATA);
        if (kat == null) {
            finish();
        }
        setBarTitle(getString(R.string.title_activity_daftar_rth) + " " + kat.getNamaKategori());
        adapter = new AdapterDaftarOfficialRTH();
        adapter.setOnItemClickListener(new OnItemClickListener<RTH>() {
            @Override
            public void onItemClick(View v, int pos, RTH data) {
                Intent intent = new Intent(ActivityDaftarOfficialRTH.this, ActivityOfficialRTHMap.class);
                intent.putExtra(ActivityOfficialRTHMap.INTENT_DATA, data);
                startActivity(intent);
            }
        });
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
            }
        });
        initData();
    }

    private void initData() {
        API api = new API(this);
        swipe.setRefreshing(true);
        api.getRTHOfficial(kat.getId(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                if (isActive()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            swipe.setRefreshing(false);
                        }
                    });
                }
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (isActive()) {
                    if (response.isSuccessful()) {
                        data = new Gson().fromJson(response.body().string(), new TypeToken<List<RTH>>() {
                        }.getType());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {

                                    adapter.replaceData(data);
                                    adapter.notifyDataSetChanged();
                                } catch (JsonSyntaxException e) {
                                    e.printStackTrace();
                                    Toast.makeText(ActivityDaftarOfficialRTH.this, "Gagal parsing data", Toast.LENGTH_SHORT).show();
                                }
                                swipe.setRefreshing(false);
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                swipe.setRefreshing(false);
                                Toast.makeText(ActivityDaftarOfficialRTH.this, "Gagal menghubungi server", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }
        });
    }
}
