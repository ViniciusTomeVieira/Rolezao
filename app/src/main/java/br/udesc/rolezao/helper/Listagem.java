package br.udesc.rolezao.helper;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.recyclerview.widget.RecyclerView;
import br.udesc.rolezao.model.Role;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import java.util.Collections;
import java.util.List;

public class Listagem {

    private static final String ARQUIVO_PREEFERENCIA = "ArquivoPreferencia";
    private static SharedPreferences preferenciasPeople;

    public static void listar (ValueEventListener valueEventListener, DatabaseReference reference, final List list, final Object object, final RecyclerView.Adapter adapter, Context context) {
        preferenciasPeople = context.getSharedPreferences(ARQUIVO_PREEFERENCIA, 0);
        valueEventListener = reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren()) {
                    Role r = (Role) ds.getValue(object.getClass());
                    if(preferenciasPeople.contains("cidade")) {
                        if(preferenciasPeople.contains("roles")){
                            if(preferenciasPeople.getInt("roles",0) == 2131230982){
                                int preco = 0;
                                if (r.getCidade().equals(preferenciasPeople.getString("cidade", "Ibirama")) && r.getDinheiro() == preco) {
                                    boolean achouIgual = false;
                                    for(Object role: list){
                                        Role rolezada = (Role) role;
                                        if(rolezada.getIdCriador().equals(r.getIdCriador())){
                                            achouIgual = true;
                                        }
                                    }
                                    if(!achouIgual){
                                        list.add(r);
                                    }
                                }
                            }else{
                                if (r.getCidade().equals(preferenciasPeople.getString("cidade", "Ibirama"))) {
                                    boolean achouIgual = false;
                                    for(Object role: list){
                                        Role rolezada = (Role) role;
                                        if(rolezada.getIdCriador().equals(r.getIdCriador())){
                                            achouIgual = true;
                                        }
                                    }
                                    if(!achouIgual){
                                        list.add(r);
                                    }
                                }
                            }
                        }else{
                            if (r.getCidade().equals(preferenciasPeople.getString("cidade", "Ibirama"))) {
                                boolean achouIgual = false;
                                for(Object role: list){
                                    Role rolezada = (Role) role;
                                    if(rolezada.getIdCriador().equals(r.getIdCriador())){
                                        achouIgual = true;
                                    }
                                }
                                if(!achouIgual){
                                    list.add(r);
                                }
                            }
                        }

                    }else{
                        boolean achouIgual = false;
                        for(Object role: list){
                            Role rolezada = (Role) role;
                            if(rolezada.getIdCriador().equals(r.getIdCriador())){
                                achouIgual = true;
                            }
                        }
                        if(!achouIgual){
                            list.add(r);
                        }
                    }
                }
                Collections.reverse(list);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

}