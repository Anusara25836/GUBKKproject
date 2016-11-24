package com.android.loginwithgmail;

import android.content.Intent;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
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

public class helpMap extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Double newLatDo;
    private Double newLogDo;
    private JSONArray recs;
    private int idFacilitate;
    private Marker markerFac;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_map);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        final Intent intent = getIntent();
        if (intent.getExtras() != null) {
            String newLat = intent.getStringExtra("latCurrent");
            String newLog = intent.getStringExtra("lngCurrent");
            String type = intent.getStringExtra("type");
            newLatDo = Double.parseDouble(newLat);
            newLogDo = Double.parseDouble(newLog);
            LatLng latlngCurrent = new LatLng(newLatDo, newLogDo);
            final Location LocationCurrent = new Location("LocationCurrent");
            LocationCurrent.setLatitude(latlngCurrent.latitude);
            LocationCurrent.setLongitude(latlngCurrent.longitude);
            //Toast.makeText(helpMap.this, "locations = "+latlngCurrent.latitude+" "+latlngCurrent.longitude, Toast.LENGTH_SHORT).show();
            // Toast.makeText(helpMap.this, "out locations = "+type, Toast.LENGTH_SHORT).show();
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonarray = new JSONObject(response);
                            recs = jsonarray.getJSONArray("output");
                            for (int i = 0; i < recs.length(); i++) {
                                JSONObject array = recs.getJSONObject(i);
                                idFacilitate = array.getInt("idFacilitate");
                                String facilitateName = array.getString("facilitateName");
                                Double facilitateLat = array.getDouble("facilitateLat");
                                Double facilitateLng = array.getDouble("facilitateLng");
                                LatLng facilitateLatLng = new LatLng(facilitateLat, facilitateLng);
                                Location facilitateLocation = new Location("facilitateLocation");
                                facilitateLocation.setLatitude(facilitateLatLng.latitude);
                                facilitateLocation.setLongitude(facilitateLatLng.longitude);
                                double distanceInMeters = (LocationCurrent.distanceTo(facilitateLocation)) / 1000;//km
                                //Toast.makeText(helpMap.this, "locations = "+distanceInMeters, Toast.LENGTH_SHORT).show();
                                //Toast.makeText(helpMap.this, "locations = "+facilitateLat+" "+facilitateLng, Toast.LENGTH_SHORT).show();
                                if (distanceInMeters < 50) {
                                    //.makeText(helpMap.this, "locations = " + facilitateName, Toast.LENGTH_SHORT).show();
                                    markerFac = mMap.addMarker(new MarkerOptions().position(new LatLng(facilitateLat, facilitateLng))
                                                                                   .title(facilitateName)
                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.logohelp))
                                    );
                                    markerFac.setZIndex(idFacilitate);
                                }
                                mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                                    @Override
                                    public void onInfoWindowClick(Marker marker) {
                                        int intid = (int) marker.getZIndex();
                                        //Toast.makeText(helpMap.this, "inside onInfoWindowClick"+intid, Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(helpMap.this, facshowweb.class);
                                        String id = "" + intid;
                                        intent.putExtra("id", id);
                                        startActivity(intent);
                                    }
                                });
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                AllRequest registerRequest = new AllRequest(type, responseListener);
                RequestQueue queue = Volley.newRequestQueue(helpMap.this);
                queue.add(registerRequest);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng LatLngcurrent = new LatLng(newLatDo, newLogDo);
        mMap.addMarker(new MarkerOptions().position(LatLngcurrent)
                .title("Your location")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.logome)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLngcurrent, 13));
    }
}
