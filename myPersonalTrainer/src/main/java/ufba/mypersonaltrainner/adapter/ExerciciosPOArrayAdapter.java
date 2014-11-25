package ufba.mypersonaltrainner.adapter;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.HashMap;
import java.util.List;

import ufba.mypersonaltrainner.util.ExercicioPO;

/**
 * Created by usuario on 11/25/2014.
 */
public final class ExerciciosPOArrayAdapter extends ArrayAdapter<ExercicioPO> {

    final int INVALID_ID = -1;

    HashMap<ExercicioPO, Integer> mIdMap = new HashMap<ExercicioPO, Integer>();

    public ExerciciosPOArrayAdapter(Context context, int textViewResourceId, List<ExercicioPO> exercicios) {
        super(context, textViewResourceId, exercicios);
        for (int i = 0; i < exercicios.size(); ++i) {
            mIdMap.put(exercicios.get(i), i);
        }
    }

    public void changeItem(ExercicioPO antigo, ExercicioPO novo) {
        int i = mIdMap.get(antigo);
        mIdMap.remove(antigo);
        mIdMap.put(novo, i);
    }

    @Override
    public long getItemId(int position) {
        if (position < 0 || position >= mIdMap.size()) {
            return INVALID_ID;
        }
        ExercicioPO item = getItem(position);
        return mIdMap.get(item);
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
