package ru.sgu.csit.ssu17;

/**
 * Created by User on 03.05.2017.
 */

import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class RssUtils {

    public static List<Article> parseRss(String rss) throws XmlPullParserException, IOException {
        final ArrayList<Article> res = new ArrayList<>();
        final XmlPullParser parser = Xml.newPullParser();
        parser.setInput(new ByteArrayInputStream(rss.trim().getBytes("UTF-8")), "UTF-8");

        while (!"channel".equals(parser.getName()))
            parser.next();

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG)
                continue;
            String name = parser.getName();

            if ("item".equals(name)) {
                parser.require(XmlPullParser.START_TAG, null, "item");
                Log.i("UriDataLoader", "Found item" + parser.getText());

                Article article = new Article();
                while (parser.next() != XmlPullParser.END_TAG) {
                    if (parser.getEventType() != XmlPullParser.START_TAG)
                        continue;
                    String itemEntry = parser.getName();
                    if ("title".equals(itemEntry)) {
                        article.title = parser.nextText();
                    } else if ("description".equals(itemEntry)) {
                        article.description = parser.nextText();
                    } else if ("pubDate".equals(itemEntry)) {
                        article.pubDate = parser.nextText();
                    } else if ("link".equals(itemEntry)) {
                        article.link = parser.nextText();
                    } else {
                        skipTag(parser);
                    }
                }
                res.add(article);

            } else {
                skipTag(parser);
            }
        }

        return res;
    }

    private static void skipTag(XmlPullParser parser) throws XmlPullParserException, IOException {
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

