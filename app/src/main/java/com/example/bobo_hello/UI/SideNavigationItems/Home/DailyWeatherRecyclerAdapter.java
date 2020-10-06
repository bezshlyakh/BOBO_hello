package com.example.bobo_hello.UI.SideNavigationItems.Home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bobo_hello.R;
import com.example.bobo_hello.Utils.Classifier;
import com.example.bobo_hello.WeatherServices.WeatherInfoContainer;
import java.util.Arrays;
import java.util.List;

public class DailyWeatherRecyclerAdapter extends RecyclerView.Adapter<DailyWeatherRecyclerAdapter.ViewHolder>{

    private List<WeatherInfoContainer> weatherList;
    private final Classifier CLASSIFIRE;

    public DailyWeatherRecyclerAdapter(WeatherInfoContainer[] weatherSource, Classifier classifier) {
        weatherList = Arrays.asList(weatherSource);
        this.CLASSIFIRE = classifier;
    }

    @NonNull
    @Override
    public DailyWeatherRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.daily_weather_item, parent,
                false);
        return new DailyWeatherRecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DailyWeatherRecyclerAdapter.ViewHolder holder, int position) {
        WeatherInfoContainer data = weatherList.get(position);
        holder.setDateTextView(data.date);
        String iconName = CLASSIFIRE.getIconName(data.icon);
        holder.setWeatherImgView(iconName);
        holder.setTempText(data.temperature);
    }

    @Override
    public int getItemCount() {
        return weatherList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView dateTextView;
        private ImageView weatherImgView;
        private TextView tempTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dateTextView = itemView.findViewById(R.id.dateItemText);
            weatherImgView = itemView.findViewById(R.id.weatherItemImg);
            tempTextView = itemView.findViewById(R.id.tempItemText);
        }

        void setWeatherImgView(String iconName){
            int id = itemView.getResources()
                    .getIdentifier("com.example.bobo_hello:drawable/"
                            + iconName, null, null);
            weatherImgView.setImageResource(id);
        }

        void setTempText(String text) {
            tempTextView.setText(text);
        }

        void setDateTextView(String text) {
            dateTextView.setText(text);
        }
    }
}
