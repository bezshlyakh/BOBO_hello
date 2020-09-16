package com.example.bobo_hello;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
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
import com.example.bobo_hello.Utils.EventCityChanged;
import com.example.bobo_hello.Utils.IRVOnItemClick;
import com.example.bobo_hello.Utils.RecyclerDataAdapter;
import org.greenrobot.eventbus.EventBus;
import java.util.ArrayList;

public class CitiesFindDialogFragment extends DialogFragment implements IRVOnItemClick {

    private RecyclerView recyclerView;

    @SuppressLint("InflateParams")
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dialog_cities, null);
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
        saveCityToPreference(cityItem);
        EventBus.getDefault().post(new EventCityChanged(cityItem));
        dismiss();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
    }

    private View.OnClickListener listener = view -> {
        dismiss();
        String currentCity = ((EditText)view).getText().toString();
        ((EditText) view).setText("");
        saveCityToPreference(currentCity);
        EventBus.getDefault().post(new EventCityChanged(currentCity));
    };

    private void saveCityToPreference(String currentCity) {
        SharedPreferences optionsPref = requireActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = optionsPref.edit();
        editor.putString("currentCity", currentCity);
        editor.apply();
    }
}
