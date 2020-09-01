package com.example.bobo_hello.UI.SideNavigationItems.Options;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import com.example.bobo_hello.Utils.OptionsContainer;
import com.example.bobo_hello.R;
import org.greenrobot.eventbus.EventBus;

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
        if(savedInstanceState!= null){
            setOptions(savedInstanceState);
        } else {
            tempSwitch.setChecked(true);
            windSwitch.setChecked(true);
        }
    }

    private void initViews(View view){
        tempSwitch = view.findViewById(R.id.tempSwitch);
        windSwitch = view.findViewById(R.id.windSwitch);
    }

    public void setOptions(@Nullable Bundle savedInstanceState){
        assert savedInstanceState != null;
        tempSwitch.setChecked(savedInstanceState.getBoolean(IS_TEMP_ON));
        windSwitch.setChecked(savedInstanceState.getBoolean(IS_WIND_ON));
    }

    @Override
    public void onPause() {
        super.onPause();
        OptionsContainer optionsContainer = new OptionsContainer();
        optionsContainer.isWindOn = windSwitch.isChecked();
        optionsContainer.isTempOn = tempSwitch.isChecked();
        EventBus.getDefault().post(optionsContainer);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putBoolean(IS_TEMP_ON, tempSwitch.isChecked());
        outState.putBoolean(IS_WIND_ON, windSwitch.isChecked());
        super.onSaveInstanceState(outState);
    }
}
