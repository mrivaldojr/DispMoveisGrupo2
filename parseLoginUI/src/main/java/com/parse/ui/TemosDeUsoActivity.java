package com.parse.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;


public class TemosDeUsoActivity extends Activity {


    private TextView termos;
    private Button aceitar;
    private CheckBox confirmo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temos_de_uso);

        termos = (TextView) findViewById(R.id.textView_termos);
        aceitar = (Button) findViewById(R.id.button_aceitar);
        confirmo = (CheckBox) findViewById(R.id.checkBox_aceitar);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("TRM_termos_de_uso");
        query.getInBackground("OMmKDKjMPe", new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                String texto = parseObject.getString("texto");
                termos.setText(texto);
            }
        });



        aceitar.setVisibility(View.INVISIBLE);

        confirmo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = ((CheckBox) v).isChecked();

                if(checked)
                    aceitar.setVisibility(View.VISIBLE);
                else
                    aceitar.setVisibility(View.INVISIBLE);
            }
        });

        aceitar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK);
                finish();
            }
        });

    }

}
