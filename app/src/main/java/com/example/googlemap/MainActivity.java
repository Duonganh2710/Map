package com.example.googlemap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap Map;
  //  private CurrentLocation currentLocation;
    private SearchView searchView;
    private double lan = 0 , lng = 0;
    private SupportMapFragment mapFragment;
    private Address address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.myMap);
        searchView = (SearchView) findViewById(R.id.searchview);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String locationSearchView = searchView.getQuery().toString();
                List<Address> addressList = null;
                if(locationSearchView != null || !locationSearchView.equals("")) {
                    Geocoder geocoder = new Geocoder(MainActivity.this);
                    try {
                        addressList = geocoder.getFromLocationName(locationSearchView, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    address = addressList.get(0);
                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                    Map.addMarker(new MarkerOptions().position(latLng).title(locationSearchView));
                    Map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                }
                 return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        //getCurrentLocation();
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
         Map = googleMap;
//        LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
//        googleMapmain.addMarker(new MarkerOptions().position(latLng).title(locationSearchView));
//        googleMapmain.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng , 10));
//        googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
    }
    private void initMap(){
        mapFragment = (SupportMapFragment) getSupportFragmentManager().
                findFragmentById(R.id.myMap);
        mapFragment.getMapAsync(this);
    }


}
