package ufba.mypersonaltrainner.util;

/**
 * Created by usuario on 11/25/2014.
 */
public class ExercicioPO {
    public final String objectId;
    public final String nome;
    public final String series;
    public final String carga;


    public ExercicioPO(String objectId, String nome, String series, String carga) {
        this.objectId = objectId;
        this.nome = nome;
        this.series = series;
        this.carga = carga;
    }

    @Override
    public String toString() {
        return "nome: " + nome +
                ", series: " + series +
                ", carga: " + carga;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ExercicioPO that = (ExercicioPO) o;

        if (objectId != null ? !objectId.equals(that.objectId) : that.objectId != null)
            return false;

        return true;
    }

}
