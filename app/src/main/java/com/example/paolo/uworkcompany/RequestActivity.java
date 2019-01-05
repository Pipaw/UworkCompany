package com.example.paolo.uworkcompany;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RequestActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    LocationManager mLocationManager;
    LocationListener mLocationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        //Request for worker once the activity launch
        setMapLocation();

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        //setting the googleMap
        mMap = googleMap;
        //Creating a location manager object for location service
        mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        //location listener
        mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                //Update the map on location change
                updateMapLocation(location);

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        /*
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        */

        //Checking if the gps and location is Enabled
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            //Requesting the location of the device every 30 seconds
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 30, 30, mLocationListener);
            //Setting the lastknownlocation if there is one
            Location lastKnownLocation = mLocationManager.getLastKnownLocation
                    (LocationManager.GPS_PROVIDER);
            //checking the last known location
            if(lastKnownLocation != null){
                //updating the map once the last known location is found
                updateMapLocation(lastKnownLocation);
            }
        }else {
            //Accessing the current location if there is no last known location
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission
                    .ACCESS_FINE_LOCATION}, 1);
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        //If the user granted the location services and gps
        if (requestCode == 1 && grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                    PackageManager.PERMISSION_GRANTED){
                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 30,
                        30, mLocationListener);
                Location lastKnownLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if(lastKnownLocation != null) {
                    updateMapLocation(lastKnownLocation);
                }
            }
        }else {

            //If the user denies the permission request, then display a toast with some more information
            Toast.makeText(this, "Please enable location services to allow GPS tracking", Toast.LENGTH_SHORT).show();
        }
    }

    //Updating the MAP of the Application
    private void updateMapLocation(Location location){
        //Setting the latitude and longitude of the location
        LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.clear();
        //Pointing the camere/view to the set location and zoomed 16.
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 16));
        mMap.addMarker(new MarkerOptions().position(userLocation).title("Your Location"));
    }


    //Initiate the request to track the device's location
    //For the requesting of the workers
    private void setMapLocation(){
        LocationRequest request = new LocationRequest();

        //Get the most accurate location data available//
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(this);
        final String path = "WorkerRequest";
        int permission = ContextCompat.checkSelfPermission(this, Manifest.permission
                .ACCESS_FINE_LOCATION);

        //If the app currently has access to the location permission...

        if(permission == PackageManager.PERMISSION_GRANTED){
            client.requestLocationUpdates(request, new LocationCallback(){
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    //super.onLocationResult(locationResult);

                    //Get a reference to the database, so your app can perform read and write operations
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                            .getReference(path);

                    Location location = locationResult.getLastLocation();
                    if(location != null){
                        //Saving the location to the Firebase Database
                        databaseReference.setValue(location);
                    }else{
                        //Notify the user if the location is not found
                        Toast.makeText(getApplicationContext(), "Could not get Location, please try again later",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }, null);
        }
    }
    /*
    public void request(View view){
        setMapLocation();
    }
    */
}
