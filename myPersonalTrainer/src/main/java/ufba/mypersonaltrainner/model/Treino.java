package ufba.mypersonaltrainner.model;

import com.parse.ParseObject;

import java.util.ArrayList;

/**
 * Created by usuario on 11/26/2014.
 */
public class Treino {
    private String nome;
    private String id;
    private ParseObject treinoPO;
    private boolean ativo;
    private ArrayList<Exercicio> exercicios;

    public Treino(String id, String nome) {
        this.nome = nome;
        this.id = id;
    }

    public Treino(String nome, String id, ParseObject treinoPO) {
        this.nome = nome;
        this.id = id;
        this.treinoPO = treinoPO;
    }

    public Treino(String objectId) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public String getId() {
        return id;
    }

    public ParseObject getTreinoPO() {
        return treinoPO;
    }

    public boolean isLoaded() {
        return treinoPO != null;
    }

    public boolean isAtivo() {
        return ativo;
    }

    @Override
    public String toString() {
        return nome;
    }
}
