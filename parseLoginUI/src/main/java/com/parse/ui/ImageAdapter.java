package com.parse.ui;

import android.content.Context;
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
    private int [] images = new int[]{
            R.drawable.tuto_0novo_treino,
            R.drawable.tuto_0sidebar,
            R.drawable.tuto_0trainig_detail,
            R.drawable.tuto_add_exercicio};

    public ImageAdapter(Context context){
        this.contexto = context;
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
        int padding = contexto.getResources().getDimensionPixelSize(R.dimen.padding_medium);
        imageView.setPadding(padding, padding, padding, padding);
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        imageView.setImageResource(images[position]);
        ((ViewPager) container).addView(imageView, 0);


        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((ImageView) object);
    }
}
