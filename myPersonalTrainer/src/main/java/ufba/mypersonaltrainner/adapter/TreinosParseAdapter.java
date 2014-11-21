package ufba.mypersonaltrainner.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

import ufba.mypersonaltrainner.R;

public class TreinosParseAdapter extends ParseQueryAdapter<ParseObject> {

    private final String LOG_TAG = TreinosParseAdapter.class.getSimpleName();

    public TreinosParseAdapter(Context context) {
        super(context, new ParseQueryAdapter.QueryFactory<ParseObject>() {
            public ParseQuery create() {
                ParseQuery query = new ParseQuery("treino");
                return query;
            }
        });
    }

    @Override
    public View getItemView(ParseObject object, View v, ViewGroup parent) {
        if (v == null) {
            v = View.inflate(getContext(), R.layout.elemento_de_lista_de_treios, null);
        }

        super.getItemView(object, v, parent);

        // Add and download the image
        //ParseImageView todoImage = (ParseImageView) v.findViewById(R.id.icon);
        //ParseFile imageFile = object.getParseFile("image");
        //if (imageFile != null) {
        //    todoImage.setParseFile(imageFile);
        //    todoImage.loadInBackground();
        //}

        // Add the title view
        TextView x = (TextView) v;
        x.setText(object.getString("trn_nome"));

        if (this.hasStableIds()) Log.v(LOG_TAG, "OBA STABLE ID!!!");
        else Log.v(LOG_TAG, "OHH NÃO TEM STABLE ID...");

        return v;
    }
}