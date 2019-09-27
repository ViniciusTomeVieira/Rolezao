package br.udesc.rolezao.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import br.udesc.rolezao.R;
import br.udesc.rolezao.helper.ConfiguracaoFirebase;
import br.udesc.rolezao.model.Usuario;

public class LoginActivity extends AppCompatActivity {

    private EditText campoEmail, campoSenha;
    private Button botaoEntrar;
    private ProgressBar progressBar;
    private Usuario usuario;
    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);
        verificarUsuarioLogado(); //Verificar se ja tem o usuario logado ou cadastrado
        inicializarComponentes();

        //Fazer login
        progressBar.setVisibility(View.GONE);
        botaoEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String textoEmail = campoEmail.getText().toString();
                String textoSenha = campoSenha.getText().toString();
                if(!textoEmail.isEmpty()){
                    if(!textoSenha.isEmpty()){
                        usuario = new Usuario();
                        usuario.setEmail(textoEmail);
                        usuario.setSenha(textoSenha);
                        validarLogin(usuario);
                    }else{
                        Toast.makeText(LoginActivity.this,"Digite a senha",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(LoginActivity.this,"Digite o email",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void verificarUsuarioLogado(){
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        if(autenticacao.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }
    }

    private void validarLogin(Usuario usuario) {
        progressBar.setVisibility(View.VISIBLE);
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();

        autenticacao.signInWithEmailAndPassword(
                usuario.getEmail(),
                usuario.getSenha()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    progressBar.setVisibility(View.GONE);
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    finish();
                }else{
                    Toast.makeText(LoginActivity.this,"Erro ao fazer login",Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }

    public void abrirCadastro(View view){
        Intent i = new Intent(LoginActivity.this, CadastroActivity.class);
        startActivity(i);
    }

    public void inicializarComponentes(){
        campoEmail = findViewById(R.id.editLoginEmail);
        campoSenha = findViewById(R.id.editLoginSenha);
        botaoEntrar = findViewById(R.id.buttonEntrar);
        progressBar = findViewById(R.id.progressLogin);
        campoEmail.requestFocus();
    }
}
