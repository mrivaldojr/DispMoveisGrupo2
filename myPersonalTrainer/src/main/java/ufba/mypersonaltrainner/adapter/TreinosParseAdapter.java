package ufba.mypersonaltrainner.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

import ufba.mypersonaltrainner.R;
import ufba.mypersonaltrainner.util.PK;

public class TreinosParseAdapter extends ParseQueryAdapter<ParseObject> {

    private final String LOG_TAG = TreinosParseAdapter.class.getSimpleName();

    public TreinosParseAdapter(Context context) {
        super(context, new ParseQueryAdapter.QueryFactory<ParseObject>() {
            public ParseQuery<ParseObject> create() {
                ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(PK.TREINO);
                // query.setCachePolicy(ParseQuery.CachePolicy.CACHE_THEN_NETWORK);
                query.whereEqualTo(PK.USER_ID, ParseUser.getCurrentUser().getObjectId());
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

        // Add the title view
        ((TextView) v).setText(object.getString(PK.TREINO_NOME));

        return v;
    }
}