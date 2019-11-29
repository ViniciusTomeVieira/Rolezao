package br.udesc.rolezao.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TableLayout;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import br.udesc.rolezao.R;

public class IntroActivity extends AppCompatActivity {
    private ViewPager screenPager;
    IntroViewPagerAdapter introViewPagerAdapter;
    Button btnProximo;
    int position = 0;
    Button btnGetStarted;

    Animation btnAnimation;

    TabLayout tabIndicator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setContentView(R.layout.activity_intro);



        btnProximo = findViewById(R.id.btn_proximo);
        btnGetStarted = findViewById(R.id.btn_comecar);
        tabIndicator =findViewById(R.id.table_indicator);
        btnAnimation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.button_animation);

        final List<ScreenItem> mList = new ArrayList<>();
        mList.add(new ScreenItem("Bem Vindo","Aqui você pode encontrar roles, caso estiver com tédio", R.drawable.boring));
        mList.add(new ScreenItem("Clique no botão + para criar um role"," Seu role ficará disponível para as pessoas da sua região", R.drawable.instrucao));
        mList.add(new ScreenItem("Vai rolar até o amanhecer","Projeto x", R.drawable.projeto_x));


        screenPager = findViewById(R.id.screen_viewpage);

        introViewPagerAdapter = new IntroViewPagerAdapter(mList,this);
        screenPager.setAdapter(introViewPagerAdapter);

        tabIndicator.setupWithViewPager(screenPager);

        btnProximo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                position = screenPager.getCurrentItem();
                if(position<mList.size()){

                    position++;
                    screenPager.setCurrentItem(position);
                }
                if(position == mList.size()-1){
                    loadLastScreen();
                }
            }
        });

        tabIndicator.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getPosition()==mList.size()-1){
                    loadLastScreen();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        btnGetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainActivity = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(mainActivity);
            }
        });
}

    private void loadLastScreen() {
        btnProximo.setVisibility(View.INVISIBLE);
        btnGetStarted.setVisibility(View.VISIBLE);
        tabIndicator.setVisibility(View.INVISIBLE);

        btnGetStarted.setAnimation(btnAnimation);

    }
}
