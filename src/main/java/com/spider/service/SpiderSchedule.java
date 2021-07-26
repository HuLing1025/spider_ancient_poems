package com.spider.service;

import com.spider.dao.IAncientPoemsDao;
import com.spider.pojo.AncientPoems;
import com.spider.utils.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.net.*;

@Component
public class SpiderSchedule {

    @Autowired
    DDRobot ddRobot;

    @Autowired
    IAncientPoemsDao iAncientPoemsDao;

    @Scheduled(cron = Global.CROONERS2SPIDER)
    private void spiderWork() throws InterruptedException {
        //ddRobot.post(JsonTool.getMessJson("开始爬取: " + Global.BASE));
        Document doc1  = HttpClientDownPage.sendGet(Global.BASE);
        if(doc1 == null){
            ddRobot.post(JsonTool.getMessJson("-------主页面为空,可能原因:反爬虫机制------"));
            System.out.println("-------主页面为空,可能原因:反爬虫机制------");
        }else {
            Elements urls = doc1.getElementsByClass("main3").first().
                                getElementsByClass("right").first().
                                getElementsByClass("sons").first().
                                getElementsByClass("cont").first().
                                select("a");
            for (Element u : urls) {
                String title = u.select("a[href]").text();
                String childUrl = "/mingjus/default.aspx?";
                for (int i = 1; i <= 10; i++) {
                    Thread.sleep(100);
                   // childUrl = URLDecoder.decode( childUrl, "UTF-8" );
                    Document doc = HttpClientDownPage.sendGet(Global.BASEURL + childUrl + "page=" + i + "&tstr=" + title + "&astr=&cstr=&xstr=");
                    System.out.println(title + ":" + Global.BASEURL + childUrl + "page=" + i + "&tstr=" + title + "&astr=&cstr=&xstr=");
                    if(doc == null){
                        ddRobot.post(JsonTool.getMessJson("-------主页面为空,可能原因:反爬虫机制------"));
                        System.out.println("-------主页面为空,可能原因:反爬虫机制------");
                    }else {
                        List<String> filters1 = new ArrayList<>();
                        List<AncientPoems> ancientPoemsList = Analyticaldata.analytical(doc, Global.BASE, filters1);
                        for (AncientPoems ancientPoems : ancientPoemsList) {
                            if (iAncientPoemsDao.Exists(ancientPoems) == 1) {
                                //System.out.println(ancientPoems.getTitle() + "已存在!");
                            } else {
                                if (iAncientPoemsDao.insertOne(ancientPoems) != 1) {
                                    System.out.println("新增失败: " + ancientPoems);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
