package com.example.bobo_hello.UI.SideNavigationItems.Options;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import com.example.bobo_hello.R;

public class OptionsFragment extends Fragment {

    private SwitchCompat tempSwitch, windSwitch;
    private final String IS_TEMP_ON = "isTempSwitchOn", IS_WIND_ON = "isWindSwitchOn" ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_options, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        setOptions();
    }

    private void initViews(View view){
        tempSwitch = view.findViewById(R.id.tempSwitch);
        windSwitch = view.findViewById(R.id.windSwitch);
    }

    public void setOptions(){
        tempSwitch.setChecked(readFromPreference().getBoolean(IS_TEMP_ON, true));
        windSwitch.setChecked(readFromPreference().getBoolean(IS_WIND_ON, true));
    }

    @Override
    public void onPause() {
        super.onPause();
        saveOptionsToPreference();
    }

    private void saveOptionsToPreference() {
        SharedPreferences optionsPref = requireActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = optionsPref.edit();
        editor.putBoolean(IS_TEMP_ON, tempSwitch.isChecked());
        editor.putBoolean(IS_WIND_ON, windSwitch.isChecked());
        editor.apply();
    }

    private SharedPreferences readFromPreference() {
        return requireActivity().getPreferences(Context.MODE_PRIVATE);
    }
}
