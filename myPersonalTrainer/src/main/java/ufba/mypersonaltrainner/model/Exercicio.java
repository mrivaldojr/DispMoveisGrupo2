package ufba.mypersonaltrainner.model;

import com.parse.ParseObject;

/**
 * Created by usuario on 11/18/2014.
 */
public final class Exercicio {
    private String nome;
    private String series;
    private String carga;
    private String id;
    private ParseObject PO;

    public Exercicio(String id, String nome, String carga, String series) {
        this.nome = nome;
        this.id = id;
        this.carga = carga;
        this.series = series;
    }

    public Exercicio(String id, String nome, String series, String carga, ParseObject PO) {
        this.nome = nome;
        this.series = series;
        this.carga = carga;
        this.id = id;
        this.PO = PO;
    }

    public Exercicio(String nome, String reps, String carga) {
        this.nome = nome;
        this.series = series;
        this.carga = carga;
    }

    public boolean isLoaded() {
        return PO != null;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public String getCarga() {
        return carga;
    }

    public void setCarga(String carga) {
        this.carga = carga;
    }

    public String getId() {
        return id;
    }

    public ParseObject getPO() {
        return PO;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Exercicio exercicio = (Exercicio) o;

        if (!carga.equals(exercicio.carga)) return false;
        if (!nome.equals(exercicio.nome)) return false;
        if (!id.equals(exercicio.id)) return false;
        if (!series.equals(exercicio.series)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = nome.hashCode();
        result = 31 * result + series.hashCode();
        result = 31 * result + carga.hashCode();
        result = 31 * result + id.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "nome: " + nome + ", series: " + series + ", carga: " + carga;
    }
}
