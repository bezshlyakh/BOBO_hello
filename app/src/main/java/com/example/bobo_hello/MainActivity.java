package com.example.bobo_hello;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.bobo_hello.fragments.CitiesFragment;

public class MainActivity extends AppCompatActivity {
    private Button optionsButton;
    //final static String CITY_KEY = "cityKey";
    final static int REQUEST_CODE = 1;
    private boolean tempOn = true, windOn = true;
    static final String IS_TEMP_ON = "is_temp_on", IS_WIND_ON = "is_wind_on";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        onOptionsBtnClickAction();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode != REQUEST_CODE) {
            super.onActivityResult(requestCode, resultCode, data);
            return;
        }

        if(resultCode == RESULT_OK && data != null){
            tempOn = data.getBooleanExtra(IS_TEMP_ON, true);
            windOn = data.getBooleanExtra(IS_WIND_ON, true);
        }
    }

    private void initView(){
        optionsButton = findViewById(R.id.optionsBtn);
    }

    private void onOptionsBtnClickAction() {
        optionsButton.setOnClickListener(view -> {
            Intent toOptionsDispIntent = new Intent(MainActivity.this, OptionsActivity.class);
            startActivityForResult(toOptionsDispIntent, REQUEST_CODE);
        });
        saveOptionsToBundle();
    }

    private void saveOptionsToBundle() {
        Bundle bundle = new Bundle();
        bundle.putBoolean("tempKey", tempOn);
        bundle.putBoolean("windKey", windOn);
        android.app.Fragment fragInfo = new android.app.Fragment();
        fragInfo.setArguments(bundle);
        @SuppressLint("CommitTransaction") FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.cities, fragInfo);
        ft.commit();
    }


}