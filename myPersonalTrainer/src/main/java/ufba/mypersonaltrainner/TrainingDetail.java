package ufba.mypersonaltrainner;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import ufba.mypersonaltrainner.model.Exercicio;
import ufba.mypersonaltrainner.util.C;
import ufba.mypersonaltrainner.util.PK;


public class TrainingDetail extends Activity {

    private final String LOG_TAG = this.getClass().getSimpleName();
    private String treinoID;
    private String treinoNome;
    private ParseObject treinoAtual;
    private ArrayAdapter<Exercicio> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.training_detail);
        final ListView lv = (ListView) findViewById(R.id.lv_exerc_treino);

        Intent intent = getIntent();
        treinoID = intent.getStringExtra(C.EXTRA_TREINO_IDPARSE);
        treinoNome = intent.getStringExtra(C.EXTRA_TREINO_NOME);

        TextView nomeTreinoTextView = (TextView) findViewById(R.id.selected_training_detail);
        nomeTreinoTextView.setText(treinoNome);

        mAdapter = new ArrayAdapter<Exercicio> (getApplicationContext(),
                android.R.layout.simple_list_item_1, new ArrayList<Exercicio>());
        lv.setAdapter(mAdapter);

        getData();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Exercicio item = (Exercicio) lv.getItemAtPosition(i);
                Intent intent = new Intent(getBaseContext(), ExercicioActivity.class);
                intent.putExtra(C.EXTRA_TREINO_IDPARSE, treinoID);
                intent.putExtra(C.EXTRA_EXERCICIO_NOME, item.nome);
                startActivity(intent);
            }
        });
    }

    private void getData() {
        ParseQuery < ParseObject > query = ParseQuery.getQuery(PK.TREINO);
        query.include(PK.EXERCICIO);
        //query.fromPin(PK.GRP_TUDO);
        query.getInBackground(treinoID, new GetCallback<ParseObject>() {
            public void done(ParseObject treino, ParseException e) {
                if (e == null) {
                    treinoAtual = treino;
                    List<ParseObject> exerciciosDoParse = treino.getList(PK.EXERCICIO);
                    ArrayList<Exercicio> listaExercicios = new ArrayList<Exercicio>();
                    for (ParseObject exercicio : exerciciosDoParse) {
                        String nome = exercicio.getString(PK.EXERCICIO_NOME);
                        String reps = exercicio.getString(PK.EXERCICIO_SERIES);
                        String carga = exercicio.getString(PK.EXERCICIO_CARGA);

                        Exercicio item = new Exercicio(nome, reps, carga);
                        listaExercicios.add(item);
                    }
                    TextView nomeTreinoTextView = (TextView) findViewById(R.id.selected_training_detail);
                    nomeTreinoTextView.setText(treinoAtual.getString(PK.TREINO_NOME));
                    mAdapter.clear();
                    mAdapter.addAll(listaExercicios);
                    mAdapter.notifyDataSetChanged();
                } else {
                    Log.e(LOG_TAG, e.getMessage());
                    e.printStackTrace();
                }
            }
        });
    }



    @Override
    public void onResume() {
        super.onResume();
        getData();
    }

    /*
        public void onCheckboxClicked(View view) {
            if (treinoAtual == null) return;
            String UID = ParseUser.getCurrentUser().getObjectId();

            boolean checked = ((CheckBox) view).isChecked();
            if (checked) {
                treinoAtual.pin(PK.GRP_ATUAIS);

            }
        }
        */
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.trainig_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_editar_treino) {
            Intent intent = new Intent(getBaseContext(), ConfigurarTreinoActivity.class);
            intent.setAction(C.ACTION_EDIT_TREINO);
            intent.putExtra(C.EXTRA_TREINO_IDPARSE, treinoID);
            intent.putExtra(C.EXTRA_TREINO_NOME, treinoNome);
            // intent.putExtra(CHAVE_EXTRA_IDPARSE_TREINO, treino.getString(PK.TREINO_ID));
            startActivity(intent);
            return true;
        }

        if(id == R.id.action_excluir_treino) {
            AlertDialog.Builder excuiDialog = new AlertDialog.Builder(this);
            excuiDialog.setMessage("Deseja Excluir esse treino?");
            excuiDialog.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    DeletarAsync delete = new DeletarAsync();
                    delete.execute();
                }
            });

            excuiDialog.setNegativeButton("Cancelar", null);

            excuiDialog.show();


        }
        return super.onOptionsItemSelected(item);
    }


    private class DeletarAsync extends AsyncTask<Void, Void, Void>{

        ProgressDialog progressDialog;

        @Override
        protected Void doInBackground(Void... params) {

            ParseQuery<ParseObject> query = ParseQuery.getQuery(PK.TREINO);
            //query.whereEqualTo("trt_ds_nome", treinoNome);
            query.getInBackground(treinoID, new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject parseObject, ParseException e) {
                    if (e == null) {
                        try {
                            parseObject.delete();
                        } catch (ParseException e1) {
                            e1.printStackTrace();
                        }
                    } else {
                        // something went wrong
                    }
                }
            });

            return null;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(TrainingDetail.this);
            progressDialog.setTitle("...");
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Espere por Favor");
            progressDialog.show();
        }


        @Override
        protected void onPostExecute(Void aVoid) {

            finish();
        }
    }
}
