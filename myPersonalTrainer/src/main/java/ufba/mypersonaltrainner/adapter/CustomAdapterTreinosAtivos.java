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

/**
 * Created by junior on 12/12/14.
 */
public class CustomAdapterTreinosAtivos extends ParseQueryAdapter {

    public CustomAdapterTreinosAtivos(Context context) {

        super(context, new QueryFactory<ParseObject>() {
            public ParseQuery create() {
            String uid = ParseUser.getCurrentUser().getObjectId();
            ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(PK.TREINO);
            query.whereEqualTo(PK.TREINO_USER, uid);
            query.whereEqualTo(PK.TREINO_ESTADO_ATIVO, true);
            return query;
            }
        });

    }

    // Customize the layout by overriding getItemView
    @Override
    public View getItemView(ParseObject object, View v, ViewGroup parent) {
        if (v == null) {
            v = View.inflate(getContext(), R.layout.list_meus_treinos_item, null);
        }

        super.getItemView(object, v, parent);

        // Add the title view
        TextView titleTextView = (TextView) v.findViewById(R.id.txt_nome_treino_lst_item);
        titleTextView.setText(object.getString(PK.TREINO_NOME));

        return v;
    }
}
