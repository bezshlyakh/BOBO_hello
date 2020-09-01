package com.example.bobo_hello;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;

import java.util.Objects;

public class OptionsActivity extends AppCompatActivity {
//
//    private Button toMainBtn;
//    @SuppressLint("UseSwitchCompatOrMaterialCode")
//    private Switch tempSwitch, windSwitch;
//    boolean isTempSwitchOn, isWindSwitchOn;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.options_display);
//        initView();
//        if(savedInstanceState != null){onRestoreInstanceState(savedInstanceState);}
//        showBackBtn();
//        setToMainBtnOnClick();
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        SharedPreferences options = getSharedPreferences("options", 0);
//        boolean tempSwitchState = options.getBoolean("tempSwitch", false);
//        boolean windSwitchState = options.getBoolean("windSwitch", false);
//        tempSwitch.setChecked(tempSwitchState);
//        windSwitch.setChecked(windSwitchState);
//
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        SharedPreferences options = getSharedPreferences("options", 0);
//        SharedPreferences.Editor editor = options.edit();
//        editor.putBoolean("tempSwitch", tempSwitch.isChecked());
//        editor.putBoolean("windSwitch", windSwitch.isChecked());
//        editor.apply();
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        if(item.getItemId() == android.R.id.home) {
//            makeIntent();
//            finish();
//        }
//        return true;
//    }
//
//    private void initView(){
//        toMainBtn = findViewById(R.id.toMainBtn);
//        tempSwitch = findViewById(R.id.tempSwitch);
//        windSwitch = findViewById(R.id.windSwitch);
//    }
//
//    private void setToMainBtnOnClick() {
//        toMainBtn.setOnClickListener(view -> {
//            makeIntent();
//            finish();
//        });
//    }
//
//    private void makeIntent() {
//        isTempSwitchOn = tempSwitch.isChecked();
//        isWindSwitchOn = windSwitch.isChecked();
//        Intent toMainIntent = new Intent(OptionsActivity.this, MainActivity.class);
//        toMainIntent.putExtra(MainActivity.IS_TEMP_ON, isTempSwitchOn);
//        toMainIntent.putExtra(MainActivity.IS_WIND_ON, isWindSwitchOn);
//        setResult(RESULT_OK, toMainIntent);
//    }
//
//    private void showBackBtn() {
//        try {
//            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
//        } catch (NullPointerException exc) {
//            exc.printStackTrace();
//        }
//    }
}