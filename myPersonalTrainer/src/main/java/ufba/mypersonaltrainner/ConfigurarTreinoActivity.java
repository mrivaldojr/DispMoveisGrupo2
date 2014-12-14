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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import ufba.mypersonaltrainner.model.Exercicio;
import ufba.mypersonaltrainner.model.Treino;
import ufba.mypersonaltrainner.util.C;
import ufba.mypersonaltrainner.util.PK;


public class ConfigurarTreinoActivity extends Activity {

    // private ArrayAdapter<Exercicio> adapterExercicios;
    private final String LOG_TAG = this.getClass().getSimpleName();
    private ListView mListViewExercicios;
    private ArrayAdapter<Exercicio> mAdapterExercicios;
    private boolean ehNovoTreino = false;
    private String idTreinoSelecionado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configurar_treino_my);

        Intent intentChamante = getIntent();
        String action = intentChamante.getAction();
        Treino treino = null;

        if (action.equals(C.ACTION_NOVO_TREINO)) {
            ehNovoTreino = true;
            idTreinoSelecionado = UUID.randomUUID().toString();
            getActionBar().setTitle(getString(R.string.action_novo_treino));
        } else if (action.equals(C.ACTION_EDIT_TREINO)) {
            idTreinoSelecionado = intentChamante.getStringExtra(C.EXTRA_TREINO_IDPARSE);
                    treino = new Treino(idTreinoSelecionado,
                    intentChamante.getStringExtra(C.EXTRA_TREINO_NOME));
            EditText nomeTreinoTextView = (EditText) findViewById(R.id.edt_nomeTreino);
            nomeTreinoTextView.setText(treino.getNome());
            getActionBar().setTitle(getString(R.string.action_editar_treino));
        }

        mListViewExercicios = (ListView) findViewById(R.id.list_execiciosAdicionados);
        mAdapterExercicios = new ArrayAdapter<Exercicio>(this,
                android.R.layout.simple_list_item_1, new ArrayList<Exercicio>());
        mListViewExercicios.setAdapter(mAdapterExercicios);

        mListViewExercicios.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getBaseContext(),
                        AdicionaExercicioAoTreinoActivity.class);
                intent.setAction(C.ACTION_EDIT_EXERCICIO);

                Exercicio exercicio = mAdapterExercicios.getItem(i);
                intent.putExtra(C.EXTRA_EXERCICIO_NOME, exercicio.getNome());
                intent.putExtra(C.EXTRA_EXERCICIO_SERIES, exercicio.getSeries());
                intent.putExtra(C.EXTRA_EXERCICIO_CARGA, exercicio.getCarga());
                intent.putExtra(C.EXTRA_ARRAY_INDEX, i);

                startActivityForResult(intent, C.EDITA_EXERCICIO_REQUEST);
            }
        });

        if (!ehNovoTreino) populateAdapterFromcloud(idTreinoSelecionado);
    }

    @Override
    protected void onResume() {
        super.onResume();
        testaVars(LOG_TAG, "onResume");
    }

    public void testaVars(String logtag, String metodo) {
        Log.v(logtag, metodo + "\n" +
                    "idTreinoSelecionado: " + idTreinoSelecionado + "\n" +
                "ehNovoTreino: " + ehNovoTreino + "\n" +
        "mAdapterExercicios" + printAdapterExercicio(mAdapterExercicios));
    }

    String printAdapterExercicio(ArrayAdapter<Exercicio> adapter) {
        String retString = "\nadapter:\n";
        for (int i = 0; i < adapter.getCount(); i++) {
            Exercicio exercicio = mAdapterExercicios.getItem(i);
            retString += Integer.toString(i) + " - " + exercicio.getNome() + ":\n" +
            "id: " + exercicio.getId() + "\n" +
            "series: " + exercicio.getSeries() + "\n" +
            "carga: " + exercicio.getCarga() + "\n";
            if (exercicio.isLoaded()) {
                ParseObject ex = exercicio.getPO();
                retString += "is Loaded\nLocal Name: " + ex.getString(PK.EXERCICIO_NOME) + "\n" +
                        "ObjectId: " + ex.getObjectId() + "\n" +
                        "ObjectId: " + ex.getObjectId() + "\n" +
                        "series: " + ex.getString(PK.EXERCICIO_SERIES) + "\n" +
                        "carga: " + ex.getString(PK.EXERCICIO_CARGA) + "\n";
            }
        }
        return retString;
    }

    // TODO CACHE
    public void onButtonSalvarTreinoClick(View view) {
        testaVars(LOG_TAG, "onButtonSalvarTreinoClick");

        EditText nomeEditText = (EditText) findViewById(R.id.edt_nomeTreino);
        String nomeTreino = nomeEditText.getText().toString();
        ParseObject treino = null;

        String uid = ParseUser.getCurrentUser().getObjectId();

        if(nomeTreino.equals("")){
            Toast toast = Toast.makeText(getBaseContext(), "Dê um Nome ao Treino.", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        if(mAdapterExercicios.getCount()==0){
            Toast toast = Toast.makeText(getBaseContext(), "Adicione exercícios.", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        if (ehNovoTreino) {
            treino = new ParseObject(PK.TREINO);
            treino.put(PK.USER_ID, uid);
        } else {
            try {
                ParseQuery<ParseObject> query = ParseQuery.getQuery(PK.TREINO);
                // query.setCachePolicy(ParseQuery.CachePolicy.CACHE_ELSE_NETWORK);
                treino = query.get(idTreinoSelecionado);
            } catch (Exception e) {
                Log.e(this.getClass().getSimpleName(), e.getMessage());
                e.printStackTrace();
            }
        }

        treino.put(PK.PIN_DATE, new Date(System.currentTimeMillis()));
        treino.put(PK.TREINO_NOME, nomeTreino);

        for (int i = 0; i < mAdapterExercicios.getCount(); i++) {

            Exercicio exercicio = mAdapterExercicios.getItem(i);
            ParseObject novoExercicio = exercicio.isLoaded() ? exercicio.getPO() :
                    new ParseObject(PK.EXERCICIO);
            novoExercicio.put(PK.EXERCICIO_NOME, exercicio.getNome());
            novoExercicio.put(PK.EXERCICIO_SERIES, exercicio.getSeries());
            novoExercicio.put(PK.EXERCICIO_CARGA, exercicio.getCarga());
            if (!exercicio.isLoaded()) treino.add(PK.EXERCICIO, novoExercicio);
        }


        try {
            treino.save();
        } catch (ParseException e) {
            erro(e);
            finish();
        }

        setResult(Activity.RESULT_OK);
        finish();
    }

    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        testaVars(LOG_TAG, "onActivityResult");
        if (resultCode == RESULT_OK) {

            String nome = data.getStringExtra(C.EXTRA_EXERCICIO_NOME);
            String series = data.getStringExtra(C.EXTRA_EXERCICIO_SERIES);
            String carga = data.getStringExtra(C.EXTRA_EXERCICIO_CARGA);
            if (requestCode == C.CRIA_EXERCICIO_REQUEST) {
                Exercicio exercicio = new Exercicio(UUID.randomUUID().toString(),
                        nome, series, carga);
                mAdapterExercicios.add(exercicio);
            } else if (requestCode == C.EDITA_EXERCICIO_REQUEST) { // então é editar treino
                int indiceToUpdate = data.getIntExtra(C.EXTRA_ARRAY_INDEX, -1);
                Exercicio velho = mAdapterExercicios.getItem(indiceToUpdate);
                mAdapterExercicios.remove(velho);
                mAdapterExercicios.insert(new Exercicio(velho.getId(), nome, series, carga),
                        indiceToUpdate);
            }
            mAdapterExercicios.notifyDataSetChanged();
        } else {
            // aqui captura outros request codes
        }
    }

    void populateAdapterFromcloud(String idTreinoSelecionado) {
        testaVars(LOG_TAG, "populateAdapterFromcloud");
        ParseUser user = ParseUser.getCurrentUser();
        ParseQuery<ParseObject> query = ParseQuery.getQuery(PK.TREINO);
        // query.setCachePolicy(ParseQuery.CachePolicy.CACHE_ELSE_NETWORK);
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

    void erro(ParseException e) {
        Log.e(LOG_TAG, "deu errado no parse, la vai mensagem e stack trace:");
        Log.e(LOG_TAG, e.getMessage());
        e.printStackTrace();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.configurar_treino_my, menu);
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

        if(id == R.id.action_novo_exercicio){
            Intent i = new Intent(this, AdicionaExercicioAoTreinoActivity.class);
            i.setAction(C.ACTION_NOVO_EXERCICIO);
            startActivityForResult(i, C.CRIA_EXERCICIO_REQUEST);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
