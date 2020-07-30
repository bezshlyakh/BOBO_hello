package com.example.bobo_hello;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private Button submitButton;
    private TextView getCityText;
    private TextView cityName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        onBtnClickAction();

    }

    private void initView(){
        submitButton = findViewById(R.id.submitButton);
        getCityText = findViewById(R.id.enterTextField);
        cityName = findViewById(R.id.cityOutput);
    }

    private void onBtnClickAction(){
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cityName.setText(getCityText.getText());
                getCityText.setText("");
            }
        });
    }

}