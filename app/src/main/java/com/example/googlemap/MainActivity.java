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
import android.view.View;
import android.widget.ImageButton;
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
    private CurrentLocation currentLocation;
    private SearchView searchView;
    private ImageButton imbLocationCurrent;
    private double lan = 0 , lng = 0;
    private SupportMapFragment mapFragment;
    private Address address;
    private String locationSearchView = new String();
    private MarkerOptions place1 , place2;
    private Polyline currentPolyline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AnhXa();
        ClickIcon();
        place1 = new MarkerOptions().position(new LatLng(currentLocation.getLocation().getLatitude() , currentLocation.getLocation().getLongitude())).title("Location Current Divice");
        place2 = new MarkerOptions().position(new LatLng(address.getLatitude() , address.getLongitude())).title(locationSearchView);

        mapFragment.getMapAsync(this);
    }
    private void AnhXa(){
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.myMap);
        imbLocationCurrent = (ImageButton) findViewById(R.id.imageButtonLocationCurrent);
        searchView = (SearchView) findViewById(R.id.searchview);
        currentLocation = new CurrentLocation(getApplicationContext());
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Map = googleMap;
       LatLng latLng = new LatLng(lan, lng);
       Map.addMarker(new MarkerOptions().position(latLng).title(locationSearchView));
       Map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
       googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
    }
    private void ClickIcon(){
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                locationSearchView = searchView.getQuery().toString();
                List<Address> addressList = null;
                if(locationSearchView != null || !locationSearchView.equals("")) {
                    Geocoder geocoder = new Geocoder(MainActivity.this);
                    try {
                        addressList = geocoder.getFromLocationName(locationSearchView, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    address = addressList.get(0);
                    lan = address.getLatitude();
                    lng = address.getLongitude();
                    onMapReady(Map);
                }
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        imbLocationCurrent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Location location = currentLocation.getLocation();
                if(location != null) {
                    lan = location.getLatitude();
                    lng = location.getLongitude();
                }
                onMapReady(Map);
            }
        });
    }


}
