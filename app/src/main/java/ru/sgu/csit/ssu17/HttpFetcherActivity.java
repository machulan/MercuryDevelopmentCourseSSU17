package ru.sgu.csit.ssu17;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.webkit.ConsoleMessage.MessageLevel.LOG;

/**
 * Created by User on 13.04.2017.
 */

public class HttpFetcherActivity extends Activity implements LoaderManager.LoaderCallbacks<List<Article>> {

    private static final String LOG_TAG = "HttpFetcherActivity";
    private int loadedCount = 0;

    private final List<Article> data = new ArrayList<>();
    private ArrayAdapter<Article> adapter;
    private FetchTask loadTask = null;
    //private


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_http_fetcher);

        Log.d(LOG_TAG, "onCreate - savedInstanceState is "
                + (savedInstanceState != null ? "not null" : "null"));

        Log.d(LOG_TAG, "onCreate - retainedInstance is "
        + getLastNonConfigurationInstance());

        Button okbtn = (Button) findViewById(R.id.ok_btn);

        if(getLastNonConfigurationInstance() != null){
            //this.l
            this.loadTask = (FetchTask) getLastNonConfigurationInstance();
            loadTask.setActivity(this);
        }

        okbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //EditText urlEdit = (EditText) findViewById(R.id.url_edit);
                //loadData(urlEdit.getText().toString());

                getLoaderManager().restartLoader(0, null, HttpFetcherActivity.this);
                //loadData();
                //some comment


            }
        });

        ListView articleList = (ListView) findViewById(R.id.articles_list);
        adapter = new ArrayAdapter<Article>(this, android.R.layout.simple_list_item_1, data);
        articleList.setAdapter(adapter);


        articleList.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Article article = (Article) parent.getItemAtPosition(position);
                String url = "http://www.sgu.ru/news.xml";//article.link
                Intent openBrowserIntent = new Intent(Intent.ACTION_VIEW);
                //List<Article> arts = new SSUNewsXmlParser().parse(url);
                openBrowserIntent.setData(Uri.parse(url));
                startActivity(openBrowserIntent);
            }
        });


        getLoaderManager().initLoader(0, null, this);
        //loadData();

        //восстановить instance state
        /*if(savedInstanceState != null){
            this.data = savedInstanceState.getString("data");
        }
        refreshUi();*/

        //if saved instance state == null => приложение впервые запущено

    }

    @Override
    public Object onRetainNonConfigurationInstance() {
        //сохраняет между двумя instances java объект
        return loadTask; // super.onRetainNonConfigurationInstance();
    }

   /* @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d("HttpFetcherActivity", "onSaveInstanceState");
        outState.putString("data", data);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        //this.data = savedInstanceState.getString("data");
        refreshUi();
    }*/

    //private void refreshUi() {
      //  TextView responseText = (TextView) findViewById(R.id.response_text);
        //responseText.setText(data != null ? data : "No data");
//    }


//    private void loadData() {
//        //new FetchTask().execute();
//        this.loadTask = new FetchTask(this);
//        loadTask.execute();
//
//    }

    @Override
    public Loader<List<Article>> onCreateLoader(int id, Bundle args) {
        return new DataLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<List<Article>> loader, List<Article> data) {
        this.data.clear();
        this.data.addAll(data);
        adapter.notifyDataSetChanged();
        //this.data = data;
        //refreshUi();
    }

    @Override
    public void onLoaderReset(Loader<List<Article>> loader) {

    }


    // private void loadData(String url) {

       /* final Handler handler = new Handler(Looper.getMainLooper());

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            ++loadedCount;
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        TextView responseText = (TextView) findViewById(R.id.response_text);
                        responseText.setText("Loaded " + loadedCount + " times");
                    }
                });
            }
        }).start();*/

      // new FetchTask(url).execute();
    //}


    private static class FetchTask extends AsyncTask<Void, Void, String> {

        private HttpFetcherActivity activity;


        private final String url = null;

        FetchTask(HttpFetcherActivity activity){
            //super();
            this.activity = activity;
        }

        public void setActivity(HttpFetcherActivity activity){
            this.activity = activity;
        }



        //FetchTask(String url){
//            super();
//            this.url = url;
//        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            //TODO
            //PROBLEM : при передаче в parse istream начинает считывание с четвертого символа
            //

            //my code below

//            if(false) {
//                SSUNewsXmlParser ssuNewsXmlParser = new SSUNewsXmlParser();
//                List<Article> articles = null;
//                StringBuilder resultString = new StringBuilder();
//                InputStream istream = null;
//
//                try {
//                    //URL url = new URL(this.url);
//                    final String ssuNewsUrl = "http://www.sgu.ru/news.xml";
//                    URL url = new URL(ssuNewsUrl);
//                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//                    try {
//                        istream = conn.getInputStream();
//
//                        articles = ssuNewsXmlParser.parse(istream);
//                        Article a1 = new Article("TITLE 1", "DESCRIPTION1", "123");
//                        Article a2 = new Article("TITLE 2", "alkdf;lakfb fds\n advv \naadddddddDDD", "15444");
//                        Article a3 = new Article("TITLE 3", "DESCasdfagf", "hfirufhv");
//
//                        articles = new ArrayList<>();
//                        articles.add(a1);
//                        articles.add(a2);
//                        articles.add(a3);
//
//                        if(true) {
//                            //articles = new ArrayList<Article>();
//                            resultString.append("articles size : ");
//                            resultString.append(articles.size());
//                            return resultString.toString();
//                        }
//
//                        //resultString.append("<article>2");
//
//
//
//                        //work with articles
//                        final String tabString = "    ";
//                        for(Article article : articles){
//                            resultString.append("<article>\n");
//                            resultString.append(tabString + "<title>" + article.title + "</title>\n");
//                            resultString.append(tabString + "<description>" + article.description + "</description>\n");
//                            resultString.append(tabString + "<pubDate>" + article.pubDate + "</pubDate>\n");
//                            resultString.append("</article>\n");
//                        }
//
//                        if (istream != null) {
//                            istream.close();
//                        }
//                    } finally {
//
//                        conn.disconnect();
//                    }
//
//                } catch (MalformedURLException e) {
//                    e.printStackTrace();
//                } catch (XmlPullParserException e) {
//                    e.printStackTrace();
//                }
//                catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//
//                return resultString.toString();
//
//                //Calendar rightNow = Calendar.getInstance();
//                //DateFormat formatter = new SimpleDateFormat("MMM dd h:mmaa"); --
//                //DateFormat formatter = new SimpleDateFormat("YYYY-MM-dd hh:mm:ss");
//                //return formatter.format(rightNow.getTime());
//            }
            //my code above

            /*try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ++loadedCount;*/
            String res = null;
            try {
                final String ssuNewsUrl = "http://www.sgu.ru/news.xml";
                URL url = new URL(ssuNewsUrl);

                //URL url = new URL(this.url);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                try {
                    InputStream istream = conn.getInputStream();
                    ByteArrayOutputStream ostream = new ByteArrayOutputStream();
                    try {
                       // istream.read();

                        byte[] buf = new byte[32 * 1024];
                        while(true){
                            int bytesRead = istream.read(buf);
                            if(bytesRead < 0)
                                break;
                            ostream.write(buf, 0, bytesRead);
                        }
                        res = ostream.toString("UTF-8");

                      /*  istream.read(buf);
                        ostream.write(buf);*/
                    } finally {
                        istream.close();
                        ostream.close();
                    }

                }finally {
                    conn.disconnect();
                }



            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            //res = res.substring(1);

            //try{
             //   List<Article> articles = new SSUNewsXmlParser().parse(res);



  //          } catch (XmlPullParserException e) {
//                e.printStackTrace();
            //} catch (IOException e) {
            //    e.printStackTrace();
            //}

            return res;
        }

        @Override
        protected void onPostExecute(String response) {
            //HttpFetcherActivity.this.data = response;
            //activity.data = response;
           // activity.refreshUi();

            //super.onPostExecute(aVoid); *
            //TextView responseText = (TextView) findViewById(R.id.response_text);
            //responseText.setText("Loaded " + loadedCount + " times");
            //responseText.setText(response != null? response : );
           // responseText.setText(response != null ? response : "Error");
        }
    }

}
