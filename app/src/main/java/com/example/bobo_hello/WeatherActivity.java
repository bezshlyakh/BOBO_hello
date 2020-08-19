package com.example.bobo_hello;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bobo_hello.fragments.WeatherInfoFragment;

import java.util.Objects;

public class WeatherActivity extends AppCompatActivity {

    private Button changeCityBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_display);
        checkOrientation();
        initView();
        showBackBtn();
        setChangeCityBtnOnClick();

        if (savedInstanceState == null) {
            WeatherInfoFragment details = new WeatherInfoFragment();
            details.setArguments(getIntent().getExtras());
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.weather_info_activity, details)
                    .commit();
        }
    }

    private void checkOrientation() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            finish();
        }
    }

    private void initView(){
        changeCityBtn = findViewById(R.id.ChangeCityButton);
    }

    private void setChangeCityBtnOnClick() {
        changeCityBtn.setOnClickListener(view -> finish());
    }

    private void showBackBtn() {
        try {
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        } catch (NullPointerException exc) {
            exc.printStackTrace();
        }
    }
}