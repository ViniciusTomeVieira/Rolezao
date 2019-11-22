package br.udesc.rolezao.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import br.udesc.rolezao.R;
import br.udesc.rolezao.helper.UsuarioFirebase;
import br.udesc.rolezao.model.Role;
import br.udesc.rolezao.model.Usuario;

import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class RoleActivity extends AppCompatActivity {

    private ImageView fotoRole;
    private TextView descricaoText,dataText, horaText, localText, valorText, pessoasConfirmadasText,verNoMapaText;
    private FloatingActionButton verNoMapaButton;
    private Button participarDoRoleButton; //Mostrar notificação
    private String idCriadorRole;
    private Role role = new Role();;
    private DatabaseReference referencia;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_role);
        //Pegar informações do banco
        FirebaseUser usuarioPerfil = UsuarioFirebase.getUsuarioAtual();
        final Usuario dadosUsuarioLogado = UsuarioFirebase.getDadosUsuarioLogado();
        idCriadorRole = dadosUsuarioLogado.getId();
        referencia = FirebaseDatabase.getInstance().getReference();
        DatabaseReference rolezada = referencia.child("roles").child(idCriadorRole);
        role.setTitulo("dale");

        // Configura Toolbar
        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle(role.getTitulo());
        System.out.println("Eu existo");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Diz que é uma janela para voltar
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp); //Trocar icone

        initComponents();
        fillComponents();
    }

    private void fillComponents() {
        //Busca imagem no storage e coloca na ImageView
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference imagem = storageReference.child("imagens").child("roles").child(idCriadorRole+".jpeg");
        Glide.with(RoleActivity.this).using(new FirebaseImageLoader()).load(imagem).into(fotoRole);

        /*descricaoText.setText(role.getDescricao());
        System.out.println( role.getDescricao());*/


    }

    private void initComponents() {
        fotoRole = findViewById(R.id.fotoRole);
        descricaoText = findViewById(R.id.descricaoText);
        dataText = findViewById(R.id.dataText);
        horaText = findViewById(R.id.horaText);
        localText = findViewById(R.id.localText);
        valorText = findViewById(R.id.valorText);
        pessoasConfirmadasText = findViewById(R.id.pessoasConfirmadasText);
        verNoMapaText = findViewById(R.id.verNoMapaText);
        verNoMapaButton = findViewById(R.id.verNoMapaButton);
        participarDoRoleButton = findViewById(R.id.participarRoleButton);
    }
}
