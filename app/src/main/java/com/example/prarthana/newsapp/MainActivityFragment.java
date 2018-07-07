package com.example.prarthana.newsapp;

import android.app.AlarmManager;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.example.prarthana.newsapp.Sync.NetworkUtil;
import com.example.prarthana.newsapp.Sync.NewsIntentService;
import com.example.prarthana.newsapp.data.NewsContract;
import com.example.prarthana.newsapp.data.NewsDBHelper;

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
import java.util.Calendar;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import static android.content.Context.MODE_PRIVATE;

public class MainActivityFragment extends Fragment {
    ArrayList<News_Article> news_List = new ArrayList<>();
    ;
    News_recycler_adapter news_recycler_adapter;
    String TAG = MainActivityFragment.class.getSimpleName();
    NewsDBHelper newsDBHelper;
    ProgressBar progressBar;
    ArrayList<ContentValues> listOfContentValues = new ArrayList<>();
    ContentResolver cr;
    RecyclerView recyclerView;
    SharedPreferences.OnSharedPreferenceChangeListener onSharedPreferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            String Source = sharedPreferences.getString(key, "the-verge");
            fetchnews fetchnews = new fetchnews();
            fetchnews.execute(Source);
        }
    };


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_main_refresh) {
            progressBar.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            if (new NetworkUtil().checkNetwork(getActivity())) {

                if (getArguments().get("id").equals("general")) {
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                    String Source = sharedPreferences.getString("Source", "the-verge");
                    new fetchnews().execute(Source);
                } else {
                    new fetchnews().execute("bbc-sport");
                }
                // Toast.makeText(this, "Refreshed", Toast.LENGTH_SHORT).show();
            }

            else{

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder//.setMessage("Not connected to internet")
                .setTitle(getString(R.string.n0_network))
                        .setView(R.layout.dialog)
                ;
                final AlertDialog dialog=builder.create();
                dialog.setContentView(R.layout.dialog);
                Button ok = dialog.findViewById(R.id.ok);
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                      dialog.dismiss();
                    }
                });
                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                builder.show();
            }
        }


        if (id == R.id.settings) {
            Intent intent = new Intent(getActivity(), SettingsActivity.class);
            this.startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // Log.e(TAG, "on save instance state");
        outState.putString("value", "1");
        outState.putSerializable("newsList", news_List);
        super.onSaveInstanceState(outState);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.e(TAG, "on restore instance state");
        if (savedInstanceState != null) {
            String value = (String) savedInstanceState.get("value");

            news_List = (ArrayList<News_Article>) savedInstanceState.getSerializable("newsList");
            news_recycler_adapter.addAll(news_List);
            news_recycler_adapter.notifyDataSetChanged();
        }

        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);


    }

    //helps in saving memory
    public static MainActivityFragment newInstance(String id) {

        Bundle args = new Bundle();
        args.putString("id", id);

        MainActivityFragment fragment = new MainActivityFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        progressBar = view.findViewById(R.id.progress);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        progressBar.setIndeterminate(true);
        recyclerView.setVisibility(View.GONE);


        SharedPreferences preferences = getActivity().getSharedPreferences("Alarm", MODE_PRIVATE);
        //String alarm;
        if (preferences.getString("alarm", "10*60*1000") == null) {
//            View msg= view.<View>findViewById(R.id.time);
//            String message= msg.toString();
//            long time= (Long.parseLong(message));
//            EditText editMessage=(EditText) view.findViewById(R.id.time);
//            String messageString=editMessage.getText().toString();
//            long time= (Long.parseLong(messageString));
            //Log.e(TAG,messageString);
            Intent intent = new Intent(getActivity(), NewsIntentService.class);
            PendingIntent pendingIntent = PendingIntent.getService(getActivity(), 1, intent, 0);
            AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(getActivity().ALARM_SERVICE);
            alarmManager.setRepeating(AlarmManager.RTC, Calendar.getInstance().getTimeInMillis(), 6 * 60 * 1000, pendingIntent);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("alarm", "1");
            editor.commit();

        }


        news_recycler_adapter = new News_recycler_adapter(news_List, getActivity());

        if (getResources().getConfiguration().orientation == ActivityInfo.SCREEN_ORIENTATION_USER) {
            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        } else {
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        }
        recyclerView.setAdapter(news_recycler_adapter);
        Log.e(TAG, "onCreate");


        //Cursor cursor;

        fetchnews fetchnews = new fetchnews();
        newsDBHelper = new NewsDBHelper(getActivity());
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        if (savedInstanceState == null) {
            if (getArguments().get("id").equals("general")) {
                newsDBHelper.openConnection();
                Uri CONTENT_URI = NewsContract.aricleEntry.CONTENT_URI.buildUpon().appendPath("title").build();

                Cursor cursor = getActivity().getContentResolver().query(NewsContract.aricleEntry.CONTENT_URI, null, null, null, null);


                //cursor.
                if (cursor.getCount() > 0) {
                    news_List.clear();
                    while (cursor.moveToNext()) {
                        News_Article news_article = new News_Article();
                        news_article.setTitle(cursor.getString(cursor.getColumnIndex(NewsContract.aricleEntry.COLUMN_TITLE)));
                        news_article.setDesc(cursor.getString(cursor.getColumnIndex(NewsContract.aricleEntry.COLUMN_DESCRIPTION)));
                        news_article.setUrl(cursor.getString(cursor.getColumnIndex(NewsContract.aricleEntry.COLUMN_URL)));
                        news_article.setImg(cursor.getString(cursor.getColumnIndex(NewsContract.aricleEntry.COLUMN_URL_TO_IMAGE)));
                        news_List.add(news_article);
                    }
                    progressBar.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    news_recycler_adapter.addAll(news_List);
                    news_recycler_adapter.notifyDataSetChanged();

                } else {
                    String Source = sharedPreferences.getString("Source", "the-verge");

                    fetchnews.execute(Source);
                }
            } else {
                fetchnews.execute("bbc-sport");
            }
        }
        sharedPreferences.registerOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener);
        cr = getActivity().getContentResolver();
//        Cursor cursor1=  cr.query(ContactsContract.Contacts.CONTENT_URI,null,null,null,null);
//        //Log.e("URI",ContactsContract.Contacts.CONTENT_URI.toString());
//        String phoneNo;
//        while(cursor1.moveToNext()){
//            String name= cursor1.getString(cursor1.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
//            phoneNo=cursor1.getString(cursor1.getColumnIndex(ContactsContract.Contacts._ID));
//        }


        createNotificationChannel();


        return view;
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "General News";
            String description = "For showing general news";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("1", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getActivity().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public class fetchnews extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            HttpsURLConnection urlConnection = null;
            BufferedReader reader = null;
            String JsonStr = null;
            String source = strings[0];
            try {
                URL url = new URL("https://newsapi.org/v1/articles?source=" + source + "&apiKey=63c1259156e44fe898c950d5662b8bfe");
                urlConnection = (HttpsURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setSSLSocketFactory(buildSslSocketFactory(getActivity()));
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
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.e(TAG, "Error");
            if (result != null) {
                news_List.clear();
                try {
                    final String articles = "articles";
                    final String author = "author";
                    final String title = "title";
                    final String url = "url";
                    final String desc = "description";
                    final String img = "urlToImage";
                    JSONObject obj = new JSONObject(result);
                    JSONArray artArray = obj.getJSONArray(articles);

                    for (int i = 0; i < artArray.length(); i++) {
                        JSONObject news_article = artArray.getJSONObject(i);
                        String news_title = news_article.getString(title);
                        String news_author = news_article.getString(author);
                        String news_desc = news_article.getString(desc);
                        String img_url = news_article.getString(img);
                        String news_url = news_article.getString(url);

                        News_Article news_art = new News_Article();
                        news_art.setTitle(news_title);
                        news_art.setAuthor(news_author);
                        news_art.setUrl(news_url);
                        news_art.setDesc(news_desc);
                        news_art.setImg(img_url);
                        news_List.add(news_art);

                        ContentValues contentValues = new ContentValues();
                        contentValues.put(NewsContract.aricleEntry.COLUMN_TITLE, news_title);
                        contentValues.put(NewsContract.aricleEntry.COLUMN_DESCRIPTION, news_desc);
                        contentValues.put(NewsContract.aricleEntry.COLUMN_URL, news_url);
                        contentValues.put(NewsContract.aricleEntry.COLUMN_URL_TO_IMAGE, img_url);
                        contentValues.put(NewsContract.aricleEntry.COLUMN_CATEGORY, "general");
                        listOfContentValues.add(contentValues);
                        //titles.add(news_title);
                    }
                    // newsDBHelper.openConnection();


                    if (getArguments().get("id").equals("general")) {
                        getActivity().getContentResolver().delete(NewsContract.aricleEntry.CONTENT_URI, null, null);

                        //newsDBHelper.clearNews();
                        ContentValues[] values = new ContentValues[listOfContentValues.size()];
                        listOfContentValues.toArray(values);

                        getActivity().getContentResolver().bulkInsert(NewsContract.aricleEntry.CONTENT_URI, values);
                    }
                    //newsDBHelper.insertNews(listOfContentValues);

                    progressBar.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);

                    news_recycler_adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
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


