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

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

import ufba.mypersonaltrainner.adapter.CustomAdapterMeusTreinos;
import ufba.mypersonaltrainner.util.C;
import ufba.mypersonaltrainner.util.PK;


public class MeusTreinos extends Activity {

    private ListView listView;
    private final String LOG_TAG = MeusTreinos.class.getSimpleName();
    private ParseQueryAdapter<ParseObject> mTreinoAdapter;
    private String UID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meus_treinos);

        UID = ParseUser.getCurrentUser().getObjectId();


/*        mTreinoAdapter = new ParseQueryAdapter<ParseObject>(this
                , new ParseQueryAdapter.QueryFactory<ParseObject>() {
            public ParseQuery<ParseObject> create() {
                ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(PK.TREINO);
                //query.setCachePolicy(ParseQuery.CachePolicy.CACHE_THEN_NETWORK);
                //query.fromPin(PK.GRP_TUDO);
                query.whereEqualTo(PK.USER_ID, UID);
                query.orderByDescending(PK.PIN_DATE);
                return query;
            }
        });*/

        mTreinoAdapter = new CustomAdapterMeusTreinos(this);
        mTreinoAdapter.setTextKey(PK.TREINO_NOME);

        // TreinosParseAdapter adapter = new TreinosParseAdapter(this);

        listView = (ListView) findViewById(R.id.listView_meus_treinos);
        listView.setAdapter(mTreinoAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getBaseContext(), TrainingDetail.class);
                ParseObject treino = (ParseObject) listView.getItemAtPosition(i);
                intent.putExtra(C.EXTRA_TREINO_IDPARSE, treino.getObjectId());
                intent.putExtra(C.EXTRA_TREINO_NOME, treino.getString(PK.TREINO_NOME));
                intent.putExtra(C.EXTRA_TREINO_EH_ATIVO, treino.getBoolean(PK.TREINO_ESTADO_ATIVO));
                startActivityForResult(intent, C.EXCLUI_EXERCICIO_REQUEST);
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == C.CRIA_TREINO_REQUEST || requestCode == C.EXCLUI_EXERCICIO_REQUEST) mTreinoAdapter.loadObjects();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mTreinoAdapter.loadObjects();
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
        if(id == R.id.action_new){
            Intent i = new Intent(getBaseContext(), ConfigurarTreinoActivity.class);
            i.setAction(C.ACTION_NOVO_TREINO);
            startActivityForResult(i, C.CRIA_TREINO_REQUEST);
        }
        return super.onOptionsItemSelected(item);
    }

    void erro(ParseException e) {
        Log.e(LOG_TAG, "deu errado no parse, la vai mensagem e stack trace:");
        Log.e(LOG_TAG, e.getMessage());
        e.printStackTrace();
    }
}
