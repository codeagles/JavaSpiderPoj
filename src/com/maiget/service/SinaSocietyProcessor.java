package com.maiget.service;

import com.maiget.dao.ESDao;
import com.maiget.dao.JedisCache;
import com.maiget.dao.MDao;
import com.maiget.model.NewsBean;
import common.CommonVar;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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

/**
 * @author codeagles 爬取新浪社会分类的新闻(新浪更新，逻辑支持更新)
 */
public class SinaSocietyProcessor implements PageProcessor {
    private static Log logger = LogFactory.getLog(SinaSocietyProcessor.class);
    private List<String> urlLists = new ArrayList<String>();
    private String POSTURL = "http://news\\.sina\\.com\\.cn/.*";
    private String ENTRYREGURL = "http://news\\.sina\\.com\\.cn/society/$";
    private static String ENTRYURL = "http://news.sina.com.cn/society/";
    private Jedis jedis = JedisCache.getJedis(CommonVar.HOST);

//	 public static void main(String[] args) {
//		 Spider.create(new
//		 SinaSocietyProcessor()).addUrl(ENTRYURL).thread(5).run();
//		 System.out.println("爬取结束");
//	 }

    @Override
    public void process(Page page) {
        //进入
        if (page.getUrl().regex(ENTRYREGURL).match()) {
            urlLists = page.getHtml().xpath("//div[@class=\'blk122\']").links().regex(POSTURL).all();
            page.addTargetRequests(urlLists);
        } else {
            //判断
            MDao mDao = new MDao();
            try {
                String title = page.getHtml().xpath("//h1[@class=\"main-title\"]/text()").get();
                String author = page.getHtml().xpath("//a[@class =\'source\']/text()").get();
                String newstime = page.getHtml().xpath("//span[@class =\'date\']/text()").get();
                if (!(title == null ) && !(author == null)) {
                    String titlemd5 = MD5Util.md5Str(title);
                    if (!jedis.sismember("md5title", titlemd5)) {
                        jedis.sadd("md5title", titlemd5);
                        System.out.println("redis written");
                        NewsBean bean = new NewsBean();
                        bean.setTitle(title);
                        bean.setAuthor(author);
                        try {
                            bean.setNewstime(DateUtils.dateToStamp(newstime));
                        } catch (ParseException e) {
                            bean.setNewstime(String.valueOf(new Date().getTime()));
                        }
                        bean.setCategory("社会");
                        bean.setOrigin("新浪新闻中心");
                        bean.setContent(page.getHtml().xpath("//div[@id=\'article\']").get());
                        String imgUrl = page.getHtml().xpath("//div[@id=\'article\']/div[@class =\'img_wrapper\']")
                                .css("img", "src").get();
                        bean.setImg(imgUrl);
                        bean.setCreatetime(new Date().getTime());
                        ESDao es = new ESDao();
                        try {

                            es.insert(bean);
                        } catch (UnknownHostException e) {
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

    @Override
    public Site getSite() {
        return new CommonVar().getSite();
    }

}
