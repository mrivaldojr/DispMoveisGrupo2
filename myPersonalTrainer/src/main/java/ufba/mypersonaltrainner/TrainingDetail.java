package ufba.mypersonaltrainner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import ufba.mypersonaltrainner.adapter.ListMeuTreinoAdapter;
import ufba.mypersonaltrainner.util.PK;


public class TrainingDetail extends Activity {

    private final String LOG_TAG = this.getClass().getSimpleName();
    final static String CHAVE_NOME_EXERCICIO = "ufba.mypersonaltrainner.nome_exercicio";
    final static String CHAVE_SERIES_EXERCICIO = "ufba.mypersonaltrainner.series_exercicio";
    final static String CHAVE_CARGA_EXERCICIO = "ufba.mypersonaltrainner.carga_exercicio";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.training_detail);
        TextView nomeTreino;

        Intent intent = getIntent();
        String treinoParseID = intent.getStringExtra(MeusTreinos.CHAVE_EXTRA_IDPARSE_TREINO);
        String treinoParseNome = intent.getStringExtra(MeusTreinos.CHAVE_EXTRA_NOME_TREINO);

        nomeTreino = (TextView) findViewById(R.id.selected_training_detail);
        nomeTreino.setText(treinoParseNome);

        final ListView lv = (ListView) findViewById(R.id.lv_exerc_treino);

        ParseQuery<ParseObject> query = ParseQuery.getQuery(PK.TREINO);
        query.include(PK.EXERCICIO);
        query.fromPin(PK.GRP_TUDO);
        query.getInBackground(treinoParseID, new GetCallback<ParseObject>() {
            public void done(ParseObject treino, ParseException e) {
                if (e == null) {
                    List<ParseObject> exerciciosDoParse = treino.getList(PK.EXERCICIO);
                    ArrayList<ItemListMeuTreino> listaExercicios = new ArrayList<ItemListMeuTreino>();
                    for (ParseObject exercicio : exerciciosDoParse) {
                        String nome = exercicio.getString(PK.EXERCICIO_NOME);
                        int reps = Integer.parseInt(exercicio.getString(PK.EXERCICIO_SERIES));
                        int carga = Integer.parseInt(exercicio.getString(PK.EXERCICIO_CARGA));

                        ItemListMeuTreino item = new ItemListMeuTreino(nome, reps, carga);
                        listaExercicios.add(item);
                    }

                    lv.setAdapter(new ListMeuTreinoAdapter(getApplicationContext(), listaExercicios));

                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            ItemListMeuTreino item = (ItemListMeuTreino) lv.getItemAtPosition(i);
                            Intent intent = new Intent(getBaseContext(), ExercicioActivity.class);
                            intent.putExtra(CHAVE_NOME_EXERCICIO, item.getNome());
                            startActivity(intent);
                        }
                    });
                } else {
                    Log.e(LOG_TAG, e.getMessage());
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.trainig_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_edit_train) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
