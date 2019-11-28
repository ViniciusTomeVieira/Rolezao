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
                        if (r.getCidade().equals(preferenciasPeople.getString("cidade", "Ibirama"))) {
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