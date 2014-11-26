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
import ufba.mypersonaltrainner.util.C;
import ufba.mypersonaltrainner.util.PK;


public class TrainingDetail extends Activity {

    private final String LOG_TAG = this.getClass().getSimpleName();
    private String treinoID;
    private String treinoNome;

    private ListMeuTreinoAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.training_detail);
        final ListView lv = (ListView) findViewById(R.id.lv_exerc_treino);

        Intent intent = getIntent();
        treinoID = intent.getStringExtra(C.EXTRA_TREINO_IDPARSE);
        treinoNome = intent.getStringExtra(C.EXTRA_TREINO_NOME);

        TextView nomeTreino = (TextView) findViewById(R.id.selected_training_detail);
        nomeTreino.setText(treinoNome);

        ParseQuery<ParseObject> query = ParseQuery.getQuery(PK.TREINO);
        query.include(PK.EXERCICIO);
        //query.fromPin(PK.GRP_TUDO);
        query.getInBackground(treinoID, new GetCallback<ParseObject>() {
            public void done(ParseObject treino, ParseException e) {
                if (e == null) {
                    List<ParseObject> exerciciosDoParse = treino.getList(PK.EXERCICIO);
                    ArrayList<ItemListMeuTreino> listaExercicios = new ArrayList<ItemListMeuTreino>();
                    mAdapter = new ListMeuTreinoAdapter(getApplicationContext(), listaExercicios);
                    for (ParseObject exercicio : exerciciosDoParse) {
                        String nome = exercicio.getString(PK.EXERCICIO_NOME);
                        int reps = Integer.parseInt(exercicio.getString(PK.EXERCICIO_SERIES));
                        int carga = Integer.parseInt(exercicio.getString(PK.EXERCICIO_CARGA));

                        ItemListMeuTreino item = new ItemListMeuTreino(nome, reps, carga);
                        listaExercicios.add(item);
                    }

                    lv.setAdapter(mAdapter);

                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            ItemListMeuTreino item = (ItemListMeuTreino) lv.getItemAtPosition(i);
                            Intent intent = new Intent(getBaseContext(), ExercicioActivity.class);
                            intent.setAction(C.ACTION_EDIT_TREINO);
                            intent.putExtra(C.EXTRA_TREINO_IDPARSE, treinoID);
                            intent.putExtra(C.EXTRA_EXERCICIO_NOME, item.getNome());
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
        if (id == R.id.action_editar_treino) {
            Intent intent = new Intent(getBaseContext(), ConfigurarTreinoActivity.class);
            intent.setAction(C.ACTION_EDIT_TREINO);
            intent.putExtra(C.EXTRA_TREINO_IDPARSE, treinoID);
            intent.putExtra(C.EXTRA_TREINO_NOME, treinoNome);
            // intent.putExtra(CHAVE_EXTRA_IDPARSE_TREINO, treino.getString(PK.TREINO_ID));
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
