package com.tanxinjialan.wisunproject;

        import android.os.Bundle;
        import android.support.v7.app.AppCompatActivity;
        import android.util.Log;

        import com.google.android.gms.maps.CameraUpdateFactory;
        import com.google.android.gms.maps.GoogleMap;
        import com.google.android.gms.maps.OnMapReadyCallback;
        import com.google.android.gms.maps.SupportMapFragment;
        import com.google.android.gms.maps.model.LatLng;
        import com.google.android.gms.maps.model.MapStyleOptions;
        import com.google.android.gms.maps.model.MarkerOptions;

public class FirstActivity extends AppCompatActivity implements OnMapReadyCallback {

    Contact arrayContact[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retrieve the content view that renders the map.
        setContentView(R.layout.activity_first);
        // Get the SupportMapFragment and request notification when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map when it's available.
     * The API invokes this callback when the map is ready for use.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Customise the styling of the base map using a JSON object defined in a string resource file. First create a MapStyleOptions object
        // from the JSON styles string, then pass this to the setMapStyle method of the GoogleMap object.
        boolean success = googleMap.setMapStyle(new MapStyleOptions(getResources().getString(R.string.style_json_aubergine)));
        if (!success) {
            Log.e("Error", "Style parsing failed.");
        }
        // Add a marker in Singapore and move the map's camera to the same location.
        LatLng singapore = new LatLng(1.37, 103.81);
        googleMap.addMarker(new MarkerOptions().position(singapore).title("Marker in Singapore"));
        // Position the map's camera in centre of Singapore.
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(singapore));
    }
}
