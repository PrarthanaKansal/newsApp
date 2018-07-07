package com.example.prarthana.newsapp;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailActivityFragment extends Fragment {


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_detail,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id= item.getItemId();


        if(id==R.id.menu_detail_share){
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            News_Article article= (News_Article) getActivity().getIntent().getSerializableExtra("News_Article");

            intent.putExtra(Intent.EXTRA_SUBJECT,article.getTitle());
            intent.putExtra(Intent.EXTRA_TEXT,article.getUrl());
            intent.setType("text/plain");
            startActivity(Intent.createChooser(intent,"SHARE"));
            // startActivity(intent);


            //Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View detailView = inflater.inflate(R.layout.fragment_detail, container);

        ImageView image = (ImageView) detailView.findViewById(R.id.article_img);
        TextView title = (TextView) detailView.findViewById(R.id.article_title);
        TextView desc = (TextView) detailView.findViewById(R.id.article_des);
        //TextView story= (TextView) findViewById(R.id.story);
        TextView url = (TextView) detailView.findViewById(R.id.url);
        Bundle arguments = getArguments();
//        if(arguments!=null){
//
//           // title.setText(arguments.getSerializable("title"));
//            title.setText(arguments.getString("title"));
//            desc.setText(arguments.getString("Description"));
//            url.setText(arguments.getString("URL"));
//            Picasso.with(getActivity()).load(arguments.getString("Image")).into(image);


//            News_Article article = (News_Article) arguments.getParcelable("News_Article");
//            title.setText(article.getTitle());
//            desc.setText(article.getDesc());
//            url.setText(article.getUrl());
//            Picasso.with(getActivity()).load(article.getImg()).into(image);
//        }


        if(getActivity().getIntent().getSerializableExtra("title") != null) {


            Intent intent =getActivity().getIntent();
            Picasso.with(getActivity()).load(intent.getStringExtra("Image")).into(image);
            title.setText(intent.getStringExtra("title"));
            desc.setText(intent.getStringExtra("Description"));
            url.setText(intent.getStringExtra("URL"));



//            News_Article article = (News_Article) getActivity().getIntent().getSerializableExtra("News_Article");
//            title.setText(article.getTitle());
//            desc.setText(article.getDesc());
//            url.setText(article.getUrl());
//            Picasso.with(getActivity()).load(article.getImg()).into(image);

        }
        return detailView;
    }
}

