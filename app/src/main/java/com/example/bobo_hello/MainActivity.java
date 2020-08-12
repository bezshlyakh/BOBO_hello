package com.example.bobo_hello;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private Button submitButton;
    private Button optionsButton;
    private RadioGroup rgCities;
    private RadioButton chosenCityRBtn;
    final static String CITY_KEY = "cityKey";
    final static int REQUEST_CODE = 1;
    private boolean tempOn, windOn;
    static final String IS_TEMP_ON = "is_temp_on", IS_WIND_ON = "is_wind_on";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        onSubmitBtnClickAction();
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
        submitButton = findViewById(R.id.toMainBtn);
        optionsButton = findViewById(R.id.optionsBtn);
        rgCities = findViewById(R.id.groupOfCities);
    }

    private void onSubmitBtnClickAction(){
        submitButton.setOnClickListener(view -> {
            chosenCityRBtn = findViewById(rgCities.getCheckedRadioButtonId());
            if(chosenCityRBtn != null){
                String city = (String) chosenCityRBtn.getText();
                Intent toWeatherDispIntent = new Intent(MainActivity.this, WeatherActivity.class);
                toWeatherDispIntent.putExtra(CITY_KEY, city);
                toWeatherDispIntent.putExtra(IS_TEMP_ON, tempOn);
                toWeatherDispIntent.putExtra(IS_WIND_ON, windOn);
                startActivity(toWeatherDispIntent);
            } else {
                Toast.makeText(getApplicationContext(),"Chose any city first", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void onOptionsBtnClickAction() {
        optionsButton.setOnClickListener(view -> {
            Intent toOptionsDispIntent = new Intent(MainActivity.this, OptionsActivity.class);
            startActivityForResult(toOptionsDispIntent, REQUEST_CODE);
        });
    }

}