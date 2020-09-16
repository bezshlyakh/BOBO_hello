package com.example.bobo_hello;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bobo_hello.Utils.Classifier;
import com.example.bobo_hello.Utils.CoordConverter;
import com.example.bobo_hello.Utils.EventCityChanged;
import com.example.bobo_hello.Utils.IRVOnItemClick;
import com.example.bobo_hello.Utils.RecyclerDataAdapter;
import com.google.android.gms.maps.model.LatLng;

import org.greenrobot.eventbus.EventBus;
import java.util.ArrayList;

public class CitiesFindDialogFragment extends DialogFragment implements IRVOnItemClick {

    private RecyclerView recyclerView;
    private CoordConverter converter;

    @SuppressLint("InflateParams")
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dialog_cities, null);
    }

    @Override
    public void onStart() {
        super.onStart();
        Geocoder geocoder = new Geocoder(requireContext());
        converter = new CoordConverter(geocoder);
    }

    private void initViews(View view) {
        view.findViewById(R.id.inputFindCity).setOnClickListener(listener);
        Classifier classifier = new Classifier();
        recyclerView = view.findViewById(R.id.cities_menu);
        setupCitiesRecycler(classifier);
    }

    private void setupCitiesRecycler(Classifier classifier) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        ArrayList<String> listData = classifier.getListOfCities();
        RecyclerDataAdapter adapter = new RecyclerDataAdapter(listData, this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClicked(String cityItem) {
        LatLng cityCoord = null;
        try {
            cityCoord = converter.getCoordinatesByCityName(cityItem);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assert cityCoord != null;
        saveCoordToPreference(cityCoord);
        EventBus.getDefault().post(new EventCityChanged(cityCoord));
        dismiss();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
    }

    private final View.OnClickListener listener = view -> {
        dismiss();
        String currentCity = ((EditText)view).getText().toString();
        ((EditText) view).setText("");
        LatLng coord = null;
        try {
            coord = converter.getCoordinatesByCityName(currentCity);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assert coord != null;
        saveCoordToPreference(coord);
        EventBus.getDefault().post(new EventCityChanged(coord));
    };

    private void saveCoordToPreference(LatLng coord) {
        SharedPreferences optionsPref = requireActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = optionsPref.edit();
        editor.putFloat("currentLatitude", (float) coord.latitude);
        editor.putFloat("currentLongitude", (float) coord.longitude);
        editor.apply();
    }
}
