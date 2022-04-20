package com.example.weatheapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class WeatherAdapterRv extends RecyclerView.Adapter<WeatherAdapterRv.ViewHolder> {
   private Context context;
   private ArrayList<Weather_Request_model> weather_request_modelArrayList;

    public WeatherAdapterRv(Context context, ArrayList<Weather_Request_model> weather_request_modelArrayList) {
        this.context = context;
        this.weather_request_modelArrayList = weather_request_modelArrayList;
    }


    @NonNull
    @Override
    public WeatherAdapterRv.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view= LayoutInflater.from(context).inflate(R.layout.weather_report_view,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherAdapterRv.ViewHolder holder, int position) {
        Weather_Request_model model= weather_request_modelArrayList.get(position);
        holder.temp_txv.setText(model.getTampreture()+"°C");
        Picasso.get().load("http:".concat(model.getIcon())).into(holder.cond_tvx);
        holder.wind_tvx.setText(model.getWind()+"Km/h");
        SimpleDateFormat input=new SimpleDateFormat("yyyy-MM-dd hh:mm");
        SimpleDateFormat output= new SimpleDateFormat( "hh:mm aa");
        try {
            Date t= input.parse(model.getTime());
            holder.time_tvx.setText(output.format(t));
        }catch (ParseException e){
            e.printStackTrace();
        }

        }

    @Override
    public int getItemCount() {
        return weather_request_modelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView temp_txv;
        private TextView wind_tvx;
        private TextView time_tvx;
        private ImageView cond_tvx;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            time_tvx = itemView.findViewById(R.id.weather_time_tvx);
            wind_tvx = itemView.findViewById(R.id.weather_wind_Tvx);
            temp_txv = itemView.findViewById(R.id.weather_Temp_Tvx);
            cond_tvx = itemView.findViewById(R.id.weather_icon_tvx);
        }
    }
}
