package ufba.mypersonaltrainner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

import ufba.mypersonaltrainner.util.PK;


public class TreinosAtivosActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treinos_ativos);

        //dados de teste
        ListView lv = (ListView) findViewById(R.id.listView_treinos_ativos);

        /*List<String> lst = new ArrayList<String>();

        for(int i=1;i<4;i++){
            String item = "Treino "+i;
            lst.add(item);
        }

        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                R.layout.list_meus_treinos_item,
                R.id.txt_nome_treino_lst_item,
                lst);*/
        ParseQueryAdapter<ParseObject> adapter;
        adapter = new ParseQueryAdapter<ParseObject>(this
                , new ParseQueryAdapter.QueryFactory<ParseObject>() {
            @Override
            public ParseQuery<ParseObject> create() {
                ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(PK.TIPO_EXERCICIO);
                query.fromPin(PK.GRP_TIPO_EXERCICIO);
                return query;
            }
        });
        adapter.setTextKey(PK.TIPO_EXERCICIO_NOME);

        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getBaseContext(), TrainingDetail.class);
                startActivity(intent);
            }
        });
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
