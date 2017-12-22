package com.maiget.service;

import com.maiget.dao.MDao;
import com.maiget.model.NewsBean;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.ArrayList;
import java.util.List;

public class TechHQProcessor implements PageProcessor{
	
	private String POSTURL ="http://tech\\.huanqiu\\.com/.*\\.html";
	private List<String> urlList = new ArrayList<String>();
	private MDao mdao = new MDao();
	private Site site = Site.me().setSleepTime(3000).setUserAgent(
			"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_2) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.65 Safari/537.31");
	
	@Override
	public Site getSite() {
		return site;
	}

	@Override
	public void process(Page page) {
		
		if(!page.getUrl().regex(POSTURL).match()){
			urlList=page.getHtml().xpath("//div[@id=\"active1Con\"]").links().all();
			page.addTargetRequests(urlList);
		}else{
			NewsBean bean = new NewsBean();
			bean.setTitle(page.getHtml().xpath("//h1/text()").get());
			bean.setAuthor("环球科技");
			bean.setOrigin("环球科技");
			bean.setCategory("科技");
			bean.setContent(page.getHtml().xpath("//div[@id=\'text\']").get());
			bean.setNewstime(page.getHtml().xpath("//strong[@class=\"timeSummary\"]/text()").get());
			bean.setImg("");
			int i = mdao.addInfo(bean);
			if(i>0){
				System.out.println("插入成功");
			}
		}

	}
//	public static void main(String[] args) {
//		Spider.create(new TechHQProcessor()).addUrl("http://tech.huanqiu.com").run();
//	}
	
}
