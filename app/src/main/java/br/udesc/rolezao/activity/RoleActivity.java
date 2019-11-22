package br.udesc.rolezao.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import br.udesc.rolezao.LocalizacaoRoleActivity;
import br.udesc.rolezao.LocalizacaoUsuarioActivity;
import br.udesc.rolezao.R;
import br.udesc.rolezao.helper.UsuarioFirebase;
import br.udesc.rolezao.model.MyCallback;
import br.udesc.rolezao.model.Role;
import br.udesc.rolezao.model.Usuario;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
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
    private String titulo = "dale";
    private Role role = new Role();;
    private DatabaseReference referencia;
    private static final String CONFIGURACOES_MAPA = "ConfiguracoesLocalRole";
    private SharedPreferences preferences;
    private static boolean active = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_role);
        active = true;
        //Pegar informações do banco
        FirebaseUser usuarioPerfil = UsuarioFirebase.getUsuarioAtual();
        final Usuario dadosUsuarioLogado = UsuarioFirebase.getDadosUsuarioLogado();
        idCriadorRole = dadosUsuarioLogado.getId();
        referencia = FirebaseDatabase.getInstance().getReference();
        preferences = this.getSharedPreferences(CONFIGURACOES_MAPA,0);

        // Configura Toolbar
        final Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("dale");
        System.out.println("Eu existo");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Diz que é uma janela para voltar
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp); //Trocar icone

        initComponents();
        fillComponents();
        readData(new MyCallback() {
            @Override
            public void onCallback(Role roleInterface) {
                toolbar.setTitle(roleInterface.getTitulo());
                role = roleInterface;
                fillComponents();
            }
        });
    }
    @Override
    public void onStop() {
        super.onStop();
        active = false;
    }

    public void readData(final MyCallback myCallback) {
        DatabaseReference rolezada = referencia.child("roles").child(idCriadorRole);
        rolezada.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //String value = dataSnapshot.child("titulo").getValue().toString();
                Role rolezada = dataSnapshot.getValue(Role.class);
                myCallback.onCallback(rolezada);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }
    private void fillComponents() {
        //Busca imagem no storage e coloca na ImageView
        if(this.active){
            StorageReference storageReference = FirebaseStorage.getInstance().getReference();
            StorageReference imagem = storageReference.child("imagens").child("roles").child(role.getNomeFoto() + ".jpeg");
            Glide.with(RoleActivity.this).using(new FirebaseImageLoader()).load(imagem).into(fotoRole);
            System.out.println(role.getNomeFoto());
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("latitude",role.getLatitude());
            editor.putString("longitude",role.getLongitude());
            editor.putString("titulo",role.getTitulo());
            System.out.println(role.getLatitude());
            System.out.println(role.getLongitude());
            System.out.println(role.getTitulo());
            editor.commit();
        }
        descricaoText.setText(role.getDescricao());
        dataText.setText(role.getDia() + "/" + role.getMes());
        horaText.setText(role.getHora());
        localText.setText(role.getLocal() + ", " + role.getNumero() + ", " + role.getCidade());
        valorText.setText("R$" + role.getDinheiro());
        pessoasConfirmadasText.setText(role.getPessoasConfirmadas() + "/" + role.getQuantidadeDePessoas());

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
        verNoMapaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i  = new Intent(getApplicationContext(), LocalizacaoRoleActivity.class);
                startActivity(i);
            }
        });
        participarDoRoleButton = findViewById(R.id.participarRoleButton);
    }
}
