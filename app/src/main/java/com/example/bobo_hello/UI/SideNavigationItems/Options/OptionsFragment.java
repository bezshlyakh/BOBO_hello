package com.example.bobo_hello.UI.SideNavigationItems.Options;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.inputmethodservice.Keyboard;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import com.example.bobo_hello.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.security.Key;

public class OptionsFragment extends Fragment {

    private final String ID_METRIC_RB = "IDMetricRB", METRIC_CHOSEN = "metricChosen";
    private final String ID_LOC_RB = "IDlocRB", LOC_CHOSEN = "locationChosen", NO_DEF_CITY = "no city chosen";
    private FloatingActionButton fabAddCity;
    private RadioGroup radioGroupMetric, radioGroupLocation;
    private EditText cityEnterTV;
    private TextView defaultCityTV;
    private final String CELCIUM_METRIC = "metric";
    private final String FARINGATE_METRIC = "imperial";
    private String chosenMetric;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_options, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        setMetricOptions();
        setOnMetricChangeListener();
        setLocationOptions();
        setOnLocChangeListener();
    }

    private void initViews(View view){
        radioGroupMetric = view.findViewById(R.id.RGMetricSystem);
        radioGroupLocation = view.findViewById(R.id.RGStartLocation);
        cityEnterTV = view.findViewById(R.id.customCityEnter);
        defaultCityTV = view.findViewById(R.id.customCityTextView);
        defaultCityTV.setVisibility(View.INVISIBLE);
        fabAddCity =  view.findViewById(R.id.FABdefaultCity);
    }

    private void setMetricOptions(){
        int checkedId = readFromPreference().getInt(ID_METRIC_RB, R.id.RBMetric);
        RadioButton checkedRadioButton = (RadioButton)radioGroupMetric.findViewById(checkedId);
        radioGroupMetric.clearCheck();
        checkedRadioButton.setChecked(true);
    }

    private void setLocationOptions(){
        int checkedId = readFromPreference().getInt(ID_LOC_RB, R.id.RBCurrLocOnStart);
        RadioButton checkedRadioButton = (RadioButton)radioGroupLocation.findViewById(checkedId);
        radioGroupLocation.clearCheck();
        checkedRadioButton.setChecked(true);
        if(checkedId == R.id.RBCurrLocOnStart){
            onRBCurrLocClick();
        } else {
            onRBCustomLocClick();
            String defCity = readFromPreference().getString(LOC_CHOSEN, NO_DEF_CITY);
            defaultCityTV.setText(defCity);
            onFABaddClick();
        }
    }

    private void setOnMetricChangeListener(){
        radioGroupMetric.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @SuppressLint("NonConstantResourceId")
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case -1:
                        Toast.makeText(requireContext(), "nothing chosen",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.RBMetric:
                        chosenMetric = CELCIUM_METRIC;
                        break;
                    case R.id.RBFaringate:
                        chosenMetric = FARINGATE_METRIC;
                        break;

                    default:
                        break;
                }
            }
        });
    }

    private void setOnLocChangeListener(){
        radioGroupLocation.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @SuppressLint("NonConstantResourceId")
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case -1:
                        Toast.makeText(requireContext(), "nothing chosen",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.RBCurrLocOnStart:
                        onRBCurrLocClick();
                        break;
                    case R.id.RBCustomLocOnStart:
                        onRBCustomLocClick();
                        onFABaddClick();
                        break;

                    default:
                        break;
                }
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        saveOptionsToPreference();
    }

    private void onRBCurrLocClick(){
        cityEnterTV.setVisibility(View.INVISIBLE);
        defaultCityTV.setVisibility(View.INVISIBLE);
        fabAddCity.setVisibility(View.INVISIBLE);
    }

    private void onRBCustomLocClick(){
        cityEnterTV.setVisibility(View.VISIBLE);
        fabAddCity.setVisibility(View.VISIBLE);
        defaultCityTV.setVisibility(View.VISIBLE);
    }

    private void onFABaddClick(){
        fabAddCity.setOnClickListener(view -> {
            defaultCityTV.setText(cityEnterTV.getText());
            cityEnterTV.clearFocus();
            cityEnterTV.setText("");
        });
    }

    private void saveOptionsToPreference() {
        SharedPreferences optionsPref = requireActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = optionsPref.edit();

        editor.putInt(ID_METRIC_RB, radioGroupMetric.getCheckedRadioButtonId());
        editor.putString(METRIC_CHOSEN, chosenMetric);

        editor.putInt(ID_LOC_RB, radioGroupLocation.getCheckedRadioButtonId());
        editor.putString(LOC_CHOSEN, defaultCityTV.getText().toString());

        editor.apply();
    }

    private SharedPreferences readFromPreference() {
        return requireActivity().getPreferences(Context.MODE_PRIVATE);
    }
}
