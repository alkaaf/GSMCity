package hoek.bubur.gsmcity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;

import hoek.bubur.gsmcity.Interface.OnLocationLock;
import hoek.bubur.gsmcity.Menu.GeoTagging.Fragment.FragmentGeoTagMap;
import hoek.bubur.gsmcity.Menu.HeatMap.Fragment.FragmentHeatMap;
import hoek.bubur.gsmcity.Menu.Ide.Fragment.FragmentIde;
import hoek.bubur.gsmcity.Menu.OfficialCari.Fragment.FragmentOfficialMapRadius;
import hoek.bubur.gsmcity.Menu.OfficialKategori.Fragment.FragmentDaftarKategori;
import hoek.bubur.gsmcity.Menu.Statistik.Fragment.FragmentStatistik;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final int PERM_EXTERNAL = 1;
    public static final int PERM_LOCATION = 2;
    public static final int PERM_CAMERA = 3;

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

        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        if(lm.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        geocoder = new Geocoder(this);

        if (new Conf(this).getConf(Conf.CONF_WSADDR).isEmpty() || new Conf(this).getConf(Conf.CONF_WSADDR) == null) {
            changeWs();
        }

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
        fm.beginTransaction().replace(R.id.fContainer, new FragmentGeoTagMap()).commit();

        checkPermission();
    }

    private void checkPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PERM_LOCATION
            );
            return;
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, PERM_CAMERA
            );
            return;
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERM_EXTERNAL
            );
            return;
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERM_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    Toast.makeText(this, "Aktifkan permisi GPS untuk menggunakan aplikasi", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            }
            case PERM_CAMERA: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    Toast.makeText(this, "Aktifkan permisi kamera untuk menggunakan aplikasi", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            }
            case PERM_EXTERNAL: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    Toast.makeText(this, "Aktifkan permisi untuk membaca data menggunakan aplikasi", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            }
        }
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
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

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
            fragment = new FragmentIde();
        } else if (id == R.id.nav_statistik) {
            fragment = new FragmentStatistik();
        } else if (id == R.id.nav_changews) {
            changeWs();
        } else if (id == R.id.nav_heatmap) {
            fragment = new FragmentHeatMap();
        }

        if (fragment != null) {
            ft.replace(R.id.fContainer, fragment);
            ft.commit();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    EditText iEditWs;
    Conf conf;
    AlertDialog alertDialog;

    private void changeWs() {
        iEditWs = new EditText(this);
        iEditWs.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        conf = new Conf(this);
        String sWs = conf.getConf(Conf.CONF_WSADDR);
        if (sWs != null) {
            iEditWs.setText(sWs);
        }
        alertDialog = new AlertDialog.Builder(this)
                .setView(iEditWs)
                .setTitle("Alamat webservice")
                .setPositiveButton("Simpan", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (iEditWs.getText().toString().isEmpty()) {
                            Toast.makeText(MainActivity.this, "Alamat tidak boleh kosong", Toast.LENGTH_SHORT).show();
                        } else {
                            conf.putConf(Conf.CONF_WSADDR, iEditWs.getText().toString().trim());
                            alertDialog.dismiss();
                        }
                    }
                })
                .setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        alertDialog.dismiss();
                    }
                })
                .create();
        alertDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                fragment.onActivityResult(requestCode, resultCode, data);
            }
        }
    }
}
