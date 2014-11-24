package ufba.mypersonaltrainner;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;


public class AdicionaExercicioAoTreinoActivity extends Activity{

    private Spinner categoriaSpinner;
    private Spinner exerciciosSpinner;
    private String categoriaEscolhida;
    private Button cancelar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adiciona_exercicio_ao_treino);

        cancelar = (Button) findViewById(R.id.button_cancelar_dialog);
        categoriaSpinner = (Spinner) findViewById(R.id.spinnerCategoria);
        exerciciosSpinner = (Spinner) findViewById(R.id.spinnerExercicio);

        ArrayAdapter<CharSequence> adapterCategorias = ArrayAdapter.createFromResource(this,
                R.array.categorias_de_exercicios,
                android.R.layout.simple_spinner_item);

        adapterCategorias.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        cancelar.setOnClickListener(clickCancelar);

        categoriaSpinner.setAdapter(adapterCategorias);

        categoriaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                categoriaEscolhida = parent.getItemAtPosition(position).toString();

                ExercicioAsync exercicioAsync = new ExercicioAsync();
                exercicioAsync.execute(categoriaEscolhida);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private View.OnClickListener clickCancelar = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_adiciona_exercicio_ao_treino, menu);
        return true;
    }

    public void confirma(View view) {
        Intent i = new Intent();

        EditText series = (EditText) findViewById(R.id.add_exercicio_series_do_exercicio);
        EditText carga = (EditText) findViewById(R.id.add_exercicio_carga_do_exercicio);

        i.putExtra(ConfigurarTreinoActivity.CHAVE_NOME, exerciciosSpinner.getSelectedItem().toString());
        i.putExtra(ConfigurarTreinoActivity.CHAVE_SERIES, series.getText().toString());
        i.putExtra(ConfigurarTreinoActivity.CHAVE_CARGA, carga.getText().toString());

        setResult(Activity.RESULT_OK, i);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class ExercicioAsync extends AsyncTask<String, Void, Void>{

        ProgressDialog progressDialog;
        List<ParseObject> listaExercicios;
        List<String> exercicios = new ArrayList<String>();
        ParseObject parseObject;
        String nome;

        @Override
        protected void onPreExecute() {

            progressDialog = new ProgressDialog(AdicionaExercicioAoTreinoActivity.this);
            progressDialog.setTitle("Carregando...");
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Espere por Favor");
            progressDialog.show();

        }

        @Override
        protected Void doInBackground(String... categoria) {

            try {
                ParseQuery<ParseObject> parseQuery = new ParseQuery<ParseObject>("EXE_exercicio");
                parseQuery.whereEqualTo("exe_ds_categoria" , categoria[0]);
                listaExercicios = parseQuery.find();

                for (ParseObject parseObject : listaExercicios) {
                    nome = parseObject.getString("exe_ds_nome");
                    exercicios.add(nome);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void Void) {
            progressDialog.dismiss();

            ArrayAdapter<String> adapterExercicio = new ArrayAdapter<String>(getBaseContext(),
                    android.R.layout.simple_spinner_item,
                    exercicios);

            adapterExercicio.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            exerciciosSpinner.setAdapter(adapterExercicio);
        }

    }

}