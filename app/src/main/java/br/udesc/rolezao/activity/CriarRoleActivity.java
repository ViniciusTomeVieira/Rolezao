package br.udesc.rolezao.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import br.udesc.rolezao.MapsActivity;
import br.udesc.rolezao.R;
import br.udesc.rolezao.helper.UsuarioFirebase;
import br.udesc.rolezao.model.Role;
import br.udesc.rolezao.model.Usuario;

public class CriarRoleActivity extends AppCompatActivity {

    private Button buttonAumentar, buttonDiminuir, buttonCriarRole, buttonMaps;
    private EditText quantidadePessoas, editTextTitulo, editTextLocal, editTextDia,editTextMes,editTextHora,editTextDescricao,editTextQuantidadePessoas, editTextDinheiro;
    private int quantidade = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criar_role);

        // Configura Toolbar

        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("Criar Rolê");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Diz que é uma janela para voltar
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp); //Trocar icone

        inicializarComponentes();
    }

    private void inicializarComponentes() {
        quantidadePessoas = findViewById(R.id.quantidadePessoas); //Feito
        editTextHora = findViewById(R.id.editTextHora); //Feito
        editTextDia = findViewById(R.id.editTextDia); //Feito
        editTextMes = findViewById(R.id.editTextMes); //Feito
        editTextLocal = findViewById(R.id.editTextLocal); //Feito
        editTextTitulo = findViewById(R.id.editTextTitulo); //Feito
        editTextDescricao = findViewById(R.id.editTextDescricao); //Feito
        editTextQuantidadePessoas = findViewById(R.id.quantidadePessoas); //Feito
        editTextDinheiro = findViewById(R.id.editTextDinheiro); //Feito
        buttonAumentar = findViewById(R.id.buttonAumentar);
        buttonDiminuir = findViewById(R.id.buttonDiminuir);
        buttonCriarRole = findViewById(R.id.buttonCriarRole);
        buttonMaps = findViewById(R.id.buttonMaps);
        buttonMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i  = new Intent(getApplicationContext(), MapsActivity.class);
                startActivity(i);
            }
        });
        buttonAumentar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantidade = Integer.parseInt(quantidadePessoas.getText().toString()) + 1;
                quantidadePessoas.setText(quantidade+"");
            }
        });
        buttonDiminuir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantidade = Integer.parseInt(quantidadePessoas.getText().toString()) - 1;
                quantidadePessoas.setText(quantidade+"");
            }
        });
        buttonCriarRole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Validar todos os campos
                if(quantidade > 0){ //Pessoas no rolê
                    if(!editTextHora.getText().toString().equals("")){//Hora
                        if(!editTextDia.getText().toString().equals("")){//Dia
                            if(!editTextMes.getText().toString().equals("")){//Mês
                                if(!editTextLocal.getText().toString().equals("")){//Local
                                    if(!editTextDescricao.getText().toString().equals("")){//Descricao
                                        if(!editTextTitulo.getText().toString().equals("")){//Titulo
                                            if(!editTextDinheiro.getText().toString().equals("")){//Dinheiro
                                                validarFormulario(true);
                                            }else{//Dinheiro = 0
                                                validarFormulario(false);
                                            }
                                        }else{
                                            Toast.makeText(getApplicationContext(),"Cadê o título???",Toast.LENGTH_SHORT).show();
                                        }
                                    }else{
                                        Toast.makeText(getApplicationContext(),"Coloca a descrição brother",Toast.LENGTH_SHORT).show();
                                    }
                                }else{
                                    Toast.makeText(getApplicationContext(),"Coloca o local amigão",Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                Toast.makeText(getApplicationContext(),"Coloca o mês camarada",Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(getApplicationContext(),"Esqueceu o dia??",Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(getApplicationContext(),"Não vai colocar a hora??",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(),"Precisa de pelo menos um no role, né?",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void validarFormulario(boolean b) {
        Toast.makeText(getApplicationContext(),"Entrou no método",Toast.LENGTH_SHORT).show();
        int dia = Integer.parseInt(editTextDia.getText().toString());
        int mes = Integer.parseInt(editTextMes.getText().toString());
        double dinheiroTotal = 0;
        try{
            dia = dia/1;
            mes = mes/1;
            if(b){
                double dinheiro = Double.parseDouble(editTextDinheiro.getText().toString());
                dinheiro = dinheiro / 1;
                dinheiroTotal = dinheiro;
            }
            //Cadastrar rolê

            Role role = new Role();
            role.setDescricao(editTextDescricao.getText().toString());
            role.setDia(Integer.parseInt(editTextDia.getText().toString()));
            role.setDinheiro(dinheiroTotal);
            role.setHora(editTextHora.getText().toString());
            role.setLocal(editTextLocal.getText().toString());
            role.setMes(Integer.parseInt(editTextMes.getText().toString()));
            role.setQuantidadeDePessoas(Integer.parseInt(editTextQuantidadePessoas.getText().toString()));
            role.setTitulo(editTextTitulo.getText().toString());
            Toast.makeText(getApplicationContext(),"Setou os valores do rolê",Toast.LENGTH_SHORT).show();
            try {
                Toast.makeText(getApplicationContext(),"Entrou no try",Toast.LENGTH_SHORT).show();
                Usuario usuarioAtual = UsuarioFirebase.getDadosUsuarioLogado();
                role.salvarRole(usuarioAtual);
                Toast.makeText(getApplicationContext(),"Rolê criado com sucesso!",Toast.LENGTH_SHORT).show();

                finish();
            }catch (Exception ex){
                Toast.makeText(getApplicationContext(),"Catch interno",Toast.LENGTH_SHORT).show();
                ex.printStackTrace();
            }

        }catch(Exception ex){
            Toast.makeText(getApplicationContext(),"Catchzada",Toast.LENGTH_SHORT).show();
            ex.printStackTrace();
        }
    }

    @Override
     public boolean onSupportNavigateUp() {
     finish();
     return false;
    }
}