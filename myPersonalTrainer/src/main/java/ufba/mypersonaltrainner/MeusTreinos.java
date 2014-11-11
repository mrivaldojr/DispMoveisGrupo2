package ufba.mypersonaltrainner;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class MeusTreinos extends Activity {

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meus_treinos);

        List<String> lst = new ArrayList<String>();

        for(int i=1;i<15;i++){
            String item = "Treino "+i;
            lst.add(item);
        }

        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                R.layout.list_meus_treinos_item,
                R.id.txt_nome_treino_lst_item,
                lst);

        listView =(ListView) findViewById(R.id.listView_meus_treinos);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getBaseContext(), TrainigDetail.class);
                startActivity(intent);
            }
        });

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
            Intent i = new Intent(getBaseContext(),ConfigurarTreinoActivity.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }
}
