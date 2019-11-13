package br.udesc.rolezao;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.collection.LLRBNode;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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

        //Mudar exibição do mapa
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        // Add a marker in Sydney and move the camera
        //-27.052094, -49.535063

        //Adicionando veneto clique
        final LatLng ibirama = new LatLng(-27.052094, -49.535063);
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Double latitude = latLng.latitude;
                Double longitude = latLng.longitude;
                //Toast.makeText(getApplicationContext(),"Latitude = " + latitude + "\nLongiude = " + longitude,Toast.LENGTH_SHORT).show();
                PolylineOptions polylineOptions = new PolylineOptions();
                polylineOptions.add(ibirama);
                polylineOptions.add(latLng);
                polylineOptions.color(Color.BLUE);
                polylineOptions.width(20);
                mMap.addPolyline(polylineOptions);
                mMap.addMarker(new MarkerOptions().position(latLng).title("Novo ponto").snippet("Festaaazadaaa")
                        /*.icon(
                        BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)*/
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.maninapartydancingwithpeople))
                );
            }
        });

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                Double latitude = latLng.latitude;
                Double longitude = latLng.longitude;
                Toast.makeText(getApplicationContext(),"Latitude = " + latitude + "\nLongiude = " + longitude,Toast.LENGTH_SHORT).show();

                mMap.addMarker(new MarkerOptions().position(latLng).title("Novo ponto").snippet("Festaaazadaaa")
                        /*.icon(
                        BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)*/
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.maninapartydancingwithpeople)));
            }
        });




        //Desenhando

        /*CircleOptions circleOptions = new CircleOptions();
        circleOptions.center(ibirama);
        circleOptions.radius(500);// em metros
        circleOptions.strokeWidth(10);
        circleOptions.strokeColor(Color.GREEN);
        circleOptions.fillColor(Color.argb(128,255,153,0));//0 a 255
        mMap.addCircle(circleOptions);*/

        PolygonOptions polygonOptions = new PolygonOptions();
        //-27.0556224,-49.5352235              -27.0555894,-49.5371239
        polygonOptions.add(new LatLng(ibirama.latitude,ibirama.longitude));
        polygonOptions.add(new LatLng(-27.055726, -49.534565));
        polygonOptions.add(new LatLng(-27.056113, -49.538932));
        polygonOptions.strokeColor(Color.GREEN);
        polygonOptions.strokeWidth(10);
        polygonOptions.fillColor(Color.argb(128,255,153,0));
        mMap.addPolygon(polygonOptions);
        mMap.addMarker(new MarkerOptions().position(ibirama).title("GameLab")
                /*.icon(
                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)*/
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.maninapartydancingwithpeople))
        );
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ibirama,18));
    }
}
