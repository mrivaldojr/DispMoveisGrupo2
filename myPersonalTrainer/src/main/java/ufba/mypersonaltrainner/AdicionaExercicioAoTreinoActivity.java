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
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import ufba.mypersonaltrainner.util.C;
import ufba.mypersonaltrainner.util.PK;


public class AdicionaExercicioAoTreinoActivity extends Activity{

    private Spinner categoriaSpinner;
    private Spinner exerciciosSpinner;
    private Spinner numSeriesSpinner;
    private String categoriaEscolhida;
    private Button cancelar;
    private List<String> exercicios = new ArrayList<String>();
    private ArrayAdapter<String> adapterExercicio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adiciona_exercicio_ao_treino);

        cancelar = (Button) findViewById(R.id.button_cancelar_dialog);
        categoriaSpinner = (Spinner) findViewById(R.id.spinnerCategoria);
        exerciciosSpinner = (Spinner) findViewById(R.id.spinnerExercicio);
        numSeriesSpinner = (Spinner) findViewById(R.id.spinnerSeries);

        String nome; String series; String carga;
        Intent intent = getIntent();
        if (intent.getAction().equals(C.ACTION_EDIT_EXERCICIO)) {
            nome = intent.getStringExtra(C.EXTRA_EXERCICIO_NOME);
            series = intent.getStringExtra(C.EXTRA_EXERCICIO_SERIES);
            carga = intent.getStringExtra(C.EXTRA_EXERCICIO_CARGA);

        }

        ArrayAdapter<CharSequence> adapterCategorias = ArrayAdapter.createFromResource(this,
                R.array.categorias_de_exercicios,
                android.R.layout.simple_spinner_item);

        ArrayAdapter<CharSequence> adapterSeries = ArrayAdapter.createFromResource(this,
                R.array.num_series,
                android.R.layout.simple_spinner_item);

        adapterCategorias.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterSeries.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        cancelar.setOnClickListener(clickCancelar);


        numSeriesSpinner.setAdapter(adapterSeries);
        categoriaSpinner.setAdapter(adapterCategorias);

        categoriaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                categoriaEscolhida = parent.getItemAtPosition(position).toString();

                if (categoriaEscolhida.equals("Escolha uma Categoria")) {
                    exercicios.clear();
                    
                    //limpa o spinner
                    adapterExercicio = new ArrayAdapter<String>(getBaseContext(),
                            android.R.layout.simple_spinner_item,
                            exercicios);
                    adapterExercicio.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    exerciciosSpinner.setAdapter(adapterExercicio);
                }

                else {
                    ExercicioAsync exercicioAsync = new ExercicioAsync();
                    exercicioAsync.execute(categoriaEscolhida);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Toast toast = Toast.makeText(getApplicationContext(), "Escolha Alguma Categoria", Toast.LENGTH_SHORT);
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
        Intent intent = new Intent();


        EditText carga = (EditText) findViewById(R.id.add_exercicio_carga_do_exercicio);

        if(exerciciosSpinner.getSelectedItem() == null || carga.getText().toString().equals("")){
            Toast toast = Toast.makeText(getBaseContext(), "Todos os Campos devem ser Preenchidos",
                    Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        intent.putExtra(C.EXTRA_EXERCICIO_IDPARSE, exerciciosSpinner.getSelectedItem().toString());
        intent.putExtra(C.EXTRA_EXERCICIO_NOME, exerciciosSpinner.getSelectedItem().toString());
        intent.putExtra(C.EXTRA_EXERCICIO_SERIES, numSeriesSpinner.getSelectedItem().toString());
        intent.putExtra(C.EXTRA_EXERCICIO_CARGA, carga.getText().toString());

        Intent intentChamante = getIntent();
        if (intentChamante.getAction().equals(C.ACTION_EDIT_EXERCICIO)) {
            intent.putExtra(C.EXTRA_ARRAY_INDEX,
                    intentChamante.getIntExtra(C.EXTRA_ARRAY_INDEX, -1));
        }
        setResult(Activity.RESULT_OK, intent);
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

                ParseQuery<ParseObject> query = ParseQuery.getQuery(PK.TIPO_EXERCICIO);
                // query.setCachePolicy(ParseQuery.CachePolicy.CACHE_ELSE_NETWORK);
                query.fromPin(PK.GRP_TIPO_EXERCICIO);
                query.whereEqualTo(PK.TIPO_EXERCICIO_CATEGORIA , categoria[0]);
                listaExercicios = query.find();

                exercicios.clear();

                for (ParseObject exercicio : listaExercicios) {
                    nome = exercicio.getString(PK.TIPO_EXERCICIO_NOME);
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

            adapterExercicio = new ArrayAdapter<String>(getBaseContext(),
                    android.R.layout.simple_spinner_item,
                    exercicios);

            adapterExercicio.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            exerciciosSpinner.setAdapter(adapterExercicio);
        }

    }

}