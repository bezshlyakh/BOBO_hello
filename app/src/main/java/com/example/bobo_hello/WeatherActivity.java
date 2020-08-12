package com.example.bobo_hello;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import java.util.Objects;

public class WeatherActivity extends AppCompatActivity {

    private Button changeCityBtn;
    private TextView cityText, tempText, windText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_display);
        initView();
        showBackBtn();
        showCityChosen();
        showOptionsChosen();
        setChangeCityBtnOnClick();
    }

    private void initView(){
        changeCityBtn = findViewById(R.id.ChangeCityButton);
        cityText = findViewById(R.id.chosenCityText);
        tempText = findViewById(R.id.tempText);
        windText = findViewById(R.id.windSpeedText);
    }

    private void showOptionsChosen() {
        if(getIntent().getBooleanExtra(MainActivity.IS_TEMP_ON, true)){
            tempText.setText(R.string.tempCurrent);
        }

        if(getIntent().getBooleanExtra(MainActivity.IS_WIND_ON, true)){
            windText.setText(R.string.windSpeed);
        }
    }

    private void showCityChosen() {
        String cityChosen = getIntent().getStringExtra(MainActivity.CITY_KEY);
        cityText.setText(cityChosen);
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