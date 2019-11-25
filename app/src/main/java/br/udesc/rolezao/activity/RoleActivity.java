package br.udesc.rolezao.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import br.udesc.rolezao.LocalizacaoRoleActivity;
import br.udesc.rolezao.LocalizacaoUsuarioActivity;
import br.udesc.rolezao.R;
import br.udesc.rolezao.helper.UsuarioFirebase;
import br.udesc.rolezao.model.MyCallback;
import br.udesc.rolezao.model.Role;
import br.udesc.rolezao.model.Usuario;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
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

import java.util.List;

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
    private boolean ehCriador = false;
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
                if(roleInterface != null){
                    toolbar.setTitle(roleInterface.getTitulo());
                    role = roleInterface;
                    fillComponents();
                }
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

            //Verifica se quem acessou é o criador do rolê
            if(role.getUsuariosNoRole().size() > 0){
                if (idCriadorRole.equals(role.getUsuariosNoRole().get(0))) {
                    ehCriador = true;
                    participarDoRoleButton.setText("Editar rolê");
                    participarDoRoleButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //Editar rolê
                            Intent i  = new Intent(getApplicationContext(), EditarRoleActivity.class);
                            startActivity(i);
                        }
                    });
                    verNoMapaText.setText("Excluir rolê");
                    verNoMapaButton.setImageResource(R.drawable.ic_delete_forever_white_24dp);
                    verNoMapaButton.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(),R.color.redzada)));
                    verNoMapaButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //Excluir rolê
                            AlertDialog.Builder msgBox = new AlertDialog.Builder(RoleActivity.this);
                            msgBox.setTitle("Excluir rolê");
                            //msgBox.setIcon(R.drawable.ic_delete_forever_white_24dp);
                            msgBox.setMessage("Tem certeza que deseja excluir o rolê?");
                            msgBox.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    role.removerRole(idCriadorRole);
                                    Toast.makeText(getApplicationContext(),"Rolê excluído com sucesso!",Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            });
                            msgBox.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                            msgBox.show();
                        }
                    });
            }else{
                    for(String usuario:role.getUsuariosNoRole()){
                        if(idCriadorRole.equals(usuario)){
                            participarDoRoleButton.setText("Sair do rolê");
                            participarDoRoleButton.setBackgroundColor(Color.RED);
                            participarDoRoleButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //Usuario sair do rolê
                                    List<String> usuarios = role.getUsuariosNoRole();
                                    boolean achou = false;
                                    for(String usuario:role.getUsuariosNoRole()){
                                        if(idCriadorRole.equals(usuario)){
                                            achou = true;
                                        }
                                    }
                                    if(achou){
                                        usuarios.remove(idCriadorRole);
                                        role.setPessoasConfirmadas(role.getPessoasConfirmadas() - 1);
                                        role.setUsuariosNoRole(usuarios);
                                        role.salvarRole(idCriadorRole);
                                        participarDoRoleButton.setText("Voltar pro rolê");
                                        participarDoRoleButton.setBackgroundColor(Color.BLUE);
                                        Toast.makeText(getApplicationContext(),"Você entrou no rolê, daleeeee",Toast.LENGTH_SHORT).show();
                                    }else{
                                        usuarios.add(idCriadorRole);
                                        role.setPessoasConfirmadas(role.getPessoasConfirmadas() + 1);
                                        role.setUsuariosNoRole(usuarios);
                                        role.salvarRole(idCriadorRole);
                                        participarDoRoleButton.setText("Sair do rolê");
                                        participarDoRoleButton.setBackgroundColor(Color.RED);
                                        Toast.makeText(getApplicationContext(),"Você entrou no rolê, daleeeee",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }
                }

            }
        }
        descricaoText.setText(role.getDescricao());
        dataText.setText(role.getDia() + "/" + role.getMes());
        horaText.setText(role.getHora());
        localText.setText(role.getLocal() + ", " + role.getNumero() + ", " + role.getCidade());
        valorText.setText("R$" + role.getDinheiro() / role.getPessoasConfirmadas());
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
        participarDoRoleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> usuarios = role.getUsuariosNoRole();
                boolean achou = false;
                for(String usuario:role.getUsuariosNoRole()){
                    if(idCriadorRole.equals(usuario)){
                        Toast.makeText(getApplicationContext(),"Você já está no rolê!!!",Toast.LENGTH_SHORT).show();
                        achou = true;
                    }
                }
                if(!achou){
                    usuarios.add(idCriadorRole);
                    role.setPessoasConfirmadas(role.getPessoasConfirmadas() + 1);
                    role.setUsuariosNoRole(usuarios);
                    role.salvarRole(idCriadorRole);
                    participarDoRoleButton.setText("Sair do rolê");
                    participarDoRoleButton.setBackgroundColor(Color.RED);
                    Toast.makeText(getApplicationContext(),"Você entrou no rolê, daleeeee",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
