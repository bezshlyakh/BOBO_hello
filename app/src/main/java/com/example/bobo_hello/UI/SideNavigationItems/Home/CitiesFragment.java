//package com.example.bobo_hello.UI.SideNavigationItems.Home;
//
//import android.content.Context;
//import android.content.SharedPreferences;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//import java.util.ArrayList;
//import com.example.bobo_hello.Utils.Classifier;
//import com.example.bobo_hello.Utils.IRVOnItemClick;
//import com.example.bobo_hello.Utils.OneDayWeatherConnector;
//import com.example.bobo_hello.R;
//import com.example.bobo_hello.Utils.RecyclerDataAdapter;
//import com.example.bobo_hello.Utils.WeatherInfoContainer;
//import org.greenrobot.eventbus.EventBus;
//
//public class CitiesFragment extends Fragment implements IRVOnItemClick {
////    private RecyclerView recyclerView;
////    private String currentCity = "";
////    private Classifier classifier;
////    private final String LAST_CITY = "lastCity";
////
////    @Override
////    public View onCreateView(LayoutInflater inflater, ViewGroup container,
////                             Bundle savedInstanceState) {
////        return inflater.inflate(R.layout.fragment_cities, container, false);
////    }
////
////    @Override
////    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
////        super.onViewCreated(view, savedInstanceState);
////        initViews(view);
////        classifier = new Classifier();
////        setupCitiesRecycler(classifier);
////        currentCity = readFromPreference().getString(LAST_CITY, "MOSCOW");
////    }
////
////    @Override
////    public void onPause() {
////        super.onPause();
////        saveCityToPreference();
////    }
////
////    private void initViews(View view) {
////        recyclerView = view.findViewById(R.id.cities_menu);
////    }
////
////    private void setupCitiesRecycler(Classifier classifier) {
////        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
////        ArrayList<String> listData = classifier.getListOfCities();
////        RecyclerDataAdapter adapter = new RecyclerDataAdapter(listData, this);
////        recyclerView.setLayoutManager(layoutManager);
////        recyclerView.setAdapter(adapter);
////    }
////
////    private WeatherInfoContainer getWeatherContainer(OneDayWeatherConnector connector) {
////        WeatherInfoContainer container = new WeatherInfoContainer();
////        container.cityName = currentCity;
////        container.temperature = connector.getTemperature();
////        container.windSpeed = connector.getWindSpeed();
////        container.icon = connector.getIcon();
////        container.classifier = classifier;
////        return container;
////    }
////
////    @Override
////    public void onItemClicked(String cityItem) {
////        currentCity = cityItem;
////        EventBus.getDefault().post(getWeatherContainer(getWeatherInfoFromServer(currentCity)));
////    }
////
////    private OneDayWeatherConnector getWeatherInfoFromServer(String cityItem) {
////        return new OneDayWeatherConnector(classifier, cityItem);
////    }
////
////    private void saveCityToPreference() {
////        SharedPreferences optionsPref = requireActivity().getPreferences(Context.MODE_PRIVATE);
////        SharedPreferences.Editor editor = optionsPref.edit();
////        editor.putString(LAST_CITY, currentCity);
////        editor.apply();
////    }
////
////    private SharedPreferences readFromPreference() {
////        return requireActivity().getPreferences(Context.MODE_PRIVATE);
////    }
////
//}
