package hoek.bubur.gsmcity;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;

import hoek.bubur.gsmcity.Interface.OnLocationLock;
import hoek.bubur.gsmcity.Menu.GeoTagging.Fragment.FragmentGeoTagMap;
import hoek.bubur.gsmcity.Menu.OfficialCari.Fragment.FragmentOfficialMapRadius;
import hoek.bubur.gsmcity.Menu.OfficialKategori.Fragment.FragmentDaftarKategori;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FragmentManager fm;
    View navHeader;
    Geocoder geocoder;

    OnLocationLock onLocationLock = new OnLocationLock() {
        long tickupdate = 0;

        @Override
        public void onLock(final LatLng latLng) {
            final TextView tvStatus = (TextView) navHeader.findViewById(R.id.tvStatus);
            TextView tvLat = (TextView) navHeader.findViewById(R.id.tvLat);
            TextView tvLng = (TextView) navHeader.findViewById(R.id.tvLng);

            tvLat.setText(latLng.latitude + "");
            tvLng.setText(latLng.longitude + "");

            // buat update lokasinya
            if (System.currentTimeMillis() - tickupdate > 1000 * 60) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            final Address addr = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1).get(0);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    tvStatus.setText(
                                            addr.getCountryName() + "\n" +
                                                    addr.getAdminArea() + "\n" +
                                                    addr.getSubAdminArea() + "\n" +
                                                    addr.getLocality() + "\n" +
                                                    addr.getSubLocality() + "\n" +
                                                    addr.getThoroughfare() + "\n"
                                    );
                                }
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                tickupdate = System.currentTimeMillis();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        geocoder = new Geocoder(this);

        fm = getSupportFragmentManager();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navHeader = navigationView.getHeaderView(0);
        BaseApp.setOnLockListener(onLocationLock);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        FragmentTransaction ft = fm.beginTransaction();
        BaseFragment fragment = null;
        int id = item.getItemId();

        if (id == R.id.nav_geotag) {
            // Handle the camera action
            fragment = new FragmentGeoTagMap();
        } else if (id == R.id.nav_official_kategori) {
            fragment = new FragmentDaftarKategori();
        } else if (id == R.id.nav_official_pencarian) {
            fragment = new FragmentOfficialMapRadius();
        } else if (id == R.id.nav_ide) {

        } else if (id == R.id.nav_statistik) {

        }

        if (fragment != null) {
            ft.replace(R.id.fContainer, fragment);
            ft.commit();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
