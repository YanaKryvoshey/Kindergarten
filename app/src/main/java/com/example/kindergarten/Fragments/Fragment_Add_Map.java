package com.example.kindergarten.Fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.kindergarten.Activitys.NewGardenActivity;
import com.example.kindergarten.R;
import com.google.android.gms.common.internal.service.Common;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Fragment_Add_Map extends Fragment  {


    boolean flag = false;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.map_fragment, container, false);


        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.google_map);
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                getAllGardens(googleMap);
                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        googleMap.animateCamera(CameraUpdateFactory.zoomTo(50),300, null);
                        //float zoomLevel = 16.0f;
                        //googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel));
                        MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.position(latLng);
                        markerOptions.title(latLng.latitude + " : " + latLng.longitude);
                        Intent myIntent = new Intent(getActivity(), NewGardenActivity.class);
                        myIntent.putExtra(NewGardenActivity.LATITUDE, latLng.latitude);
                        myIntent.putExtra(NewGardenActivity.LONGITUDE, latLng.longitude);
                        startActivity(myIntent);
                        googleMap.clear();
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 20));
                        googleMap.addMarker(markerOptions);
                    }
                });
            }
        });
        
        return view;
    }

    private void getAllGardens(GoogleMap googleMap) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("gardens");
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        double latitude = ds.child("latitude").getValue(double.class);
                        double longituds = ds.child("longitude").getValue(double.class);
                        Log.d("pttt", "latitude: " + latitude + "   longituds: " + longituds);
                        LatLng latLng = new LatLng(latitude, longituds);
                        String name = ds.getKey();

                       googleMap.addMarker(new MarkerOptions().position(latLng).title(name));
                        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                    }
                    flag = true;
                } catch (Exception ex) {
                    Log.d("pttt", "Failed to read latitude and longituds.", ex);
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.d("pttt", "Failed to read latitude and longituds.", error.toException());
            }
        });
    }



}