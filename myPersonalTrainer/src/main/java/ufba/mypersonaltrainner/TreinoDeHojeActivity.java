package ufba.mypersonaltrainner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import ufba.mypersonaltrainner.adapter.ListMeuTreinoAdapter;
import ufba.mypersonaltrainner.util.PK;


public class TreinoDeHojeActivity extends Activity {

    private ListMeuTreinoAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.treino_de_hoje);

        mAdapter = new ListMeuTreinoAdapter(this, new ArrayList<ItemListMeuTreino>());

        ParseUser user = ParseUser.getCurrentUser();
        int indiceTreinoDeHoje = user.getInt(PK.USER_INDICE_TREINO_ATUAL);

        ParseQuery treinosQuery = ParseQuery.getQuery(PK.TREINO);
        treinosQuery.whereEqualTo(PK.TREINO_USER, user.getObjectId());
        treinosQuery.whereEqualTo(PK.TREINO_ATIVO_ORDEM, indiceTreinoDeHoje);

        treinosQuery.getFirstInBackground(new GetCallback() {
            @Override
            public void done(ParseObject oTreinoAtual, ParseException e) {
                if (e == null) {
                    mAdapter.addAll;
                } else {
                    Log.e(TrainingDetail.class.getSimpleName(),
                            "não deu pra get treino ou count treinos ativos ou atualizar o treino: " +
                                    e.getMessage());
                    e.printStackTrace();
                }
            }
        });

        ListView listExercicios = (ListView) findViewById(R.id.lv_treinoDeHoje);

        ItemListMeuTreino item = new ItemListMeuTreino("Exercicio"+i, 3, i+20);


        listExercicios.setAdapter(new ListMeuTreinoAdapter(this, new List));

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
