package br.udesc.rolezao.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import br.udesc.rolezao.R;
import br.udesc.rolezao.activity.CriarRoleActivity;
import br.udesc.rolezao.activity.RoleActivity;
import br.udesc.rolezao.helper.UsuarioFirebase;
import br.udesc.rolezao.model.ModelListagemFeed;
import br.udesc.rolezao.model.Role;

public class AdapterFeed extends RecyclerView.Adapter<AdapterFeed.MyViewHolder> {

    private List<Role> listagem = new ArrayList();
    private Context context;
    private static final String CONFIGURACOES_MAPA = "ConfiguracoesLocalRole";
    private SharedPreferences preferences;


    public AdapterFeed(List<Role> listagem, Context context) {
        this.listagem = listagem;
        this.context = context;
        preferences = context.getSharedPreferences(this.CONFIGURACOES_MAPA, 0);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.postagem_layout, parent, false);
        return new MyViewHolder(viewLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Role listagemFeed = listagem.get(position);
        holder.descricao.setText(listagemFeed.getDescricao());
        holder.dataFim.setText("");
        holder.dataInicio.setText(listagemFeed.getDia() + "/"+ listagemFeed.getMes() + " "+ listagemFeed.getHora());
        holder.local.setText(listagemFeed.getLocal());
        holder.preco.setText(String.valueOf(listagemFeed.getDinheiro()));
        holder.qtdPessoas.setText(listagemFeed.getPessoasConfirmadas() + "/" + listagemFeed.getQuantidadeDePessoas());
        holder.tvTitulo.setText(listagemFeed.getTitulo());

        if(listagemFeed.getCaminhoFoto() != null){
            Glide.with(context).load(listagemFeed.getCaminhoFoto()).into(holder.imagem);
        }else{
            holder.imagem.setImageResource(R.drawable.backgrounddegrade);
        }

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("idCriador", listagemFeed.getIdCriador());

        editor.commit();
        holder.imagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, RoleActivity.class));
            }
        });

    }

    @Override
    public int getItemCount() {
        return listagem.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView tvTitulo;
        public TextView local;
        public TextView descricao;
        public ImageView imagem;
        public TextView preco;
        public TextView qtdPessoas;
        public TextView dataInicio;
        public TextView dataFim;
        public CardView cardPostagem;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitulo = itemView.findViewById(R.id.titulo_postagem);
            local = itemView.findViewById(R.id.local_role);
            descricao = itemView.findViewById(R.id.descricao_role);
            imagem = itemView.findViewById(R.id.imagem_role);
            qtdPessoas = itemView.findViewById(R.id.pessoas_confirmadas);
            preco = itemView.findViewById(R.id.preco);
            dataInicio = itemView.findViewById(R.id.data_inicio);
            dataFim = itemView.findViewById(R.id.data_fim_role);
            cardPostagem = itemView.findViewById(R.id.card_view_postagem);
        }
    }

}
