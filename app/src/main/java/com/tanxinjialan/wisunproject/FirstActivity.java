package com.tanxinjialan.wisunproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class FirstActivity extends AppCompatActivity implements GoogleMap.OnMarkerClickListener, OnMapReadyCallback {

    private String district = null;
    private int[] log_case = {0, 0, 0, 0, 0};
    private int central = 0;
    private int north = 1;
    private int south = 2;
    private int east = 3;
    private int west = 4;
    private int groupPos = 0;
    private int childPos = 0;
    private Contact[][] arrayContact = new Contact[5][1000];
    private GoogleMap gMap;
    private Socket mSocket;
    private Emitter.Listener listener = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            FirstActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject jsonObj = (JSONObject) args[0];
                    //data would contain the parsed msg
                    //Handle your data on your UI thread here
                    try {
                        if (jsonObj.getString("status").equals("ACTIVE")) {
                            district = jsonObj.getString("district");
                            switch (district) {
                                case "Central":
                                    createContact(central, log_case[central], jsonObj);
                                    break;
                                case "North":
                                    createContact(north, log_case[north], jsonObj);
                                    break;
                                case "South":
                                    createContact(south, log_case[south], jsonObj);
                                    break;
                                case "East":
                                    createContact(east, log_case[east], jsonObj);
                                    break;
                                case "West":
                                    createContact(west, log_case[west], jsonObj);
                                    break;
                                default:
                                    break;
                            }
                        }

                    } catch (JSONException e) {

                        e.printStackTrace();
                    }
                }
            });
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retrieve the content view that renders the map.
        setContentView(R.layout.activity_first);
        // Get the SupportMapFragment and request notification when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);
        mapFragment.getMapAsync(this);

        // Set up expandable list view
        ExpandableListView expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
        LinkedHashMap<String, List<String>> expandableListAddress = ExpandableListData.getData();
        List<String> expandableListDistrict = new ArrayList<String>(expandableListAddress.keySet());
        ExpandableListAdapter expandableListAdapter = new CustomExpandableListAdapter(this, expandableListDistrict, expandableListAddress);
        expandableListView.setAdapter(expandableListAdapter);
        //socket config
        try {
            // localhost address with port
            mSocket = IO.socket("http://192.168.1.95:8001/");
        } catch (URISyntaxException ignored) {

        }
        mSocket.on("new_case", listener);

        mSocket.connect();

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
                Geocoder gc = new Geocoder(getBaseContext(), Locale.getDefault());
                try {
                    groupPos = groupPosition;
                    childPos = childPosition;

                    List<Address> list = gc.getFromLocationName(arrayContact[groupPosition][childPosition].getAddress() + " Singapore", 1);
                    Address add = list.get(0);
                    Toast.makeText(getApplicationContext(), arrayContact[groupPosition][childPosition].getBlock_name(), Toast.LENGTH_LONG).show();

                    LatLng Position = new LatLng(add.getLatitude(), add.getLongitude());
                    gMap.addMarker(new MarkerOptions().position(Position).title(arrayContact[groupPosition][childPosition].getBlock_name()));

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

        // Set a listener for marker click.
        gMap.setOnMarkerClickListener(this);
    }

    /**
     * Called when the user clicks a marker.
     */
    @Override
    public boolean onMarkerClick(final Marker marker) {

        // If user wants to delete item
        // Show alert message to reassure to delete item
        AlertDialog alert = new AlertDialog.Builder(FirstActivity.this).create();
        if (arrayContact[groupPos][childPos].getStatus().equals("ACTIVE")) {
            alert.setTitle("Accept");
            alert.setMessage("Accept Case " + arrayContact[groupPos][childPos].getCase_no() + "?");
            arrayContact[groupPos][childPos].setStatus("ACCEPTED");
        }
        alert.setTitle("Accept");
        alert.setMessage("Accept Case " + arrayContact[groupPos][childPos].getCase_no() + "?");
        // "OK" Button on alert message
        alert.setButton(DialogInterface.BUTTON_POSITIVE, "YES", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                AsyncTaskRunner postReq = new AsyncTaskRunner();
                postReq.execute();

                Intent i = new Intent(FirstActivity.this, BlockPlanActivity.class);
                i.putExtra("Case_no", arrayContact[groupPos][childPos].getCase_no());
                i.putExtra("status", arrayContact[groupPos][childPos].getStatus());
                i.putExtra("BlockName", arrayContact[groupPos][childPos].getBlock_name());
                i.putExtra("Address", arrayContact[groupPos][childPos].getAddress());
                i.putExtra("Unit_no", arrayContact[groupPos][childPos].getUnit_no());
                i.putExtra("Postal_code", arrayContact[groupPos][childPos].getPostal_code());
                i.putExtra("Contact", arrayContact[groupPos][childPos].getContact_no());
                ExpandableListData.removeData(arrayContact[groupPos][childPos].getDistrict(), childPos);
                startActivity(i);

                Log.i("Status", "OK button clicked!");

            }
        });
        // "Cancel" Button on alert message
        alert.setButton(DialogInterface.BUTTON_NEGATIVE, "NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.i("Status", "Cancel button clicked!");
                dialog.dismiss();
            }
        });

        alert.show();
        // Return false to indicate that we have not consumed the event and that we wish
        // for the default behavior to occur (which is for the camera to move such that the
        // marker is centered and for the marker's info window to open, if it has one).
        return false;
    }

    private void createContact(int groupPos, int childPos, JSONObject jsonObj) throws JSONException {
        arrayContact[groupPos][childPos] = new Contact(jsonObj.getInt("case_no"),
                jsonObj.getString("time_lodged"),
                jsonObj.getString("last_update"),
                jsonObj.getString("status"),
                jsonObj.getString("id"),
                jsonObj.getInt("contact"),
                jsonObj.getString("district"),
                jsonObj.getString("type"),
                jsonObj.getString("blockname"),
                jsonObj.getString("address"),
                jsonObj.getString("unitno"),
                jsonObj.getString("postalcode"),
                jsonObj.getDouble("lat"),
                jsonObj.getDouble("lng"));

        ExpandableListData.addData(arrayContact[groupPos][childPos].getDistrict(),
                arrayContact[groupPos][childPos].getBlock_name(),
                arrayContact[groupPos][childPos].getAddress(),
                arrayContact[groupPos][childPos].getUnit_no(),
                arrayContact[groupPos][childPos].getPostal_code());
        log_case[childPos]++;
        if (arrayContact[groupPos][childPos].getStatus().equals("DONE")) {
            ExpandableListData.removeData(arrayContact[groupPos][childPos].getDistrict(), childPos);
        }
    }

    private class AsyncTaskRunner extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("http://192.168.1.95:8001/caseupdate");

            try {
                List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(2);
                nameValuePair.add(new BasicNameValuePair("case_no", String.valueOf(arrayContact[groupPos][childPos].getCase_no())));
                Log.i("Test", arrayContact[groupPos][childPos].getStatus());
                nameValuePair.add(new BasicNameValuePair("status", arrayContact[groupPos][childPos].getStatus()));
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));

                // Execute HTTP Post Request
                httpClient.execute(httpPost);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }
    }
}
