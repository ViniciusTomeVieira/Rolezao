package br.udesc.rolezao.activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import br.udesc.rolezao.LocalizacaoRoleActivity;
import br.udesc.rolezao.LocalizacaoUsuarioActivity;
import br.udesc.rolezao.R;
import br.udesc.rolezao.api.NotificacaoService;
import br.udesc.rolezao.helper.UsuarioFirebase;
import br.udesc.rolezao.model.MyCallback;
import br.udesc.rolezao.model.Notificacao;
import br.udesc.rolezao.model.NotificacaoDados;
import br.udesc.rolezao.model.Role;
import br.udesc.rolezao.model.Usuario;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
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
    private Role role = new Role();
    private DatabaseReference referencia;
    private static final String CONFIGURACOES_MAPA = "ConfiguracoesLocalRole";
    private SharedPreferences preferences;
    private static boolean active = false;
    private boolean ehCriador = false;
    private Retrofit retrofit;
    private String baseUrl;
    private Usuario usuarioCriador = new Usuario();
    private Usuario dadosUsuarioLogado;
    private StorageReference imagem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_role);
        active = true;
        //Pegar informações do banco
        FirebaseUser usuarioPerfil = UsuarioFirebase.getUsuarioAtual();
        dadosUsuarioLogado = UsuarioFirebase.getDadosUsuarioLogado();
        idCriadorRole = dadosUsuarioLogado.getId();
        referencia = FirebaseDatabase.getInstance().getReference();
        preferences = this.getSharedPreferences(CONFIGURACOES_MAPA,0);

        //Retrofit
        baseUrl = "https://fcm.googleapis.com/fcm/";
        retrofit = new Retrofit.Builder().baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Configura Toolbar
        final Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("dale");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Diz que é uma janela para voltar
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp); //Trocar icone

        initComponents();
        fillComponents();
        readData(new MyCallback() {
            @Override
            public void onCallback(Role roleInterface) {
                if(roleInterface != null && active){
                    toolbar.setTitle(roleInterface.getTitulo());
                    role = roleInterface;
                    fillComponents();
                }
            }

            @Override
            public void onCallbackUsuario(Usuario usuario) {
                if(usuario != null && active){
                    usuarioCriador = usuario;
                }
            }
        });
    }
    @Override
    public void onStop() {
        super.onStop();
        active = false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        active = false;
    }

    public void readData(final MyCallback myCallback) {
        DatabaseReference rolezada = referencia.child("roles").child("d3yy7LKUGFTxDZLgx5FBxOJIQv43");
        DatabaseReference usuariozada = referencia.child("usuarios").child("d3yy7LKUGFTxDZLgx5FBxOJIQv43");
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
        usuariozada.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Usuario usuariozada = dataSnapshot.getValue(Usuario.class);
                myCallback.onCallbackUsuario(usuariozada);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void fillComponents() {
        //Busca imagem no storage e coloca na ImageView
        if(!RoleActivity.this.isDestroyed()){
            StorageReference storageReference = FirebaseStorage.getInstance().getReference();
            imagem = storageReference.child("imagens").child("roles").child(role.getNomeFoto() + ".jpeg");
            Glide.with(RoleActivity.this).using(new FirebaseImageLoader()).load(imagem).into(fotoRole);
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
                                        role.salvarRole("d3yy7LKUGFTxDZLgx5FBxOJIQv43");
                                        participarDoRoleButton.setText("Voltar pro rolê");
                                        participarDoRoleButton.setBackgroundColor(Color.BLUE);
                                        Toast.makeText(getApplicationContext(),"Você saiu do rolê, que pena :(",Toast.LENGTH_SHORT).show();
                                    }else{
                                        usuarios.add(idCriadorRole);
                                        role.setPessoasConfirmadas(role.getPessoasConfirmadas() + 1);
                                        role.setUsuariosNoRole(usuarios);
                                        role.salvarRole("d3yy7LKUGFTxDZLgx5FBxOJIQv43");
                                        participarDoRoleButton.setText("Sair do rolê");
                                        participarDoRoleButton.setBackgroundColor(Color.RED);
                                        Toast.makeText(getApplicationContext(),"Você voltou pro rolê, daleeeee",Toast.LENGTH_SHORT).show();
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

    private void enviarNotificacao() {
        String token = preferences.getString("token","Miami");
        //Monta objeto notificacao
        String to = token; //Token
        Notificacao notificacao = new Notificacao("Você entrou no rolê!!!","Parabéns, você entrou no rolê " + role.getTitulo() + "!!");
        NotificacaoDados notificacaoDados = new NotificacaoDados(to,notificacao);

        NotificacaoService service = retrofit.create(NotificacaoService.class);
        Call<NotificacaoDados> call = service.salvarNotificacao(notificacaoDados);
        call.enqueue(new Callback<NotificacaoDados>() {
            @Override
            public void onResponse(Call<NotificacaoDados> call, Response<NotificacaoDados> response) {
                if(response.isSuccessful()){

                }
            }

            @Override
            public void onFailure(Call<NotificacaoDados> call, Throwable t) {

            }
        });
    }

    private void enviarNotificacaoParaCriador() {
        String token = usuarioCriador.getToken();
        //Monta objeto notificacao
        String to = token; //Token
        Notificacao notificacao = new Notificacao(dadosUsuarioLogado.getNome() + " entrou em " + role.getTitulo(),"Você tem mais um no rolê! Que legal! " + role.getTitulo() + "!!");
        NotificacaoDados notificacaoDados = new NotificacaoDados(to,notificacao);

        NotificacaoService service = retrofit.create(NotificacaoService.class);
        Call<NotificacaoDados> call = service.salvarNotificacao(notificacaoDados);
        call.enqueue(new Callback<NotificacaoDados>() {
            @Override
            public void onResponse(Call<NotificacaoDados> call, Response<NotificacaoDados> response) {
                if(response.isSuccessful()){

                }
            }

            @Override
            public void onFailure(Call<NotificacaoDados> call, Throwable t) {

            }
        });
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
                    role.salvarRole("d3yy7LKUGFTxDZLgx5FBxOJIQv43");
                    enviarNotificacao();
                    enviarNotificacaoParaCriador();
                    Toast.makeText(getApplicationContext(),"Você entrou no rolê, daleeeee",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
    @Override
     public boolean onSupportNavigateUp() {
        active = false;
        finish();
        return false;
    }
}
