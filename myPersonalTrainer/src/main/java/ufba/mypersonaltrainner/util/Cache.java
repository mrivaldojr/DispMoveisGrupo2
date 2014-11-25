package ufba.mypersonaltrainner.util;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.List;

/**
 * Created by usuario on 11/25/2014.
 */
public class Cache {

    // TODO sync inteligente (mas cuidado com condições de corrida...)
    // TODO query só pra ver se tem algo no cache, tem jeito melhor?

    public static void limpaache() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(PK.TREINO);
        query.fromLocalDatastore();
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (e == null)
                    ParseObject.unpinAllInBackground(parseObjects);
                else {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void showTreinos(String pinGroup, String logtag) {
        try {
            ParseQuery<ParseObject> query = ParseQuery.getQuery(PK.TREINO);
            query.fromPin(pinGroup);
            List<ParseObject> treinos = query.find();
            for (final ParseObject treino : treinos) {
                String t = "==DEBUG==\n";
                t += "nome: " + treino.getString(PK.TREINO_NOME) + "\nexercicios:\n";
                List<ParseObject> exercicios = treino.getList(PK.EXERCICIO);

                for (ParseObject exercicio : exercicios) {
                    t += "n: " + exercicio.getString(PK.EXERCICIO_NOME) + "\n";
                    t += "s: " + exercicio.getString(PK.EXERCICIO_SERIES) + "\n";
                    t += "c: " + exercicio.getString(PK.EXERCICIO_CARGA) + "\n";
                }
                t += treino.getDate(PK.PIN_DATE) + "\n";
                Log.w(logtag, t);
                treino.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        treino.unpinInBackground(PK.GRP_SUJO);
                    }
                });
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public static boolean conectado(Activity activity) {
        ConnectivityManager cm = (ConnectivityManager) activity
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return (ni != null) && (ni.isConnected());
    }

    public static void salvaTreinosSujos(final Activity activity) {
        // Salva os objetos do grupo de sujos no parse cloud

        ParseQuery<ParseObject> query = ParseQuery.getQuery(PK.TREINO);
        query.fromPin(PK.GRP_SUJO);

        query.findInBackground(new FindCallback<ParseObject>() {

            @Override
            public void done(List<ParseObject> treinosSujos, ParseException exception) {
                if (exception != null) return;
                if (!treinosSujos.isEmpty()) {
                    Toast.makeText(activity.getApplicationContext(),
                            "Upando os treinos sujos...", Toast.LENGTH_SHORT).show();
                }
                for (ParseObject treino : treinosSujos) {
                    try {
                        treino.save();
                        treino.unpin(PK.GRP_SUJO);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public static void carregaTreinos() {
        // Pega treinos do parse e bota no datastore local
        // Bloqueia UI

        ParseQuery<ParseObject> query = ParseQuery.getQuery(PK.TREINO);
        query.fromPin(PK.GRP_TUDO);
        query.setLimit(1);
        try {
            if (query.count() != 0) return;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        query = ParseQuery.getQuery(PK.TREINO);
        try {
            List<ParseObject> treinosDoParse = query.find();
            for (ParseObject treino : treinosDoParse) {
                treino.pin(PK.GRP_TUDO);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public static void carregaTiposExercicios() {
        // Carrega tipos de exercicios do parse e passa pro datastore local
        // Bloqueia UI
        ParseQuery<ParseObject> query = ParseQuery.getQuery(PK.TIPO_EXERCICIO);
        query.fromPin(PK.GRP_TIPO_EXERCICIO);
        query.setLimit(1);

        try {
            if (query.count() == 0) {
                query = ParseQuery.getQuery(PK.TIPO_EXERCICIO);
                List<ParseObject> tiposExercicios = query.find();
                ParseObject.pinAll(PK.GRP_TIPO_EXERCICIO, tiposExercicios);
            }
        } catch(ParseException e) {
            e.printStackTrace();
        }
    }
}
