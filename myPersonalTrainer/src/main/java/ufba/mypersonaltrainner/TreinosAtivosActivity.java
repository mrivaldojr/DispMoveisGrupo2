package ufba.mypersonaltrainner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

import ufba.mypersonaltrainner.util.C;
import ufba.mypersonaltrainner.util.PK;


public class TreinosAtivosActivity extends Activity {

//    private ArrayAdapter<Treino> mTreinosAtivosAdapter;
    private ParseQueryAdapter<ParseObject> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treinos_ativos);


        adapter = new ParseQueryAdapter<ParseObject>(this,
                new ParseQueryAdapter.QueryFactory<ParseObject>() {
            @Override
            public ParseQuery<ParseObject> create() {
                String uid = ParseUser.getCurrentUser().getObjectId();
                ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(PK.TREINO);
                query.whereEqualTo(PK.TREINO_USER, uid);
                query.whereEqualTo(PK.TREINO_ESTADO_ATIVO, true);
                return query;
            }
        });
        adapter.setTextKey(PK.TREINO_NOME);

        ListView lv = (ListView) findViewById(R.id.listView_treinos_ativos);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getBaseContext(), TrainingDetail.class);
                ParseObject treino = adapter.getItem(i);
                intent.putExtra(C.EXTRA_TREINO_IDPARSE, treino.getObjectId());
                intent.putExtra(C.EXTRA_TREINO_NOME, treino.getString(PK.TREINO_NOME));
                intent.putExtra(C.EXTRA_TREINO_EH_ATIVO, treino.getBoolean(PK.TREINO_ESTADO_ATIVO));
                startActivityForResult(intent, C.EDITA_TREINO_REQUEST);
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == C.EDITA_TREINO_REQUEST) {
                Toast.makeText(getApplicationContext(), "foi clicado back, ativo modificado", Toast.LENGTH_LONG).show();
                adapter.loadObjects();
                // adapter.notifyDataSetChanged();
            }
        } else if (resultCode == RESULT_CANCELED) {
            if (requestCode == C.EDITA_TREINO_REQUEST) {
                Toast.makeText(getApplicationContext(), "foi cliado back, ativo igual", Toast.LENGTH_LONG).show();
            }
        }
    }

    //@Override
    //protected void onResume() {
    //    super.onResume();
    //    adapter.loadObjects();
    //    adapter.notifyDataSetChanged();
    //}

    /*    private void populaAdapter() {
        mTreinosAtivosAdapter.clear();
        List<ParseObject> treinosAtivos = TreinosAtivos.getAll();
        if (treinosAtivos == null) return;
        for (ParseObject treinoAtivo : treinosAtivos) {
            try {
                treinoAtivo.fetchIfNeeded();
                ParseObject treino = treinoAtivo.getParseObject(PK.ATIVO_TREINO).fetchIfNeeded();
                mTreinosAtivosAdapter.add(new Treino(treino.getString(PK.TREINO_NOME),
                        treino.getObjectId()));
            } catch (ParseException e) {
                Log.v(this.getClass().getSimpleName(),
                        "Erro ao pegar do parse o treno dentro do treinoAtivo\n" +
                                e.getMessage());
                e.printStackTrace();
            }
        }
    }*/


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
                getMenuInflater().inflate(R.menu.treinos_ativos, menu);
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
