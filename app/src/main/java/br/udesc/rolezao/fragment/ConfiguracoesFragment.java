package br.udesc.rolezao.fragment;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import br.udesc.rolezao.LocalizacaoUsuarioActivity;
import br.udesc.rolezao.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConfiguracoesFragment extends Fragment {

    private Button buttonSalvar;
    private EditText editTextCidade;
    private RadioGroup radioGroupRoles;
    private RadioButton roleTodos,roleGratuitos;
    private static final String ARQUIVO_PREEFERENCIA = "ArquivoPreferencia";
    private SharedPreferences preferences;
    private TextView visualizar;

    public ConfiguracoesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_configuracoes, container, false);
        buttonSalvar = view.findViewById(R.id.buttonSalvarConfiguracoes);
        editTextCidade = view.findViewById(R.id.editTextCidade);
        radioGroupRoles = view.findViewById(R.id.radioGroupRoles);
        roleTodos = view.findViewById(R.id.radioButtonRoleTodos);
        roleGratuitos = view.findViewById(R.id.radioButtonRolesGratuitos);
        preferences = this.getActivity().getSharedPreferences(ARQUIVO_PREEFERENCIA,0);
        visualizar = view.findViewById(R.id.visualizarText);
        visualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i  = new Intent(getActivity().getApplicationContext(), LocalizacaoUsuarioActivity.class);
                startActivity(i);
            }
        });

        buttonSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = preferences.edit();
                String cidadeAlterada = editTextCidade.getText().toString();
                int roles = radioGroupRoles.getCheckedRadioButtonId();
                editor.putString("cidade",cidadeAlterada);
                editor.putInt("roles",roles);
                editor.commit();
                Toast.makeText(getActivity().getApplicationContext(),"Configurações salvas!",Toast.LENGTH_SHORT).show();
            }
        });
        //Recuperar dados salvos
        if(preferences.contains("cidade")){
            String cidade = preferences.getString("cidade","Ibirama");
            editTextCidade.setText(cidade);
        }else{
            editTextCidade.setText("Ibirama");
        }
        if(preferences.contains("roles")){
            if(preferences.getInt("roles", roleTodos.getId()) != roleTodos.getId()){
                roleGratuitos.setChecked(true);
            }else{
                roleTodos.setChecked(true);
            }
        }
        return view;
    }

}
