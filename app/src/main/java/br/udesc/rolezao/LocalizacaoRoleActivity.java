package br.udesc.rolezao;

import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LocalizacaoRoleActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private SharedPreferences preferences;
    private String[] permissoes = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};
    private static final String CONFIGURACOES_MAPA = "ConfiguracoesLocalRole";
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
        preferences = this.getSharedPreferences(CONFIGURACOES_MAPA,0);
        toolbar.setTitle(preferences.getString("titulo","Local do Rolê") + "(aguarde alguns segundos...)");
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
                if(!usuarioClicou){
                    mMap.clear(); //Limpar mapa
                    Double latitudezada = Double.parseDouble(preferences.getString("latitude",""));
                    Double longitudezada = Double.parseDouble(preferences.getString("longitude",""));
                    System.out.println("latitude: " + latitude);
                    System.out.println("longitude: " + longitude);
                    LatLng localUsuario = new LatLng(latitudezada, longitudezada);
                    mMap.addMarker(new MarkerOptions().position(localUsuario).title("Sua localização").icon(BitmapDescriptorFactory.fromResource(R.drawable.maninapartydancingwithpeople)));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(localUsuario,18));
                    toolbar.setTitle(preferences.getString("titulo","Local do Rolê"));
                    Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                    try {
                        List<Address> listaEndereco = geocoder.getFromLocation(latitudezada,longitudezada,1);
                        if(listaEndereco != null && listaEndereco.size() > 0){
                            Address endereco = listaEndereco.get(0);
                            rua.setText(endereco.getThoroughfare());
                            numero.setText(endereco.getFeatureName());
                            cidade.setText(endereco.getSubAdminArea());
                            estado.setText(endereco.getAdminArea());
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

            }
        });
    }
}
