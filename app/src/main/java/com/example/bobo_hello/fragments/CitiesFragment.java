package com.example.bobo_hello.fragments;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import com.example.bobo_hello.IRVOnItemClick;
import com.example.bobo_hello.OptionsContainer;
import com.example.bobo_hello.R;
import com.example.bobo_hello.RecyclerDataAdapter;
import com.example.bobo_hello.WeatherActivity;
import com.example.bobo_hello.WeatherInfoContainer;

public class CitiesFragment extends Fragment implements IRVOnItemClick {
    private RecyclerView recyclerView;
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
        setupCitiesRecycler();
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
        recyclerView = view.findViewById(R.id.cities_menu);
    }

    private void setupCitiesRecycler() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        ArrayList<String> listData = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.cities)));
        RecyclerDataAdapter adapter = new RecyclerDataAdapter(listData, this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void showWeatherDisp() {
        if (isExistWeatherDisp) {
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

    @Override
    public void onItemClicked(String itemText, int position) {
        Toast.makeText(getContext(), itemText, Toast.LENGTH_SHORT).show();
        currentPosition = position;
        showWeatherDisp();
    }
}
