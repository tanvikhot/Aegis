package com.example.aegis;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.service.autofill.Dataset;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;

import com.example.aegis.data.UserLocation;
import com.example.aegis.data.UserProfile;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private final DatabaseReference dbref = FirebaseDatabase.getInstance().getReference("userlocations");
    private final DatabaseReference upref = FirebaseDatabase.getInstance().getReference("userprofile");
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    public void editProfile(View view) {
        startActivity(new Intent(MapsActivity.this, ProfileActivity.class));
    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        final GoogleMap mMap = googleMap;


        LocationManager locationManagerCt = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (Check_FINE_LOCATION(this)) {
            final Location myLocation = locationManagerCt
                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);

            final LatLng myLatLng = new LatLng(myLocation.getLatitude(),
                    myLocation.getLongitude());


            googleMap.moveCamera(CameraUpdateFactory.newLatLng(myLatLng));

            // Zoom in the Google Map
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));

            final FirebaseUser currentUser = mAuth.getCurrentUser();
            dbref.child(currentUser.getUid()).setValue(new UserLocation(myLatLng.latitude, myLatLng.longitude));
//
            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
//            googleMap.addMarker(new MarkerOptions().position(myLatLng)
//                    .title("Me").snippet("I am here!")
//                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.placeholder)));

            dbref.orderByKey().addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    mMap.clear();
                    if (dataSnapshot == null)
                        return;
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String userId = snapshot.getKey();
                        final UserLocation userLocation = snapshot.getValue(UserLocation.class);
                        upref.child(userId).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Location location = new Location("point");
                                location.setLatitude(userLocation.getLatitude());
                                location.setLongitude(userLocation.getLongitude());
                                System.out.println(dataSnapshot.getKey() + " " + dataSnapshot.getValue());
                                UserProfile userProfile;
                                if (dataSnapshot.getValue() != null) {
                                    userProfile = dataSnapshot.getValue(UserProfile.class);
                                } else {
                                    userProfile = new UserProfile("Unknown", "Unknown");
                                }
                                LatLng latLng = new LatLng(userLocation.getLatitude(), userLocation.getLongitude());
                                mMap.addMarker(new MarkerOptions().position(latLng)
                                        .title(userProfile.getName())
                                        .snippet(String.format("This person is %.2f meters away from you", myLocation.distanceTo(location)))
                                        .icon(BitmapDescriptorFactory.fromResource((userProfile.getGender().equalsIgnoreCase(getString(R.string.male))?R.drawable.boy:(userProfile.getGender().equalsIgnoreCase(getString(R.string.female))?R.drawable.girl1:R.drawable.placeholder)))));
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });


                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }


    public static boolean Check_FINE_LOCATION(Activity act)
    {
        int result = ContextCompat.checkSelfPermission(act, Manifest.permission.ACCESS_FINE_LOCATION);
        return result == PackageManager.PERMISSION_GRANTED;
    }
}
