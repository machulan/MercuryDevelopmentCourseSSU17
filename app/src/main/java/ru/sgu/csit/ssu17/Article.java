package ru.sgu.csit.ssu17;

/**
 * Created by User on 19.04.2017.
 */

public class Article {
    public final String title;
    public final String description;
    public final String pubDate;
    //public final String link;
    public Article(String title, String description, String pubDate){
        this.title = title;
        this.description = description;
        this.pubDate = pubDate;
    }

    @Override
    public String toString() {
        return title;
    }
}
