package com.example.prarthana.newsapp;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class News_recycler_adapter extends RecyclerView.Adapter<News_recycler_adapter.NewsViewHolder> {
    ArrayList<News_Article> newsArticles;
    NewsViewHolder viewHolder;
    Context context;

    public News_recycler_adapter(ArrayList<News_Article> newsArticles, Context context){
        this.newsArticles=newsArticles;
        this.context=context;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item, parent, false);
        viewHolder=new NewsViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        News_Article article=newsArticles.get(position);
        holder.title.setText(article.getTitle());
        //holder.description.setText(article.getDesc());
        Picasso.with(context).load(article.getImg()).into(holder.img);
    }

    @Override
    public int getItemCount() {
        return newsArticles.size();
    }

    public void addAll(ArrayList<News_Article> newsArticles) {
        this.newsArticles=newsArticles;

    }

    public class NewsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView title;
       // TextView description;
        ImageView img;

        public NewsViewHolder(View itemView) {
            super(itemView);
            title= (TextView) itemView.findViewById(R.id.tle);
            //description=(TextView) itemView.findViewById(R.id.des);
            img= (ImageView) itemView.findViewById(R.id.image);
            itemView.setOnClickListener(this);
            //TextView description= (TextView) itemView.findViewById(R.id.des);

        }

        @Override
        public void onClick(View v) {
            News_Article article=newsArticles.get(getAdapterPosition());

            ((MainActivity) context).onItemSelected(article);
//            Intent intent= new Intent(context,DetailActivity.class);
//            intent.putExtra("News_Article",article);

//            intent.putExtra("title",article.getTitle());
//            intent.putExtra("Description",article.getDesc());
//            intent.putExtra("Image",article.getImg());
//            intent.putExtra("URL",article.getUrl());
            //context.startActivity(intent);
        }
    }

}

