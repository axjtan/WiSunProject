package com.tanxinjialan.wisunproject;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;

public class FirstActivity extends AppCompatActivity implements OnMapReadyCallback {

    Contact arrayContact[];
    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    List<String> expandableListDistrict;
    LinkedHashMap<String, List<String>> expandableListAddress;
    private GoogleMap gMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retrieve the content view that renders the map.
        setContentView(R.layout.activity_first);
        // Get the SupportMapFragment and request notification when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);
        mapFragment.getMapAsync(this);

        // Set up expandable list view
        expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
        expandableListAddress = ExpandableListData.getData();
        expandableListDistrict = new ArrayList<String>(expandableListAddress.keySet());
        expandableListAdapter = new CustomExpandableListAdapter(this, expandableListDistrict, expandableListAddress);
        expandableListView.setAdapter(expandableListAdapter);

        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                //mMap.clear();
            }
        });

        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                gMap.clear();
            }
        });
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                gMap.clear();
                String full_address = expandableListAddress.get(expandableListDistrict.get(groupPosition)).get(childPosition);
                String[] location = full_address.split(",");
                Geocoder gc = new Geocoder(getBaseContext(), Locale.getDefault());
                try {
                    List<Address> list = gc.getFromLocationName(location[1] + " Singapore", 1);
                    Address add = list.get(0);
                    Toast.makeText(getApplicationContext(), location[0], Toast.LENGTH_LONG).show();

                    LatLng Position = new LatLng(add.getLatitude(), add.getLongitude());
                    gMap.addMarker(new MarkerOptions().position(Position).title(location[0]));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return false;
            }
        });
    }

    /**
     * Manipulates the map when it's available.
     * The API invokes this callback when the map is ready for use.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
        // Customise the styling of the base map using a JSON object defined in a string resource file. First create a MapStyleOptions object
        // from the JSON styles string, then pass this to the setMapStyle method of the GoogleMap object.
        boolean success = gMap.setMapStyle(new MapStyleOptions(getResources().getString(R.string.style_json_aubergine)));
        if (!success) {
            Log.e("Error", "Style parsing failed.");
        }
        // Add a marker in Singapore and move the map's camera to the same location.
        LatLng singapore = new LatLng(1.37, 103.81);
        //googleMap.addMarker(new MarkerOptions().position(singapore).title("Marker in Singapore"));
        // Position the map's camera in centre of Singapore.
        gMap.moveCamera(CameraUpdateFactory.newLatLng(singapore));
    }
}
