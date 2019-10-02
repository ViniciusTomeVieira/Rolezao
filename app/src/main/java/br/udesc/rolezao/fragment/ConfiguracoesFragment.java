package br.udesc.rolezao.fragment;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.udesc.rolezao.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConfiguracoesFragment extends Fragment {


    public ConfiguracoesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_configuracoes, container, false);
    }

}
