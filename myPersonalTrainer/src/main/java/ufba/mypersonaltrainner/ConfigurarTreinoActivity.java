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
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import ufba.mypersonaltrainner.adapter.ExerciciosArrayAdapter;
import ufba.mypersonaltrainner.model.Exercicio;
import ufba.mypersonaltrainner.util.C;
import ufba.mypersonaltrainner.util.DynamicListView;
import ufba.mypersonaltrainner.util.ExercicioPO;
import ufba.mypersonaltrainner.util.PK;


public class ConfigurarTreinoActivity extends Activity {


    // private ArrayAdapter<Exercicio> adapterExercicios;
    private final String LOG_TAG = this.getClass().getSimpleName();
    private ListView mListViewExercicios;
    private ArrayAdapter<ExercicioPO> mAdapterExercicios;
    private int mRequest;
    private List<ParseObject> mExerciciosPO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configurar_treino_my);

        Intent intentChamante = getIntent();
        final String idTreinoSelecionado = intentChamante.getStringExtra(C.EXTRA_TREINO_IDPARSE);
        final String nomeTreino = intentChamante.getStringExtra(C.EXTRA_TREINO_NOME);

        String action = intentChamante.getAction();
        if (action.equals(C.ACTION_NOVO_TREINO)) {
            mRequest = C.CRIA_TREINO_REQUEST;
            String novoTreinoTitle = getString(R.string.action_novo_treino);
            getActionBar().setTitle(novoTreinoTitle);
            mAdapterExercicios = new ArrayAdapter<ExercicioPO>(this,
                    android.R.layout.simple_list_item_1,
                    buscaListaExercicioDoParse(idTreinoSelecionado));
        } else if (action.equals(C.ACTION_EDIT_TREINO)) {
            mRequest = C.EDITA_TREINO_REQUEST;
            EditText nomeTreinoTextView = (EditText) findViewById(R.id.edt_nomeTreino);
            nomeTreinoTextView.setText(nomeTreino);
            String editaTreinoTitle = getString(R.string.action_editar_treino);
            getActionBar().setTitle(editaTreinoTitle);
            mAdapterExercicios = new ArrayAdapter<ExercicioPO>(this,
                    android.R.layout.simple_list_item_1, new ArrayList<ExercicioPO>());
        }

        mListViewExercicios = (ListView) findViewById(R.id.list_execiciosAdicionados);
                mListViewExercicios.setAdapter(mAdapterExercicios);

        mListViewExercicios.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getBaseContext(),
                        AdicionaExercicioAoTreinoActivity.class);
                ExercicioPO exercicio = (ExercicioPO) mListViewExercicios.getItemAtPosition(i);
                startActivityForResult(intent, C.EDITA_EXERCICIO_REQUEST);
            }
        });
    }

    ArrayList<ExercicioPO> buscaListaExercicioDoParse(String idTreino) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(PK.TREINO);
        query.include(PK.EXERCICIO);
        //query.fromPin(PK.GRP_TUDO);
        ParseObject treinoPO = null;
        try {
            treinoPO = query.get(idTreino);
        } catch(ParseException e) {
            Log.e(this.getClass().getSimpleName(), e.getMessage());
            e.printStackTrace();
        }
        mExerciciosPO = treinoPO.getList(PK.EXERCICIO);
        ArrayList<ExercicioPO> exercicios = new ArrayList<ExercicioPO>();
        for (ParseObject exercicio : mExerciciosPO) {
            String objectId = exercicio.getObjectId();
            String nome = exercicio.getString(PK.EXERCICIO_NOME);
            String reps = exercicio.getString(PK.EXERCICIO_SERIES);
            String carga = exercicio.getString(PK.EXERCICIO_CARGA);
            exercicios.add(new ExercicioPO(objectId, nome, reps, carga));
        }

        return exercicios;
    }

    public void salvarTreino(View view) {

        EditText nomeEditText = (EditText) findViewById(R.id.edt_nomeTreino);
        final String treinoNome = nomeEditText.getText().toString();

        final ParseObject treino = new ParseObject(PK.TREINO);
        treino.put(PK.PIN_DATE, new Date(System.currentTimeMillis()));
        treino.put(PK.TREINO_ID, UUID.randomUUID().toString());
        treino.put(PK.TREINO_NOME, treinoNome);

        for (int i = 0; i < mAdapterExercicios.getCount(); i++) {
            ExercicioPO exercicio = mAdapterExercicios.getItem(i);
            ParseObject novoExercicio = new ParseObject(PK.EXERCICIO);
            novoExercicio.put(PK.EXERCICIO_NOME, exercicio.nome);
            novoExercicio.put(PK.EXERCICIO_SERIES, exercicio.series);
            novoExercicio.put(PK.EXERCICIO_CARGA, exercicio.carga);
            treino.add(PK.EXERCICIO, novoExercicio);
            // TODO CACHE
        }

        //try {
            //treino.pin(PK.GRP_TUDO);
        treino.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Toast.makeText(getApplicationContext(), "Salvou!", Toast.LENGTH_LONG).show();
                } else {
                    erro(e);
                    finish();
                }
                /*if (e != null)  {
                    Log.v(LOG_TAG, "Pinou!");
                    Toast.makeText(getApplicationContext(), "Pinou!", Toast.LENGTH_LONG).show();
                    treino.pinInBackground(PK.GRP_SUJO);
                }*/
            }
        });
        setResult(Activity.RESULT_OK);
        finish();
        /*} catch (ParseException e) {
            erro(e);
            finish();
        }*/
    }

    void erro(ParseException e) {
        Log.e(LOG_TAG, "deu errado no parse, la vai mensagem e stack trace:");
        Log.e(LOG_TAG, e.getMessage());
        e.printStackTrace();
        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
    }

    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (requestCode == C.CRIA_EXERCICIO_REQUEST) {
            if (resultCode == RESULT_OK) {
                String idParse = data.getStringExtra(C.EXTRA_EXERCICIO_IDPARSE);
                String nome = data.getStringExtra(C.EXTRA_EXERCICIO_NOME);
                String series = data.getStringExtra(C.EXTRA_EXERCICIO_SERIES);
                String carga = data.getStringExtra(C.EXTRA_EXERCICIO_CARGA);

                mAdapterExercicios.add(new ExercicioPO(idParse, nome, series, carga));
                mAdapterExercicios.notifyDataSetChanged();
            }
        }
        if (requestCode == C.EDITA_EXERCICIO_REQUEST) {
            // TODO falta fazer esse...
            if (resultCode == RESULT_OK) {
                String idParse = data.getStringExtra(C.EXTRA_EXERCICIO_IDPARSE);
                String nome = data.getStringExtra(C.EXTRA_EXERCICIO_NOME);
                String series = data.getStringExtra(C.EXTRA_EXERCICIO_SERIES);
                String carga = data.getStringExtra(C.EXTRA_EXERCICIO_CARGA);

                mAdapterExercicios.add(new ExercicioPO(idParse, , series, carga));
                mAdapterExercicios.
                mAdapterExercicios.notifyDataSetChanged();
            }
        }
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
            Intent i = new Intent(this,AdicionaExercicioAoTreinoActivity.class);
            startActivityForResult(i, C.CRIA_EXERCICIO_REQUEST);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
