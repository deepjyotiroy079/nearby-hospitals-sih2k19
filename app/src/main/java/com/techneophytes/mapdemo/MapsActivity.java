package com.techneophytes.mapdemo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private static final String TAG = "MapsActivity";
    private GoogleMap mMap;
    private GoogleApiClient client;
    private LocationRequest request;
    Location lastLocation;
    private static String GET_BEDS_URL = "http://192.168.43.47/hospital/getHospitalInfoByName.php?hospital_name=";
    private Marker currentLocationMarker; // this is a marker
    public static final int REQUEST_LOCATION_CODE = 99;
    GetNearByPlacesData getNearByPlacesData;
    int PROXIMITY_RADIUS = 10000;
    public double latitude, longitude;
    private String hospitalName;
    private TextView total_bed_count;
    private TextView available_beds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION_CODE: {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // ie the permission is granted
                    if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        if (client == null) {
                            buildGoogleApiClient(); // this will create a new client
                        }
                        mMap.setMyLocationEnabled(true);
                    }
                } else { // permission denied
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
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
    public void onMapReady(GoogleMap googleMap){

        mMap = googleMap;

        if(mMap != null) {
            mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                @Override
                public View getInfoWindow(Marker marker) {
                    return null;
                }

                @Override
                public View getInfoContents(Marker marker) {

                    View row = getLayoutInflater().inflate(R.layout.custom_snippet, null);
                    TextView hospital_name = (TextView)row.findViewById(R.id.hospital_name);
                    // TextView hospital_location = (TextView)row.findViewById(R.id.hospital_location);
                    total_bed_count = (TextView)row.findViewById(R.id.total_bed_count);
                    available_beds = (TextView)row.findViewById(R.id.available_beds);

                    LatLng latLng = marker.getPosition();
                    hospitalName = marker.getTitle();
                    /**Name of the hospital + the vicinity*/
                    hospital_name.setText(hospitalName);
                    String test = hospitalName.replace(" ", "+");
                    Toast.makeText(MapsActivity.this, test, Toast.LENGTH_SHORT).show();
                    /**Number of total beds*/
                    // total_bed_count.setText("Total Beds : 250");

                    /**Number of available beds*/
                    // available_beds.setText("Vacant beds: 200");
                    StringRequest stringRequest = new StringRequest(Request.Method.GET, GET_BEDS_URL+test, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject ob = new JSONObject(response);
                                JSONArray array = ob.getJSONArray("beds");
                                JSONObject data = array.getJSONObject(0);
                                int t_beds = Integer.parseInt(data.getString("Total_beds"));
                                int a_beds = Integer.parseInt(data.getString("Total_beds_vacant"));

                                total_bed_count.setText("Total Beds : "+t_beds);
                                available_beds.setText("Available Beds : "+a_beds);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(MapsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                    RequestQueue requestQueue = Volley.newRequestQueue(MapsActivity.this);
                    requestQueue.add(stringRequest);
                    return row;
                }
            });
        }
        Log.d(TAG, "onMapReady: Map is ready");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }

    }

    protected synchronized void buildGoogleApiClient() {
        Log.d(TAG, "buildGoogleApiClient: Building Google api");
        client = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        client.connect();
    }

    // onclick listener for button
    /*public void onClick(View view) {

        if(view.getId() == R.id.B_hospital) {
            mMap.clear();
            String hospital = "hospital";
            String url = getUrl(latitude, longitude, hospital);

            Object dataTransfer[] = new Object[2];
            dataTransfer[0] = mMap;
            dataTransfer[1] = url;
            Log.d(TAG, "onClick: latitude : "+latitude+" longitude : "+longitude);
            getNearByPlacesData = new GetNearByPlacesData();
            getNearByPlacesData.execute(dataTransfer);
            Toast.makeText(this, "Showing nearby hospitals", Toast.LENGTH_LONG).show();
        }
        // Toast.makeText(this, "Some popup", Toast.LENGTH_SHORT).show();
    }*/

    private String getUrl(double latitude, double longitude, String nearbyPlace) {
        StringBuilder googlePlaceUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlaceUrl.append("location="+latitude+","+longitude);
        Log.d(TAG, "getUrl: latitude : "+latitude+" longitude: "+longitude);
        googlePlaceUrl.append("&radius="+PROXIMITY_RADIUS);
        googlePlaceUrl.append("&type="+nearbyPlace);
        googlePlaceUrl.append("&sensor=true");
        googlePlaceUrl.append("&key="+"your_api_here");

        Log.d("MapsActivity", "url = "+googlePlaceUrl.toString());

        return googlePlaceUrl.toString();

    }
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        // create a location request object
        request = new LocationRequest();
        request.setInterval(100);
        request.setFastestInterval(1000);
        request.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(client, request, this);
        }
    }

    public boolean checkLocationPermission() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_CODE);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_CODE);
            }
            return false;
        } else {
            return false;
        }
    }


    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        lastLocation = location;

        latitude = location.getLatitude();
        longitude = location.getLongitude();

        if(currentLocationMarker != null) {
            currentLocationMarker.remove();
        }

        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Location");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        currentLocationMarker = mMap.addMarker(markerOptions);
        Log.d(TAG, "onLocationChanged: latitude : "+location.getLatitude()+" longitude : "+location.getLongitude());
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomBy(12));

        mMap.clear();
        String hospital = "hospital";
        String url = getUrl(latitude, longitude, hospital);

        Object dataTransfer[] = new Object[2];
        dataTransfer[0] = mMap;
        dataTransfer[1] = url;
        Log.d(TAG, "onClick: latitude : "+latitude+" longitude : "+longitude);
        getNearByPlacesData = new GetNearByPlacesData();
        getNearByPlacesData.execute(dataTransfer);
        Toast.makeText(this, "Showing nearby hospitals", Toast.LENGTH_LONG).show();

        if(client != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(client, this);
        }
    }
}
