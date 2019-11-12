package br.udesc.rolezao.model;

import com.google.firebase.database.DatabaseReference;

import br.udesc.rolezao.helper.ConfiguracaoFirebase;

public class Role {
    private String titulo;
    private String local;
    private int dia;
    private int mes;
    private String hora;
    private String descricao;
    private int quantidadeDePessoas;
    private double dinheiro;

    public void salvarRole(Usuario usuario){
        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebase();
        DatabaseReference rolesRef = firebaseRef.child("roles").child(usuario.getId());
        rolesRef.setValue(this);
    }



    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public int getDia() {
        return dia;
    }

    public void setDia(int dia) {
        this.dia = dia;
    }

    public int getMes() {
        return mes;
    }

    public void setMes(int mes) {
        this.mes = mes;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getQuantidadeDePessoas() {
        return quantidadeDePessoas;
    }

    public void setQuantidadeDePessoas(int quantidadeDePessoas) {
        this.quantidadeDePessoas = quantidadeDePessoas;
    }

    public double getDinheiro() {
        return dinheiro;
    }

    public void setDinheiro(double dinheiro) {
        this.dinheiro = dinheiro;
    }
}