package ufba.mypersonaltrainner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import ufba.mypersonaltrainner.util.PK;


public class ExercicioActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_execicio);

        TextView nomeExercicioTextView = (TextView) findViewById(R.id.textViewNome);
        final TextView descricaoExercicioTextView = (TextView) findViewById(R.id.textViewDescricao);
        final ParseImageView imagemDoExercicio = (ParseImageView) findViewById(R.id.imageView);

        Intent intent = getIntent();

        String nome = intent.getStringExtra(TrainingDetail.CHAVE_NOME_EXERCICIO);
        nomeExercicioTextView.setText(nome);

        imagemDoExercicio.setPlaceholder(getResources()
                .getDrawable(R.drawable.place_holder));

        try {
            ParseQuery<ParseObject> query = ParseQuery.getQuery(PK.TIPO_EXERCICIO);
            query.fromPin(PK.GRP_TIPO_EXERCICIO);
            query.whereEqualTo(PK.TIPO_EXERCICIO_NOME, nome);
            ParseObject exercicio = query.getFirst();

            String descricao = exercicio.getString(PK.TIPO_EXERCICIO_DESCRICAO);
            descricaoExercicioTextView.setText(descricao);

            ParseFile parseFileDaImagem = exercicio.getParseFile(PK.TIPO_EXERCICIO_IMAGEM);

            if (parseFileDaImagem != null) {
                imagemDoExercicio.setParseFile(parseFileDaImagem);
                imagemDoExercicio.loadInBackground();
            }
        } catch (ParseException e) {
            Log.d(this.getClass().getSimpleName(), e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.execicio, menu);
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
