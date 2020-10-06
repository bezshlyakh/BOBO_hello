package com.example.bobo_hello;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import android.os.PersistableBundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.bobo_hello.UI.CitiesFindDialogFragment;
import com.example.bobo_hello.UI.SideNavigationItems.AppInfo.AppInfoFragment;
import com.example.bobo_hello.UI.SideNavigationItems.History.HistoryFragment;
import com.example.bobo_hello.UI.SideNavigationItems.Home.HomeFragment;
import com.example.bobo_hello.UI.SideNavigationItems.Map.MapFragment;
import com.example.bobo_hello.UI.SideNavigationItems.Options.OptionsFragment;
import com.example.bobo_hello.Utils.CoordConverter;
import com.example.bobo_hello.Utils.EventCityChanged;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.navigation.NavigationView;

import org.greenrobot.eventbus.EventBus;

public class MainActivity extends AppCompatActivity {

    private NavigationView navigationView;
    private DrawerLayout drawer;
    private CitiesFindDialogFragment findCityDialog;
    private static final int PERMISSION_REQUEST_CODE = 100;
    private LatLng currLocation;
    private LocationManager mLocManager = null;
    private final float DEFAULT_LATITUDE = 55.75f;
    private final float DEFAULT_LONGITUDE = 37.62f;
    private final String TAG = "mainActivityError", ID_LOC_RB = "IDlocRB", LOC_CHOSEN = "locationChosen", NO_DEF_CITY = "no city chosen";
    private int currFragID;
    private CoordConverter converter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initGUI();
        initStartLocation();
        if (savedInstanceState == null) {
            checkCurrentFragment(currFragID);
        }
        setOnClickForSideMenuItems();
        initNotificationChannel();
    }

    private void initGUI(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        findCityDialog = new CitiesFindDialogFragment();
        mLocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Geocoder geocoder = new Geocoder(this);
        converter = new CoordConverter(geocoder);
    }

    private void initStartLocation(){
        int lockID = readFromPreference().getInt(ID_LOC_RB, 0);
        if(lockID != R.id.RBCurrLocOnStart){
            String defCity = readFromPreference().getString(LOC_CHOSEN, NO_DEF_CITY);
            try {
                currLocation = converter.getCoordinatesByCityName(defCity);
            } catch (InterruptedException e) {
                e.printStackTrace();
                Log.d(TAG, "GEO converter error - can't convert city name");
                currLocation = new LatLng(DEFAULT_LONGITUDE, DEFAULT_LATITUDE);
            }
        } else {
            onCurrLocationClick();
        }
        saveCoordToPreference(currLocation);
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
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION
                    },
                    PERMISSION_REQUEST_CODE);
        } else {
            Location loc = mLocManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
            if(loc != null){
                currLocation = new LatLng(loc.getLatitude(), loc.getLongitude());
            } else {
                currLocation = new LatLng(DEFAULT_LATITUDE, DEFAULT_LONGITUDE);
            }
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
            currFragID = item.getItemId();
            checkCurrentFragment(currFragID);
            return true;
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("current fragment", currFragID);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putInt("current fragment", currFragID);
    }

    @Override
    public void onRestoreInstanceState(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onRestoreInstanceState(savedInstanceState, persistentState);
        currFragID = savedInstanceState.getInt("current fragment");
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        currFragID = savedInstanceState.getInt("current fragment");
        checkCurrentFragment(currFragID);
    }

    @SuppressLint("NonConstantResourceId")
    private void checkCurrentFragment(int id){
        switch (id) {
            case 0:
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
        fragmentTransaction.addToBackStack("previous fragment");
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

    private SharedPreferences readFromPreference() {
        return getPreferences(Context.MODE_PRIVATE);
    }


}