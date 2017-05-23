package hoek.bubur.gsmcity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by dalbo on 5/19/2017.
 */

public class BaseFragment extends Fragment {
    boolean active = false;
    ProgressDialog pd;
    ActionBar actionBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public void showLoad() {
        pd = new ProgressDialog(getContext());
        pd.setMessage(getString(R.string.pd_memuat));
        pd.show();
    }

    public void hideLoad() {
        pd.dismiss();
    }

    @Override
    public void onStart() {
        super.onStart();
        active = true;
    }

    @Override
    public void onStop() {
        super.onStop();
        active = false;
    }

    public boolean isActive() {
        return active;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public AppCompatActivity getAppCompatActivity() {
        return ((AppCompatActivity) getActivity());
    }
//    public void setBarTitle(String title){
//        actionBar.setTitle(title);
//    }
}
