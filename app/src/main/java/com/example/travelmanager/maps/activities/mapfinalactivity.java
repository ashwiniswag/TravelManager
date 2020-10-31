package com.example.travelmanager.maps.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.travelmanager.R;
import com.example.travelmanager.maps.activities.DirectionParser;
import com.example.travelmanager.maps.models.PlacesConstant;
import com.example.travelmanager.maps.models.Results;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class mapfinalactivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final int AUTOCOMPLETE_REQUEST_CODE = 73;
    private static final int REQUEST_LOCATION = 1;
    private boolean flag = false;
    private GoogleMap mMap;
    private View locationButton;
    Marker lastmarker;
    Context context;
    private Results results2;
    View mapview;
    static double latitude;
    private LatLng pos;
    private com.example.travelmanager.maps.models.Location location1, location2;
    private Marker marker;
    private double lat, lng;
    static double currlatitude;
    static double longitude;
    private String type;
    static double currlongitude;
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    String origin;
    String destination;
    private LinearLayout main_ll;
    private SearchView searchbar;
    private TextView dist_time;
    String id;
    List<Results> results = new ArrayList<Results>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapfinal);
        //getSupportActionBar().hide();
        context = getApplicationContext();


        main_ll=findViewById(R.id.main_ll);
        searchbar=findViewById(R.id.search_bar);
        dist_time=findViewById((R.id.time_dist));
        initPlaces();
        fab_setUp();
        //navigation_setup();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapview = mapFragment.getView();
        mapFragment.getMapAsync(this);
        main_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
              Uri.parse("http://maps.google.com/maps?saddr="+Double.toString(currlatitude)+","+Double.toString(currlongitude)+"&daddr="+Double.toString(latitude)+","+Double. toString(longitude)));
              startActivity(intent);
            }
        });
        Intent act=getIntent();

            id = act.getStringExtra("id");



            if (id.equals("1")) {
                results = PlacesConstant.results;
                Toast.makeText(this, String.valueOf(results.size()), Toast.LENGTH_LONG).show();
            }
            else if(id.equals("2")){
                Bundle bundle = getIntent().getExtras();

                if (bundle != null) {
                    results2 = (Results) bundle.getSerializable("result");
                    lat = bundle.getDouble("lat");
                    lng = bundle.getDouble("lng");
                    type = bundle.getString("type");
                    location2 = results2.getGeometry().getLocation();
                    Toast.makeText(this, String.valueOf(lat), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Got Nothing!!", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            else if(id.equals("0")){
                Toast.makeText(this,"map from location",Toast.LENGTH_SHORT).show();
                Bundle bundle = getIntent().getExtras();
                lat = bundle.getDouble("lat");
                lng = bundle.getDouble("lng");
            }
        searchbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSearchCalled();
            }
        });
    }


    private void fab_setUp() {
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMap != null) {
                    if (locationButton != null)
                        locationButton.callOnClick();
                    mMap.clear();
                    main_ll.setVisibility((View.GONE));
                    latitude = currlatitude;
                    longitude = currlongitude;
                }
            }
        });
    }

    private void initPlaces() {
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), "AIzaSyDYoQybddM6c-Daz0bHVe7h2tuyzxHmW1k");
        }

       // PlacesClient placesClient = Places.createClient(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(mapfinalactivity.this,
                            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                        flag = true;
                        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
                        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                            Intent intent1 = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(intent1);
                        }
                        mMap.setMyLocationEnabled(true);

                    }

                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap gMap) {

        this.mMap = gMap;
        checkPermission();
    
        if (mMap != null) {
            locationButton = ((View) mapview.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
            // and next place it, on bottom right (as Google Maps app)
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)
                    locationButton.getLayoutParams();
            locationButton.setVisibility(View.GONE);

            // position on right bottom
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            layoutParams.setMargins(0, 0, 30, 30);
            mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
                @Override
                public void onMyLocationChange(Location arg0) {
                    if (lastmarker != null)
                        lastmarker.remove();
                    mMap.setMyLocationEnabled(true);
                    currlatitude = arg0.getLatitude();
                    currlongitude = arg0.getLongitude();
                    //lastmarker = mMap.addMarker(new MarkerOptions().position(new LatLng(arg0.getLatitude(), arg0.getLongitude())).title("It's Me!"));
                }
            });
            if(id.equals("2")) {
                if (type.equals("distance")) {
                    LatLng destinationPosition = new LatLng(Double.valueOf(location2.getLat()), Double.valueOf(location2.getLng()));
                    LatLng currentPosition = new LatLng(lat, lng);
                    mMap.addMarker(new MarkerOptions().position(destinationPosition)
                            .title(results2.getName())
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                            .snippet(results2.getVicinity())
                            .alpha(1f))
                            .showInfoWindow();

                    mMap.moveCamera(CameraUpdateFactory.newLatLng(destinationPosition));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(destinationPosition, 13.0f));
                    mMap.getUiSettings().setCompassEnabled(true);
                    mMap.getUiSettings().setZoomControlsEnabled(true);

                    // for current
                    mMap.addMarker(new MarkerOptions().position(currentPosition)
                            .title("Your Location")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                            .alpha(1f))
                            .showInfoWindow();

                    mMap.moveCamera(CameraUpdateFactory.newLatLng(destinationPosition));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(destinationPosition, 13.0f));
                    mMap.getUiSettings().setCompassEnabled(true);
                    mMap.getUiSettings().setZoomControlsEnabled(true);
                    origin="origin="+lat+","+lng;
                    destination="destination="+Double.valueOf(location2.getLat())+","+Double.valueOf(location2.getLng());
                    String url = getDirectionsUrl(origin,destination);

                    FetchUrl fetchUrl = new FetchUrl();

                    fetchUrl.execute(url);
                    //move map camera
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(currentPosition));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentPosition,15.0f));
                }
                else {
                    pos = new LatLng(Double.valueOf(location2.getLat()), Double.valueOf(location2.getLng()));
                    //Toast.makeText(this, String.valueOf(pos), Toast.LENGTH_SHORT).show();
                    //marker.remove();
                    mMap.addMarker(new MarkerOptions().position(pos)
                            .title(results2.getName())
                            .snippet(results2.getVicinity())
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                            .alpha(1f));

                    mMap.getUiSettings().setCompassEnabled(true);
                    mMap.getUiSettings().setZoomControlsEnabled(true);
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(pos)); // move the camera to the position
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(pos,16.5f));
                }
            }
            else if(id.equals("0")){
                pos = new LatLng(lat, lng);
                //Toast.makeText(this, String.valueOf(pos), Toast.LENGTH_SHORT).show();
                //marker.remove();
                mMap.addMarker(new MarkerOptions().position(pos)
                        .title("YOUR LOCATION")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                        .alpha(1f));

                mMap.getUiSettings().setCompassEnabled(true);
                mMap.getUiSettings().setZoomControlsEnabled(true);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(pos)); // move the camera to the position
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(pos,16.5f));
            }else
            mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(currlatitude,currlongitude)));
            //mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

                @Override
                public void onMapClick(LatLng latLng) {
                    mMap.clear();
                    main_ll.setVisibility(View.GONE);
                    searchbar.onActionViewCollapsed();
                    latitude = latLng.latitude;
                    longitude = latLng.longitude;
                    mMap.addMarker(new MarkerOptions().position(latLng).title("Destination").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE)));
                    origin="origin="+currlatitude+","+currlongitude;
                    destination="destination="+latitude+","+longitude;
                    String url = getDirectionsUrl(origin, destination);
                    FetchUrl FetchUrl = new FetchUrl();

                    // Start downloading json data from Google Directions API
                    System.out.println(url);
                    FetchUrl.execute(url);
                    LatLngBounds.Builder builder = new LatLngBounds.Builder();

                    ArrayList<Marker>markers=new ArrayList<>();
                    markers.clear();
                    markers.add(mMap.addMarker(new MarkerOptions().position(new LatLng(currlatitude,currlongitude))));
                    markers.add(mMap.addMarker(new MarkerOptions().position(new LatLng(latitude,longitude))));
                    for (Marker marker : markers) {
                        builder.include(marker.getPosition());
                    }
                    LatLngBounds bounds = builder.build();
                    int padding = 250;
                    CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);

                    mMap.moveCamera(cu);
                    mMap.animateCamera(cu);
                }
            });
            Intent routeData = getIntent();
            if (getIntent() != null) {
                LatLng latlang = routeData.getParcelableExtra("routeData");
                if(latlang!=null) {
                    origin = "origin=" + currlatitude + "," + currlongitude;
                    destination = "destination=" + latlang.latitude + "," + latlang.longitude;
                    getRoute();
                }
         }
            if(id.equals("1")){
                for (int i = 0; i < results.size(); i++) {
                    MarkerOptions markerOptions = new MarkerOptions();
                    Results googlePlace = results.get(i);
                    double lat = Double.parseDouble(googlePlace.getGeometry().getLocation().getLat());
                    double lng = Double.parseDouble(googlePlace.getGeometry().getLocation().getLng());
                    String placeName = googlePlace.getName();
                    String vicinity = googlePlace.getVicinity();
                    LatLng latLng = new LatLng(lat, lng);
                    markerOptions.position(latLng);
                    markerOptions.title(placeName);
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                    // add marker to map
                    mMap.addMarker(markerOptions).showInfoWindow();;
                    // move camera
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                    //googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));
                    mMap.getUiSettings().setCompassEnabled(true);
                    mMap.getUiSettings().setZoomControlsEnabled(true);
                }
            }
        }

    }


    public void checkPermission() {
        if (ContextCompat.checkSelfPermission(mapfinalactivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(mapfinalactivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(mapfinalactivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } else {
                ActivityCompat.requestPermissions(mapfinalactivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }

        } else {
            locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                Intent intent1 = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent1);
            }
            mMap.setMyLocationEnabled(true);

        }
    }

    public void searchLocation(String location) {

        List<Address> addressList = null;

        if (location != null || !location.equals("")) {
            Geocoder geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(location, 1);

            } catch (IOException e) {
                e.printStackTrace();
            }
            Address address = addressList.get(0);
            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
            latitude = address.getLatitude();
            longitude = address.getLongitude();
            mMap.clear();
            mMap.addMarker(new MarkerOptions().position(latLng).title(location));
            mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

            searchbar.onActionViewExpanded();
            searchbar.setQuery(location, true);
            //Toast.makeText(getApplicationContext(), address.getLatitude() + " " + address.getLongitude(), Toast.LENGTH_LONG).show();
        }
    }


//    public void navigation_setup() {
//        dl = findViewById(R.id.activity_mapfinal);
//        t = new ActionBarDrawerToggle(this, dl, R.string.Open, R.string.Close);
//
//        dl.addDrawerListener(t);
//        t.syncState();
//
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//
//        nv = (NavigationView) findViewById(R.id.nv);
//        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                int id = item.getItemId();
//                Intent intent;
//                switch (id) {
//                    case R.id.pasttrips:
//                        Toast.makeText(MainActivity.this, "Past Trips", Toast.LENGTH_SHORT).show();
//                        intent = new Intent(MainActivity.this, PastTripsActivity.class);
//                        startActivity(intent);
//                        break;
//                    case R.id.uptrips:
//                        Toast.makeText(MainActivity.this, "Upcoming Trips", Toast.LENGTH_SHORT).show();
//                        break;
//                    case R.id.recommendations:
//                        Toast.makeText(MainActivity.this,"wait",Toast.LENGTH_SHORT).show();
//                        intent = new Intent(MainActivity.this, Recommendation.class);
//                        startActivity(intent);
//                        break;
//                    case R.id.profile:
//                        Toast.makeText(MainActivity.this, "Profile", Toast.LENGTH_SHORT).show();
//                        intent = new Intent(MainActivity.this, ProfilePage.class);
//                        startActivity(intent);
//                        break;
//                    case R.id.signout:
//                        Toast.makeText(MainActivity.this, "Sign Out", Toast.LENGTH_SHORT).show();
//                        break;
//                    case R.id.about:
//                        Toast.makeText(MainActivity.this, "About", Toast.LENGTH_SHORT).show();
//                        break;
//                    default:
//                        return true;
//                }
//
//
//                return true;
//
//            }
//        });
//
//
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//
//        if (t.onOptionsItemSelected(item))
//            return true;
//
//        switch (item.getItemId()) {
//            case R.id.search:
//                onSearchCalled();
//                return true;
//            case android.R.id.home:
//                finish();
//                return true;
//            default:
//                return false;
//        }
//
//    }

    public void onSearchCalled() {
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG);
        // Start the autocomplete intent.
        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields).setCountry("IN")
                .build(this);
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                //Log.i(TAG, "Place: " + place.getName() + ", " + place.getId() + ", " + place.getAddress());
                Toast.makeText(mapfinalactivity.this, place.getName() , Toast.LENGTH_LONG).show();
                String address = place.getAddress();
                searchLocation(address);
                // do query with address

            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                Toast.makeText(mapfinalactivity.this, "Error: " + status.getStatusMessage(), Toast.LENGTH_LONG).show();
                //Log.i(TAG, status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // R.menu.mymenu is a reference to an xml file named mymenu.xml which should be inside your res/menu directory.
//        // If you don't have res/menu, just create a directory named "menu" inside res
//        getMenuInflater().inflate(R.menu.main_menu, menu);
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    public void open_sheet(View view) {
//        BottomSheet bottomSheetFragment = new BottomSheet(this);
//        bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
//    }

    public double get_curr_lat() {
        return latitude;
    }

    public double get_curr_long() {
        return longitude;
    }

    private void getRoute() {
        String url = getDirectionsUrl(origin, destination);
        FetchUrl FetchUrl = new FetchUrl();

        // Start downloading json data from Google Directions API

        FetchUrl.execute(url);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(currlatitude,currlongitude)));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
    }

    private String getDirectionsUrl(String origin, String destination) {
        String sensor = "sensor=false";
        String mode = "mode=driving";

        // Building the parameters to the web service
        String parameters = origin + "&" + destination + "&" + sensor + "&" + mode;

        // Output format
        String output = "json";

        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters+"&key=AIzaSyBo37tN8stGk4peRtNeagiDV0bDa7jvt5U";

        return url;
    }

    private class FetchUrl extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
                Log.d("Background Task data", data.toString());
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);

        }
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();
            Log.d("downloadUrl", data.toString());
            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                Log.d("ParserTask",jsonData[0].toString());
                DirectionParser parser = new DirectionParser();
                Log.d("ParserTask", parser.toString());

                // Starts parsing data
                routes = parser.parse(jObject);
                Log.d("ParserTask","Executing routes");
                Log.d("ParserTask",routes.toString());

            } catch (Exception e) {
                Log.d("ParserTask",e.toString());
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points;
            PolylineOptions lineOptions = null;
            String distance = "";
            String duration = "";
            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);
                    if(j==0){    // Get distance from the list
                        distance = (String)point.get("distance");
                        continue;
                    }else if(j==1){ // Get duration from the list
                        duration = (String)point.get("duration");
                        continue;
                    }
                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(13);
                lineOptions.color(Color.BLUE);

                Log.d("onPostExecute","onPostExecute lineoptions decoded");

            }

            // Drawing polyline in the Google Map for the i-th route
            if(lineOptions != null) {
                mMap.addPolyline(lineOptions);
                dist_time.setText(duration+" "+distance);
                main_ll.setVisibility(View.VISIBLE);
            }
            else {
                Log.d("onPostExecute","without Polylines drawn");
            }
        }
    }


}

