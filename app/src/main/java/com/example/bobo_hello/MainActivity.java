package com.example.bobo_hello;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import com.example.bobo_hello.UI.SideNavigationItems.AppInfo.AppInfoFragment;
import com.example.bobo_hello.UI.SideNavigationItems.Home.HomeFragment;
import com.example.bobo_hello.UI.SideNavigationItems.Options.OptionsFragment;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private NavigationView navigationView;
    private DrawerLayout drawer;
    private CitiesFindDialogFragment findCityDialog;

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
        findCityDialog = new CitiesFindDialogFragment();
        setHomeFragment();
        setOnClickForSideMenuItems();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_up_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_find_city) {
            onClickCityDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onClickCityDialog() {
        findCityDialog.show(getSupportFragmentManager(), "findCityDialog");
    }

    @Override
    public void onBackPressed() {
        if(getSupportFragmentManager().getBackStackEntryCount() > 1) {
            navigationView.setCheckedItem(R.id.nav_home);
        }
        super.onBackPressed();
    }

    private void setOnClickForSideMenuItems() {
        navigationView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.nav_home: {
                    setHomeFragment();
                    drawer.closeDrawers();
                    break;
                }
                case R.id.nav_settings: {
                    setOptionsFragment();
                    drawer.closeDrawers();
                    break;
                }
                case R.id.nav_history: {
                    setAppInfoFragment();
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

    private void setOptionsFragment() {
        setFragment(new OptionsFragment());
    }

    private void setAppInfoFragment() {
        setFragment(new AppInfoFragment());
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.appFragmentContainer, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}