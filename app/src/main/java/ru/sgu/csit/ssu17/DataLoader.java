package ru.sgu.csit.ssu17;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.os.AsyncTask;

import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
        StringBuilder resultString = new StringBuilder();
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
                    articles = new SSUNewsXmlParser().parse(istream);



                    final String tabString = "    ";
                    for(Article article : articles){
                        resultString.append("<article>\n");
                        resultString.append(tabString + "<title>" + article.title + "</title>\n");
                        resultString.append(tabString + "<description>" + article.description + "</description>\n");
                        resultString.append(tabString + "<pubDate>" + article.pubDate + "</pubDate>\n");
                        resultString.append("</article>\n");
                    }




                    /*byte[] buf = new byte[32 * 1024];
                    while(true){
                        int bytesRead = istream.read(buf);
                        if(bytesRead < 0)
                            break;
                        ostream.write(buf, 0, bytesRead);
                    }
                    res = ostream.toString("UTF-8");*/

                      /*  istream.read(buf);
                        ostream.write(buf);*/
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
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
        //return res;
        return articles;//resultString.toString();
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
