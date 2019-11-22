package br.udesc.rolezao.fragment;


import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseUser;

import br.udesc.rolezao.R;
import br.udesc.rolezao.activity.EditarPerfilActivity;
import br.udesc.rolezao.activity.MainActivity;
import br.udesc.rolezao.activity.RoleActivity;
import br.udesc.rolezao.helper.UsuarioFirebase;
import br.udesc.rolezao.model.Role;
import br.udesc.rolezao.model.Usuario;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class PerfilFragment extends Fragment {

    private TextView nomeUsuario, cidadeUsuario, nivelUsuario, conquistasUsuario, dinheiroUsuario;
    private ProgressBar progressBarUsuario;
    private CircleImageView fotoUsuario;
    private FloatingActionButton buttonEditarPerfil;
    private static final String ARQUIVO_PREEFERENCIA = "ArquivoPreferencia";
    private SharedPreferences preferences;


    public PerfilFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);

        //Configuração dos componentes
        nomeUsuario = view.findViewById(R.id.nomeUsuario);
        cidadeUsuario = view.findViewById(R.id.cidadeUsuario);
        nivelUsuario = view.findViewById(R.id.nivelUsuario);
        conquistasUsuario = view.findViewById(R.id.conquistasUsuario);
        dinheiroUsuario = view.findViewById(R.id.dinheiroUsuario);
        progressBarUsuario = view.findViewById(R.id.progressBarUsuario);
        fotoUsuario = view.findViewById(R.id.fotoEditarPerfil);
        buttonEditarPerfil = view.findViewById(R.id.buttonEditarPerfil);

        //Abrir edição do perfil

        buttonEditarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i  = new Intent(getActivity(), EditarPerfilActivity.class);
                startActivity(i);
            }
        });

         cidadeUsuario.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent i  = new Intent(getActivity(), RoleActivity.class);
                 startActivity(i);
             }
         });

        //Mostra os dados do usuario
        FirebaseUser usuarioPerfil = UsuarioFirebase.getUsuarioAtual();
        Usuario dadosUsuarioLogado = UsuarioFirebase.getDadosUsuarioLogado();

        //Colocando dados no perfil do usuario
        nomeUsuario.setText(usuarioPerfil.getDisplayName());
        nivelUsuario.setText(dadosUsuarioLogado.getNivel()+"");
        progressBarUsuario.setProgress(dadosUsuarioLogado.getExperiencia());
        conquistasUsuario.setText(dadosUsuarioLogado.getConquistas()+"");
        dinheiroUsuario.setText(dadosUsuarioLogado.getDinheiro()+"");


        Uri url = usuarioPerfil.getPhotoUrl();
        if(url != null){
            //Glide.with(PerfilFragment.this).load(url).into(fotoUsuario);
            Glide.with(getContext()).load(url).into(fotoUsuario);
        }else{
            fotoUsuario.setImageResource(R.drawable.avatar);
        }
        preferences = this.getActivity().getSharedPreferences(ARQUIVO_PREEFERENCIA,0);
        if(preferences.contains("cidade")){
            String cidade = preferences.getString("cidade","Ibirama");
            cidadeUsuario.setText(cidade);
        }else{
            cidadeUsuario.setText("Ibirama");
        }
        return view;
    }

}
