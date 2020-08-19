package com.example.bobo_hello;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.bobo_hello.fragments.CitiesFragment;

public class MainActivity extends AppCompatActivity {
    private Button optionsButton;
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

    @Override
    protected void onResume() {
        super.onResume();
        updateCitiesFragWithOptions();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(IS_TEMP_ON, tempOn);
        outState.putBoolean(IS_WIND_ON, windOn);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        tempOn = savedInstanceState.getBoolean(IS_TEMP_ON);
        windOn = savedInstanceState.getBoolean(IS_WIND_ON);
    }

    private void initView(){
        optionsButton = findViewById(R.id.optionsBtn);
    }

    private void onOptionsBtnClickAction() {
        optionsButton.setOnClickListener(view -> {
            Intent toOptionsDispIntent = new Intent(MainActivity.this, OptionsActivity.class);
            startActivityForResult(toOptionsDispIntent, REQUEST_CODE);
        });
    }

    private void updateCitiesFragWithOptions() {
        CitiesFragment fragment = new CitiesFragment();
        fragment.update(createOptionContainer());
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.cities, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.addToBackStack("Some_Key_2");
        ft.commit();
    }

    private OptionsContainer createOptionContainer() {
        OptionsContainer container = new OptionsContainer();
        container.isTempOn = tempOn;
        container.isWindOn = windOn;
        return container;
    }


}