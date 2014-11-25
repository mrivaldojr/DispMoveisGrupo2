package ufba.mypersonaltrainner.util;

/**
 * Created by usuario on 11/25/2014.
 */
public class ExercicioPO {
    public final CharSequence objectId;
    public final CharSequence nome;
    public final CharSequence series;
    public final CharSequence carga;


    public ExercicioPO(CharSequence objectId, CharSequence nome, CharSequence series, CharSequence carga) {
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
