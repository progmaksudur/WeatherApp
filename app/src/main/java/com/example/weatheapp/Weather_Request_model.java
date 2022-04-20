package com.example.weatheapp;

public class Weather_Request_model {
        private String time;
        private String tampreture;
        private String icon;
        private String wind;

    public Weather_Request_model(String time, String tampreture, String icon, String wind) {
        this.time = time;
        this.tampreture = tampreture;
        this.icon = icon;
        this.wind = wind;

    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTampreture() {
        return tampreture;
    }

    public void setTampreture(String tampreture) {
        this.tampreture = tampreture;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getWind() {
        return wind;
    }

    public void setWind(String wind) {
        this.wind = wind;
    }


}
