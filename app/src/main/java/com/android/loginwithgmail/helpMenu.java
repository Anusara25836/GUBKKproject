package com.android.loginwithgmail;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.renderscript.Double2;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

public class helpMenu extends Activity {
    private double latCurrentDo;
    private double lngCurrentDo;
    private String latCurrent;
    private String lngCurrent;
    private LatLng latLngcur ;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_menu);
        final Button hospital = (Button) findViewById(R.id.bhospital);
        Button bpolice = (Button) findViewById(R.id.bpolice);
        Button bExchange = (Button) findViewById(R.id.bExchange);
        Button bEmbassy = (Button) findViewById(R.id.bEmbassy);
        Button bTRAVEL = (Button) findViewById(R.id.bTRAVEL);
        Button bCONVERSATION = (Button) findViewById(R.id.bCONVERSATION);
        Button bsouvenir = (Button) findViewById(R.id.bsouvenir);
        Button bFestival = (Button) findViewById(R.id.bFestival);
        final Intent intent = getIntent();
        if (intent.getExtras() != null) {
            latCurrent = intent.getStringExtra("latCurrent");
            lngCurrent = intent.getStringExtra("lngCurrent");
            latCurrentDo = Double.parseDouble(latCurrent);
            lngCurrentDo = Double.parseDouble(lngCurrent);
            latLngcur = new LatLng(latCurrentDo, lngCurrentDo);
            //Toast.makeText(helpMenu.this, "locations = " + latCurrent + "" + lngCurrent, Toast.LENGTH_SHORT).show();
        }
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        final LocationListener listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                lngCurrentDo =location.getLongitude();
                latCurrentDo=location.getLatitude();
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

        hospital.setOnClickListener(new View.OnClickListener() {
            private int x;

            @Override
            public void onClick(View view) {
                type = "1";
                String typeString = "" + type;
                //====================== get data =====================================/
                if (latCurrent != null && lngCurrent != null) {
                    Intent intenthelp = new Intent(helpMenu.this, helpMap.class);
                    if (latCurrent != null && lngCurrent != null) {
                        String latCurrent = "" + latLngcur.latitude;
                        String lngCurrent = "" + latLngcur.longitude;
                        intenthelp.putExtra("latCurrent", latCurrent);
                        intenthelp.putExtra("lngCurrent", lngCurrent);
                        intenthelp.putExtra("type", typeString);
                        startActivity(intenthelp);
                    }
                } else {
                    LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                    if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        if (ActivityCompat.checkSelfPermission(helpMenu.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(helpMenu.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            showGPSDisabledAlertToUser();
                            return;
                        }
                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, listener);
                        if(lngCurrentDo != 0.0 && latCurrentDo != 0.0){
                            //Toast.makeText(helpMenu.this, "GPS is Enabled in your devide"+latCurrentDo+" "+lngCurrentDo+" "+type, Toast.LENGTH_SHORT).show();
                            Intent intenthelphos = new Intent(helpMenu.this, helpMap.class);
                            String latCurrent = "" + latCurrentDo;
                            String lngCurrent = "" + lngCurrentDo;
                            intenthelphos.putExtra("latCurrent", latCurrent);
                            intenthelphos.putExtra("lngCurrent", lngCurrent);
                            intenthelphos.putExtra("type", typeString);
                            startActivity(intenthelphos);
                        }else {
                            Toast.makeText(helpMenu.this, "waiting for gps , click again", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        showGPSDisabledAlertToUser();
                    }
                }
            }
        });
        bpolice.setOnClickListener(new View.OnClickListener() {
            private int x;

            @Override
            public void onClick(View view) {
                type = "2";
                String typeString = "" + type;
                //====================== get data =====================================/
                if (latCurrent != null && lngCurrent != null) {
                    Intent intenthelp = new Intent(helpMenu.this, helpMap.class);
                    if (latCurrent != null && lngCurrent != null) {
                        String latCurrent = "" + latLngcur.latitude;
                        String lngCurrent = "" + latLngcur.longitude;
                        intenthelp.putExtra("latCurrent", latCurrent);
                        intenthelp.putExtra("lngCurrent", lngCurrent);
                        intenthelp.putExtra("type", typeString);
                        startActivity(intenthelp);
                    }
                } else {
                    LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                    if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        if (ActivityCompat.checkSelfPermission(helpMenu.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(helpMenu.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            showGPSDisabledAlertToUser();
                            return;
                        }
                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, listener);
                        if(lngCurrentDo != 0.0 && latCurrentDo != 0.0){
                            //Toast.makeText(helpMenu.this, "GPS is Enabled in your devide"+latCurrentDo+" "+lngCurrentDo+" "+type, Toast.LENGTH_SHORT).show();
                            Intent intenthelphos = new Intent(helpMenu.this, helpMap.class);
                            String latCurrent = "" + latCurrentDo;
                            String lngCurrent = "" + lngCurrentDo;
                            intenthelphos.putExtra("latCurrent", latCurrent);
                            intenthelphos.putExtra("lngCurrent", lngCurrent);
                            intenthelphos.putExtra("type", typeString);
                            startActivity(intenthelphos);
                        }else {
                            Toast.makeText(helpMenu.this, "waiting for gps , click again", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        showGPSDisabledAlertToUser();
                    }
                }
            }
        });
        bExchange.setOnClickListener(new View.OnClickListener() {
            private int x;

            @Override
            public void onClick(View view) {
                type = "5";
                String typeString = "" + type;
                //====================== get data =====================================/
                if (latCurrent != null && lngCurrent != null) {
                    Intent intenthelp = new Intent(helpMenu.this, helpMap.class);
                    if (latCurrent != null && lngCurrent != null) {
                        String latCurrent = "" + latLngcur.latitude;
                        String lngCurrent = "" + latLngcur.longitude;
                        intenthelp.putExtra("latCurrent", latCurrent);
                        intenthelp.putExtra("lngCurrent", lngCurrent);
                        intenthelp.putExtra("type", typeString);
                        startActivity(intenthelp);
                    }
                } else {
                    LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                    if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        if (ActivityCompat.checkSelfPermission(helpMenu.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(helpMenu.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            showGPSDisabledAlertToUser();
                            return;
                        }
                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, listener);
                        if(lngCurrentDo != 0.0 && latCurrentDo != 0.0){
                           // Toast.makeText(helpMenu.this, "GPS is Enabled in your devide"+latCurrentDo+" "+lngCurrentDo+" "+type, Toast.LENGTH_SHORT).show();
                            Intent intenthelphos = new Intent(helpMenu.this, helpMap.class);
                            String latCurrent = "" + latCurrentDo;
                            String lngCurrent = "" + lngCurrentDo;
                            intenthelphos.putExtra("latCurrent", latCurrent);
                            intenthelphos.putExtra("lngCurrent", lngCurrent);
                            intenthelphos.putExtra("type", typeString);
                            startActivity(intenthelphos);
                        }else {
                            Toast.makeText(helpMenu.this, "waiting for gps , click again", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        showGPSDisabledAlertToUser();
                    }
                }
            }
        });
        bEmbassy.setOnClickListener(new View.OnClickListener() {
            private int x;

            @Override
            public void onClick(View view) {
                type = "6";
                String typeString = "" + type;
                //====================== get data =====================================/
                if (latCurrent != null && lngCurrent != null) {
                    Intent intenthelp = new Intent(helpMenu.this, helpMap.class);
                    type = "2";
                    if (latCurrent != null && lngCurrent != null) {
                        String latCurrent = "" + latLngcur.latitude;
                        String lngCurrent = "" + latLngcur.longitude;
                        intenthelp.putExtra("latCurrent", latCurrent);
                        intenthelp.putExtra("lngCurrent", lngCurrent);
                        intenthelp.putExtra("type", typeString);
                        startActivity(intenthelp);
                    }
                } else {
                    LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                    if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        if (ActivityCompat.checkSelfPermission(helpMenu.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(helpMenu.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            showGPSDisabledAlertToUser();
                            return;
                        }
                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, listener);
                        if(lngCurrentDo != 0.0 && latCurrentDo != 0.0){
                            //Toast.makeText(helpMenu.this, "GPS is Enabled in your devide"+latCurrentDo+" "+lngCurrentDo+" "+type, Toast.LENGTH_SHORT).show();
                            Intent intenthelphos = new Intent(helpMenu.this, helpMap.class);
                            String latCurrent = "" + latCurrentDo;
                            String lngCurrent = "" + lngCurrentDo;
                            intenthelphos.putExtra("latCurrent", latCurrent);
                            intenthelphos.putExtra("lngCurrent", lngCurrent);
                            intenthelphos.putExtra("type", typeString);
                            startActivity(intenthelphos);
                        }else {
                            Toast.makeText(helpMenu.this, "waiting for gps , click again", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        showGPSDisabledAlertToUser();
                    }
                }
            }
        });
        bTRAVEL.setOnClickListener(new View.OnClickListener() {
            private int x;

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(helpMenu.this, helpWep.class);
                String travel = "travel";
                intent.putExtra("checkweb", travel);
                startActivity(intent);
            }
        });
        bCONVERSATION.setOnClickListener(new View.OnClickListener() {
            private int x;

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(helpMenu.this, helpWep.class);
                String conversation = "conversation";
                intent.putExtra("checkweb", conversation);
                startActivity(intent);
            }
        });
        bsouvenir.setOnClickListener(new View.OnClickListener() {
            private int x;

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(helpMenu.this, helpWep.class);
                intent.putExtra("checkweb", "souvenir");
                startActivity(intent);
            }
        });
        bFestival.setOnClickListener(new View.OnClickListener() {
            private int x;

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(helpMenu.this, helpWep.class);
                intent.putExtra("checkweb", "festival");
                startActivity(intent);
            }
        });
    }
    private void showGPSDisabledAlertToUser(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("GPS is disabled in your device. Would you like to enable it?")
                .setCancelable(false)
                .setPositiveButton("Goto Settings Page To Enable GPS",
                        new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int id){
                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(callGPSSettingIntent);
                            }
                        });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

}
