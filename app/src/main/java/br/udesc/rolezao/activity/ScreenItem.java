package br.udesc.rolezao.activity;

public class ScreenItem {
    String Titulo, descricao;
    int ScreenImg;

    public ScreenItem(String titulo, String descricao, int screenImg) {
        Titulo = titulo;
        this.descricao = descricao;
        ScreenImg = screenImg;
    }

    public String getTitulo() {
        return Titulo;
    }

    public void setTitulo(String titulo) {
        Titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getScreenImg() {
        return ScreenImg;
    }

    public void setScreenImg(int screenImg) {
        ScreenImg = screenImg;
    }
}
