package com.valyrian.scanandpay;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private static final int LOCATION_REQUEST_CODE =1 ;
    LocationManager mLocationManager;
    LocationListener mLocationListener;
    private FusedLocationProviderClient fusedLocationClient;
    Double lat,lng;
    List<ReminderDetails> reminder;
    public void addStaticReminder(){

        reminder = new ArrayList<>();
        ReminderDetails listItem = new ReminderDetails(new LatLng(12.9396882, 77.6899813),80.0,"Welcome to Walmart Store");
        reminder.add(listItem);
        listItem = new ReminderDetails(new LatLng(12.9371619,77.6951926),80.0,"Welcome to Walmart Store");
        reminder.add(listItem);
        listItem = new ReminderDetails(new LatLng(12.9323275,77.6851886),80.0,"Welcome to Walmart Store");
        reminder.add(listItem);
        listItem = new ReminderDetails(new LatLng(12.9269913,77.69195139999999),80.0,"Welcome to Walmart Store");
        reminder.add(listItem);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        addStaticReminder();

    }
    public static BitmapDescriptor bitmapDescriptorFromVector(Context context, @DrawableRes int vectorDrawableResourceId) {
        Drawable background = ContextCompat.getDrawable(context, R.drawable.walmart_lightning);
        background.setBounds(0, 0, background.getIntrinsicWidth(), background.getIntrinsicHeight());
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorDrawableResourceId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth() , vectorDrawable.getIntrinsicHeight() );
        Bitmap bitmap = Bitmap.createBitmap(background.getIntrinsicWidth(), background.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        background.draw(canvas);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    public void makeCircle(LatLng latlng, String s)
    {
        Marker marker = mMap.addMarker(new MarkerOptions().position(latlng).title("Store "+s));
        marker.setIcon(bitmapDescriptorFromVector(this, R.drawable.walmart_lightning));
        //marker.showInfoWindow();

        Double radius = 80.0;
        mMap.addCircle(new CircleOptions()
                .center(latlng)
                .radius(radius)
                .strokeColor(ContextCompat.getColor(this, R.color.colorAccent))
                .fillColor(ContextCompat.getColor(this, R.color.colorPrimary)));

    }


    private void getLocation() {
        LatLng storeA = new LatLng(12.9396882, 77.6899813);
        LatLng storeB = new LatLng(12.9371619,77.6951926);
        LatLng storeC = new LatLng(12.9323275,77.6851886);
        LatLng storeD = new LatLng(12.9269913,77.69195139999999);
       // mMap.addMarker(new MarkerOptions().position(storeA).title("Store A"));
       // mMap.addMarker(new MarkerOptions().position(storeB).title("Store B"));
        //mMap.addMarker(new MarkerOptions().position(storeC).title("Store C"));
        //mMap.addMarker(new MarkerOptions().position(storeD).title("Store D"));
        makeCircle(storeA,"A");
        makeCircle(storeB,"B");
        makeCircle(storeC,"C");
        makeCircle(storeD,"D");

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {


            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION)){
                new AlertDialog.Builder(this)
                        .setTitle(getResources().getString(R.string.locationDialogTitle))
                        .setMessage(getResources().getString(R.string.locationDialogMessage))
                        .setPositiveButton(getResources().getString(R.string.allow), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(MapsActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION
                                        ,Manifest.permission.ACCESS_COARSE_LOCATION},LOCATION_REQUEST_CODE);
                            }
                        }).setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Toast.makeText(MapsActivity.this,getResources().getString(R.string.permissionDenid),Toast.LENGTH_LONG);
                    }
                }).create().show();

            }else{
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION
                        ,Manifest.permission.ACCESS_COARSE_LOCATION},LOCATION_REQUEST_CODE);
            }

        } else {
            fusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    lat=location.getLatitude();
                    lng=location.getLongitude();
                    LatLng curLocation = new LatLng(location.getLatitude(),location.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(curLocation).title(getResources().getString(R.string.MyLocation)));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(curLocation,15));
                }
            });
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            fusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    LatLng curLocation = new LatLng(location.getLatitude(),location.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(curLocation).title(getResources().getString(R.string.MyLocation)));
                    mMap.moveCamera(CameraUpdateFactory.zoomIn());
                }
            });
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
        mMap = googleMap;
        getLocation();
        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    public void myLocationButton(View v)
    {
        mMap.clear();
        getLocation();
    }
    public void nearbyStores(View v)
    {
        final float[] results1= new float[5];
        float[] results = new float[1];
        String[] stores = {"Store A","Store B","Store C","Store D"};
        final DecimalFormat form = new DecimalFormat("0.00");
        for(int i=0;i<4;i++)
        {
            Location.distanceBetween(lat,lng,reminder.get(i).latLng.latitude,reminder.get(i).latLng.longitude,results);
            results1[i]=results[0];
        }

        for(int i=0;i<4;i++) {

            if(results1[i]<=80.0){

                android.app.AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new android.app.AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new android.app.AlertDialog.Builder(this);
                }
                final String store = stores[i];
                builder.setTitle("Welcome To Walmart!")
                        .setMessage("You are nearby "+ store + ".\n Do you want to shop?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(MapsActivity.this, MainActivity.class);
                                intent.putExtra("storeName", store);
                                startActivity(intent);
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("No,Show me Nearby stores", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                results1[0]=results1[0]/(float)1000.0;
                                results1[1]=results1[1]/(float)1000.0;
                                results1[2]=results1[2]/(float)1000.0;
                                results1[3]=results1[3]/(float)1000.0;
                                Intent intent = new Intent(MapsActivity.this, StoreList.class);
                                intent.putExtra("storeDist1", form.format(results1[0]));
                                intent.putExtra("storeDist2", form.format(results1[1]));
                                intent.putExtra("storeDist3", form.format(results1[2]));
                                intent.putExtra("storeDist4", form.format(results1[3]));
                                startActivity(intent);
                                dialog.dismiss();
                            }
                        })
                        .show();
                return;
            }
        }

        results1[0]=results1[0]/(float)1000.0;
        results1[1]=results1[1]/(float)1000.0;
        results1[2]=results1[2]/(float)1000.0;
        results1[3]=results1[3]/(float)1000.0;

        Intent intent = new Intent(this, StoreList.class);
        intent.putExtra("storeDist1", form.format(results1[0]));
        intent.putExtra("storeDist2", form.format(results1[1]));
        intent.putExtra("storeDist3", form.format(results1[2]));
        intent.putExtra("storeDist4", form.format(results1[3]));
        startActivity(intent);

    }
}