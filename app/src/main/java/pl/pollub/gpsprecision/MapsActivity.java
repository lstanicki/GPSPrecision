package pl.pollub.gpsprecision;

import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import pl.pollub.gpsprecision.CustomLocationManager;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MapsActivity extends FragmentActivity {
    public DatabaseHelper database;
    public ArrayList<Marker> markerList;
    View rootView;
    GPSTracker gps;
    NetworkTracker network;
    CustomLocationManager customLocation;
    Button btnShowLocation;
    Button btnShowNetworkLocation;

    double latitude = 0;
    double longitude = 0;
    private GoogleMap mMap; //musi być null jesli google play serwisy apk nie sa dostepne
    private GoogleApiClient client;
    List<String[]> markersForTest = null;

    //@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        database = new pl.pollub.gpsprecision.DatabaseHelper(this);
        markersForTest = new ArrayList<String[]>();

        //lokalizacja z gps
        btnShowLocation = (Button) findViewById(R.id.GPSButton);
        btnShowLocation.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                gps = new pl.pollub.gpsprecision.GPSTracker(MapsActivity.this);

                if (gps.canGetLocation()) {
                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();

                    if (latitude != 0 && longitude != 0) {
                        Toast.makeText(getApplicationContext(),
                                "Twoje położenie to -\nSzerokość: " + latitude + "\nDługość: "
                                        + longitude, Toast.LENGTH_LONG).show();
                        markersForTest.add(new String[]{
                                "Lozalizacja z GPS",
                                String.valueOf(latitude),
                                String.valueOf(longitude)
                        });
                    } else {
                        Toast.makeText(getApplicationContext(), "Nie można odczytać położenia z GPS", Toast.LENGTH_LONG).show();
                    }

                } else {
                    gps.showGPSAlert();
                }
            }
        });

        btnShowNetworkLocation = (Button) findViewById(R.id.networkButton);
        btnShowNetworkLocation.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                network = new pl.pollub.gpsprecision.NetworkTracker(MapsActivity.this);

                if (network.canGetLocation()) {
                    double latitude = network.getLatitude();
                    double longitude = network.getLongitude();

                    if (latitude != 0 && longitude != 0) {
                        Toast.makeText(getApplicationContext(),
                                "Twoje położenie to -\nSzerokość: " + latitude + "\nDługość: "
                                        + longitude, Toast.LENGTH_LONG).show();
                        markersForTest.add(new String[]{
                                "Lokalizacja z sieci",
                                String.valueOf(latitude),
                                String.valueOf(longitude)
                        });
                    } else {
                        Toast.makeText(getApplicationContext(), "Nie można odczytać położenia z sieci", Toast.LENGTH_LONG).show();
                    }

                } else {
                    //gps.showGPSAlert();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

/*    public void onSearch(View view) {

        EditText locationTextField = (EditText) findViewById(R.id.locationTextField);
        String location = locationTextField.getText().toString();
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
            mMap.addMarker(new MarkerOptions().position(latLng).title("Moja pozycja wyszukana"));
            mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

        }
    }*/

    public void onChangeType(View view) {
        if (mMap.getMapType() == GoogleMap.MAP_TYPE_NORMAL) {
            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        } else if (mMap.getMapType() == GoogleMap.MAP_TYPE_SATELLITE) {
            mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        } else if (mMap.getMapType() == GoogleMap.MAP_TYPE_HYBRID) {
            mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        } else {
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.startTest:
                Toast.makeText(getApplicationContext(),
                        "Do testu zostanie wybranych 5 puntków w pobliżu Twojej obecnej lokalizacji", Toast.LENGTH_SHORT).show();
                mMap.clear();
                Toast.makeText(getApplicationContext(),
                        "Przeliczanie odległości...", Toast.LENGTH_SHORT).show();
                updateDistance();
                getMarkersForTest();
                return true;
/*            case R.id.calculateDistance:
                Toast.makeText(getApplicationContext(),
                        "Przeliczanie odległości...", Toast.LENGTH_SHORT).show();
                updateDistance();
                return true;*/
            case R.id.endTest:
                mMap.clear();
                Toast.makeText(getApplicationContext(),
                        "Czyszczenie mapy...", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.saveResults:
                Toast.makeText(getApplicationContext(),
                        "Zapisywanie...", Toast.LENGTH_SHORT).show();
                try {
                    saveToCSVFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return true;
            case R.id.showAllMarkers:
                getAllMarkers();
                return true;
            case R.id.clearMap:
                Toast.makeText(getApplicationContext(),
                        "Czyszczenie mapy...", Toast.LENGTH_SHORT).show();
                mMap.clear();
                return true;
            case R.id.closeApp:
                finish();
                System.exit(0);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onTestStart() {
        updateDistance();
        //customMarkers();

        //Toast.makeText(getApplicationContext(), "Ilość dostępnych: " + markerList.size(), Toast.LENGTH_SHORT).show();

    }

    public void onZoom(View view) {
        if (view.getId() == R.id.zoomInButton) {
            mMap.animateCamera(CameraUpdateFactory.zoomIn());
        }
        if (view.getId() == R.id.zoomOutButton) {
            mMap.animateCamera(CameraUpdateFactory.zoomOut());
        }
    }


    private void setUpMapIfNeeded() {
        if (mMap == null) {
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();

            if (mMap != null) {
                setUpMap();
            }
        }
    }

    public void getAllMarkers() {
        List<pl.pollub.gpsprecision.MyMarker> markers = database.getAllMarkers();

        for (pl.pollub.gpsprecision.MyMarker myMarker : markers) {
            LatLng latLng = new LatLng(myMarker.getLatitude(), myMarker.getLongitude());

            MarkerOptions marker = new MarkerOptions()
                    .position(latLng)
                    .title(myMarker.getName())
                    .snippet("Współrzędne: " + myMarker.getLatitude() + ", " + myMarker.getLongitude())
                    .anchor(0.5f, 0.5f);

            mMap.addMarker(marker);
        }
    }

    public void getMarkersForTest() {
        List<pl.pollub.gpsprecision.MyMarker> markersFortest = database.getMarkersForTest();

        for (pl.pollub.gpsprecision.MyMarker myMarker : markersFortest) {
            LatLng latLng = new LatLng(myMarker.getLatitude(), myMarker.getLongitude());

            MarkerOptions marker = new MarkerOptions()
                    .position(latLng)
                    .title(myMarker.getName())
                    .snippet("Współrzędne: " + myMarker.getLatitude() + ", " + myMarker.getLongitude())
                    .anchor(0.5f, 0.5f)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_place_black_36dp));

            mMap.addMarker(marker);
        }
    }

    public void updateDistance() {
        List<pl.pollub.gpsprecision.MyMarker> markers = database.getAllMarkers();

        for (pl.pollub.gpsprecision.MyMarker marker : markers) {
            if (customLocation.canGetLocation()) {
                latitude = customLocation.getLatitude();
                longitude = customLocation.getLongitude();
            }

            Location currentLocation = new Location("Moja Lokalizacja");
            currentLocation.setLatitude(latitude);
            currentLocation.setLongitude(longitude);

            Location markerLoc = new Location("Lokalizacja markera");
            markerLoc.setLatitude(marker.getLatitude());
            markerLoc.setLongitude(marker.getLongitude());

            float distanceTo = currentLocation.distanceTo(markerLoc);

            marker.setDistanceFrom((int) distanceTo);

            database.updateMarker(marker);
        }

    }

    private void saveToCSVFile() throws IOException {
        //tworzenie folderu, jeśli nie istnieje
        File directory = new File(Environment.getExternalStorageDirectory().toString() + "/GPSPrecision");

        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");

        if (directory != null && !directory.exists()
                && !directory.mkdirs()) {
            try {
                throw new IOException("Cannot create dir "
                        + directory.getAbsolutePath());
            } catch (IOException e) {

                e.printStackTrace();
            }
        }

        CSVWriter writer = new CSVWriter(new FileWriter("/storage/emulated/0/GPSPrecision/scores_" + dateFormat.format(date) + ".csv"), ',');
        //CSVWriter writer = new CSVWriter(new FileWriter("/storage/extSdCard/GPSPrecision/scores_" + dateFormat.format(date) + ".csv"), ',');

        List<String[]> data = new ArrayList<String[]>();
        List<pl.pollub.gpsprecision.MyMarker> makrersToCSV = database.getMarkersForTest();
        data.add(new String[]{"Nazwa Markera", "Szerokosc", "Długosc", "Odleglosc"});
        for (pl.pollub.gpsprecision.MyMarker myMarker : makrersToCSV) {
            data.add(new String[]{
                    myMarker.getName(),
                    String.valueOf(myMarker.getLatitude()),
                    String.valueOf(myMarker.getLongitude())
            });
        }
        data.add(new String[]{"Test"});
        writer.writeAll(data);

        if(markersForTest != null) {
            writer.writeAll(markersForTest);
        }
        writer.close();
    }

    private void setUpMap() {
        customLocation = new CustomLocationManager(MapsActivity.this);

        double latitude = 0;
        double longitude = 0;

        mMap.setMyLocationEnabled(true);

        if (customLocation.canGetLocation()) {
            latitude = customLocation.getLatitude();
            longitude = customLocation.getLongitude();
        }

        LatLng myLoc = new LatLng(latitude, longitude);

        //ustawia przy wlaczeniu aplikacji na punkt w ktorym jestesmy
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLoc, 15));

        //addMarkers();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Maps Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.lukasz.gpsprecision/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();
        //gps.stopUsingGPS();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Maps Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.lukasz.gpsprecision/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}