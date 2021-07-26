package com.spider.utils;

import com.spider.dao.IAncientPoemsDao;
import com.spider.pojo.AncientPoems;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class Analyticaldata {

    @Autowired
    public static DDRobot ddRobot;

    @Autowired
    public static IAncientPoemsDao iAncientPoemsDao;

    public static List<AncientPoems> analytical(Document document, String url, List<String> filters) {
        List<AncientPoems> ancientPoemsList = new ArrayList<>();
        Elements urls = document.getElementsByClass("main3").first().
                        getElementsByClass("left").first().
                        getElementsByClass("sons").first().
                        getElementsByClass("cont");
        for (Element u : urls) {
            AncientPoems ancientPoems = new AncientPoems();
            Elements as = u.select("a");
            if (as.size() != 2) {
                break;
            }
            String authorAndTitle = as.get(1).text();
            String[] split = authorAndTitle.split("《");
            String author = split[0];
            String title = "《" + split[1];
            String content = as.get(0).text();
            ancientPoems.setTitle(title);
            ancientPoems.setAuthor(author);
            ancientPoems.setContent(content);
            //System.out.println(ancientPoems);
            ancientPoemsList.add(ancientPoems);
        }
        return ancientPoemsList;
    }
}
