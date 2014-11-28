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
import android.widget.ListView;

import com.parse.ParseException;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;

import ufba.mypersonaltrainner.model.Treino;
import ufba.mypersonaltrainner.util.C;
import ufba.mypersonaltrainner.util.PK;
import ufba.mypersonaltrainner.util.TreinosAtivos;


public class TreinosAtivosActivity extends Activity {

    private ArrayAdapter<Treino> mTreinosAtivosAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treinos_ativos);

        ListView lv = (ListView) findViewById(R.id.listView_treinos_ativos);

        List<String> lst = new ArrayList<String>();

        for(int i=1;i<4;i++){
            String item = "Treino "+i;
            lst.add(item);
        }
        mTreinosAtivosAdapter = new ArrayAdapter<Treino>(this,
                // R.layout.list_meus_treinos_item,
                android.R.layout.simple_list_item_1,
                new ArrayList<Treino>());

        /*ParseQueryAdapter<ParseObject> adapter;
        adapter = new ParseQueryAdapter<ParseObject>(this
                , new ParseQueryAdapter.QueryFactory<ParseObject>() {
            @Override
            public ParseQuery<ParseObject> create() {
                ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(PK.TIPO_EXERCICIO);
                query.fromPin(PK.GRP_TIPO_EXERCICIO);
                return query;
            }
        });
        adapter.setTextKey(PK.TIPO_EXERCICIO_NOME);*/
        lv.setAdapter(mTreinosAtivosAdapter);
        populaAdapter();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getBaseContext(), TrainingDetail.class);
                Treino treino = mTreinosAtivosAdapter.getItem(i);
                intent.putExtra(C.EXTRA_TREINO_IDPARSE, treino.getId());
                intent.putExtra(C.EXTRA_TREINO_NOME, treino.getNome());
                startActivity(intent);
            }
        });
    }

    private void populaAdapter() {
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
    }

    @Override
    protected void onResume() {
        super.onResume();
        populaAdapter();
    }

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
