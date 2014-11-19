package ufba.mypersonaltrainner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

import ufba.mypersonaltrainner.model.Exercicio;


public class ConfigurarTreinoActivity extends Activity {

    private ListView listViewExercicios;
    private ArrayAdapter<Exercicio> adapterExercicios;
    private String text;
    static final int CRIA_EXERCICIO_REQUEST = 0;
    static final String CHAVE_NOME = "chave";
    static final String CHAVE_SERIES = "series";
    static final String CHAVE_CARGA = "nome";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configurar_treino_my);

        // List<Exercicio> lst = new ArrayList<Exercicio>();

        adapterExercicios = new ArrayAdapter<Exercicio>(this,
                android.R.layout.simple_list_item_1, new ArrayList<Exercicio>());

        listViewExercicios = (ListView) findViewById(R.id.list_execiciosAdicionados);
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
        String treinoNome = nomeEditText.getText().toString();
        treinoNome = (treinoNome != null && treinoNome != "") ? treinoNome :
                "treino gerado no " +  ConfigurarTreinoActivity.class.getSimpleName();

        final ParseUser user = ParseUser.getCurrentUser();
        final ParseRelation<ParseObject> treinos = user.getRelation("treinos");

        final ParseObject treino = new ParseObject("treino");
        treino.put("trn_user", user);
        treino.put("trn_nome", treinoNome);
        ParseRelation<ParseObject> exercicios = treino.getRelation("exercicios");

        for (int i = 0; i < adapterExercicios.getCount(); i++) {
            Exercicio ex = adapterExercicios.getItem(i);
            ParseObject exercicio = new ParseObject("exercicio");
            exercicio.put("exe_nome", ex.getNome());
            exercicio.put("exe_serie", ex.getSeries());
            exercicio.put("exe_carga", ex.getCarga());
            exercicios.add(exercicio);
        }

        try {
            treino.save();
            treinos.add(treino);
            user.saveInBackground(new SaveCallback() {
                public void done(ParseException e) {
                    if (e != null) {
                        Log.e("ConfigurarTreinoActivity.class", e.getMessage());
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        return;
                    }
                    ParseQuery<ParseObject> query = treinos.getQuery();
                    //  query.whereEqualTo("trn_user", user);
                    query.findInBackground(new FindCallback<ParseObject>() {
                        public void done(List<ParseObject> treinosDoParse, ParseException e) {
                            if (e == null) {
                                for (ParseObject treino : treinosDoParse) {
                                    text = treino.getString("trn_nome" + "\n");
                                    treino.getRelation("exercicios").getQuery().findInBackground(
                                            new FindCallback<ParseObject>() {
                                                public void done(List<ParseObject> exerciciosDoParse, ParseException e) {
                                                    if (e == null) {
                                                        for (ParseObject exercicio : exerciciosDoParse) {
                                                            text += exercicio.getString("exe_nome") + "\n";
                                                            text += exercicio.getString("exe_serie") + "\n";
                                                            text += exercicio.getString("exe_carga") + "\n\n";
                                                        }
                                                    } else {
                                                        text += e.getMessage();
                                                        Log.e("ConfigurarTreinoActivity.class", text);
                                                        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
                                                    }
                                                }
                                            });
                                }
                            }
                        }
                    });
                }
            });
        } catch (ParseException e) {
            Log.e("ConfigurarTreino.class", "não deu pra salvar treino, la vai o stack trace:");
            Log.e("ConfigurarTreino.class", e.getMessage());
            e.printStackTrace();
        }

        /*treinos.add(treino);
        user.saveInBackground(new SaveCallback() {
            public void done(ParseException e) {
                if (e != null) {
                    Log.e("ConfigurarTreinoActivity.class", e.getMessage());
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    return;
                    }
                ParseQuery<ParseObject> query = treinos.getQuery();
               //  query.whereEqualTo("trn_user", user);
                query.findInBackground(new FindCallback<ParseObject>() {
                    public void done(List<ParseObject> treinosDoParse, ParseException e) {
                        if (e == null) {
                            for (ParseObject treino : treinosDoParse) {
                                text = treino.getString("trn_nome" + "\n");
                                treino.getRelation("exercicios").getQuery().findInBackground(
                                    new FindCallback<ParseObject>() {
                                        public void done(List<ParseObject> exerciciosDoParse, ParseException e) {
                                            if (e == null) {
                                                for (ParseObject exercicio : exerciciosDoParse) {
                                                    text += exercicio.getString("exe_nome") + "\n";
                                                    text += exercicio.getString("exe_serie") + "\n";
                                                    text += exercicio.getString("exe_carga") + "\n\n";
                                                    }}
                                            else {
                                                text += e.getMessage();
                                                Log.e("ConfigurarTreinoActivity.class", text);
                                                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
                                                }}});}}}});}});*/
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

                adapterExercicios.add(new Exercicio(nome, series, carga));
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
