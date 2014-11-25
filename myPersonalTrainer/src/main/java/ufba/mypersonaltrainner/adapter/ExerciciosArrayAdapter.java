package ufba.mypersonaltrainner.adapter;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ufba.mypersonaltrainner.ConfigurarTreinoActivity;
import ufba.mypersonaltrainner.model.Exercicio;
import ufba.mypersonaltrainner.util.ExercicioPO;

/**
 * Created by usuario on 11/25/2014.
 */
public class ExerciciosArrayAdapter extends ArrayAdapter<Exercicio> {

    final int INVALID_ID = -1;

    HashMap<Exercicio, Integer> mIdMap = new HashMap<Exercicio, Integer>();

    public ExerciciosArrayAdapter(Context context, int textViewResourceId, List<Exercicio> exercicios) {
        super(context, textViewResourceId, exercicios);
        for (int i = 0; i < exercicios.size(); ++i) {
            mIdMap.put(exercicios.get(i), i);
        }
    }

    @Override
    public long getItemId(int position) {
        if (position < 0 || position >= mIdMap.size()) {
            return INVALID_ID;
        }
        Exercicio item = getItem(position);
        return mIdMap.get(item);
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
