package com.example.firebaseapp;

public class Jogador {

    public String id;
    public String nomeCompleto;
    public String nomeCamiseta;
    public String idUsuario;

    public Jogador() {
    }

    public Jogador(String id, String nomeCompleto, String nomeCamiseta) {
        this.id = id;
        this.nomeCompleto = nomeCompleto;
        this.nomeCamiseta = nomeCamiseta;
    }

    public Jogador(String nomeCompleto, String nomeCamiseta) {
        this.nomeCompleto = nomeCompleto;
        this.nomeCamiseta = nomeCamiseta;
    }

    public Jogador(String id, String nomeCompleto, String nomeCamiseta, String idUsuario) {
        this.id = id;
        this.nomeCompleto = nomeCompleto;
        this.nomeCamiseta = nomeCamiseta;
        this.idUsuario = idUsuario;
    }

    public String toString(){
        return nomeCompleto + "\n" + nomeCamiseta;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    public String getNomeCamiseta() {
        return nomeCamiseta;
    }

    public void setNomeCamiseta(String nomeCamiseta) {
        this.nomeCamiseta = nomeCamiseta;
    }
}
