package br.udesc.rolezao.fragment;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import br.udesc.rolezao.R;
import br.udesc.rolezao.activity.CriarRoleActivity;
import br.udesc.rolezao.adapter.AdapterFeed;
import br.udesc.rolezao.helper.ConfiguracaoFirebase;
import br.udesc.rolezao.helper.Listagem;
import br.udesc.rolezao.model.ModelListagemFeed;
import br.udesc.rolezao.model.Role;

/**
 * A simple {@link Fragment} subclass.
 */
public class FeedFragment extends Fragment {

    private FloatingActionButton btnCigarro;
    private RecyclerView recyclerFeed;
    private AdapterFeed adapterFeed;
    private DatabaseReference postagemRef = ConfiguracaoFirebase.getFirebase().child("roles");
    private ValueEventListener valueEventListener;
    private List<Role> listagem = new ArrayList();
    private static final String ARQUIVO_PREEFERENCIA = "ArquivoPreferencia";
    private static SharedPreferences preferenciasPeople;


    public FeedFragment() {
    // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_feed, container, false);
        btnCigarro = view.findViewById(R.id.btn_cigarro);

        recyclerFeed = view.findViewById(R.id.recycler_feed);
        recyclerFeed.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        adapterFeed = new AdapterFeed(listagem,getActivity());
        recyclerFeed.setAdapter(adapterFeed);

        btnCigarro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity().getApplicationContext(), CriarRoleActivity.class));
            }
        });
         return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        preferenciasPeople = getActivity().getApplicationContext().getSharedPreferences(ARQUIVO_PREEFERENCIA, 0);
        if(!preferenciasPeople.contains("cidade")) {
            SharedPreferences.Editor editor = preferenciasPeople.edit();
            editor.putString("cidade", "Ibirama");
            editor.commit();
        }
        if(listagem.isEmpty()) {
           Listagem.listar(valueEventListener, postagemRef, listagem, new Role(), adapterFeed, this.getContext());
        }
    }

}