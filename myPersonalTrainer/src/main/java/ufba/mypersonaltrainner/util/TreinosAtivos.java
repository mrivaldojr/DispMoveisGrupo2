package ufba.mypersonaltrainner.util;

import android.util.Log;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.Arrays;
import java.util.List;

import ufba.mypersonaltrainner.TrainingDetail;

public class TreinosAtivos {

    public static void add(final String treinoObjectID) {

        final ParseUser user = ParseUser.getCurrentUser();
        String UID = user.getObjectId();

        ParseQuery<ParseObject> query = ParseQuery.getQuery(PK.TREINO);

        query.getInBackground(treinoObjectID, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject treinoPraAtivar, ParseException e) {
                if (e == null) {
                    final ParseObject treinoAtivo = new ParseObject(PK.ATIVO);
                    treinoAtivo.put(PK.ATIVO_TREINO, treinoPraAtivar);
                    Object treinosAtivosNoParse = user.get(PK.USER_TREINOS_ATIVOS);
                    int proxIndiceTreinosAtuais = (treinosAtivosNoParse == null) ? 0
                            : ((List)treinosAtivosNoParse).size();
                    treinoAtivo.put(PK.ATIVO_ORDEM, proxIndiceTreinosAtuais);
                    user.add(PK.USER_TREINOS_ATIVOS, treinoAtivo);
                   // treinoAtivo.saveInBackground();
                    user.saveInBackground();
                } else {
                    Log.e(TrainingDetail.class.getSimpleName(),
                            "não deu pra get um treino do parse" + e.getMessage());
                    e.printStackTrace();
                }
            }
        });
    }

    public static List<ParseObject> getAll() {
        ParseUser user = ParseUser.getCurrentUser();
        return user.getList(PK.USER_TREINOS_ATIVOS);
    }

    public static void remove(String treinoID) {
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
    }
}
