package com.example.logingoogle;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class SelectLocationActivity extends AppCompatActivity implements
        GoogleMap.OnCameraMoveListener, GoogleMap.OnCameraIdleListener,
        OnMapReadyCallback {

    private GoogleMap mapa;
    ImageView imgPinUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_location);

        imgPinUp = findViewById(R.id.imgLocationPinUp);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onCameraIdle() {
        imgPinUp.setVisibility(View.GONE);
        MarkerOptions markerOptions = new MarkerOptions().position(mapa.getCameraPosition().target)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_pickup));
        mapa.addMarker(markerOptions);
    }

    @Override
    public void onCameraMove() {
        mapa.clear();
        imgPinUp.setVisibility(View.VISIBLE);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapa = googleMap;
        LatLng UPV = new LatLng(20.375469, -99.983367); //Nos ubicamos en el tec
        mapa.addMarker(new MarkerOptions().position(UPV).title("Marca en ITSJR").draggable(false));
        mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(UPV,16f));
        mapa.setOnCameraMoveListener(this);
        mapa.setOnCameraIdleListener(this);
    }
}
