package ufba.mypersonaltrainner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;


public class ConfigurarTreinoActivity extends Activity {

    private ListView listViewExercicios;
    private ArrayAdapter<String> adapterExercicios;
    static final int CRIA_EXERCICIO_REQUEST = 0;
    static final String CHAVE_NOME = "chave";
    static final String CHAVE_SERIES = "series";
    static final String CHAVE_CARGA = "nome";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configurar_treino_my);

        List<String> lst = new ArrayList<String>();

        adapterExercicios = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, lst);

        listViewExercicios =(ListView) findViewById(R.id.list_execiciosAdicionados);
        listViewExercicios.setAdapter(adapterExercicios);

        listViewExercicios.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getBaseContext(), TrainigDetail.class);
                startActivity(intent);
            }
        });
    }

    public void salvaTreino(View view) {

        EditText nomeEditText = (EditText) findViewById(R.id.edt_nomeTreino);
        String nome = nomeEditText.getText().toString();
        nome = (nome != null && nome != "") ? nome :
                "treino gerado no " +  ConfigurarTreinoActivity.class.getSimpleName();
        ParseObject treino = new ParseObject("TRT_treino");
        treino.put("trt_ds_nome", nome);

        // treino.saveInBackground();

        final String nomeF = nome;

        treino.saveInBackground(new SaveCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    ParseQuery<ParseObject> query = ParseQuery.getQuery("TRT_treino")
                            .whereEqualTo("trt_ds_nome", nomeF);
                    query.getFirstInBackground(new GetCallback<ParseObject>() {
                        public void done(ParseObject object, ParseException e) {
                            CharSequence text;
                            if (e == null) {
                                text = object.getString("trt_ds_nome");
                            } else {
                                text = e.getMessage();
                            }
                            Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void addExercicioDialog(View view) {
        // Do something in response to button click
        Intent i = new Intent(this,AdicionaExercicioAoTreinoActivity.class);
        startActivityForResult(i, CRIA_EXERCICIO_REQUEST);
    }

    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (requestCode == CRIA_EXERCICIO_REQUEST) {
            if (resultCode == RESULT_OK) {
                String nome = data.getStringExtra(CHAVE_NOME);
                String series = data.getStringExtra(CHAVE_SERIES);
                String carga = data.getStringExtra(CHAVE_CARGA);
                adapterExercicios.add(nome + " " + series + " " + carga);
                adapterExercicios.notifyDataSetChanged();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.configurar_treino_my, menu);
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
