package com.example.prarthana.newsapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class News_Adapter extends ArrayAdapter<News_Article> {
    ArrayList<News_Article> news;
    private final Context context;

    public News_Adapter(@NonNull Context context, ArrayList<News_Article> newsList) {
        super(context, 0,newsList);
        this.context=context;
        this.news=newsList;
    }

    @Nullable
    @Override
    public News_Article getItem(int position) {
        return news.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
       // return super.getView(position, convertView, parent);
        if (convertView == null) {
            //Context context;
            convertView = LayoutInflater.from(context).inflate(R.layout.news_item, parent, false);
        }
        TextView title= (TextView) convertView.findViewById(R.id.tle);
        //TextView description=(TextView) convertView.findViewById(R.id.des);
        ImageView img= (ImageView) convertView.findViewById(R.id.image);

        News_Article title_str=getItem(position);
        title.setText(title_str.getTitle());
       // description.setText(title_str.getDesc());
        Picasso.with(context).load(title_str.getImg()).into(img);
        return convertView;
    }
}
