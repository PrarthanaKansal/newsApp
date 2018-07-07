package com.example.prarthana.newsapp;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class DetailActivity extends AppCompatActivity {

  //  @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater=getMenuInflater();
//        inflater.inflate(R.menu.menu_detail,menu);
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id= item.getItemId();
//
//
//        if(id==R.id.menu_detail_share){
//            Intent intent = new Intent();
//            intent.setAction(Intent.ACTION_SEND);
//            News_Article article= (News_Article) getIntent().getSerializableExtra("News_Article");
//
//            intent.putExtra(Intent.EXTRA_SUBJECT,article.getTitle());
//            intent.putExtra(Intent.EXTRA_TEXT,article.getUrl());
//           intent.setType("text/plain");
//           startActivity(Intent.createChooser(intent,"SHARE"));
//           // startActivity(intent);
//
//
//            //Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show();
//        }
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
//        ImageView image= (ImageView) findViewById(R.id.article_img);
//        TextView title= (TextView) findViewById(R.id.article_title);
//        TextView desc=(TextView) findViewById(R.id.article_des);
//        //TextView story= (TextView) findViewById(R.id.story);
//        TextView url= (TextView) findViewById(R.id.url);
//        if(getIntent().getSerializableExtra("News_Article")!=null){
//            Intent intent=getIntent();
//
//
//            News_Article article= (News_Article) getIntent().getSerializableExtra("News_Article");
//            title.setText(article.getTitle());
//            desc.setText(article.getDesc());
//            url.setText(article.getUrl());
//            Picasso.with(this).load(article.getImg()).into(image);
//            title.setText(intent.getStringExtra("title"));
//            desc.setText(intent.getStringExtra("Description"));
//            url.setText(intent.getStringExtra("URL"));
//            Picasso.with(this).load(intent.getStringExtra("Image")).into(image);
        }
    }




