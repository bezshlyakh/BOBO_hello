package com.example.bobo_hello;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import com.example.bobo_hello.UI.SideNavigationItems.AppInfo.AppInfoFragment;
import com.example.bobo_hello.UI.SideNavigationItems.History.HistoryFragment;
import com.example.bobo_hello.UI.SideNavigationItems.Home.HomeFragment;
import com.example.bobo_hello.UI.SideNavigationItems.Map.MapFragment;
import com.example.bobo_hello.UI.SideNavigationItems.Options.OptionsFragment;
import com.example.bobo_hello.Utils.EventCityChanged;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.navigation.NavigationView;

import org.greenrobot.eventbus.EventBus;

public class MainActivity extends AppCompatActivity {

    private NavigationView navigationView;
    private DrawerLayout drawer;
    private CitiesFindDialogFragment findCityDialog;
    private static final int PERMISSION_REQUEST_CODE = 10;
    private LatLng currLocation;
    private LocationManager mLocManager = null;
    private final float DEFAULT_LATITUDE = 55.75f;
    private final float DEFAULT_LONGITUDE = 37.62f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        mLocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        findCityDialog = new CitiesFindDialogFragment();
        setHomeFragment();
        setOnClickForSideMenuItems();
        initNotificationChannel();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_up_menu, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_find_city: {
                onClickCityDialog();
                return true;
            }
            case R.id.menu_cur_location: {
                onCurrLocationClick();
                saveCoordToPreference(currLocation);
                EventBus.getDefault().post(new EventCityChanged(currLocation));
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void onClickCityDialog() {
        findCityDialog.show(getSupportFragmentManager(), "findCityDialog");
    }

    public void onCurrLocationClick(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Location loc = mLocManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
            if(loc != null){
                currLocation = new LatLng(loc.getLatitude(), loc.getLongitude());
            } else {
                currLocation = new LatLng(DEFAULT_LATITUDE, DEFAULT_LONGITUDE);
            }
        } else {
            requestLocationPermissions();
        }
    }

    private void requestLocationPermissions() {
        if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE)) {
            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION
                    },
                    PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onBackPressed() {
        if(getSupportFragmentManager().getBackStackEntryCount() > 1) {
            navigationView.setCheckedItem(R.id.nav_home);
        }
        super.onBackPressed();
    }

    @SuppressLint("NonConstantResourceId")
    private void setOnClickForSideMenuItems() {
        navigationView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.nav_home: {
                    setHomeFragment();
                    drawer.closeDrawers();
                    break;
                }
                case R.id.nav_map: {
                    setMapFragment();
                    drawer.closeDrawers();
                    break;
                }
                case R.id.nav_settings: {
                    setOptionsFragment();
                    drawer.closeDrawers();
                    break;
                }
                case R.id.nav_history: {
                    setHistoryFragment();
                    drawer.closeDrawers();
                    break;
                }
                case R.id.nav_app_info: {
                    setAppInfoFragment();
                    drawer.closeDrawers();
                    break;
                }
            }
            return true;
        });
    }

    private void setHomeFragment() {
        HomeFragment fragment = new HomeFragment();
        setFragment(fragment);
    }

    private void setMapFragment() {
        setFragment(new MapFragment());
    }

    private void setOptionsFragment() {
        setFragment(new OptionsFragment());
    }

    private void setAppInfoFragment() {
        setFragment(new AppInfoFragment());
    }

    private void setHistoryFragment() {
        setFragment(new HistoryFragment());
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.appFragmentContainer, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void initNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel channel = new NotificationChannel("2", "name", importance);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void saveCoordToPreference(LatLng coord) {
        SharedPreferences optionsPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = optionsPref.edit();
        editor.putFloat("currentLatitude", (float) coord.latitude);
        editor.putFloat("currentLongitude", (float) coord.longitude);
        editor.apply();
    }

}