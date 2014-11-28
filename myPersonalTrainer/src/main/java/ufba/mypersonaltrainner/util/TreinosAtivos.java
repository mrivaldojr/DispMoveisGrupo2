package ufba.mypersonaltrainner.util;

import android.util.Log;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import ufba.mypersonaltrainner.TrainingDetail;

public class TreinosAtivos {
    public static void add(String treinoObjectID) {
        try {
            String uid = ParseUser.getCurrentUser().getObjectId();
            ParseQuery treinosQuery = ParseQuery.getQuery(PK.TREINO);
            treinosQuery.whereEqualTo(PK.TREINO_USER, uid);
            ParseObject oTreino = treinosQuery.get(treinoObjectID);

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


/*    public static List<ParseObject> getAll() {
        try {
            String uid = ParseUser.getCurrentUser().getObjectId();
            ParseQuery treinosQuery = ParseQuery.getQuery
            return treinosQuery.find();
        } catch (ParseException e) {
            Log.e(TrainingDetail.class.getSimpleName(),
                    "não deu pra get treino ou count treinos ativos ou atualizar o treino: " +
                            e.getMessage());
            e.printStackTrace();
        }
        return null;
    }*/

/*    public static void remove(String treinoID) {
        ParseUser user = ParseUser.getCurrentUser();
        List<ParseObject> treinosAtivos = user.getList(PK.USER_TREINOS_ATIVOS);
        for (ParseObject treinoAtivo : treinosAtivos) {
            ParseObject treino = (ParseObject) treinoAtivo.get(PK.ATIVO_TREINO);
            if (treino.getObjectId() == treinoID) {
                user.removeAll(PK.USER_TREINOS_ATIVOS, Arrays.asList(treinoAtivo));
                user.saveInBackground();
                break;
            }
        }
    }*/
}
