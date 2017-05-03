package ru.sgu.csit.ssu17;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 20.04.2017.
 */

public class SSUNewsXmlParser {
    // We don't use namespaces
    private static final String ns = null;

    public List<Article> parse(InputStream in) throws XmlPullParserException, IOException {
    //public List<Article> parse(String in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);

            //boolean b = in.markSupported();
            //until this place all things are good "UTF-32BE", "UTF-16BE" <= good
            // "UTF-32LE", "UTF-16LE"
            in.read();
            parser.setInput(in, null); //эта строка почему-то считывает 4 символа '\n<?x' из in

            //parser.setInput(new StringReader(in));


            Article a1 = new Article("Географы СГУ провели областной обучающий семинар по метеорологии для учителей",
                    "Сотрудники кафедры метеорологии и климатологии географического факультета СГУ провели семинар по проведению",
                    "2017-04-21 06:00:00", "http://www.sgu.ru/news/2017-05-03/delegaciya-sgu-prinyala-uchastie-v-pedagogicheskom");
            Article a2 = new Article("TITLE 2", "alkdf;lakfb fds\n advv \naadddddddDDD", "15444", "http://www.sgu.ru/news.xml");
            Article a3 = new Article("TITLE 3", "DESCasdfagf", "hfirufhv", "http://www.sgu.ru/news.xml");

            List<Article> articles = new ArrayList<Article>();
            articles.add(a1);
            articles.add(a2);
            articles.add(a3);
            if(true){
                return articles;
            }


           // ByteArrayOutputStream ostream = new ByteArrayOutputStream();
            //try {
                // istream.read();

               /* byte[] buf = new byte[1];
                int bytesRead = -1;
                char cur = '<';
                while(cur != '>'){
                    bytesRead = in.read(buf);
                    if(bytesRead < 0)
                        break;
                    ostream.write(buf, 0, bytesRead);
                    String curStr = ostream.toString("UTF-8");
                    cur = curStr.charAt()
                }*/

                /*
                byte[] buf = new byte[32 * 1];
                while(true){
                    int bytesRead = in.read(buf);
                    if(bytesRead < 0)
                        break;
                    ostream.write(buf, 0, bytesRead);
                }

                */



              /*  String res = ostream.toString("UTF-8");
                String r = res;
            } finally {
                ostream.close();
            }*/





            parser.nextTag();

           /* List<Article> articles = new ArrayList<Article>();
            //TODO (DELETE "IF TRUE" BELOW) AND articles (one string) ABOVE
            if(true){
                return articles;
            }*/

            //after this place nothing works

            return readFeed(parser);
        } finally {
            in.close();
        }
    }

    private List<Article> readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        List<Article> articles = new ArrayList<Article>();

        parser.require(XmlPullParser.START_TAG, ns, "rss");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the item tag
            if (name.equals("item")) {
                articles.add(readArticle(parser));
            } else {
                skip(parser);
            }
        }
        return articles;
    }

    // Parses the contents of an article. If it encounters a title, description, or pubDate tag,
    // hands them off to their respective "read" methods for processing. Otherwise, skips the tag.
    private Article readArticle(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "item");
        String title = null;
        String description = null;
        String pubDate = null;
        String link = null;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("title")) {
                title = readTitle(parser);
            } else if (name.equals("description")) {
                description = readDescription(parser);
            } else if (name.equals("pubDate")) {
                pubDate = readPubDate(parser);
            } else if (name.equals("link")) {
                link = readLink(parser);
            } else {
                skip(parser);
            }
        }
        return new Article(title, description, pubDate, link);
    }

    // Processes title tags in the feed.
    private String readTitle(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "title");
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "title");
        return title;
    }

    // Processes description tags in the feed.
    private String readDescription(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "description");
        String description = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "description");
        return description;
    }

    // Processes description tags in the feed.
    private String readPubDate(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "pubDate");
        String pubDate = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "pubDate");
        return pubDate;
    }

    //extract link value
    private String readLink(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "link");
        String link = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "link");
        return link;
    }

    // For the tags title, decription and pubDate, extracts their text values.
    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }
}
