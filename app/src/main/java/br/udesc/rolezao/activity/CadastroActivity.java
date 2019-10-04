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
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

import br.udesc.rolezao.R;
import br.udesc.rolezao.helper.ConfiguracaoFirebase;
import br.udesc.rolezao.model.Usuario;

public class CadastroActivity extends AppCompatActivity {

    private EditText campoNome, campoEmail, campoSenha;
    private Button botaoCadastrar;
    private ProgressBar progressBar;
    private Usuario usuarioMain;
    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        inicializarComponentes();

        //CadastroUsuario
        progressBar.setVisibility(View.GONE);
        botaoCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textoNome = campoNome.getText().toString();
                String textoEmail = campoEmail.getText().toString();
                String textoSenha = campoSenha.getText().toString();

                if(!textoNome.isEmpty()){
                    if(!textoEmail.isEmpty()){
                        if(!textoSenha.isEmpty()){
                            usuarioMain = new Usuario();
                            usuarioMain.setNome(textoNome);
                            usuarioMain.setEmail(textoEmail);
                            usuarioMain.setSenha(textoSenha);
                            cadastrarUsuario(usuarioMain);
                        }else{
                            Toast.makeText(CadastroActivity.this,"Preencha a senha!",Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(CadastroActivity.this,"Preencha o email!",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(CadastroActivity.this,"Preencha o nome!",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public void cadastrarUsuario(Usuario usuario){
        progressBar.setVisibility(View.VISIBLE);
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.createUserWithEmailAndPassword(
                usuario.getEmail(),
                usuario.getSenha()
        ).addOnCompleteListener(
                this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            try{
                                progressBar.setVisibility(View.GONE);
                                //Salvar dados no firebase

                                String idUsuario = task.getResult().getUser().getUid();
                                usuarioMain.setId(idUsuario);
                                usuarioMain.setCidade("Ibirama");
                                usuarioMain.setConquistas(0);
                                usuarioMain.setDinheiro(0.0);
                                usuarioMain.setNivel(1);
                                usuarioMain.salvar();
                                Toast.makeText(CadastroActivity.this,"Cadastro com sucesso!",Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                finish();
                            }catch(Exception e){
                                e.printStackTrace();
                            }

                        }else{
                            progressBar.setVisibility(View.GONE);
                            String erroExcecao = "";
                            try{
                                throw  task.getException();
                            }catch (FirebaseAuthWeakPasswordException e){
                                erroExcecao = "Digite uma senha mais forte";
                            }catch (FirebaseAuthInvalidCredentialsException e){
                                erroExcecao = "Digite um email valido";
                            }catch (FirebaseAuthUserCollisionException e){
                                erroExcecao = "Conta já cadastrada";
                            }catch (Exception e){
                                erroExcecao = "ao cadastrar usuário: " + e.getMessage();
                                e.printStackTrace();
                            }
                            Toast.makeText(CadastroActivity.this,"Erro: " + erroExcecao,Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
    }

    public void inicializarComponentes(){
        campoNome = findViewById(R.id.editCadastroNome);
        campoEmail = findViewById(R.id.editCadastroEmail);
        campoSenha = findViewById(R.id.editCadastroSenha);
        botaoCadastrar = findViewById(R.id.buttonEntrar);
        progressBar = findViewById(R.id.progressCadastro);

        campoNome.requestFocus();
    }
}
