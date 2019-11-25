package br.udesc.rolezao.activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.drawable.Icon;
import android.media.tv.TvContract;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import br.udesc.rolezao.R;
import br.udesc.rolezao.fragment.ConfiguracoesFragment;
import br.udesc.rolezao.fragment.FeedFragment;
import br.udesc.rolezao.fragment.PerfilFragment;
import br.udesc.rolezao.helper.ConfiguracaoFirebase;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth autenticacao;




    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        // Configura Toolbar

        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("Rolezão");

        setSupportActionBar(toolbar);

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();

        //Configuração BottomNavigation
        configuraBottomNavigation();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,"1" )
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Rolezão criado!")
                .setContentText("Parabéns!!!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

// notificationId is a unique int for each notification that you must define
        notificationManager.notify(1, builder.build());

    }

    //Método responsável por criar a BottomNavigation

    private void configuraBottomNavigation(){
        BottomNavigationViewEx bottomNavigationViewEx = findViewById(R.id.bottomNavigation);

        //faz configurações iniciais do bottomNavigation

        //bottomNavigationViewEx.enableAnimation(true);
        //bottomNavigationViewEx.enableShiftingMode(true);
        //bottomNavigationViewEx.enableItemShiftingMode(true);
        //bottomNavigationViewEx.setTextVisibility(true);

        //Habilitar navegação
            habilitarNavegacao(bottomNavigationViewEx);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.viewPager, new FeedFragment()).commit();
    }

    /**
     * Método responsável por tratar eventos de click na BottomNavigation
     * @param viewEx
     */
    private void habilitarNavegacao(final BottomNavigationViewEx viewEx){
        viewEx.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                switch (menuItem.getItemId()){
                    case R.id.ic_home :
                        fragmentTransaction.replace(R.id.viewPager, new FeedFragment()).commit();
                        trocarCorItem(viewEx,0);
                        return true;
                    case R.id.ic_perfil :
                        fragmentTransaction.replace(R.id.viewPager, new PerfilFragment()).commit();
                        return true;
                    case R.id.ic_configuracoes :
                        fragmentTransaction.replace(R.id.viewPager, new ConfiguracoesFragment()).commit();
                        return true;
                }
                return false;
            }
        });
    }

    private void trocarCorItem(BottomNavigationViewEx viewEx, int position){
        Menu menu = viewEx.getMenu();
        MenuItem menuItem = menu.getItem(position);
        //Icon icon = new Icon(1);
        //menuItem.setIcon();
        //Continua...
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_sair:
                deslogar();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deslogar() {
        try {
            autenticacao.signOut();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}