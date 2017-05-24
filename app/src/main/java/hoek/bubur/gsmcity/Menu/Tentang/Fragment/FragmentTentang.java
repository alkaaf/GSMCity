package hoek.bubur.gsmcity.Menu.Tentang.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import hoek.bubur.gsmcity.BaseFragment;
import hoek.bubur.gsmcity.R;

/**
 * Created by dalbo on 5/24/2017.
 */

public class FragmentTentang extends BaseFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tentang, container, false);
        return v;
    }
}
