package ufba.mypersonaltrainner.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import ufba.mypersonaltrainner.ItemListMeuTreino;
import ufba.mypersonaltrainner.R;

/**
 * Created by junior on 28/10/14.
 */
public class ListMeuTreinoAdapter extends BaseAdapter {

    Context context;
    List <ItemListMeuTreino> listExerc;


    public ListMeuTreinoAdapter(Context context, List<ItemListMeuTreino> itens) {
        this.listExerc = itens;
        this.context = context ;
    }

    @Override
    public int getCount() {
        return listExerc.size();
    }

    @Override
    public Object getItem(int position) {
        return listExerc.get(position);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ItemListMeuTreino item = listExerc.get(position);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.list_treino_hoje_item, null);

        TextView nomeExercicio = (TextView) layout.findViewById(R.id.item_lv_nome);
        nomeExercicio.setText(item.getNome());

        TextView repExercicio = (TextView) layout.findViewById(R.id.item_lv_rep);
        repExercicio.setText(Integer.toString(item.getRepeticoes()));

        TextView cargaExercicio = (TextView) layout.findViewById(R.id.item_lv_carga);
        cargaExercicio.setText(Integer.toString(item.getCarga()));

        return layout;
    }
}
