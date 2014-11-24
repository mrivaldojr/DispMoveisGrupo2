package ufba.mypersonaltrainner;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

import java.util.List;


public class MeusTreinos extends Activity {

    private ListView listView;
    private final String LOG_TAG = MeusTreinos.class.getSimpleName();
    static final int CRIA_TREINO_REQUEST = 0;
    static final String CHAVE_IDPARSE_TREINO = "ufba.mypersonaltrainner..id_parse";
    private ParseQueryAdapter<ParseObject> mTreinoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_meus_treinos);

        /*List<String> lst = new ArrayList<String>();

        for(int i=1;i<15;i++){
            String item = "Treino "+i;
            lst.add(item);
        }

        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                R.layout.list_meus_treinos_item,
                R.id.txt_nome_treino_lst_item,
                lst);
                */

        mTreinoAdapter = new ParseQueryAdapter<ParseObject>(this
                , new ParseQueryAdapter.QueryFactory<ParseObject>() {
            public ParseQuery<ParseObject> create() {
                ParseQuery query = new ParseQuery("treino");
                query.fromPin("tudo");
                query.orderByDescending("pinnedAt");
                return query;
            }
        });
        mTreinoAdapter.setTextKey("trn_nome");

//        TreinosParseAdapter adapter = new TreinosParseAdapter(this);
        if (mTreinoAdapter.hasStableIds())  Log.v(LOG_TAG, "OBA STABLE ID!!!");
        else  Log.v(LOG_TAG, "Ohh não tem stable id...");

        listView = (ListView) findViewById(R.id.listView_meus_treinos);
        listView.setAdapter(mTreinoAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getBaseContext(), TrainingDetail.class);
                ParseObject treino = (ParseObject) listView.getItemAtPosition(i);
                intent.putExtra("NOME_TREINO",treino.getString("trn_nome"));
                intent.putExtra(CHAVE_IDPARSE_TREINO, treino.getObjectId());
                startActivity(intent);
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (requestCode == CRIA_TREINO_REQUEST) {
            if (resultCode == RESULT_OK) {
                mTreinoAdapter.loadObjects();
                //mTreinoAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.meus_treinos, menu);
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
        if(id == R.id.action_new){
            Intent i = new Intent(getBaseContext(), ConfigurarTreinoActivity.class);
            startActivityForResult(i, CRIA_TREINO_REQUEST);
        }
        if(id == R.id.action_refresh){
            refresh();
        }
        if(id == R.id.action_clear) {
            Toast.makeText(getApplicationContext(), "LIMPANDO", Toast.LENGTH_SHORT).show();
            // Limpa o cache local da categoria "tudo"
            ParseQuery<ParseObject> query = ParseQuery.getQuery("treino");
            query.fromPin("tudo");
            try {
                List<ParseObject> treinos = query.find();
                ParseObject.unpinAll("tudo", treinos);
            } catch (ParseException e) {
                erro(e);
            }
            mTreinoAdapter.loadObjects();
        }
        if(id == R.id.action_load){
            Toast.makeText(getApplicationContext(), "LOADANDO", Toast.LENGTH_SHORT).show();
            // Carrega treinos do parse e coloca no datastore com pin.
            ParseQuery<ParseObject> query = ParseQuery.getQuery("treino");
            query.fromPin("tudo");
            try {
                if (query.count() == 0) {
                    query = ParseQuery.getQuery("treino");
                    List<ParseObject> treinos = query.find();
                    for (ParseObject treino : treinos) {
                        treino.pin("tudo");
                    }
                }
            } catch (ParseException e) {
                erro(e);
            }
/*
            Toast.makeText(getApplicationContext(), "Pinmod", Toast.LENGTH_SHORT).show();
            // Carrega treinos do parse e coloca no datastore com pin.
            ParseQuery<ParseObject> query = ParseQuery.getQuery("treino");
            query.fromPin("tudo");
            try {
                List<ParseObject> treinos = query.find();
                for (ParseObject treino : treinos) {
                    if (treino.isDirty()) {
                        Log.d(LOG_TAG, treino.getString("trn_nome"));
                        treino.pinInBackground("modificados");
                    }
                }
            } catch (ParseException e) {
                erro(e);
            }
*/

            mTreinoAdapter.loadObjects();
        }
        return super.onOptionsItemSelected(item);
    }

    void erro(ParseException e) {
        Log.e(LOG_TAG, "deu errado no parse, la vai mensagem e stack trace:");
        Log.e(LOG_TAG, e.getMessage());
        e.printStackTrace();
        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
    }

    void refresh() {
        ParseQuery<ParseObject> query;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if ((ni == null) || (!ni.isConnected())) {
            return;
        }
        Toast.makeText(getApplicationContext(), "LIMPANDO", Toast.LENGTH_SHORT).show();
        // Limpa o cache local da categoria "tudo"
        query = ParseQuery.getQuery("treino");
        query.fromPin("tudo");
        try {
            List<ParseObject> treinos = query.find();
            ParseObject.unpinAll("tudo", treinos);
        } catch (ParseException e) {
            erro(e);
        }

        Toast.makeText(getApplicationContext(), "LOADANDO", Toast.LENGTH_LONG).show();
        // Carrega treinos do parse e coloca no datastore com pin.
        query = ParseQuery.getQuery("treino");
        query.include("exercicios");
        query.fromPin("tudo");
        try {
            if (query.count() == 0) {
                query = ParseQuery.getQuery("treino");
                List<ParseObject> treinos = query.find();
                for (ParseObject treino : treinos) {
                    treino.pin("tudo");
                }
            }
        } catch (ParseException e) {
            erro(e);
        }
        mTreinoAdapter.loadObjects();
    }
}
