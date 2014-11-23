package ufba.mypersonaltrainner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;


public class AdicionaExercicioAoTreinoActivity extends Activity {

    //public static String EXERCICIO

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adiciona_exercicio_ao_treino);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_adiciona_exercicio_ao_treino, menu);
        return true;
    }

    public void confirma(View view) {
        Intent i = new Intent();

        EditText nome = (EditText) findViewById(R.id.add_exercicio_nome_do_exercicio);
        EditText series = (EditText) findViewById(R.id.add_exercicio_series_do_exercicio);
        EditText carga = (EditText) findViewById(R.id.add_exercicio_carga_do_exercicio);

        i.putExtra(ConfigurarTreinoActivity.CHAVE_NOME, nome.getText().toString());
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
}
