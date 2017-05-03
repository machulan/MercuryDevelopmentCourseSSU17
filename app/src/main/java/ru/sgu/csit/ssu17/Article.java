package ru.sgu.csit.ssu17;

/**
 * Created by User on 19.04.2017.
 */

public class Article {
    public String title;
    public String description;
    public String pubDate;
    public String link;

    public final String UNKNOWN = "UNKNOWN";

    public Article(){
        this.title = UNKNOWN;
        this.description = UNKNOWN;
        this.pubDate = UNKNOWN;
        this.link = UNKNOWN;
    }

    //public final String link;
    public Article(String title, String description, String pubDate, String link){
        this.title = title;
        this.description = description;
        this.pubDate = pubDate;
        this.link = link;
    }

    @Override
    public String toString() {
        return title;
    }
}
