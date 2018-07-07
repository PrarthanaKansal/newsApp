package com.example.prarthana.newsapp;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.example.prarthana.newsapp.data.NewsDBHelper;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    // ArrayList<String> titles;
    ArrayList<News_Article> news_List;
    News_recycler_adapter news_recycler_adapter;
    String TAG = MainActivity.class.getSimpleName();
    NewsDBHelper newsDBHelper;
    ArrayList<ContentValues> listOfContentValues = new ArrayList<>();
    ContentResolver cr;
    Boolean twoPane;
    ViewPager viewPager;
    TabLayout tabLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);

        if (findViewById(R.id.detailfragmentcontainer) == null) {
            twoPane = false;

        } else {
            twoPane = true;
        }
        viewPagerAdapter ViewPagerAdapter= new viewPagerAdapter(getSupportFragmentManager());
        ViewPagerAdapter.addFragment(MainActivityFragment.newInstance("general"), "General");
        ViewPagerAdapter.addFragment(MainActivityFragment.newInstance("sports"), "Sports");

        viewPager.setAdapter(ViewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);


    }

    public void onItemSelected(News_Article article) {

        if (twoPane == false) {
            Intent intent = new Intent(this, DetailActivity.class);
            //intent.putExtra("News_Article",news_article);
            intent.putExtra("title", article.getTitle());
            intent.putExtra("Description", article.getDesc());
            intent.putExtra("Image", article.getImg());
            intent.putExtra("URL", article.getUrl());
            this.startActivity(intent);
        }
       else{
            Bundle bundle = new Bundle();
//            //bundle.putSerializable("News_Article",article);
            bundle.putString("title",article.getTitle());
            bundle.putString("Description",article.getDesc());
            bundle.putString("Image",article.getImg());
            bundle.putString("URL",article.getUrl());
            android.widget.FrameLayout view = findViewById(R.id.detailfragmentcontainer);
            view.removeAllViews();

//
//
//
            DetailActivityFragment fragment = new DetailActivityFragment();
            fragment.setArguments(bundle);
//
            getSupportFragmentManager().beginTransaction().replace(R.id.detailfragmentcontainer,fragment,"fragment").commit();
        }

    }


//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        Log.e(TAG, "OnSave");
//        outState.putString("value", "1");
//        outState.putSerializable("newsArticles", news_List);
//        super.onSaveInstanceState(outState);
//    }
//
//    @Override
//    protected void onRestoreInstanceState(Bundle savedInstanceState) {
//        Log.e(TAG, "OnRestore");
//        super.onRestoreInstanceState(savedInstanceState);
//        String value = (String) savedInstanceState.get("value");
//        news_List = (ArrayList<News_Article>) savedInstanceState.getSerializable("newsArticles");
//        news_recycler_adapter.addAll(news_List);
//        news_recycler_adapter.notifyDataSetChanged();
//    }

}

