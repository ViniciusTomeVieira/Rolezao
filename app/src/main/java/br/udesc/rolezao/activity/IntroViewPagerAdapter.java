package br.udesc.rolezao.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.List;

import br.udesc.rolezao.R;

public class IntroViewPagerAdapter extends PagerAdapter {
    List<ScreenItem> mListScreen;
    Context mContext;

    public IntroViewPagerAdapter(List<ScreenItem> mListScreen, Context mContext) {
        this.mListScreen = mListScreen;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layoutScreen = inflater.inflate(R.layout.layout_screen,null);

        ImageView imgSlide = layoutScreen.findViewById(R.id.intro_img);
        TextView titulo = layoutScreen.findViewById(R.id.intro_title);
        TextView descricao = layoutScreen.findViewById(R.id.intro_description);

        titulo.setText(mListScreen.get(position).getTitulo());
        descricao.setText(mListScreen.get(position).getDescricao());
        imgSlide.setImageResource(mListScreen.get(position).getScreenImg());

        container.addView(layoutScreen);
        return layoutScreen;
    }

    @NonNull


    @Override
    public int getCount() {
        return mListScreen.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {

        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }
}
