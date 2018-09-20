package com.vinayakspot.mobilechallenge;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class DeliveryDetailActivity extends AppCompatActivity implements OnMapReadyCallback {

    LatLng loc_latlng;
    Double loc_lat;
    Double loc_lng;

    TextView text_desc, text_address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_detail);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        text_desc = (TextView)findViewById(R.id.text_desc);
        text_address = (TextView)findViewById(R.id.text_address);
        text_desc.setText(getIntent().getExtras().getString("text_desc"));
        text_address.setText(getIntent().getExtras().getString("text_address"));

        loc_lat = Double.parseDouble(getIntent().getExtras().getString("loc_latitude"));
        loc_lng = Double.parseDouble(getIntent().getExtras().getString("loc_longitude"));

        loc_latlng = new LatLng(loc_lat, loc_lng);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        googleMap.addMarker(new MarkerOptions().position(loc_latlng));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(loc_latlng));

        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

}
