package br.udesc.rolezao.model;

import java.util.Date;

public class ModelListagemFeed {

    private String  titulo;
    private String local;
    private String descricao;
    private String caminhoimagem;
    private double coordX;
    private double coordY;
    private double preco;
    private int qtdPessoas;
    private int qtdPessoasConfirmadas;
    private Date dataInicio;
    private Date dataFim;

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

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getCaminhoimagem() {
        return caminhoimagem;
    }

    public void setCaminhoimagem(String caminhoimagem) {
        this.caminhoimagem = caminhoimagem;
    }

    public double getCoordX() {
        return coordX;
    }

    public void setCoordX(double coordX) {
        this.coordX = coordX;
    }

    public double getCoordY() {
        return coordY;
    }

    public void setCoordY(double coordY) {
        this.coordY = coordY;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public int getQtdPessoas() {
        return qtdPessoas;
    }

    public void setQtdPessoas(int qtdPessoas) {
        this.qtdPessoas = qtdPessoas;
    }

    public int getQtdPessoasConfirmadas() {
        return qtdPessoasConfirmadas;
    }

    public void setQtdPessoasConfirmadas(int qtdPessoasConfirmadas) {
        this.qtdPessoasConfirmadas = qtdPessoasConfirmadas;
    }

    public Date getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    public Date getDataFim() {
        return dataFim;
    }

    public void setDataFim(Date dataFim) {
        this.dataFim = dataFim;
    }

}