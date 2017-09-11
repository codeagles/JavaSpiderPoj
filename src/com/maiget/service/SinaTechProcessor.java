package com.maiget.service;

import java.util.ArrayList;
import java.util.List;

import com.maiget.dao.MDao;
import com.maiget.model.NewsBean;

import common.CommonVar;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

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
	private static int total = 0;
	private static int num = 0;
	@Override
	public void process(Page page) {
		if(page.getUrl().regex(ENTRYREGURL).match()){
			urlLists = page.getHtml().xpath("//ul[@id=\'rcon1\']").links().regex(POSTURL).all();
			page.addTargetRequests(urlLists);
		}else{
			total++;
			MDao dao = new MDao();
			String title = page.getHtml().xpath("//h1[@id=\"main_title\"]/text()").get().toString();
			String author = page.getHtml().xpath("//a[@data-sudaclick=\'media_name\']/text()").get().toString();
			if(author.isEmpty()){
				author = page.getHtml().xpath("//span[@class=\'source\']/text()").get().toString();
			}
			if(!dao.findByNews(title)){
				if(!title.isEmpty()||!author.isEmpty()){
					NewsBean bean = new NewsBean();
					bean.setTitle(title);
					bean.setAuthor(author);
					bean.setCategory("科技");
					bean.setOrigin("新浪科技");
					bean.setNewstime(page.getHtml().xpath("//span[@class=\'titer\']/text()").get());
					bean.setContent(page.getHtml().xpath("//div[@id=\'artibody\']").get());
					String imgUrl = page.getHtml().xpath("//div[@id=\'artibody\']/div[@class=\'img_wrapper\']")
							.css("img", "src").get();
					bean.setImg(imgUrl);
					int i = dao.addInfo(bean);
					if (i > 0) {
						num++;
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
//		Spider.create(new SinaTechProcessor()).addUrl(ENTRYURL).run();
//		System.out.println("爬取结束，共爬取"+total+"个数据，有效数据为"+num+"个。");
//	}

}
