package com.example.googlemap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
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
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

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
import java.io.UnsupportedEncodingException;
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
    private EditText extOrigin , extdestination;
    private ImageButton imbDirectionPath ,imdDirectionDialog;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AnhXa();
        progressBar.setVisibility(View.INVISIBLE);
        ClickIcon();
        place1 = new MarkerOptions().position(new LatLng(currentLocation.getLocation().getLatitude() , currentLocation.getLocation().getLongitude())).title("Location Current Divice");
        place2 = new MarkerOptions().position(new LatLng(address.getLatitude() , address.getLongitude())).title(locationSearchView);

        mapFragment.getMapAsync(this);
    }
    private void AnhXa(){
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.myMap);
        imbLocationCurrent = (ImageButton) findViewById(R.id.imageButtonLocationCurrent);
        extOrigin = (EditText) findViewById(R.id.editTextOrigin);
        extdestination = (EditText) findViewById(R.id.editTextDestination);
        searchView = (SearchView) findViewById(R.id.searchview);
        imbDirectionPath = (ImageButton) findViewById(R.id.imageButton);
        imdDirectionDialog = (ImageButton) findViewById(R.id.imageButtonDirectionDialog);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        currentLocation = new CurrentLocation(getApplicationContext());
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
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
                progressBar.setVisibility(View.VISIBLE);
                if(location != null) {
                    progressBar.setVisibility(View.INVISIBLE);
                    lan = location.getLatitude();
                    lng = location.getLongitude();
                }
                onMapReady(Map);
            }
        });
//        imdDirectionDialog.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                DialoDirectionPath();
//            }
//        });
    }

    private void DialoDirectionPath(){
        Dialog dialog = new Dialog(getApplicationContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.direction);
        AnhXa();
        final String origin = extOrigin.getText().toString();
        final String director = extdestination.getText().toString();
        if(origin.isEmpty() || director.isEmpty()){
            Toast.makeText(this , "Please enter full" , Toast.LENGTH_LONG);
            return;
        }
        imbDirectionPath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    new DirectionFinder(getApplicationContext() , origin , director).execute();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });
    }


}
