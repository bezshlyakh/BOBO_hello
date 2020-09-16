package com.example.bobo_hello.Utils;

import android.annotation.SuppressLint;
import android.util.Log;
import com.example.bobo_hello.AppForDB;
import com.example.bobo_hello.UI.SideNavigationItems.History.CityEntity;
import com.example.bobo_hello.UI.SideNavigationItems.History.TemperatureEntity;
import com.example.bobo_hello.UI.SideNavigationItems.History.WeatherHisSource;
import com.example.bobo_hello.UI.SideNavigationItems.History.WeatherHistoryDao;
import com.example.bobo_hello.weatherModel.WeatherRequest;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import javax.net.ssl.HttpsURLConnection;

public class OneDayWeatherConnector {
    private static final String TAG = "WEATHER";
    private static final String WEATHER_URL = "https://api.openweathermap.org/data/2.5/weather?";
    private static final String API_KEY = "3aa391c929cfb72a03bebcdf69fc4c8a";
    private String cityName;
    private String cityId;
    private String temperature;
    private String pressure;
    private String humidity;
    private String windSpeed;
    private int windDirection;
    private String icon;
    private WeatherHisSource weatherHisSource;
    private CityEntity cityEntity;
    private TemperatureEntity temperatureEntity;


    public OneDayWeatherConnector(Classifier classifier, String city) {
        this.cityName = city;
        this.cityId = classifier.getCityID(city);
        WeatherHistoryDao weatherHistoryDao = AppForDB
                .getInstance()
                .getEducationDao();
        this.weatherHisSource = new WeatherHisSource(weatherHistoryDao);
        this.cityEntity = new CityEntity();
        this.temperatureEntity = new TemperatureEntity();

        try {
            if(!cityName.equals("")){
                final URL uri = new URL(WEATHER_URL + "q=" + cityName + "&units=metric&appid=" + API_KEY);
                Thread th = new Thread(new Runnable() {
                    public void run() {
                        HttpsURLConnection urlConnection = null;
                        try {
                            urlConnection = (HttpsURLConnection) uri.openConnection();
                            urlConnection.setRequestMethod("GET");
                            urlConnection.setReadTimeout(10000);
                            BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                            String result = getLines(in);
                            Gson gson = new Gson();
                            final WeatherRequest weatherRequest = gson.fromJson(result, WeatherRequest.class);
                            setWeatherInfo(weatherRequest);
                            saveWeatherInfoToDB();
                        } catch (Exception e) {
                            Log.e(TAG, "Fail connection", e);
                            e.printStackTrace();
                        } finally {
                            if (null != urlConnection) {
                                urlConnection.disconnect();
                            }
                        }
                    }
                });
                th.start();
                th.join();
            }
        } catch (Exception e) {
            Log.e(TAG, "Fail URI", e);
            e.printStackTrace();
        }
    }

    private String getLines(BufferedReader reader) {
        StringBuilder rawData = new StringBuilder(1024);
        String tempVariable;

        while (true) {
            try {
                tempVariable = reader.readLine();
                if (tempVariable == null) break;
                rawData.append(tempVariable).append("\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return rawData.toString();
    }

    private void setWeatherInfo(WeatherRequest weatherRequest) {
        temperature = String.format(Locale.getDefault(), "%.1f", weatherRequest.getMain().getTemp());
        pressure = String.format(Locale.getDefault(), "%d", weatherRequest.getMain().getPressure());
        humidity = String.format(Locale.getDefault(), "%d", weatherRequest.getMain().getHumidity());
        windSpeed = String.format(Locale.getDefault(), "%.1f", weatherRequest.getWind().getSpeed());
        windDirection = weatherRequest.getWind().getDeg();
        icon = String.valueOf(weatherRequest.getWeather()[0].getIcon());
    }

    private void saveWeatherInfoToDB() {
        cityEntity.cityName = cityName;
        weatherHisSource.addCity(cityEntity);
        temperatureEntity.cityId = cityEntity.id;
        temperatureEntity.temperature = temperature;
        temperatureEntity.date = getCurrentTime();
        weatherHisSource.addTemp(temperatureEntity);
    }

    private String getCurrentTime(){
        @SuppressLint("SimpleDateFormat") DateFormat date = new SimpleDateFormat("MMM dd yyyy, h:mm");
        return date.format(Calendar.getInstance().getTime());
    }

    public String getCityId(String city) {
        return cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public String getTemperature() {
        return temperature;
    }

    public String getPressure() {
        return pressure;
    }

    public String getHumidity() {
        return humidity;
    }

    public String getWindSpeed() {
        return windSpeed;
    }

    public int getWindDirection() {
        return windDirection;
    }

    public String getIcon() {
        return icon;
    }
}
