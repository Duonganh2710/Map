package com.example.googlemap;

import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class DirectionFinder {
    private String direction_URL_API = "https://maps.googleapis.com/maps/api/directions/json?";
    private String googleMapsApi = "AIzaSyBlR-FpZCu14D0Bn5nISExUi3V9VqDld_Q";
    private Context context;
    private String origin , destination;

    public DirectionFinder(Context context, String origin, String dentination) {
        this.context = context;
        this.origin = origin;
        this.destination = dentination;
    }
    public void execute()throws UnsupportedEncodingException{
         new DownloadRawData().execute(createUrl());
    }
    private String createUrl()throws UnsupportedEncodingException{
        String urlOrigin = URLEncoder.encode(origin , "utf-8");
        String urlDestination = URLEncoder.encode(destination, "utf-8");
        return direction_URL_API + "origin=" + urlOrigin + "&destination=" + urlDestination + "&key=" + googleMapsApi;
    }
    public class DownloadRawData extends AsyncTask<String , Void , String>{
        @Override
        protected String doInBackground(String... strings) {
            StringBuffer buffer = new StringBuffer();
            try {
                URL url = new URL(strings[0]);
                InputStream inputStream = url.openConnection().getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null){
                    buffer.append(line + "\n");
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }
}
