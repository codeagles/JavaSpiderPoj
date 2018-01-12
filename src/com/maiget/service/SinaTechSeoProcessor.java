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
 * @author codeagles 爬取新浪科技分类--基础数据爬虫 爬取页面新浪方不会更新(或更新频率很低)
 */
public class SinaTechSeoProcessor implements PageProcessor {
    private static Log logger = LogFactory.getLog(SinaTechSeoProcessor.class);
    private List<String> urlLists = new ArrayList<>();
    private String ENTRYURL = "http://tech\\.sina\\.com\\.cn$";
    private String POSTURL = "http://tech\\.sina\\.com\\.cn/it/.*\\.shtml";
    //	private String POSTURL = "http://tech\\.sina\\.com\\.cn/i/2017-09-07/doc-ifykuffc4229538\\.shtml";
    private Jedis jedis = JedisCache.getJedis(CommonVar.HOST);


    @Override
    public void process(Page page) {
        System.out.println("开始爬取链接");
        if (page.getUrl().regex(ENTRYURL).match()) {
            urlLists = page.getHtml().xpath("//ul[@class=\"seo_data_list\"]").links().regex(POSTURL).all();
            page.addTargetRequests(urlLists);
        } else {
            try {
                String author = page.getHtml().xpath("//a[@class =\'source ent-source\']/text()").get();
                String title = page.getHtml().xpath("//h1/text()").get().toString();
                String newstime = page.getHtml().xpath("//span[@class =\'date\']/text()").get();
                if ("".equals(author) || null == author) {
                    author = page.getHtml().xpath("//span[@class =\'source\']/text()").get();
                }
                if (!(title == null ) && !(author == null)) {
                    String titlemd5 = MD5Util.md5Str(title);
                    if (!jedis.sismember("md5title", titlemd5)) {
                        jedis.sadd("md5title", titlemd5);
                        NewsBean bean = new NewsBean();
                        bean.setTitle(page.getHtml().xpath("//h1/text()").get());
                        bean.setAuthor(author);
                        try {
                            bean.setNewstime(DateUtils.dateToStamp(newstime));
                        } catch (ParseException e) {
                            bean.setNewstime(String.valueOf(new Date().getTime()));
                        }
                        bean.setCategory("科技");
                        bean.setOrigin("新浪科技");
                        bean.setContent(page.getHtml().xpath("//div[@id=\'artibody\']").get());
                        String imgUrl = page.getHtml().xpath("//div[@id=\'artibody\']/div[@class =\'img_wrapper\']").css("img", "src").get();
                        bean.setImg(imgUrl);
                        bean.setCreatetime(new Date().getTime());
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

    @Override
    public Site getSite() {
        return new CommonVar().getSite();
    }

//    public static void main(String[] args) {
//        Spider.create(new SinaTechSeoProcessor()).addUrl("http://tech.sina.com.cn").thread(5).run();
//        System.out.println("爬取结束");
//    }

}
