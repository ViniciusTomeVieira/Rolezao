package br.udesc.rolezao.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

import br.udesc.rolezao.R;

public class EditarPerfilActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);

        // Configura Toolbar

        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("Editar Perfil");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Diz que é uma janela para voltar
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white_24dp); //Trocar icone
    }
}
