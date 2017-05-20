package hoek.bubur.gsmcity.Custom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import butterknife.BindView;
import butterknife.ButterKnife;
import hoek.bubur.gsmcity.Model.RTH;
import hoek.bubur.gsmcity.R;

/**
 * Created by dalbo on 5/20/2017.
 */

public class RTHInfoWindow implements GoogleMap.InfoWindowAdapter {
    Context context;
    RTH rth;

    public RTHInfoWindow(Context context, RTH rth) {
        this.context = context;
        this.rth = rth;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @BindView(R.id.tvNama)
    TextView tvNama;
    @BindView(R.id.tvFasilitas)
    TextView tvFasilitas;
    @BindView(R.id.imgFoto)
    ImageView imgFoto;
    @Override
    public View getInfoContents(Marker marker) {
        View v = LayoutInflater.from(context).inflate(R.layout.map_info_rth, null, false);
        ButterKnife.bind(this, v);
        tvNama.setText(rth.getNamaLokasi());
        tvFasilitas.setText(rth.getFasilitas());
        return v;
    }
}
