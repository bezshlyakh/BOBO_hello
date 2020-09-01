package com.example.bobo_hello.UI.SideNavigationItems.Home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Objects;
import com.example.bobo_hello.Utils.Classifier;
import com.example.bobo_hello.Utils.IRVOnItemClick;
import com.example.bobo_hello.Utils.OneDayWeatherConnector;
import com.example.bobo_hello.Utils.OptionsContainer;
import com.example.bobo_hello.R;
import com.example.bobo_hello.Utils.RecyclerDataAdapter;
import com.example.bobo_hello.WeatherActivity;
import com.example.bobo_hello.Utils.WeatherInfoContainer;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class CitiesFragment extends Fragment implements IRVOnItemClick {
    private RecyclerView recyclerView;
    //private boolean tempOn = true, windOn = true;
    private int currentPosition = 0;
    private String currentCity = "";
    private Classifier classifier;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cities, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        classifier = new Classifier();
        setupCitiesRecycler(classifier);
        if (savedInstanceState != null) {
            currentPosition = savedInstanceState.getInt("CurrentPos", 0);
            currentCity = savedInstanceState.getString("CurrentCity");
        }
    }

//    @Override
//    public void onActivityCreated(Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        if (savedInstanceState != null) {
//            currentPosition = savedInstanceState.getInt("CurrentPos", 0);
//            currentCity = savedInstanceState.getString("CurrentCity");
//        }
//    }
//
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt("CurrentPos", currentPosition);
        outState.putString("CurrentCity", currentCity);
        super.onSaveInstanceState(outState);
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        EventBus.getDefault().register(this);
//    }
//
//    @Override
//    public void onStop() {
//        EventBus.getDefault().unregister(this);
//        super.onStop();
//    }
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onOptionsContainer(OptionsContainer optionsContainer){
//        tempOn = optionsContainer.isTempOn;
//        windOn = optionsContainer.isWindOn;
//    }

    private void initViews(View view) {
        recyclerView = view.findViewById(R.id.cities_menu);
    }

    private void setupCitiesRecycler(Classifier classifier) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        ArrayList<String> listData = classifier.getListOfCities();
        RecyclerDataAdapter adapter = new RecyclerDataAdapter(listData, this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

//    private void showWeatherDisp(OneDayWeatherConnector connector) {
//        if (isExistWeatherDisp) {
//            WeatherInfoFragment detail = (WeatherInfoFragment)
//                    Objects.requireNonNull(getFragmentManager()).findFragmentById(R.id.weather_info);
//
//            if (detail == null || detail.getIndex() != currentPosition) {
//                detail = WeatherInfoFragment.create(getWeatherContainer(connector));
//                FragmentTransaction ft = getFragmentManager().beginTransaction();
//                ft.replace(R.id.weather_info, detail);
//                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//                ft.addToBackStack("Some_Key");
//                ft.commit();
//            }
//        } else {
//            Intent intent = new Intent();
//            intent.setClass(Objects.requireNonNull(getActivity()), WeatherActivity.class);
//            intent.putExtra("index", getWeatherContainer(connector));
//            startActivity(intent);
//        }
//    }

    private WeatherInfoContainer getWeatherContainer(OneDayWeatherConnector connector) {
        WeatherInfoContainer container = new WeatherInfoContainer();
        container.position = currentPosition;
        container.cityName = currentCity;
        container.temperature = connector.getTemperature();
        container.windSpeed = connector.getWindSpeed();
        container.icon = connector.getIcon();
//        container.isTempOn = tempOn;
//        container.isWindOn = windOn;
        container.classifier = classifier;
        return container;
    }

    @Override
    public void onItemClicked(String cityItem, int position) {
        currentPosition = position;
        currentCity = cityItem;
        EventBus.getDefault().post(getWeatherContainer(getWeatherInfoFromServer(currentCity)));
        //showWeatherDisp(getWeatherInfoFromServer(currentCity));
    }

    private OneDayWeatherConnector getWeatherInfoFromServer(String cityItem) {
        return new OneDayWeatherConnector(classifier, cityItem);
    }
}
