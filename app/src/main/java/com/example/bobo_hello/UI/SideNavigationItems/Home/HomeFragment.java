package com.example.bobo_hello.UI.SideNavigationItems.Home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.example.bobo_hello.R;
import com.example.bobo_hello.Utils.OptionsContainer;
import com.example.bobo_hello.Utils.WeatherInfoContainer;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class HomeFragment extends Fragment {

    private boolean tempOn = true, windOn = true;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

//    @Override
//    public void onActivityCreated(Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        setFragment(new CitiesFragment());
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
        public void onOptionsContainer(OptionsContainer optionsContainer){
        tempOn = optionsContainer.isTempOn;
        windOn = optionsContainer.isWindOn;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
        public void onWeatherContainer(WeatherInfoContainer weatherInfoContainer){
        weatherInfoContainer.isTempOn = tempOn;
        weatherInfoContainer.isWindOn = windOn;
        setFragment(WeatherInfoFragment.create(weatherInfoContainer));
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.homeFragmentContainer, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

}
