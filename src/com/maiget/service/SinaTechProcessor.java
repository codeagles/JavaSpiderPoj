package com.maiget.service;

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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 
 * @author codeagles
 *
 */
public class SinaTechProcessor implements PageProcessor{
	private static String ENTRYURL = "http://tech.sina.com.cn";
	private String ENTRYREGURL = "http://tech\\.sina\\.com\\.cn$";
	private List<String> urlLists = new ArrayList<>();
	private String POSTURL = "http://tech\\.sina\\.com\\.cn/.*\\.shtml";
	private Jedis jedis = JedisCache.getJedis(CommonVar.HOST);

	@Override
	public void process(Page page) {
		if(page.getUrl().regex(ENTRYREGURL).match()){
			urlLists = page.getHtml().xpath("//ul[@id=\'rcon1\']").links().regex(POSTURL).all();
			page.addTargetRequests(urlLists);
		}else{
			MDao dao = new MDao();
			String title = page.getHtml().xpath("//h1[@class=\"main-title\"]/text()").get();
			String author = page.getHtml().xpath("//a[@class =\'source ent-source\']/text()").get();
			if(author.isEmpty()){
				author = page.getHtml().xpath("//span[@class=\'source\']/text()").get().toString();
			}
			String newstime = page.getHtml().xpath("//span[@class=\'date\']/text()").get();
			String titlemd5 = MD5Util.md5Str(title);//加密
			if(!jedis.sismember("md5title", titlemd5)){//添加redis set集合去重
				jedis.sadd("md5title", titlemd5);//不存在则添加
				System.out.println("redis written");
				if(!title.isEmpty()||!author.isEmpty()){
					NewsBean bean = new NewsBean();
					bean.setTitle(title);
					bean.setAuthor(author);
					bean.setCategory("科技");
					bean.setOrigin("新浪科技");
					try {
						bean.setNewstime(DateUtils.dateToStamp(newstime));
					} catch (ParseException e) {
						e.printStackTrace();
					}
					bean.setContent(page.getHtml().xpath("//div[@id=\'artibody\']").get());
					String imgUrl = page.getHtml().xpath("//div[@id=\'artibody\']/div[@class=\'img_wrapper\']")
							.css("img", "src").get();
					bean.setImg(imgUrl);
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:ss:mm");
					bean.setCreatetime(new Date().getTime());
					int i = dao.addInfo(bean);
					if (i > 0) {
						System.out.println("insert successed！");
					} else {
						System.out.println("inset failed");
					}
				}
			}
		}
	}

	@Override
	public Site getSite() {
		return new CommonVar().getSite();
	}

//	public static void main(String[] args) {
//		Spider.create(new SinaTechProcessor()).addUrl(ENTRYURL).thread(3).run();
////		System.out.println("爬取结束，共爬取"+total+"个数据，有效数据为"+num+"个。");
//	}

}
