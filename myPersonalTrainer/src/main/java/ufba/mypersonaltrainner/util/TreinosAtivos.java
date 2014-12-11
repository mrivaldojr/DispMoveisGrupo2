package ufba.mypersonaltrainner.util;

import android.util.Log;

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
            treinosQuery.whereEqualTo(PK.USER_ID, uid);

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

    // TODO Tratar treinos ativos repetidos.

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

    public static void remove(final String treinoID) {
        String uid = ParseUser.getCurrentUser().getObjectId();
        final ParseQuery treinosQuery = ParseQuery.getQuery(PK.TREINO);

        treinosQuery.whereEqualTo(PK.TREINO_USER, uid);
        treinosQuery.whereEqualTo(PK.TREINO_ESTADO_ATIVO, true);
        treinosQuery.orderByAscending(PK.TREINO_ATIVO_ORDEM);

        try {
            List tList = treinosQuery.find();
            ParseObject oTreino = treinosQuery.get(treinoID);

            for (int i = 1 + oTreino.getInt(PK.TREINO_ATIVO_ORDEM);
                 i < tList.size(); i++)
            {
                ParseObject treino = (ParseObject) tList.get(i);
                treino.put(PK.TREINO_ATIVO_ORDEM, i - 1);
                treino.saveInBackground();
                Log.v(TrainingDetail.class.getSimpleName(),
                        "ativo: " + treino.getString(PK.TREINO_NOME));
            }
            oTreino.remove(PK.TREINO_ATIVO_ORDEM);
            oTreino.remove(PK.TREINO_ESTADO_ATIVO);
            oTreino.save();
        } catch (ParseException e) {
            Log.e(TrainingDetail.class.getSimpleName(), "não deu pra get treino: " +
                    e.getMessage());
            e.printStackTrace();
        }

        //treinosQuery.findInBackground(new FindCallback() {
        //
        //    @Override
        //    public void done(List tList, ParseException e) {
        //        if (e == null) {
        //
        //            try {
        //                ParseObject oTreino = treinosQuery.get(treinoID);
        //                int i = 1 + oTreino.getInt(PK.TREINO_ATIVO_ORDEM);
        //                for (; i < tList.size(); i++) {
        //                    ParseObject treino = (ParseObject) tList.get(i);
        //                    treino.put(PK.TREINO_ATIVO_ORDEM, i - 1);
        //                    treino.saveInBackground();
        //                    Log.v(TrainingDetail.class.getSimpleName(),
        //                            "ativo: " + treino.getString(PK.TREINO_NOME));
        //                }
        //                oTreino.remove(PK.TREINO_ATIVO_ORDEM);
        //                oTreino.remove(PK.TREINO_ESTADO_ATIVO);
        //                oTreino.saveInBackground();
        //            } catch (ParseException ex) {
        //                ex.printStackTrace();
        //            }
        //        } else {
        //            Log.e(TrainingDetail.class.getSimpleName(), "não deu pra get treino: " +
        //                    e.getMessage());
        //            e.printStackTrace();
        //        }
        //    }
        //});
    }

}
