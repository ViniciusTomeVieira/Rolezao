package br.udesc.rolezao;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.collection.LLRBNode;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private SharedPreferences preferences;
    private String[] permissoes = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};
    private static final String CONFIGURACOES_MAPA = "ConfiguracoesMapa";
    private LocationManager locationManager;
    private LocationListener locationListener;
    private TextView rua,numero,cidade,estado;
    private FloatingActionButton buttonOk;
    private boolean usuarioClicou = false;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("aguarde alguns segundos...");
        preferences = this.getSharedPreferences(CONFIGURACOES_MAPA,0);
        rua = findViewById(R.id.Rua);
        numero = findViewById(R.id.Numero);
        cidade = findViewById(R.id.Cidade);
        estado = findViewById(R.id.Estado);
        buttonOk = findViewById(R.id.buttonConfirmar);
        Permissoes.validarPermissoes(permissoes, this, 1);
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }
    /*SharedPreferences.Editor editor = preferences.edit();
    String cidadeAlterada = endereco.getSubAdminArea();
                        editor.putString("cidade",cidadeAlterada);
                        editor.commit();*/



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //Mudar exibição do mapa
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                // Add a marker in Sydney and move the camera
                Double latitude = location.getLatitude();
                Double longitude = location.getLongitude();
                toolbar.setTitle("Selecione o local do rolê");
                if(!usuarioClicou){
                    mMap.clear(); //Limpar mapa
                    LatLng localUsuario = new LatLng(latitude, longitude);
                    mMap.addMarker(new MarkerOptions().position(localUsuario).title("Sua localização").icon(BitmapDescriptorFactory.fromResource(R.drawable.maninapartydancingwithpeople)));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(localUsuario,18));
                    Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                    try {
                        List<Address> listaEndereco = geocoder.getFromLocation(latitude,longitude,1);
                        if(listaEndereco != null && listaEndereco.size() > 0){
                            Address endereco = listaEndereco.get(0);
                            rua.setText(endereco.getThoroughfare());
                            numero.setText(endereco.getFeatureName());
                            cidade.setText(endereco.getSubAdminArea());
                            estado.setText(endereco.getAdminArea());
                            SharedPreferences.Editor editor = preferences.edit();
                            String ruaAlterada = endereco.getThoroughfare();
                            String numeroAlterado = endereco.getFeatureName();
                            String cidadeAlterada = endereco.getSubAdminArea();
                            String estadoAlterado = endereco.getAdminArea();
                            editor.putString("rua",ruaAlterada);
                            editor.putString("numero",numeroAlterado);
                            editor.putString("cidade",cidadeAlterada);
                            editor.putString("estado",estadoAlterado);
                            editor.putString("latitude", endereco.getLatitude()+"");
                            editor.putString("longitude", endereco.getLongitude()+"");
                            editor.commit();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }


                /*
                 * Geocoding -> processo de transformar um endereco
                 * ou descricao de um local em latitude/longitude
                 * Reverse Geocoding -> processo de transformar latidude/longitude
                 * em um endereco
                 * */

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
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    5000,
                    10,
                    locationListener
            );
        }

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Double latitude = latLng.latitude;
                Double longitude = latLng.longitude;
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(latLng).title("Local do rolê").snippet("Seu rolê será marcado aqui")
                        /*.icon(
                        BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)*/
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.maninapartydancingwithpeople))
                );
                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                try {
                    List<Address> listaEndereco = geocoder.getFromLocation(latitude,longitude,1);
                    if(listaEndereco != null && listaEndereco.size() > 0){
                        Address endereco = listaEndereco.get(0);
                        rua.setText(endereco.getThoroughfare());
                        numero.setText(endereco.getFeatureName());
                        cidade.setText(endereco.getSubAdminArea());
                        estado.setText(endereco.getAdminArea());
                        SharedPreferences.Editor editor = preferences.edit();
                        String ruaAlterada = endereco.getThoroughfare();
                        String numeroAlterado = endereco.getFeatureName();
                        String cidadeAlterada = endereco.getSubAdminArea();
                        String estadoAlterado = endereco.getAdminArea();
                        editor.putString("rua",ruaAlterada);
                        editor.putString("numero",numeroAlterado);
                        editor.putString("cidade",cidadeAlterada);
                        editor.putString("estado",estadoAlterado);
                        editor.putString("latitude", endereco.getLatitude()+"");
                        editor.putString("longitude", endereco.getLongitude()+"");
                        editor.commit();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
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

        /*PolygonOptions polygonOptions = new PolygonOptions();
        //-27.0556224,-49.5352235              -27.0555894,-49.5371239
        polygonOptions.add(new LatLng(ibirama.latitude,ibirama.longitude));
        polygonOptions.add(new LatLng(-27.055726, -49.534565));
        polygonOptions.add(new LatLng(-27.056113, -49.538932));
        polygonOptions.strokeColor(Color.GREEN);
        polygonOptions.strokeWidth(10);
        polygonOptions.fillColor(Color.argb(128,255,153,0));
        mMap.addPolygon(polygonOptions);*/
       /* mMap.addMarker(new MarkerOptions().position(ibirama).title("GameLab")
                *//*.icon(
                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)*//*
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.maninapartydancingwithpeople))
        );
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ibirama,18));*/
    }
}
