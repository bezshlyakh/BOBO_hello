package com.example.bobo_hello.UI.SideNavigationItems.History;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bobo_hello.AppForDB;
import com.example.bobo_hello.R;
import com.example.bobo_hello.Utils.EventCityChanged;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class HistoryFragment extends Fragment {

    private RecyclerView recyclerView;
    private HistoryRecyclerAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        setupHisRecycler();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    private void initViews(View view) {
        recyclerView = view.findViewById(R.id.cities_his_list);
    }

    private void setupHisRecycler() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        adapter = new HistoryRecyclerAdapter(getWeatherSource());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private  void updateHisRecycler(){
        recyclerView.swapAdapter(new HistoryRecyclerAdapter(getWeatherSource()), false);
    }

    private WeatherHisSource getWeatherSource(){
        WeatherHistoryDao weatherHistoryDao = AppForDB
                .getInstance()
                .getWeatherDao();
        return new WeatherHisSource(weatherHistoryDao);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCityChanged(EventCityChanged eventCityChanged){
        adapter.notifyDataSetChanged();
        updateHisRecycler();
    }

}
