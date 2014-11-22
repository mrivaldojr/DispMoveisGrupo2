package ufba.mypersonaltrainner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import ufba.mypersonaltrainner.adapter.ListMeuTreinoAdapter;


public class TreinoDeHojeActivity extends Activity {

    private ListView listExercicios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.treino_de_hoje);

        listExercicios = (ListView) findViewById(R.id.lv_treinoDeHoje);

        List<ItemListMeuTreino> lista = new ArrayList<ItemListMeuTreino>();

        for(int i=0; i<20;i++){
            ItemListMeuTreino item = new ItemListMeuTreino("Exercicio"+i, 3, i+20);

            item.setCarga(i+20);
            item.setNome("Exercicio"+i);
            item.setRepeticoes(3);

            lista.add(item);
        }

        listExercicios.setAdapter(new ListMeuTreinoAdapter(this, lista));

        listExercicios.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getBaseContext(), ExercicioActivity.class);
                startActivity(intent);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.treino_de_hoje, menu);
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
