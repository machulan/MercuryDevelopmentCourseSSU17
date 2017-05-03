package ru.sgu.csit.ssu17;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 20.04.2017.
 */

public class DataLoader extends AsyncTaskLoader<List<Article>> {

    private List<Article> data;

    public DataLoader(Context context) {
        super(context);
    }

    @Override
    public List<Article> loadInBackground() {

        List<Article> articles = null;
        final String ssuNewsUrl = "http://www.sgu.ru/news.xml";

        try {
            String rss = new NetUtils().httpGet(ssuNewsUrl);
            articles = new RssUtils().parseRss(rss);

            //LOGGING
            StringBuilder resultString = new StringBuilder();
            final String tabString = "    ";
            for(Article article : articles){
                resultString.append("<article>\n");
                resultString.append(tabString + "<title>" + article.title + "</title>\n");
                resultString.append(tabString + "<description>" + article.description + "</description>\n");
                resultString.append(tabString + "<pubDate>" + article.pubDate + "</pubDate>\n");
                resultString.append("</article>\n");
            }
            Log.d("resultString", resultString.toString());

        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }

        return articles;
    }


    @Override
    protected void onStartLoading() {
        if(data == null) {
            forceLoad();
        } else{
            deliverResult(data);
        }
       // super.onStartLoading();
        //forceLoad();
    }
}
