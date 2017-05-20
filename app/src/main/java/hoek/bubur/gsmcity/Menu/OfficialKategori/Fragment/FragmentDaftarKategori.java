package hoek.bubur.gsmcity.Menu.OfficialKategori.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import hoek.bubur.gsmcity.BaseFragment;
import hoek.bubur.gsmcity.Menu.OfficialKategori.Activity.ActivityDaftarOfficialRTH;
import hoek.bubur.gsmcity.Model.KategoriRTH;
import hoek.bubur.gsmcity.R;

/**
 * Created by dalbo on 5/20/2017.
 */

public class FragmentDaftarKategori extends BaseFragment implements View.OnClickListener {
    @BindView(R.id.v1)
    View v1;
    @BindView(R.id.v2)
    View v2;
    @BindView(R.id.v3)
    View v3;
    @BindView(R.id.v4)
    View v4;
    @BindView(R.id.v5)
    View v5;
    @BindView(R.id.v6)
    View v6;
    @BindView(R.id.tvKat1)
    TextView tvKat1;
    @BindView(R.id.tvKat2)
    TextView tvKat2;
    @BindView(R.id.tvKat3)
    TextView tvKat3;
    @BindView(R.id.tvKat4)
    TextView tvKat4;
    @BindView(R.id.tvKat5)
    TextView tvKat5;
    @BindView(R.id.tvKat6)
    TextView tvKat6;

    List<KategoriRTH> kat = KategoriRTH.getSample();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_kategori_rth, container, false);
        ButterKnife.bind(this, v);

//        ((AppCompatActivity) getActivity()).getActionBar().setTitle(R.string.title_fragment_kategori);
        tvKat1.setText(kat.get(0).getNamaKategori());
        tvKat2.setText(kat.get(1).getNamaKategori());
        tvKat3.setText(kat.get(2).getNamaKategori());
        tvKat4.setText(kat.get(3).getNamaKategori());
        tvKat5.setText(kat.get(4).getNamaKategori());
        tvKat6.setText(kat.get(5).getNamaKategori());
        initListener();
        return v;
    }

    private void initListener() {
        v1.setOnClickListener(this);
        v2.setOnClickListener(this);
        v3.setOnClickListener(this);
        v4.setOnClickListener(this);
        v5.setOnClickListener(this);
        v6.setOnClickListener(this);
    }

    private void startDaftarActivity(KategoriRTH kategori) {
        Intent intent = new Intent(getContext(), ActivityDaftarOfficialRTH.class);
        intent.putExtra(ActivityDaftarOfficialRTH.INTENT_DATA, kategori);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.v1:
                startDaftarActivity(kat.get(0));
                break;
            case R.id.v2:
                startDaftarActivity(kat.get(1));
                break;
            case R.id.v3:
                startDaftarActivity(kat.get(2));
                break;
            case R.id.v4:
                startDaftarActivity(kat.get(3));
                break;
            case R.id.v5:
                startDaftarActivity(kat.get(4));
                break;
            case R.id.v6:
                startDaftarActivity(kat.get(5));
                break;
        }
    }
}
