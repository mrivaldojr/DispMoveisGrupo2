package ufba.mypersonaltrainner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import ufba.mypersonaltrainner.model.Exercicio;
import ufba.mypersonaltrainner.util.C;
import ufba.mypersonaltrainner.util.PK;


public class TreinoDeHojeActivity extends Activity {

    private ParseQueryAdapter<ParseObject> mAdapter;
    private Button btFinalizar;
    private ArrayAdapter<Exercicio> mAdapterExercicios;
    private ListView mListViewExercicios;
    private ParseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.treino_de_hoje);
        btFinalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.getInt(PK.USER_INDICE_TREINO_ATUAL);
                ParseQuery<ParseObject> query = ParseQuery.getQuery(PK.TREINO);
                query.whereEqualTo(PK.TREINO_ESTADO_ATIVO, true);
                try {
                    int numTreinos = query.count();
                    int atual = user.getInt(PK.USER_INDICE_TREINO_ATUAL);
                    atual = (atual + 1) % numTreinos;
                    user.put(PK.USER_INDICE_TREINO_ATUAL, atual);
                    user.saveInBackground();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        btFinalizar = (Button) findViewById(R.id.button);

        ParseUser user = ParseUser.getCurrentUser();
        final int indiceTreinoDeHoje = user.getInt(PK.USER_INDICE_TREINO_ATUAL);


        final String uid = user.getObjectId();

        ParseQuery<ParseObject> query = ParseQuery.getQuery(PK.TREINO);
        query.whereEqualTo(PK.TREINO_USER, uid);
        query.whereEqualTo(PK.TREINO_ATIVO_ORDEM, indiceTreinoDeHoje);
        ParseObject treino = null;
        try {
            if (!user.containsKey(PK.USER_INDICE_TREINO_ATUAL)) {
                user.put(PK.USER_INDICE_TREINO_ATUAL, 0);
                user.save();
            }
            if (query.count() == 0) finish();
            treino = query.getFirst();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // TreinosParseAdaptert adaper = new TreinosParseAdapter(this);

        mListViewExercicios = (ListView) findViewById(R.id.lv_treinoDeHoje);
        mAdapterExercicios = new ArrayAdapter<Exercicio>(this,
                android.R.layout.simple_list_item_1, new ArrayList<Exercicio>());
        mListViewExercicios.setAdapter(mAdapterExercicios);

        mListViewExercicios.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getBaseContext(),
                        ExercicioActivity.class);

                Exercicio exercicio = mAdapterExercicios.getItem(i);
                intent.putExtra(C.EXTRA_EXERCICIO_NOME, exercicio.getNome());

                startActivity(intent);
            }
        });
        populateAdapterFromcloud(treino.getObjectId());
    }



    void populateAdapterFromcloud(String idTreinoSelecionado) {
        ParseUser user = ParseUser.getCurrentUser();
        ParseQuery<ParseObject> query = ParseQuery.getQuery(PK.TREINO);
        query.include(PK.EXERCICIO);
        query.whereEqualTo(PK.USER_ID, user.getObjectId());

        ParseObject treinoPO = null;
        try {
            treinoPO = query.get(idTreinoSelecionado);
        } catch (ParseException e) {
            Log.e(this.getClass().getSimpleName(), e.getMessage());
            e.printStackTrace();
        }

        List<ParseObject> exerciciosDaNuvem;
        exerciciosDaNuvem = treinoPO.getList(PK.EXERCICIO);
        ArrayList<Exercicio> exercicios = new ArrayList<Exercicio>();
        for (ParseObject exercicio : exerciciosDaNuvem) {
            String objectId = exercicio.getObjectId();
            String nome = exercicio.getString(PK.EXERCICIO_NOME);
            String reps = exercicio.getString(PK.EXERCICIO_SERIES);
            String carga = exercicio.getString(PK.EXERCICIO_CARGA);
            exercicios.add(new Exercicio(objectId, nome, reps, carga, exercicio));
        }
        mAdapterExercicios.addAll(exercicios);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.treino_de_hoje, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
