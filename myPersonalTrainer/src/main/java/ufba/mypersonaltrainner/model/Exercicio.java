package ufba.mypersonaltrainner.model;

/**
 * Created by usuario on 11/18/2014.
 */
public final class Exercicio {
    public CharSequence nome;
    public CharSequence series;
    public CharSequence carga;

    public Exercicio(CharSequence nome, CharSequence series, CharSequence carga) {
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
