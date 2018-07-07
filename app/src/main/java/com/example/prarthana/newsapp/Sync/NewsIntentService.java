package com.example.prarthana.newsapp.Sync;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.example.prarthana.newsapp.DetailActivity;
import com.example.prarthana.newsapp.MainActivity;
import com.example.prarthana.newsapp.News_Article;
import com.example.prarthana.newsapp.R;
import com.example.prarthana.newsapp.data.NewsContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

public class NewsIntentService extends IntentService {
    ArrayList<ContentValues> listOfContentValues= new ArrayList<>();
    String TAG =NewsIntentService.class.getSimpleName();
    News_Article notificationArticle = null;



    public NewsIntentService() {
        super("NewsIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.e("NewsIntentService","Intent");
        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);
        String Source=sharedPreferences.getString("Source","the-verge");
        new fetchnews().execute(Source);


    }
    public class fetchnews extends AsyncTask<String,Void,String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            HttpsURLConnection urlConnection = null;
            BufferedReader reader = null;
            String JsonStr = null;
            String source= strings[0];
            try {
                URL url = new URL("https://newsapi.org/v1/articles?source="+ source+"&apiKey=63c1259156e44fe898c950d5662b8bfe");
                urlConnection = (HttpsURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setSSLSocketFactory(buildSslSocketFactory(getApplicationContext()));
                urlConnection.connect();
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }
                JsonStr = buffer.toString();
                Log.d("News Response", JsonStr);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(TAG, "Error closing stream", e);
                    }
                }
            }

            return JsonStr;
        }
        @Override
        protected void onPostExecute (String result){
            super.onPostExecute(result);
            if(result!=null){

                try {
                    final String articles="articles";
                    final String author="author";
                    final String title="title";
                    final String url = "url";
                    final String desc="description";
                    final String img="urlToImage";
                    JSONObject obj= new JSONObject(result);
                    JSONArray artArray= obj.getJSONArray(articles);

                    for(int i=0;i<artArray.length();i++){
                        JSONObject news_article=artArray.getJSONObject(i);
                        String news_title=news_article.getString(title);
                        String news_author=news_article.getString(author);
                        String news_desc=news_article.getString(desc);
                        String img_url=news_article.getString(img);
                        String news_url=news_article.getString(url);

                        News_Article news_art= new News_Article();
                        news_art.setTitle(news_title);
                        news_art.setAuthor(news_author);
                        news_art.setUrl(news_url);
                        news_art.setDesc(news_desc);
                        news_art.setImg(img_url);
                        if(notificationArticle==null){
                            notificationArticle=news_art;
                        }

                        ContentValues contentValues= new ContentValues();
                        contentValues.put(NewsContract.aricleEntry.COLUMN_TITLE,news_title);
                        contentValues.put(NewsContract.aricleEntry.COLUMN_DESCRIPTION,news_desc);
                        contentValues.put(NewsContract.aricleEntry.COLUMN_URL,news_url);
                        contentValues.put(NewsContract.aricleEntry.COLUMN_URL_TO_IMAGE,img_url);
                        contentValues.put(NewsContract.aricleEntry.COLUMN_CATEGORY,"general");
                        listOfContentValues.add(contentValues);
                        //titles.add(news_title);
                    }
                    // newsDBHelper.openConnection();





                    getContentResolver().delete(NewsContract.aricleEntry.CONTENT_URI,null,null);
                    //newsDBHelper.clearNews();
                    ContentValues[] values= new ContentValues[listOfContentValues.size()];
                    listOfContentValues.toArray(values);

                    getContentResolver().bulkInsert(NewsContract.aricleEntry.CONTENT_URI,values);
                    issueNotification();
                    //newsDBHelper.insertNews(listOfContentValues);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    private void issueNotification() {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("title", notificationArticle.getTitle());
        intent.putExtra("Description", notificationArticle.getDesc());
        intent.putExtra("Image", notificationArticle.getImg());
        intent.putExtra("URL", notificationArticle.getUrl());

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntentWithParentStack(intent);

        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);


//
//        PendingIntent resultPendingIntent =
//                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

//        PendingIntent pendingIntent = PendingIntent.getActivity(getBaseContext(),1,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, "1")
                .setSmallIcon(R.drawable.ic_stat_name)
                .setContentTitle(notificationArticle.getTitle()).setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.download))
                .setContentText(notificationArticle.getDesc())
                .setVisibility(NotificationCompat.VISIBILITY_SECRET)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setStyle(new NotificationCompat.BigTextStyle()

                       .bigText(notificationArticle.getDesc()))
//                .setStyle(new NotificationCompat.BigPictureStyle()
//                        .bigPicture(BitmapFactory.decodeResource(getResources(),R.drawable.download)))
                .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })
                .setContentIntent(resultPendingIntent)
                .setAutoCancel(true);



        final NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);


// notificationId is a unique int for each notification that you must define
//        final Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//
//                notificationManager.notify(1, mBuilder.build());
//            }
//        },5000);

        notificationManager.notify(1, mBuilder.build());
    }

    private SSLSocketFactory buildSslSocketFactory(Context context) {
        // Add support for self-signed (local) SSL certificates
        // Based on http://developer.android.com/training/articles/security-ssl.html#UnknownCa
        try {

            // Load CAs from an InputStream
            // (could be from a resource or ByteArrayInputStream or ...)
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            // From https://www.networking4all.com/en/ssl+certificates/quickscan/?lang=en&archive=latest&host=newsapi.org
            InputStream is = context.getResources().getAssets().open("USERTrustRSACertificationAuthority.crt");
            InputStream caInput = new BufferedInputStream(is);
            Certificate ca;
            try {
                ca = cf.generateCertificate(caInput);
                // System.out.println("ca=" + ((X509Certificate) ca).getSubjectDN());
            } finally {
                caInput.close();
            }

            // Create a KeyStore containing our trusted CAs
            String keyStoreType = KeyStore.getDefaultType();
            KeyStore keyStore = KeyStore.getInstance(keyStoreType);
            keyStore.load(null, null);
            keyStore.setCertificateEntry("ca", ca);

            // Create a TrustManager that trusts the CAs in our KeyStore
            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(keyStore);

            // Create an SSLContext that uses our TrustManager
            SSLContext context1 = SSLContext.getInstance("TLS");
            context1.init(null, tmf.getTrustManagers(), null);
            return context1.getSocketFactory();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }
}


