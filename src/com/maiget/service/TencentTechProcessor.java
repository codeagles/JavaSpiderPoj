package com.maiget.service;

import com.maiget.dao.ESDao;
import com.maiget.dao.JedisCache;
import com.maiget.model.NewsBean;
import common.CommonVar;
import redis.clients.jedis.Jedis;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import util.DateUtils;
import util.MD5Util;

import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TencentTechProcessor implements PageProcessor{
	private static String ENTRYURL = "http://tech.qq.com";
	private String ENTRYREGURL = "http://tech\\.qq\\.com$";
	private List<String> urlLists = new ArrayList<>();
	private String POSTURL = "http://tech\\.qq\\.com/a/.*.htm";
	private Jedis jedis = JedisCache.getJedis(CommonVar.HOST);
	
	@Override
	public void process(Page page) {
		if(page.getUrl().regex(ENTRYREGURL).match()){
			urlLists = page.getHtml().xpath("//div[@class=\'list first\']//h3[@class=\'f18 l26\']").links()
					.regex(POSTURL).all();
			page.addTargetRequests(urlLists);
		}else{
//			MDao mdao = new MDao();
			ESDao es = new ESDao();

			String title = page.getHtml().xpath("//h1/text()").get().toString();
			String titlemd5 = MD5Util.md5Str(title);
			String author = page.getHtml().xpath("//span[@class=\'a_source\']/text()").get();
			String newstime = page.getHtml().xpath("//span[@class=\'a_time\']/text()").get();
			String content = page.getHtml().xpath("//div[@class=\'Cnt-Main-Article-QQ\']").get();
			String img = page.getHtml().xpath("//div[@class=\'Cnt-Main-Article-QQ\']/p").css("img", "src").get();
//			if(!jedis.sismember("md5title", titlemd5)) {//添加redis set集合去重
//				jedis.sadd("md5title", titlemd5);//不存在则添加
				if (!title.isEmpty() && !author.isEmpty()) {
					NewsBean bean = new NewsBean();
					bean.setAuthor(author);
					bean.setTitle(title);
					bean.setCategory("科技");
					bean.setOrigin("腾讯科技");
					try {
						bean.setNewstime(DateUtils.dateToStamp(newstime));
					} catch (ParseException e) {
						e.printStackTrace();
					}
					bean.setImg(img);
					bean.setContent(content);
					bean.setCreatetime(new Date().getTime());
//					int i = mdao.addInfo(bean);
					try {
						es.insert(bean);
					} catch (UnknownHostException e) {
						e.printStackTrace();
					}

				}
//
			}
//		}
	}

	@Override
	public Site getSite() {
		return new CommonVar().getSite();
	}


	public static void main(String[] args) {
		Spider.create(new TencentTechProcessor()).addUrl(ENTRYURL).run();
		System.out.println("爬取结束");
	}
}
