package com.example.bobo_hello.UI.SideNavigationItems.History;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bobo_hello.R;

import java.util.ArrayList;
import java.util.List;

public class HistoryRecyclerAdapter extends RecyclerView.Adapter<HistoryRecyclerAdapter.ViewHolder> {
    private List<CityAndTemperatureResult> cities;

    public HistoryRecyclerAdapter(WeatherHisSource dataSource) {
        cities = dataSource.getData();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.city_his_item, parent,
                false);
        return new ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CityAndTemperatureResult data = cities.get(position);
        holder.setCitiesText(data.cityName);
        holder.setTempText(data.temperature);
        holder.setDateTextView(data.date);
    }

    @Override
    public int getItemCount() {
        System.out.println(cities.size());
        return cities.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView cityTextView;
        private TextView tempTextView;
        private TextView dateTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cityTextView = itemView.findViewById(R.id.cityHisName);
            tempTextView = itemView.findViewById(R.id.lastTemperature);
            dateTextView = itemView.findViewById(R.id.lastUpdateData);
        }

        void setCitiesText(String text) {
            cityTextView.setText(text);
        }

        void setTempText(String text) {
            tempTextView.setText(text);
        }

        void setDateTextView(String text) {
            dateTextView.setText(text);
        }
    }
}
