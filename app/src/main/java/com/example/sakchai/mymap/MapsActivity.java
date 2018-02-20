package com.example.sakchai.mymap;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.AlteredCharSequence;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sakchai.mymap.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private GoogleMap mMap;
    AlertDialog.Builder b;
    AlertDialog.Builder menu;
    AlertDialog.Builder edit;
    AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        b = new AlertDialog.Builder(this);
        menu = new AlertDialog.Builder(this);
        edit = new AlertDialog.Builder(this);


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnInfoWindowClickListener(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
            return;
        } else {
            mMap.setMyLocationEnabled(true);


        }


        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                final LatLng la = latLng;

                Log.i("Location", latLng.latitude + "," + latLng.longitude);

                final View addview = getLayoutInflater().inflate(R.layout.infowindows, null);

                b.setPositiveButton("Cancle", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                    }
                });
                b.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        LatLng target = new LatLng(la.latitude, la.longitude);
                        EditText ed = (EditText) addview.findViewById(R.id.editText);
                        MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.position(target);
                        markerOptions.title(ed.getText().toString());
                        Marker marker = mMap.addMarker(markerOptions);
                        dialog.cancel();


                    }
                });
                b.setView(addview);
                dialog = b.create();
                dialog.show();


            }
        });


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {


            } else {

            }
        }
    }

    @Override
    public void onInfoWindowClick(final Marker marker) {
        System.out.println(marker.getTitle());

        final View menuView = getLayoutInflater().inflate(R.layout.menu, null);

        Button btEdit = (Button) menuView.findViewById(R.id.button2);
        Button btDel = (Button) menuView.findViewById(R.id.button3);

        btEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final View edView = getLayoutInflater().inflate(R.layout.infowindows, null);
                final EditText ed = (EditText) edView.findViewById(R.id.editText);
                ed.setText(marker.getTitle());
                edit.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        marker.setTitle(ed.getText().toString());
                        marker.hideInfoWindow();
                        dialog.cancel();

                    }
                });
                edit.setNegativeButton("CANCEl", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                          dialog.cancel();
                    }
                });

                edit.setView(edView);
              
                dialog = edit.create();
                dialog.show();

            }
        });

        btDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                marker.remove();
                dialog.cancel();
            }
        });

        menu.setView(menuView);
        dialog = menu.create();
        dialog.show();


    }
}
