package com.maiget.service;

import com.maiget.dao.ESDao;
import com.maiget.dao.JedisCache;
import com.maiget.dao.MDao;
import com.maiget.model.NewsBean;
import common.CommonVar;
import redis.clients.jedis.Jedis;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import util.DateUtils;
import util.MD5Util;

import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TechHQProcessor implements PageProcessor {

    private String POSTURL = "http://tech\\.huanqiu\\.com/.*\\.html";
    private List<String> urlList = new ArrayList<String>();
    private MDao mdao = new MDao();
    private Site site = Site.me().setSleepTime(3000).setUserAgent(
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_2) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.65 Safari/537.31");
    private Jedis jedis = JedisCache.getJedis(CommonVar.HOST);

    @Override
    public Site getSite() {
        return site;
    }

    @Override
    public void process(Page page) {

        if (!page.getUrl().regex(POSTURL).match()) {
            urlList = page.getHtml().xpath("//div[@id=\"active1Con\"]").links().all();
            page.addTargetRequests(urlList);
        } else {
            try {
                NewsBean bean = new NewsBean();
                String title = page.getHtml().xpath("//h1/text()").get();
                String newstime = page.getHtml().xpath("//strong[@class=\"timeSummary\"]/text()").get();
                if (!(title == null )) {
                    String titlemd5 = MD5Util.md5Str(title);
                    if (!jedis.sismember("md5title", titlemd5)) {//添加redis set集合去重
                        jedis.sadd("md5title", titlemd5);//不存在则添加
                        System.out.println("redis written");
                        bean.setTitle(title);
                        bean.setAuthor("环球科技");
                        bean.setOrigin("环球科技");
                        bean.setCategory("科技");
                        bean.setContent(page.getHtml().xpath("//div[@id=\'text\']").get());
                        try {
                            bean.setNewstime(DateUtils.dateToStamp(newstime));
                        } catch (ParseException e) {
                            bean.setNewstime(String.valueOf(new Date().getTime()));
                        }
                        String imgUrl = page.getHtml().xpath("//div[@id=\'text\']")
                                .css("img", "src").get();
                        bean.setCreatetime(new Date().getTime());
                        bean.setImg(imgUrl);
                        ESDao es = new ESDao();
                        try {
                            es.insert(bean);
                        } catch (UnknownHostException e) {
                            MDao mDao = new MDao();
                            int i = mDao.addInfo(bean);
                            if (i > 0) {
                                System.out.println("insert successed！");
                            } else {
                                System.out.println("failed！");
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

//    public static void main(String[] args) {
//        Spider.create(new TechHQProcessor()).addUrl("http://tech.huanqiu.com").run();
//    }

}
