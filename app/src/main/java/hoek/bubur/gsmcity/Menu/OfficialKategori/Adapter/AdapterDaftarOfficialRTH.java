package hoek.bubur.gsmcity.Menu.OfficialKategori.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import hoek.bubur.gsmcity.Global.GlobalRecyclerAdapter;
import hoek.bubur.gsmcity.Model.RTH;
import hoek.bubur.gsmcity.R;

/**
 * Created by dalbo on 5/20/2017.
 */

public class AdapterDaftarOfficialRTH extends GlobalRecyclerAdapter<RTH, AdapterDaftarOfficialRTH.Holder> {
    @Override
    public AdapterDaftarOfficialRTH.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_daftar_rth, parent, false);
        return new Holder(v);
    }

    @Override
    public void onBindDataToHolder(Holder holder, int pos, RTH data) {
        if (data.getNamaLokasi() == null || data.getNamaLokasi().isEmpty()) {

        } else {
            holder.tvNama.setText(data.getNamaLokasi());
        }
        holder.tvAlamat.setText(data.getAlamat());
    }

    class Holder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvNama)
        TextView tvNama;
        @BindView(R.id.tvAlamat)
        TextView tvAlamat;

        public Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getOnItemClickListener().onItemClick(view, getAdapterPosition(), getData(getAdapterPosition()));
                }
            });
        }
    }
}
