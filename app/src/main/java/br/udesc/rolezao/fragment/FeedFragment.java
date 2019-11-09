package br.udesc.rolezao.fragment;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import br.udesc.rolezao.R;
import br.udesc.rolezao.activity.CriarRoleActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class FeedFragment extends Fragment {

    private FloatingActionButton btnCigarro;

    public FeedFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_feed, container, false);
        btnCigarro = view.findViewById(R.id.btn_cigarro);

        btnCigarro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity().getApplicationContext(), CriarRoleActivity.class));
            }
        });
         return view;
    }
}