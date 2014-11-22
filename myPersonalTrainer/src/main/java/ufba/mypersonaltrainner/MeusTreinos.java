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

import com.parse.ParseObject;
import com.parse.ParseQueryAdapter;


public class MeusTreinos extends Activity {

    private ListView listView;
    private final String LOG_TAG = MeusTreinos.class.getSimpleName();
    static final int CRIA_TREINO_REQUEST = 0;
    static final String CHAVE_IDPARSE_TREINO = "ufba.mypersonaltrainner..id_parse";
    private ParseQueryAdapter<ParseObject> mTreinoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        mTreinoAdapter = new ParseQueryAdapter<ParseObject>(this, "treino");
        mTreinoAdapter.setTextKey("trn_nome");

//        TreinosParseAdapter adapter = new TreinosParseAdapter(this);

        if (mTreinoAdapter.hasStableIds())  Log.v(LOG_TAG, "OBA STABLE ID!!!");
        else  Log.v(LOG_TAG, "OHH NÃO TEM STABLE ID...");


        listView = (ListView) findViewById(R.id.listView_meus_treinos);
        listView.setAdapter(mTreinoAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getBaseContext(), TrainingDetail.class);
                ParseObject treino = (ParseObject) listView.getItemAtPosition(i);
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
            startActivityForResult(i, CRIA_TREINO_REQUEST);;
        }
        return super.onOptionsItemSelected(item);
    }
}
