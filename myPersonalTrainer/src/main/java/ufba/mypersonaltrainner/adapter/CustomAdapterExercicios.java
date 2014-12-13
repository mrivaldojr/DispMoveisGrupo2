package ufba.mypersonaltrainner.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ufba.mypersonaltrainner.R;
import ufba.mypersonaltrainner.model.Exercicio;


public class CustomAdapterExercicios  extends BaseAdapter {

    Context context;
    List<Exercicio> listExerc;


    public CustomAdapterExercicios(Context context, ArrayList<Exercicio> itens) {
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
        return i;
    }

    public void clear() {
        listExerc.clear();
    }

    public void add(Exercicio elemento) {
        listExerc.add(elemento);
    }

    public void addAll(ArrayList<Exercicio> lista) {
        listExerc.addAll(lista);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        TextView nomeView;
        TextView seriesView;
        TextView cargaView;

        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.list_treino_hoje_item, viewGroup, false);
            nomeView = (TextView) convertView.findViewById(R.id.item_lv_nome);
            seriesView = (TextView) convertView.findViewById(R.id.item_lv_rep);
            cargaView = (TextView) convertView.findViewById(R.id.item_lv_carga);
            convertView.setTag(new ViewHolder(nomeView, seriesView, cargaView));
        } else {
            ViewHolder viewHolder = (ViewHolder) convertView.getTag();
            nomeView = viewHolder.nomeView;
            seriesView = viewHolder.seriesView;
            cargaView = viewHolder.cargaView;
        }

        Exercicio item = listExerc.get(position);

        nomeView.setText(item.getNome());
        // LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // View layout = inflater.inflate(R.layout.list_treino_hoje_item, null);

        nomeView.setText(item.getNome());
        seriesView.setText(item.getSeries());
        cargaView.setText(item.getCarga());

        return convertView;
    }


    private static class ViewHolder {
        public final TextView nomeView;
        public final TextView seriesView;
        public final TextView cargaView;

        public ViewHolder(TextView nomeView, TextView seriesView, TextView cargaView) {
            this.nomeView = nomeView;
            this.seriesView = seriesView;
            this.cargaView = cargaView;
        }
    }
}
