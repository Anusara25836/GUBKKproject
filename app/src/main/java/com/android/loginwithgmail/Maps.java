package com.android.loginwithgmail;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
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
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class Maps extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener
        , DirectionFinderListener, GoogleMap.OnMarkerClickListener, GoogleMap.OnInfoWindowClickListener {
    private GoogleMap mMap;
    private CheckBox ckAttraction;
    private CheckBox ckRestaurant;
    private CheckBox ckall;
    private CheckBox ckMarket;
    private Button gps;
    private Button bHelpService;
    private Button bMutiSearch;
    private Button btravel;
    private LocationManager locationManager;
    private LocationListener listener;
    private LatLng latLngCurrent;
    private Marker mapscurrenr;
    private EditText searchBar;
    private MarkerOptions markerCurrent = new MarkerOptions();
    private Marker markerchoose;
    private LatLng latLngchoose;
    private List<Polyline> polylinePaths = new ArrayList<>();
    private ProgressDialog progressDialog;
    private String distance;
    private String duration;
    private JSONArray recs;
    private Location Locationchoose;
    private List<Marker> markersattList;
    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();
    private Marker markeratt;
    private Marker markerres;
    private Marker markerMarket;
    private boolean chooselogo = false;
    private LatLng bangkoklatlng;
    private boolean checkall = false;
    private List<Marker> ResList;
    private List<Marker> markerMarketList;
    private Intent intent;
    private boolean checkshow = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        final SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        gps = (Button) findViewById(R.id.currentButton);
        bHelpService = (Button) findViewById(R.id.bHelpService);
        bMutiSearch = (Button) findViewById(R.id.bMutiSearch);
        btravel = (Button) findViewById(R.id.traval);
        ckAttraction = (CheckBox) findViewById(R.id.ckAttraction);
        ckRestaurant = (CheckBox) findViewById(R.id.ckRestaurant);
        ckMarket = (CheckBox) findViewById(R.id.ckMarket);
        ckall = (CheckBox) findViewById(R.id.all);
        searchBar = (EditText) findViewById(R.id.searchBar);
        //==================current location==========================//
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                latLngCurrent = new LatLng(location.getLatitude(), location.getLongitude());
                if (mapscurrenr != null) {
                    mapscurrenr.remove();
                }
                mapscurrenr = mMap.addMarker(markerCurrent
                        .position(latLngCurrent)
                        .title("choose i to be choose point")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.logome)));
                if (chooselogo) {
                    latLngCurrent = latLngchoose;
                    mapscurrenr.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.logopoint));
                } else {
                    mapscurrenr.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.logome));
                }
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {}
            @Override
            public void onProviderEnabled(String s) {}
            @Override
            public void onProviderDisabled(String s) {
                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(i);
            }
        };
        configure_button();
        ckall.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private int a;
            @Override
            public void onCheckedChanged(CompoundButton cb, boolean isChecked) {
                if (isChecked) {
                    displayall();
                    ckall.setBackground(getDrawable(R.drawable.all));
                }else {
                    ckAttraction.setChecked(false);
                    ckMarket.setChecked(false);
                    ckRestaurant.setChecked(false);
                    ckall.setBackground(getDrawable(R.drawable.allck));
                }

            }
        });
        ckAttraction.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private int idAttraction;
            @Override
            public void onCheckedChanged(CompoundButton cb, boolean isChecked) {
                if (isChecked) {
                    ckAttraction.setBackground(getDrawable(R.drawable.attck));
                    markersattList = new ArrayList<Marker>();
                    if (latLngchoose != null && markerchoose != null) {
                        Locationchoose = new Location("Locationchoose");
                        Locationchoose.setLatitude(latLngchoose.latitude);
                        Locationchoose.setLongitude(latLngchoose.longitude);
                        Response.Listener<String> responseListener = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonarray = new JSONObject(response);
                                    recs = jsonarray.getJSONArray("output");
                                   // Toast.makeText(Maps.this, "go true"+recs, Toast.LENGTH_SHORT).show();
                                    for (int i = 0; i < recs.length(); i++) {
                                        JSONObject array = recs.getJSONObject(i);
                                        idAttraction = array.getInt("idAttraction");
                                        String attName = array.getString("attName");
                                        Double attLat = array.getDouble("attLat");
                                        Double attLng = array.getDouble("attLng");
                                        LatLng attlatlng = new LatLng(attLat, attLng);
                                        Location attLocation = new Location("attLocation");
                                        attLocation.setLatitude(attLat);
                                        attLocation.setLongitude(attLng);
                                        double distanceInMeters = (Locationchoose.distanceTo(attLocation)) / 1000;//km
                                        if (distanceInMeters < 1) {
                                            markeratt = mMap.addMarker(new MarkerOptions().position(new LatLng(attLat, attLng))
                                                    .title(attName)
                                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.logoatt)));
                                            markeratt.setZIndex(idAttraction);
                                            markeratt.setTag("att");
                                            markeratt.setPosition(attlatlng);
                                            markersattList.add(markeratt);
                                        }
                                    }
                                    if (chooselogo != false) {
                                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mapscurrenr.getPosition(), 15));
                                    } else {
                                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markerchoose.getPosition(), 15));
                                    }
                                    mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                                        @Override
                                        public void onInfoWindowClick(Marker marker) {
                                            if (marker.equals(mapscurrenr)) {
                                                if (markerchoose != null) {
                                                    markerchoose.remove();
                                                }
                                                latLngchoose = marker.getPosition();
                                                mapscurrenr.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.logopoint));
                                                chooselogo = true;
                                                ckAttraction.setChecked(false);
                                            }
                                            intentclass(marker);
                                        }
                                    });
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        };
                        AttRequest LoginRequest = new AttRequest(responseListener);
                        RequestQueue queue = Volley.newRequestQueue(Maps.this);
                        queue.add(LoginRequest);
                    } else {
                        if (checkall) {
                            checkall = false;
                            //Toast.makeText(Maps.this, "go true", Toast.LENGTH_SHORT).show();
                            Response.Listener<String> responseListener = new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONObject jsonarray = new JSONObject(response);
                                        recs = jsonarray.getJSONArray("output");
                                        for (int i = 0; i < recs.length(); i++) {
                                            JSONObject array = recs.getJSONObject(i);
                                            idAttraction = array.getInt("idAttraction");
                                            String attName = array.getString("attName");
                                            Double attLat = array.getDouble("attLat");
                                            Double attLng = array.getDouble("attLng");
                                            LatLng attlatlng = new LatLng(attLat, attLng);
                                            markeratt = mMap.addMarker(new MarkerOptions().position(new LatLng(attLat, attLng))
                                                    .title(attName)
                                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.attsm)));
                                            markeratt.setZIndex(idAttraction);
                                            markeratt.setPosition(attlatlng);
                                            markeratt.setTag("att");
                                            markersattList.add(markeratt);
                                        }
                                        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                                            @Override
                                            public void onInfoWindowClick(Marker marker) {
                                                if (marker.equals(mapscurrenr)) {
                                                    if (markerchoose != null) {
                                                        markerchoose.remove();
                                                    }
                                                    latLngchoose = marker.getPosition();
                                                    mapscurrenr.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.logopoint));
                                                    chooselogo = true;
                                                    ckAttraction.setChecked(false);
                                                }
                                                intentclass(marker);
                                            }
                                        });
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            };
                            AttRequest AttRequest = new AttRequest(responseListener);
                            RequestQueue queue = Volley.newRequestQueue(Maps.this);
                            queue.add(AttRequest);
                        } else {
                            final AlertDialog.Builder alertgps = new AlertDialog.Builder(Maps.this);
                            alertgps.setTitle("How to use function ");
                            alertgps.setMessage("Choose point by click,please");
                            alertgps.setNegativeButton("cancel",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    });
                            alertgps.show();
                            ckAttraction.setChecked(false);
                            ckAttraction.setBackground(getDrawable(R.drawable.attnor));
                        }
                    }
                } else {
                    checkallicon();
                    checkall = false;
                    chooselogo = false;

                    ckAttraction.setBackground(getDrawable(R.drawable.attnor));
                    if (markersattList != null) {
                        for (int i = 0; i < markersattList.size(); i++) {
                            markersattList.get(i).remove();
                            markersattList.set(i, null);
                        }
                    }
                }
            }
        });
        ckRestaurant.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private int idRestaurant;

            @Override
            public void onCheckedChanged(CompoundButton cb, boolean isChecked) {
                if (isChecked) {
                    ckRestaurant.setBackground(getDrawable(R.drawable.resck));
                    ResList = new ArrayList<Marker>();
                    if (latLngchoose != null && markerchoose != null) {
                        Locationchoose = new Location("Locationchoose");
                        Locationchoose.setLatitude(latLngchoose.latitude);
                        Locationchoose.setLongitude(latLngchoose.longitude);
                        Response.Listener<String> responseListener = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonarray = new JSONObject(response);
                                    recs = jsonarray.getJSONArray("outputRes");
                                    for (int i = 0; i < recs.length(); i++) {
                                        JSONObject arrayRes = recs.getJSONObject(i);
                                        idRestaurant = arrayRes.getInt("idRestaurant");
                                        String ResName = arrayRes.getString("ResName");
                                        Double ResLat = arrayRes.getDouble("ResLat");
                                        Double ResLang = arrayRes.getDouble("ResLang");
                                        LatLng reslatlng = new LatLng(ResLat, ResLang);
                                        //=========res att data=================
                                        Location resLocation = new Location("resLocation");
                                        resLocation.setLatitude(ResLat);
                                        resLocation.setLongitude(ResLang);
                                        //=========res att data=================//
                                        double distanceInMeters = (Locationchoose.distanceTo(resLocation)) / 1000;//km
                                        if (distanceInMeters < 1) {
                                            //Toast.makeText(Maps.this, "recs =" + distanceInMeters, Toast.LENGTH_SHORT).show();
                                            markerres = mMap.addMarker(new MarkerOptions().position(new LatLng(ResLat, ResLang))
                                                    .title(ResName)
                                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.logores)));
                                            markerres.setZIndex(idRestaurant);
                                            markerres.setPosition(reslatlng);
                                            markerres.setTag("res");
                                            ResList.add(markerres);
                                        }
                                    }
                                    if (chooselogo != false) {
                                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mapscurrenr.getPosition(), 15));
                                    } else {
                                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markerchoose.getPosition(), 15));
                                    }
                                    mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                                        @Override
                                        public void onInfoWindowClick(Marker marker) {
                                            if (marker.equals(mapscurrenr)) {
                                                if (markerchoose != null) {
                                                    markerchoose.remove();
                                                }
                                                latLngchoose = marker.getPosition();
                                                mapscurrenr.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.logopoint));
                                                chooselogo = true;
                                                ckRestaurant.setChecked(false);
                                            }
                                            intentclass(marker);
                                        }
                                    });
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        };
                        ResRequest ResRequest = new ResRequest(responseListener);
                        RequestQueue queue = Volley.newRequestQueue(Maps.this);
                        queue.add(ResRequest);
                    } else {
                        if (checkall) {
                            checkall = false;
                            Response.Listener<String> responseListener = new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONObject jsonarray = new JSONObject(response);
                                        recs = jsonarray.getJSONArray("outputRes");
                                        for (int i = 0; i < recs.length(); i++) {
                                            JSONObject arrayRes = recs.getJSONObject(i);
                                            idRestaurant = arrayRes.getInt("idRestaurant");
                                            String ResName = arrayRes.getString("ResName");
                                            Double ResLat = arrayRes.getDouble("ResLat");
                                            Double ResLang = arrayRes.getDouble("ResLang");
                                            LatLng reslatlng = new LatLng(ResLat, ResLang);
                                            markerres = mMap.addMarker(new MarkerOptions()
                                                    .position(new LatLng(ResLat, ResLang))
                                                    .title(ResName)
                                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ressm4)));
                                            markerres.setPosition(reslatlng);
                                            markerres.setZIndex(idRestaurant);
                                            markerres.setTag("res");
                                            ResList.add(markerres);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            };
                            ResRequest ResRequest = new ResRequest(responseListener);
                            RequestQueue queue = Volley.newRequestQueue(Maps.this);
                            queue.add(ResRequest);
                        } else {
                            final AlertDialog.Builder alertgps = new AlertDialog.Builder(Maps.this);
                            alertgps.setTitle("How to use function res");
                            alertgps.setMessage("Choose point by click");
                            alertgps.setNegativeButton("cancel",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    });
                            alertgps.show();
                            ckRestaurant.setChecked(false);
                            ckRestaurant.setBackground(getDrawable(R.drawable.resno));
                        }
                    }
                } else {
                    checkallicon();
                    checkall = false;
                    chooselogo = false;
                    ckRestaurant.setBackground(getDrawable(R.drawable.resno));
                    if (ResList != null) {
                        for (int i = 0; i < ResList.size(); i++) {
                            ResList.get(i).remove();
                            ResList.set(i, null);
                        }
                    }
                }
            }
        });
        ckMarket.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private int idMarket;

            @Override
            public void onCheckedChanged(CompoundButton cb, boolean isChecked) {
                if (isChecked) {
                    markerMarketList = new ArrayList<Marker>();
                    ckMarket.setBackground(getDrawable(R.drawable.marck));
                    if (latLngchoose != null && markerchoose != null) {
                        Locationchoose = new Location("Locationchoose");
                        Locationchoose.setLatitude(latLngchoose.latitude);
                        Locationchoose.setLongitude(latLngchoose.longitude);
                        Response.Listener<String> responseListener = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonarray = new JSONObject(response);
                                    recs = jsonarray.getJSONArray("outputmarket");
                                    for (int i = 0; i < recs.length(); i++) {
                                        JSONObject array = recs.getJSONObject(i);
                                        idMarket = array.getInt("idMarket");
                                        String marketName = array.getString("marketName");
                                        Double marketLat = array.getDouble("marketLat");
                                        Double marketLng = array.getDouble("marketLng");
                                        LatLng Marlatlng = new LatLng(marketLat, marketLng);
                                        Location MarLocation = new Location("MarLocation");
                                        MarLocation.setLatitude(marketLat);
                                        MarLocation.setLongitude(marketLng);
                                        double distanceInMeters = (Locationchoose.distanceTo(MarLocation)) / 1000;
                                        if (distanceInMeters < 1) {
                                            markerMarket = mMap.addMarker(new MarkerOptions().position(new LatLng(marketLat, marketLng))
                                                    .title(marketName)
                                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.logomar)));
                                            markerMarket.setZIndex(idMarket);
                                            markerMarket.setPosition(Marlatlng);
                                            markerMarket.setTag("mar");
                                            markerMarketList.add(markerMarket);
                                        }
                                    }
                                    if (chooselogo != false) {
                                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mapscurrenr.getPosition(), 15));
                                    } else {
                                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markerchoose.getPosition(), 15));
                                    }
                                    mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                                        @Override
                                        public void onInfoWindowClick(Marker marker) {
                                            if (marker.equals(mapscurrenr)) {
                                                if (markerchoose != null) {
                                                    markerchoose.remove();
                                                }
                                                latLngchoose = marker.getPosition();
                                                mapscurrenr.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.logopoint));
                                                chooselogo = true;
                                                ckMarket.setChecked(false);
                                            }
                                            intentclass(marker);
                                        }
                                    });
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        };
                        MarRequest MarRequest = new MarRequest(responseListener);
                        RequestQueue queue = Volley.newRequestQueue(Maps.this);
                        queue.add(MarRequest);
                    } else {
                        if (checkall) {
                            checkall = false;
                            Response.Listener<String> responseListener = new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONObject jsonarray = new JSONObject(response);
                                        recs = jsonarray.getJSONArray("outputmarket");
                                        for (int i = 0; i < recs.length(); i++) {
                                            JSONObject arraymar = recs.getJSONObject(i);
                                            int idMarket = arraymar.getInt("idMarket");
                                            String marketName = arraymar.getString("marketName");
                                            Double marketLat = arraymar.getDouble("marketLat");
                                            Double marketLng = arraymar.getDouble("marketLng");
                                            LatLng Marlatlng = new LatLng(marketLat, marketLng);
                                            markerMarket = mMap.addMarker(new MarkerOptions().position(new LatLng(marketLat, marketLng))
                                                    .title(marketName)
                                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.marssm)));
                                            markerMarket.setZIndex(idMarket);
                                            markerMarket.setPosition(Marlatlng);
                                            markerMarketList.add(markerMarket);
                                            markerMarket.setTag("mar");
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            };
                            MarRequest MarRequest = new MarRequest(responseListener);
                            RequestQueue queue = Volley.newRequestQueue(Maps.this);
                            queue.add(MarRequest);
                        } else {
                            final AlertDialog.Builder alertgps = new AlertDialog.Builder(Maps.this);
                            alertgps.setTitle("How to use function");
                            alertgps.setMessage("Choose point by click");
                            alertgps.setNegativeButton("cancel",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    });
                            alertgps.show();
                            ckMarket.setChecked(false);
                            ckMarket.setBackground(getDrawable(R.drawable.marnor));
                        }
                    }
                } else {
                    checkallicon();
                    checkall = false;
                    chooselogo = false;
                    ckMarket.setBackground(getDrawable(R.drawable.marnor));
                    if (markerMarketList != null) {
                        for (int i = 0; i < markerMarketList.size(); i++) {
                            markerMarketList.get(i).remove();
                            markerMarketList.set(i, null);
                        }
                    }
                }
            }
        });
        bHelpService.setOnClickListener(new View.OnClickListener() {
            private String Text = "";

            @Override
            public void onClick(View view) {
                Intent intenthelp = new Intent(Maps.this, helpMenu.class);
                if (latLngCurrent != null) {
                    String latCurrent = "" + latLngCurrent.latitude;
                    String lngCurrent = "" + latLngCurrent.longitude;
                    intenthelp.putExtra("latCurrent", latCurrent);
                    intenthelp.putExtra("lngCurrent", lngCurrent);
                }
                startActivity(intenthelp);
            }
        });
        btravel.setOnClickListener(new View.OnClickListener() {
            private int y;
            @Override
            public void onClick(View view) {
                if (latLngCurrent != null && latLngchoose != null) {
                    sendRequest();
                    if (distance != null) {
                        AlertDialog.Builder alertTravel = new AlertDialog.Builder(Maps.this);
                        alertTravel.setTitle("Distance and Duration");
                        alertTravel.setMessage("Distance : " + distance + "\n" + "Duration : " + duration);
                        alertTravel.setPositiveButton("Back to maps",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngCurrent, 15));
                                    }
                                });
                        alertTravel.setNegativeButton("Clear maps",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (polylinePaths != null) {
                                            for (int i = 0; i < polylinePaths.size(); i++) {
                                                mMap.clear();
                                            }
                                            ckAttraction.setChecked(false);
                                            ckRestaurant.setChecked(false);
                                            ckMarket.setChecked(false);
                                            if (markerchoose != null) {
                                                markerchoose.remove();
                                            }
                                        }
                                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(bangkoklatlng, 11));
                                        dialog.cancel();
                                    }
                                });
                        alertTravel.show();
                    } else {
                        AlertDialog.Builder alertTravel = new AlertDialog.Builder(Maps.this);
                        alertTravel.setTitle("How to use travel Function");
                        alertTravel.setMessage("click again for see Distance and Duration ");
                        alertTravel.setNegativeButton("ok",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                        alertTravel.show();
                    }

                } else {
                    AlertDialog.Builder alertTravel = new AlertDialog.Builder(Maps.this);
                    alertTravel.setTitle("How to search travel");
                    alertTravel.setMessage("you have to touch on maps for point \n and open gps");
                    alertTravel.setNegativeButton("ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    alertTravel.show();
                }
            }
        });
        bMutiSearch.setOnClickListener(new View.OnClickListener() {
            private int z;
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Maps.this, mutiSearch.class);
                startActivity(intent);
            }
        });
    }
    public void checkallicon(){
                 if(ckAttraction.isChecked() == false && ckMarket.isChecked() == false && ckRestaurant.isChecked() == false ){
                    ckall.setChecked(false);
                }
    }
    public void displayall() {
        if (markerchoose != null) {
            markerchoose.remove();
            markerchoose = null;
            checkall = true;
        }
        checkall = true;
        ckAttraction.setChecked(true);
        checkall = true;
        ckRestaurant.setChecked(true);
        checkall = true;
        ckMarket.setChecked(true);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 10:
                configure_button();
                break;
            default:
                break;
        }
    }

    void configure_button() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{
                        android.Manifest.permission.ACCESS_COARSE_LOCATION,
                        android.Manifest.permission.ACCESS_FINE_LOCATION,
                        android.Manifest.permission.INTERNET}, 10);
            } else {
                mapscurrenr = mMap.addMarker(markerCurrent.position(latLngCurrent).title("choose point around"));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLngCurrent, 16));
            }
        }
        // this code won't execute IF permissions are not allowed, because in the line above there is return statement.
        gps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (latLngCurrent != null) {
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLngCurrent, 13));
                } else
                    //noinspection MissingPermission
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, listener);
            }
        });
    }

    public void intentclass(Marker marker) {
        if (marker.getTag().equals("att")) {
            intent = new Intent(Maps.this, attShow.class);
            int intid = (int) marker.getZIndex();
            String id = "" + intid;
            intent.putExtra("id", id);
            startActivity(intent);
        }
        if (marker.getTag().equals("res")) {
            intent = new Intent(Maps.this, resShow.class);
            int intid = (int) marker.getZIndex();
            String id = "" + intid;
            intent.putExtra("id", id);
            startActivity(intent);
        }
        if (marker.getTag().equals("mar")) {
            intent = new Intent(Maps.this, marShow.class);
            int intid = (int) marker.getZIndex();
            String id = "" + intid;
            intent.putExtra("id", id);
            startActivity(intent);
        }
    }

    public void onMapSearch(View view) {
        EditText locationSearch = (EditText) findViewById(R.id.searchBar);
        String location = locationSearch.getText().toString();
        List<Address> addressList = null;
        if (location.isEmpty()) {
            Toast.makeText(Maps.this, "Enter name of location ", Toast.LENGTH_SHORT).show();
        } else {
            Geocoder geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(location, 1);
                if (addressList != null) {
                    mMap.clear();
                    Address address = addressList.get(0);
                    String countryName = "" + address.getCountryName();
                    if (countryName.equals("Thailand")) {
                        if (markerchoose != null) {
                            markerchoose.remove();
                        }
                        LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                        latLngchoose = latLng;
                        markerchoose = mMap.addMarker(new MarkerOptions()
                                .position(latLngchoose)
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.logopoint)));
                        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));
                        chooselogo = false;
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
                    } else {
                        Toast.makeText(Maps.this, "Dont find name of location in Thailand", Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (Exception e) {
                Toast.makeText(Maps.this, "Don't find name of location", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        bangkoklatlng = new LatLng(13.756328, 100.501936);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(bangkoklatlng));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(bangkoklatlng, 15));
        mMap.setOnMapClickListener(this);
        mMap.setOnMarkerClickListener(this);
        mMap.setOnInfoWindowClickListener(this);
    }

    @Override
    public void onMapClick(LatLng latLng) {
        if (mapscurrenr != null) {
            mapscurrenr.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.logome));
            ckAttraction.setChecked(false);
            ckRestaurant.setChecked(false);
            ckMarket.setChecked(false);
            chooselogo = false;
        }
        if (markerchoose == null) {
            latLngchoose = latLng;
            markerchoose = mMap.addMarker(new MarkerOptions()
                    .position(latLngchoose)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.logopoint)));
            latLngchoose = latLng;
        } else {
            markerchoose.remove();
            latLngchoose = latLng;
            markerchoose = mMap.addMarker(new MarkerOptions()
                    .position(latLngchoose)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.logopoint)));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
            chooselogo = false;
        }
        ckAttraction.setChecked(false);
        ckRestaurant.setChecked(false);
        ckMarket.setChecked(false);
    }

    private void sendRequest() {
        String origin = latLngchoose.latitude + "," + latLngchoose.longitude;
        String destination = latLngCurrent.latitude + "," + latLngCurrent.longitude;
        if (origin.isEmpty() || origin == "") {
            Toast.makeText(this, "Please click current button and wait!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (destination.isEmpty() || destination == "") {
            Toast.makeText(this, "Please click point and wait!", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            new DirectionFinder(this, origin, destination).execute();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDirectionFinderStart() {
        progressDialog = ProgressDialog.show(this, "Please wait.",
                "Finding direction..!", true);
        if (originMarkers != null) {
            for (Marker marker : originMarkers) {
                marker.remove();
            }
        }
        if (destinationMarkers != null) {
            for (Marker marker : destinationMarkers) {
                marker.remove();
            }
        }
        if (polylinePaths != null) {
            for (Polyline polyline : polylinePaths) {
                polyline.remove();
            }
        }
    }

    @Override
    public void onDirectionFinderSuccess(List<Route> routes) {
        progressDialog.dismiss();
        polylinePaths = new ArrayList<>();
        originMarkers = new ArrayList<>();
        destinationMarkers = new ArrayList<>();
        if (polylinePaths != null) {
            polylinePaths.clear();
        }
        if (markerchoose != null) {
            markerchoose.remove();
        }
        checkshow = true;
        if (checkshow) {
            checkshow = false;
            for (Route route : routes) {
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(route.startLocation, 10));
                distance = route.distance.text;
                duration = route.duration.text;
                originMarkers.add(mMap.addMarker(new MarkerOptions()
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.sou))
                        .title(route.startAddress)
                        .position(route.startLocation)));
                destinationMarkers.add(mMap.addMarker(new MarkerOptions()
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.des))
                        .title(route.endAddress)
                        .position(route.endLocation)));
                PolylineOptions polylineOptions = new PolylineOptions().
                        geodesic(true).
                        color(Color.BLUE).
                        width(10);
                for (int i = 0; i < route.points.size(); i++)
                    polylineOptions.add(route.points.get(i));
                polylinePaths.add(mMap.addPolyline(polylineOptions));
            }
        }

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        latLngchoose = marker.getPosition();
        marker.setPosition(latLngchoose);
        return false;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        if (marker.equals(mapscurrenr)) {
            if (markerchoose != null) {
                markerchoose.setPosition(latLngchoose);
                markerchoose.remove();
            }
            latLngchoose = marker.getPosition();
            mapscurrenr.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.logopoint));
            chooselogo = true;
        }
    }
}
