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
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import ufba.mypersonaltrainner.model.Exercicio;
import ufba.mypersonaltrainner.util.PK;


public class ConfigurarTreinoActivity extends Activity {

    static final int CRIA_EXERCICIO_REQUEST = 0;
    static final String CHAVE_NOME = "ufba.mypersonaltrainner.chave";
    static final String CHAVE_SERIES = "ufba.mypersonaltrainner.series";
    static final String CHAVE_CARGA = "ufba.mypersonaltrainner.nome";
    private final String LOG_TAG = this.getClass().getSimpleName();
    private ListView listViewExercicios;
    private ArrayAdapter<Exercicio> adapterExercicios;

/*    public void mostraTreinos() {
        final ParseUser user = ParseUser.getCurrentUser();
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
    }*/

        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configurar_treino_my);

        // List<Exercicio> lst = new ArrayList<Exercicio>();

        adapterExercicios = new ArrayAdapter<Exercicio>(this,
                android.R.layout.simple_list_item_1, new ArrayList<Exercicio>());

        listViewExercicios = (ListView) findViewById(R.id.list_execiciosAdicionados);
        listViewExercicios.setAdapter(adapterExercicios);

        listViewExercicios.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getBaseContext(), TrainingDetail.class);
                startActivity(intent);
            }
        });
    }

    public void salvaTreino(View view) {

        EditText nomeEditText = (EditText) findViewById(R.id.edt_nomeTreino);
        final String treinoNome = nomeEditText.getText().toString();

        final ParseObject treino = new ParseObject(PK.TREINO);
        treino.put(PK.PIN_DATE, new Date(System.currentTimeMillis()));
        treino.put(PK.TREINO_ID, UUID.randomUUID().toString());
        treino.put(PK.TREINO_NOME, treinoNome);

        for (int i = 0; i < adapterExercicios.getCount(); i++) {
            Exercicio exercicio = adapterExercicios.getItem(i);
            ParseObject novoExercicio = new ParseObject(PK.EXERCICIO);
            novoExercicio.put(PK.EXERCICIO_NOME, exercicio.getNome());
            novoExercicio.put(PK.EXERCICIO_SERIES, exercicio.getSeries());
            novoExercicio.put(PK.EXERCICIO_CARGA, exercicio.getCarga());
            treino.add(PK.EXERCICIO, novoExercicio);
        }

        try {
            treino.pin(PK.GRP_TUDO);
            treino.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e != null)  {
                        treino.pinInBackground(PK.GRP_SUJO);
                    }
                }
            });
            Log.v(LOG_TAG, "Sucesso!");
            Toast.makeText(getApplicationContext(), "Sucesso!", Toast.LENGTH_LONG).show();
            setResult(Activity.RESULT_OK);
            finish();
        } catch (ParseException e) {
            erro(e);
            finish();
        }
    }

    void erro(ParseException e) {
        Log.e(LOG_TAG, "deu errado no parse, la vai mensagem e stack trace:");
        Log.e(LOG_TAG, e.getMessage());
        e.printStackTrace();
        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
    }

    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (requestCode == CRIA_EXERCICIO_REQUEST) {
            if (resultCode == RESULT_OK) {
                String nome = data.getStringExtra(CHAVE_NOME);
                String series = data.getStringExtra(CHAVE_SERIES);
                String carga = data.getStringExtra(CHAVE_CARGA);

                adapterExercicios.add(new Exercicio(nome, series, carga));
                adapterExercicios.notifyDataSetChanged();
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
            startActivityForResult(i, CRIA_EXERCICIO_REQUEST);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
