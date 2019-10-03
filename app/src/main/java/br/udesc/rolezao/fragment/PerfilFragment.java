package br.udesc.rolezao.fragment;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import br.udesc.rolezao.R;
import br.udesc.rolezao.activity.EditarPerfilActivity;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class PerfilFragment extends Fragment {

    private TextView nomeUsuario, cidadeUsuario, nivelUsuario, conquistasUsuario, dinheiroUsuario;
    private ProgressBar progressBarUsuario;
    private CircleImageView fotoUsuario;
    private FloatingActionButton buttonEditarPerfil;


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
        fotoUsuario = view.findViewById(R.id.fotoUsuario);
        buttonEditarPerfil = view.findViewById(R.id.buttonEditarPerfil);

        //Abrir edição do perfil

        buttonEditarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i  = new Intent(getActivity(), EditarPerfilActivity.class);
                startActivity(i);
            }
        });

        return view;
    }

}
