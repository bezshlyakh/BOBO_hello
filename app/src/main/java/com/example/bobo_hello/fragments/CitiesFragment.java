package com.example.bobo_hello.fragments;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.Objects;

import com.example.bobo_hello.MainActivity;
import com.example.bobo_hello.OptionsContainer;
import com.example.bobo_hello.R;
import com.example.bobo_hello.WeatherActivity;
import com.example.bobo_hello.WeatherInfoContainer;

public class CitiesFragment extends Fragment {

    private ListView listView;
    private TextView emptyTextView;
    private boolean tempOn = true, windOn = true;
    private boolean isExistWeatherDisp;
    private int currentPosition = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cities, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        initList();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        isExistWeatherDisp = getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE;
        if (savedInstanceState != null) {
            currentPosition = savedInstanceState.getInt("CurrentCity", 0);
        }
        if (isExistWeatherDisp) {
            listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            showWeatherDisp();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt("CurrentCity", currentPosition);
        super.onSaveInstanceState(outState);
    }

    public void update(OptionsContainer optionsContainer) {
        tempOn = optionsContainer.isTempOn;
        windOn = optionsContainer.isWindOn;
    }

    private void initViews(View view) {
        listView = view.findViewById(R.id.listOfCities);
        emptyTextView = view.findViewById(R.id.cities_list_empty_view);
    }

    private void initList() {
        ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(Objects.requireNonNull(getActivity()), R.array.cities,
                        android.R.layout.simple_list_item_activated_1);
        listView.setAdapter(adapter);

        listView.setEmptyView(emptyTextView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currentPosition = position;
                showWeatherDisp();
            }
        });
    }

    private void showWeatherDisp() {
        if (isExistWeatherDisp) {
            listView.setItemChecked(currentPosition, true);
            WeatherInfoFragment detail = (WeatherInfoFragment)
                    Objects.requireNonNull(getFragmentManager()).findFragmentById(R.id.weather_info);

            if (detail == null || detail.getIndex() != currentPosition) {
                detail = WeatherInfoFragment.create(getWeatherContainer());
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.weather_info, detail);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.addToBackStack("Some_Key");
                ft.commit();
            }
        } else {
            Intent intent = new Intent();
            intent.setClass(Objects.requireNonNull(getActivity()), WeatherActivity.class);
            intent.putExtra("index", getWeatherContainer());
            startActivity(intent);
        }
    }

    private WeatherInfoContainer getWeatherContainer() {
        String[] cities = getResources().getStringArray(R.array.cities);
        WeatherInfoContainer container = new WeatherInfoContainer();
        container.position = currentPosition;
        container.cityName = cities[currentPosition];
        container.isTempOn = tempOn;
        container.isWindOn = windOn;
        return container;
    }
}
