package ufba.mypersonaltrainner.model;

/**
 * Created by usuario on 11/18/2014.
 */
public class Exercicio {
    private String nome;
    private String series;
    private String carga;

    public Exercicio(String nome, String series, String carga) {
        this.nome = nome;
        this.series = series;
        this.carga = carga;
    }

    public String getNome() {
        return nome;
    }

    public String getSeries() {
        return series;
    }

    public String getCarga() {
        return carga;
    }

    @Override
    public String toString() {
        return "nome: " + nome + ", series: " + series + ", carga: " + carga;
    }
}
