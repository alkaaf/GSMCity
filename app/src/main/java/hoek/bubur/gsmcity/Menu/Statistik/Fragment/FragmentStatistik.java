package hoek.bubur.gsmcity.Menu.Statistik.Fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import butterknife.BindView;
import butterknife.ButterKnife;
import hoek.bubur.gsmcity.BaseFragment;
import hoek.bubur.gsmcity.R;

/**
 * Created by dalbo on 5/23/2017.
 */

public class FragmentStatistik extends BaseFragment {
    @BindView(R.id.cobaa)
    ImageView cobaa;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_statistik, container, false);
        ButterKnife.bind(this, v);
        Glide.with(getContext()).load("http://192.168.137.1/gsmcity/foto/159239584e0aa8.jpg").listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                Log.i("GLIDER", "Fail");
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                Log.i("GLIDER", "SUCCE");
                return false;
            }
        }).into(cobaa);
        return v;

    }
}
