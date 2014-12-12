package ufba.mypersonaltrainner.util;

import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

import ufba.mypersonaltrainner.TrainingDetail;

public class TreinosAtivos {

    public static void add(String treinoObjectID) {

        try {
            String uid = ParseUser.getCurrentUser().getObjectId();
            ParseQuery treinosQuery = ParseQuery.getQuery(PK.TREINO);
            ParseObject oTreino = treinosQuery.get(treinoObjectID);

            treinosQuery = ParseQuery.getQuery(PK.TREINO);
            treinosQuery.whereEqualTo(PK.TREINO_ESTADO_ATIVO, true);
            treinosQuery.whereEqualTo(PK.TREINO_USER, uid);

            int proxIndiceTreinosAtuais = treinosQuery.count();

            oTreino.put(PK.TREINO_ESTADO_ATIVO, true);
            oTreino.put(PK.TREINO_ATIVO_ORDEM, proxIndiceTreinosAtuais);
            oTreino.saveInBackground();
        } catch (ParseException e) {
            Log.e(TrainingDetail.class.getSimpleName(),
                    "erro em get treino|count treinos ativos|atualizar treino: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void remove(final String treinoID) {
        String uid = ParseUser.getCurrentUser().getObjectId();

        try {
            ParseObject oTreino = ParseQuery.getQuery(PK.TREINO).get(treinoID);
            final int posDepoisDoTreino = 1 + oTreino.getInt(PK.TREINO_ATIVO_ORDEM);
            oTreino.remove(PK.TREINO_ATIVO_ORDEM);
            oTreino.remove(PK.TREINO_ESTADO_ATIVO);
            oTreino.save();

            ParseQuery treinosQuery = ParseQuery.getQuery(PK.TREINO);
            treinosQuery.whereEqualTo(PK.TREINO_USER, uid);
            treinosQuery.whereEqualTo(PK.TREINO_ESTADO_ATIVO, true);
            treinosQuery.orderByAscending(PK.TREINO_ATIVO_ORDEM);
            treinosQuery.findInBackground(new FindCallback() {

                @Override
                public void done(List tList, ParseException e) {
                    if (e == null) {
                        for (int i = 0; i < tList.size(); i++) {
                            ParseObject treino = (ParseObject) tList.get(i);
                            treino.put(PK.TREINO_ATIVO_ORDEM, i);
                            treino.saveInBackground();
                        }
                    } else {
                        e.printStackTrace();
                    }
                }
            });
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
    }

    public static List<ParseObject> getAll() {
        try {
            String uid = ParseUser.getCurrentUser().getObjectId();
            ParseQuery treinosQuery = ParseQuery.getQuery(PK.TREINO);
            treinosQuery.whereEqualTo(PK.TREINO_USER, uid);
            treinosQuery.whereEqualTo(PK.TREINO_ESTADO_ATIVO, true);
            return treinosQuery.find();
        } catch (ParseException e) {
            Log.e(TrainingDetail.class.getSimpleName(),
                    "não deu pra get treino ou count treinos ativos ou atualizar o treino: " +
                            e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}

// TODO Tratar treinos ativos repetidos.