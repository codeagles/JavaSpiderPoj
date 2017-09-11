package com.maiget.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.maiget.dao.MDao;
import com.maiget.model.NewsBean;
import com.sun.jna.platform.win32.User32Util;

import common.CommonVar;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * 
 * @author codeagles 爬取新浪科技分类--基础数据爬虫 爬取页面新浪方不会更新(或更新频率很低)
 */
public class SinaTechSeoProcessor implements PageProcessor {
	private static Log logger = LogFactory.getLog(SinaTechSeoProcessor.class);
	private List<String> urlLists = new ArrayList<>();
	private String ENTRYURL = "http://tech\\.sina\\.com\\.cn$";
	private String POSTURL = "http://tech\\.sina\\.com\\.cn/it/.*\\.shtml";
//	private String POSTURL = "http://tech\\.sina\\.com\\.cn/i/2017-09-07/doc-ifykuffc4229538\\.shtml";

	@Override
	public void process(Page page) {
		if (page.getUrl().regex(ENTRYURL).match()) {
			urlLists = page.getHtml().xpath("//ul[@class=\"seo_data_list\"]").links().regex(POSTURL).all();
			page.addTargetRequests(urlLists);
		} else {
			if (!(page.getHtml().xpath("//h1[@id=\"main_title\"]/text()").get().toString().isEmpty())
					&& !(page.getHtml().xpath("//a[@class =\'ent1 fred\']/text()").get().toString().isEmpty())) {
				NewsBean bean = new NewsBean();
				bean.setTitle(page.getHtml().xpath("//h1[@id=\"main_title\"]/text()").get());
				bean.setAuthor(page.getHtml().xpath("//a[@class =\'ent1 fred\']/text()").get());
				bean.setNewstime(page.getHtml().xpath("//span[@class =\'titer\']/text()").get());
				bean.setCategory("科技");
				bean.setOrigin("新浪科技");
				bean.setContent(page.getHtml().xpath("//div[@id=\'artibody\']").get());
				String imgUrl = page.getHtml().xpath("//div[@id=\'artibody\']/div[@class =\'img_wrapper\']").css("img", "src").get();
				bean.setImg(imgUrl);
				MDao mDao = new MDao();
				int i = mDao.addInfo(bean);
				if (i > 0) {
					logger.info("insert successed！");
					System.out.println("insert successed！");
				} else {
					logger.error("insert failed！");
				}
			}
		}

	}

	@Override
	public Site getSite() {
		return new CommonVar().getSite();
	}

	public static void main(String[] args) {
		Spider.create(new SinaTechProcessor()).addUrl("http://tech.sina.com.cn").thread(10).run();
		System.out.println("爬取结束");
	}

}
