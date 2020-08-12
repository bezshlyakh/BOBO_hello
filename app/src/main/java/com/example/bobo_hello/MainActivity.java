package com.example.bobo_hello;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class MainActivity extends AppCompatActivity {
    private Button submitButton;
    private RadioButton chosenCityRBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        onBtnClickAction();
    }

    private void initView(){
        submitButton = findViewById(R.id.submitButton);
        RadioGroup rgCities = findViewById(R.id.groupOfCities);
        chosenCityRBtn = findViewById(rgCities.getCheckedRadioButtonId());
    }

    private void onBtnClickAction(){
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //String city = (String) chosenCityRBtn.getText();
            }
        });
    }

}