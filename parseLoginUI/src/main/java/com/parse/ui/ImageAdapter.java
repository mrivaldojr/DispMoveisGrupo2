package com.parse.ui;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


/**
 * Created by junior on 08/12/14.
 */
public class ImageAdapter extends PagerAdapter {

    private Context contexto;
    private Resources resources;

    private int [] images = new int[]{
            R.drawable.tuto1_perfil,
            R.drawable.tuto2_meus_treinos,
            R.drawable.tuto3_destalhes,
            R.drawable.tuto4_exercicio,
            R.drawable.tuto5_novo_treino,
            R.drawable.tuto6_adicionar_exerc,
            R.drawable.tuto7_treino_hoje
            };

    public ImageAdapter(Context context){
        this.contexto = context;
        this.resources = context.getResources();
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view == ((ImageView) o);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = new ImageView(contexto);
        //int padding = contexto.getResources().getDimensionPixelSize(R.dimen.padding_medium);
        //imageView.setPadding(padding, padding, padding, padding);
        //imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        imageView.setImageBitmap(BitmapFactory.decodeResource(resources, images[position]));
       // imageView.setImageResource(images[position]);
        ((ViewPager) container).addView(imageView, 0);


        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View)object;
        ((ViewPager) container).removeView(view);
        view = null;
    }
}
