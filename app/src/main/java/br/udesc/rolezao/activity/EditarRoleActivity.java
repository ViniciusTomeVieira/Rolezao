package br.udesc.rolezao.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import br.udesc.rolezao.MapsActivity;
import br.udesc.rolezao.R;
import br.udesc.rolezao.helper.ConfiguracaoFirebase;
import br.udesc.rolezao.helper.UsuarioFirebase;
import br.udesc.rolezao.model.MyCallback;
import br.udesc.rolezao.model.Role;
import br.udesc.rolezao.model.Usuario;

public class EditarRoleActivity extends AppCompatActivity {

    private Button buttonAumentar, buttonDiminuir, buttonCriarRole, buttonMaps, buttonAdicionarFoto;
    private EditText quantidadePessoas, editTextTitulo, editTextLocal, editTextDia,editTextMes,editTextHora,editTextDescricao,editTextQuantidadePessoas, editTextDinheiro;
    private CheckBox abriuMapa;
    private int quantidade = 0;
    private SharedPreferences preferences;
    private static final String CONFIGURACOES_MAPA = "ConfiguracoesMapa";
    private static final int SELECAO_GALERIA = 200;
    private Usuario usuarioLogado;
    private StorageReference storageRef;
    private String identificadorUsuario;
    private String urlFotoRole;
    private DatabaseReference referencia;
    private Role role = new Role();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_role);
        usuarioLogado = UsuarioFirebase.getDadosUsuarioLogado();
        storageRef = ConfiguracaoFirebase.getFirebaseStorage();
        identificadorUsuario = UsuarioFirebase.getIdentificadorUsuario();
        referencia = FirebaseDatabase.getInstance().getReference();

        // Configura Toolbar

        final Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("Editar Rolê");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Diz que é uma janela para voltar
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp); //Trocar icone


        inicializarComponentes();
        readData(new MyCallback() {
            @Override
            public void onCallback(Role roleInterface) {
                toolbar.setTitle(roleInterface.getTitulo());
                role = roleInterface;
                fillComponents();
            }
        });
    }

    private void fillComponents() {
        editTextTitulo.setText(role.getTitulo());
        editTextLocal.setText(role.getLocal());
        editTextDia.setText(role.getDia()+"");
        editTextMes.setText(role.getMes()+"");
        editTextHora.setText(role.getHora()+"");
        editTextDescricao.setText(role.getDescricao());
        quantidadePessoas.setText(role.getQuantidadeDePessoas()+"");
        editTextDinheiro.setText(role.getDinheiro()+"");
    }
    public void readData(final MyCallback myCallback) {
        DatabaseReference rolezada = referencia.child("roles").child(identificadorUsuario);
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
        abriuMapa = findViewById(R.id.localAdicionado);
        preferences = this.getSharedPreferences(CONFIGURACOES_MAPA,0);
        buttonAdicionarFoto = findViewById(R.id.buttonAdicionarFoto);
        buttonAdicionarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Adicionar Foto
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                if(i.resolveActivity(getPackageManager()) != null){
                    startActivityForResult(i,SELECAO_GALERIA);
                }
            }
        });
        buttonMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i  = new Intent(getApplicationContext(), MapsActivity.class);
                startActivity(i);
                abriuMapa.setChecked(true);
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
                                            if(abriuMapa.isChecked()) {
                                                if (!editTextDinheiro.getText().toString().equals("")) {//Dinheiro
                                                    validarFormulario(true);
                                                } else {//Dinheiro = 0
                                                    validarFormulario(false);
                                                }
                                            }else{
                                                Toast.makeText(getApplicationContext(),"Clica no mapa para colocar a localização, brother",Toast.LENGTH_SHORT).show();
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
        int dia = Integer.parseInt(editTextDia.getText().toString());
        int mes = Integer.parseInt(editTextMes.getText().toString());
        String hora = editTextHora.getText().toString();
        double dinheiroTotal = 0;
        try{
            dia = dia/1;
            mes = mes/1;
            if(dia > 0 && dia < 32){

            }else{
                Toast.makeText(getApplicationContext(),"Dia inválido",Toast.LENGTH_SHORT).show();
                dia = dia/0;
            }
            if(mes > 0 && mes < 13){

            }else{
                Toast.makeText(getApplicationContext(),"Mes inválido",Toast.LENGTH_SHORT).show();
                mes = mes/0;
            }
            if(hora.length() > 0 && hora.length() < 6 && hora.length() != 3){
                if(hora.length() == 4){
                    String hora1 = hora.substring(0,2);
                    String hora2 = hora.substring(2,4);
                    int hora1int = Integer.parseInt(hora1);
                    int hora2int = Integer.parseInt(hora2);
                    if(hora1int < 24 && hora2int < 60 ){
                        hora = hora1 + ":" + hora2;
                    }else{
                        Toast.makeText(getApplicationContext(),"Hora inválida",Toast.LENGTH_SHORT).show();
                        dia = dia/0;
                    }

                }else if(hora.length() == 5){
                    if(hora.substring(2,3).equals(":")){

                    }else{
                        Toast.makeText(getApplicationContext(),"Hora inválida",Toast.LENGTH_SHORT).show();
                        dia = dia/0;
                    }
                }else{
                    int horaint = Integer.parseInt(hora);
                    if(horaint < 24){
                        hora = hora + ":00";
                    }else{
                        Toast.makeText(getApplicationContext(),"Hora inválida",Toast.LENGTH_SHORT).show();
                        dia = dia/0;
                    }

                }
            }else{
                Toast.makeText(getApplicationContext(),"Hora inválida",Toast.LENGTH_SHORT).show();
                dia = dia/0;
            }
            if(b){
                double dinheiro = Double.parseDouble(editTextDinheiro.getText().toString());
                dinheiroTotal = dinheiro;
            }
            List<String> usuariosNoRole = new ArrayList<>();
            usuariosNoRole.add(identificadorUsuario);
            //Cadastrar rolê
            role.setDescricao(editTextDescricao.getText().toString());
            role.setDia(Integer.parseInt(editTextDia.getText().toString()));
            role.setDinheiro(Double.parseDouble(editTextDinheiro.getText().toString()));
            role.setHora(hora);
            role.setUsuariosNoRole(usuariosNoRole);
            role.setLocal(editTextLocal.getText().toString());
            role.setMes(Integer.parseInt(editTextMes.getText().toString()));
            role.setQuantidadeDePessoas(Integer.parseInt(editTextQuantidadePessoas.getText().toString()));
            role.setTitulo(editTextTitulo.getText().toString());
            role.setPessoasConfirmadas(1);
            role.setNomeFoto(urlFotoRole);
            Toast.makeText(getApplicationContext(),"Entrou no método",Toast.LENGTH_SHORT).show();
            if(preferences.contains("cidade")){
                String cidade = preferences.getString("cidade","Miami");
                role.setCidade(cidade);
            }
            if(preferences.contains("rua")){
                String rua = preferences.getString("rua","Miami");
                role.setLocal(rua);
            }
            if(preferences.contains("numero")){
                String numero = preferences.getString("numero","Miami");
                role.setNumero(numero);
            }
            if(preferences.contains("estado")){
                String estado = preferences.getString("estado","Miami");
                role.setEstado(estado);
            }
            if(preferences.contains("latitude")){
                String latitude = preferences.getString("latitude","Miami");
                role.setLatitude(latitude);
            }
            if(preferences.contains("longitude")){
                String longitude = preferences.getString("longitude","Miami");
                role.setLongitude(longitude);
            }
            try {
                Usuario usuarioAtual = UsuarioFirebase.getDadosUsuarioLogado();
                role.salvarRole(usuarioAtual.getId());
                Toast.makeText(getApplicationContext(),"Rolê criado com sucesso!",Toast.LENGTH_SHORT).show();

                finish();
            }catch (Exception ex){
                ex.printStackTrace();
            }

        }catch(Exception ex){

        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            Bitmap imagem = null;
            try{
                //Selecao apenas da galeria
                switch(requestCode){
                    case SELECAO_GALERIA:
                        Uri localImagemSelecionada = data.getData();
                        imagem = MediaStore.Images.Media.getBitmap(getContentResolver(),localImagemSelecionada);
                        break;
                }

                //Caso usuario tenha escolhido uma imagem

                if(imagem != null){
                    //buttonAdicionarFoto.setBackgroundColor(Color.GREEN);
                    buttonAdicionarFoto.setText("Foto adicionada");
                    //Configura imagem na tela

                    //imageEditarPerfil.setImageBitmap(imagem);
                    buttonAdicionarFoto.setBackgroundColor(Color.GREEN);
                    //Recuperar dados da imagem para o firebase
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    imagem.compress(Bitmap.CompressFormat.JPEG,70,baos);
                    byte[] dadosImagem = baos.toByteArray();
                    //Salvar imagem no firebase
                    Random random = new Random();
                    urlFotoRole = usuarioLogado.getId() + random.nextInt(100);
                    StorageReference imagemRef = storageRef.
                            child("imagens")
                            .child("roles")
                            .child(urlFotoRole + ".jpeg");
                    UploadTask uploadTask = imagemRef.putBytes(dadosImagem);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(EditarRoleActivity.this,"Erro ao fazer upload da imagem",Toast.LENGTH_SHORT).show();

                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            //Recuperar local da foto
                            Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                            while(!uri.isComplete());
                            Uri url = uri.getResult();
                            role.setCaminhoFoto(url.toString());

                        }
                    });
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}
