package br.udesc.rolezao.model;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

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
    private String cidade;
    private String estado;
    private String numero;
    private String latitude, longitude;
    private String caminhoFoto;
    private int pessoasConfirmadas;
    private String nomeFoto;
    private List<String> usuariosNoRole = new ArrayList<>();
    public void salvarRole(String id){
        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebase();
        DatabaseReference rolesRef = firebaseRef.child("roles").child(id);
        rolesRef.setValue(this);
    }

    public List<String> getUsuariosNoRole() {
        return usuariosNoRole;
    }

    public void setUsuariosNoRole(List<String> usuariosNoRole) {
        this.usuariosNoRole = usuariosNoRole;
    }

    public int getPessoasConfirmadas() {
        return pessoasConfirmadas;
    }

    public void setPessoasConfirmadas(int pessoasConfirmadas) {
        this.pessoasConfirmadas = pessoasConfirmadas;
    }

    public String getNomeFoto() {
        return nomeFoto;
    }

    public void setNomeFoto(String nomeFoto) {
        this.nomeFoto = nomeFoto;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getCaminhoFoto() {
        return caminhoFoto;
    }

    public void setCaminhoFoto(String caminhoFoto) {
        this.caminhoFoto = caminhoFoto;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
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
