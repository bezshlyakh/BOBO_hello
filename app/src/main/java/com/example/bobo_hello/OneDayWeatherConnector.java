package com.example.bobo_hello;

import android.util.Log;
import com.example.bobo_hello.weatherModel.WeatherRequest;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;

public class OneDayWeatherConnector {
    private static final String TAG = "WEATHER";
    private static final String WEATHER_URL = "https://api.openweathermap.org/data/2.5/weather?";
    private static final String API_KEY = "3aa391c929cfb72a03bebcdf69fc4c8a";
    private String cityId;
    private String temperature;
    private String pressure;
    private String humidity;
    private String windSpeed;
    private String icon;

    public OneDayWeatherConnector(Classifier classifier, String city) {

        this.cityId = classifier.getCityID(city);
        try {
            if(cityId != null){
                final URL uri = new URL(WEATHER_URL + "id=" + cityId + "&units=metric&appid=" + API_KEY);
                Thread th = new Thread(new Runnable() {
                    public void run() {
                        HttpsURLConnection urlConnection = null;
                        try {
                            System.out.println("Thread started");
                            urlConnection = (HttpsURLConnection) uri.openConnection();
                            urlConnection.setRequestMethod("GET");
                            urlConnection.setReadTimeout(10000);
                            BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                            String result = getLines(in);
                            System.out.println("Result is " + result);
                            Gson gson = new Gson();
                            final WeatherRequest weatherRequest = gson.fromJson(result, WeatherRequest.class);
                            setWeatherInfo(weatherRequest);
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
        icon = String.valueOf(weatherRequest.getWeather()[0].getIcon());
    }

    public String getCityId(String city) {
        return cityId;
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

    public String getIcon() {
        return icon;
    }
}
