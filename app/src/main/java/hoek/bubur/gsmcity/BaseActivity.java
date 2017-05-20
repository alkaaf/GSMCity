package hoek.bubur.gsmcity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by dalbo on 5/19/2017.
 */

public class BaseActivity extends AppCompatActivity{
    boolean active = false;
    ProgressDialog pd;
    ActionBar actionBar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actionBar = getSupportActionBar();
        pd = new ProgressDialog(this);
        pd.setMessage(getString(R.string.pd_memuat));
    }

    public void showLoad(){
        pd.show();
    }

    public void hideLoad(){
        pd.dismiss();
    }

    @Override
    protected void onStart() {
        super.onStart();
        active = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        active = false;
    }

    public boolean isActive() {
        return active;
    }

    public void setBarTitle(String title){
        actionBar.setTitle(title);
    }
}
