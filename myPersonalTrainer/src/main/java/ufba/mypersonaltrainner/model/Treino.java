package ufba.mypersonaltrainner.model;

/**
 * Created by usuario on 11/26/2014.
 */
public class Treino {
    private String nome;
    private String objectId;
    private boolean ativo;

    public Treino(String nome, String objectId, boolean ativo) {
        this.nome = nome;
        this.objectId = objectId;
        this.ativo = ativo;
    }

    public String getNome() {
        return nome;
    }

    public String getObjectId() {
        return objectId;
    }

    public boolean isAtivo() {
        return ativo;
    }

    @Override
    public String toString() {
        return nome;
    }
}
