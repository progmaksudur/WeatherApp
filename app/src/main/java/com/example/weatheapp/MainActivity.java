package com.example.weatheapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private TextView cityName_Tv;
    private EditText cityName_EditText;
    private ImageButton search_Button;
    private TextView weather_temp_Tv;
    private ImageView weather_icon_Iv;
    private RecyclerView weather_report_Rv;
    private LocationManager locationManager;
    private String cityName="Dhaka";
    private int PERMISSION_CODE=1;
    private ArrayList<Weather_Request_model> weather_request_modelArrayList;
    private WeatherAdapterRv weatherAdapterRv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        cityName_Tv=findViewById(R.id.city_Name);
        cityName_EditText=findViewById(R.id.city_name_search);
        search_Button=findViewById(R.id.search_button);
        weather_temp_Tv=findViewById(R.id.weather_temp);
        weather_icon_Iv=findViewById(R.id.weather_icon);
        weather_report_Rv=findViewById(R.id.weather_report_view);
        weather_request_modelArrayList= new ArrayList<>();

        weatherAdapterRv= new WeatherAdapterRv(this,weather_request_modelArrayList);


        locationManager=(LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_CODE);
        }


        Location location= locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        //Toast.makeText(this, ""+location.getLongitude()+""+location.getLatitude(), Toast.LENGTH_LONG).show();
       // cityName=getCityName(location.getLongitude(),location.getLatitude());

        getWeatherReport(cityName);
        cityName_Tv.setText(cityName);

        search_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String city=cityName_EditText.getText().toString();
                if(city.isEmpty()){
                    cityName_EditText.setError("Enter City Name");
                    cityName_EditText.requestFocus();
                }
                else{
                    cityName_Tv.setText(city);
                    getWeatherReport(city);

                }
            }
        });
        weather_report_Rv.setAdapter(weatherAdapterRv);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==PERMISSION_CODE){
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(this, "Provide Permission", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private String getCityName(double longitude, double latitude){
          String cityName="Not found";
        Geocoder geocoder= new Geocoder(getBaseContext(),Locale.getDefault());
        try {
            List<Address> addresses= geocoder.getFromLocation(latitude,longitude,10);
            for (Address address:addresses){
                if(address!=null){
                    String city=address.getLocality();
                    if(city!=null&& !city.equals("")){
                        cityName=city;
                        Toast.makeText(this, ""+cityName, Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(this, "City Not Found", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }

        return cityName;
    }
    public void getWeatherReport(String cityName){
        String Url="http://api.weatherapi.com/v1/forecast.json?key=fa2c093f924d40d8a88181524221004&q="+cityName+"&days=1&aqi=no&alerts=no";
        Toast.makeText(this, ""+Url, Toast.LENGTH_LONG).show();
        cityName_Tv.setText(cityName);
        RequestQueue requestQueue= Volley.newRequestQueue(MainActivity.this);

        JsonObjectRequest jsonObjectRequest= new JsonObjectRequest(Request.Method.GET,Url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                weather_request_modelArrayList.clear();
                weather_report_Rv.setVisibility(View.VISIBLE);
                Toast.makeText(MainActivity.this, "Enter", Toast.LENGTH_SHORT).show();
                try{
                    String temp=response.getJSONObject("current").getString("temp_c");
                    weather_temp_Tv.setText(temp+"°C");
                    int isday = response.getJSONObject("current").getInt("is_day");
                    if(isday==1){
                        getWindow().findViewById(R.id.background).setBackgroundResource(R.drawable.weather_day);
                    }
                    else {
                        getWindow().findViewById(R.id.background).setBackgroundResource(R.drawable.weather_night);
                    }
                    String condition=response.getJSONObject("current").getJSONObject("condition").getString("text");
                    String condition_icon=response.getJSONObject("current").getJSONObject("condition").getString("icon");
                    Picasso.get().load("http:".concat(condition_icon)).into(weather_icon_Iv);

                    JSONObject forecastObj=response.getJSONObject("forecast");
                    JSONObject forecastO=forecastObj.getJSONArray("forecastday").getJSONObject(0);
                    JSONArray hourArray=forecastO.getJSONArray("hour");

                    for(int i=0;i<hourArray.length();i++){
                        JSONObject hourObj=hourArray.getJSONObject(i);
                        String time=hourObj.getString("time");
                        Toast.makeText(MainActivity.this, ""+time, Toast.LENGTH_SHORT).show();
                        String temper=hourObj.getString("temp_c");
                        Toast.makeText(MainActivity.this, ""+temper, Toast.LENGTH_SHORT).show();
                        String img=hourObj.getJSONObject("condition").getString("icon");
                        Toast.makeText(MainActivity.this, ""+img, Toast.LENGTH_SHORT).show();
                        String wind=hourObj.getString("wind_kph");
                        Toast.makeText(MainActivity.this, ""+wind, Toast.LENGTH_SHORT).show();
                        weather_request_modelArrayList.add(new Weather_Request_model(time,temper,img,wind));
                    }
                    weather_report_Rv.setAdapter(weatherAdapterRv);
                    weatherAdapterRv.notifyDataSetChanged();




                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, " "+error, Toast.LENGTH_LONG).show();
                System.out.println(error);
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

}