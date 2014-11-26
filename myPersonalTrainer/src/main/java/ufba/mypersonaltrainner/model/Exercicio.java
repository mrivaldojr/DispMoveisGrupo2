package ufba.mypersonaltrainner.model;

/**
 * Created by usuario on 11/18/2014.
 */
public final class Exercicio {
    public String nome;
    public String series;
    public String carga;

    public Exercicio(String nome, String String, String carga) {
        this.nome = nome;
        this.series = series;
        this.carga = carga;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Exercicio exercicio = (Exercicio) o;

        if (!carga.equals(exercicio.carga)) return false;
        if (!nome.equals(exercicio.nome)) return false;
        if (!series.equals(exercicio.series)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = nome.hashCode();
        result = 31 * result + series.hashCode();
        result = 31 * result + carga.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "nome: " + nome + ", series: " + series + ", carga: " + carga;
    }
}
