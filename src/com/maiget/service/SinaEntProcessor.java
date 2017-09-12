package com.maiget.service;

import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.maiget.dao.JedisCache;
import com.maiget.dao.MDao;
import com.maiget.model.NewsBean;

import common.CommonVar;
import redis.clients.jedis.Jedis;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import util.MD5Util;

public class SinaEntProcessor implements PageProcessor {
	private static Log logger = LogFactory.getLog(SinaEntProcessor.class);
	private static String ENTRYURL = "http://ent.sina.com.cn/weibo/";
	private String ENTRYREGURL = "http://ent\\.sina\\.com\\.cn/weibo/$";
	private List<String> urlLists = new ArrayList<>();
	private String POSTURL = "http://ent\\.sina\\.com\\.cn/.*";
	private Jedis jedis = JedisCache.getJedis(CommonVar.HOST);

	
//	public static void main(String[] args) {
//		Spider.create(new SinaEntProcessor()).addUrl(ENTRYURL).thread(1).run();
//		System.out.println("爬取结束");
//	}

	@Override
	public void process(Page page) {
		MDao mDao = new MDao();
		if (page.getUrl().regex(ENTRYREGURL).match()) {
			urlLists = page.getHtml().xpath("//ul[@class=\'module_common_list clearfix\']").links().regex(POSTURL)
					.all();
			page.addTargetRequests(urlLists);
		} else {
			String title = page.getHtml().xpath("//h1[@id=\"main_title\"]/text()").get().toString();
			String author = page.getHtml().xpath("//a[@data-sudaclick =\'media_name\']/text()").get().toString();
			String titlemd5 = MD5Util.md5Password(title);
			if(!jedis.sismember("md5title", titlemd5)){
				jedis.sadd("md5title", titlemd5);
				System.out.println("redis written");
				if (!(title.isEmpty()) && !(author.isEmpty())) {
					NewsBean bean = new NewsBean();
					bean.setTitle(title);
					bean.setAuthor(author);
					bean.setNewstime(page.getHtml().xpath("//span[@class =\'titer\']/text()").get());
					bean.setCategory("娱乐");
					bean.setOrigin("新浪娱乐");
					bean.setContent(page.getHtml().xpath("//div[@id=\'artibody\']").get());
					String imgUrl = page.getHtml().xpath("//div[@id=\'artibody\']/div[@class =\'img_wrapper\']")
							.css("img", "src").get();
					bean.setImg(imgUrl);

					int i = mDao.addInfo(bean);
					if (i > 0) {
						System.out.println("insert successed！");
						logger.info("insert successed！");
					} else {
						logger.error("insert failed！");
					}
				}
			}
		}
	}

	@Override
	public Site getSite() {
		return new CommonVar().getSite();
	}

}
