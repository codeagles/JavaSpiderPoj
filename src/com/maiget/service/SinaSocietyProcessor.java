package com.maiget.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.maiget.dao.MDao;
import com.maiget.model.NewsBean;

import common.CommonVar;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * 
 * @author codeagles 爬取新浪社会分类的新闻(新浪更新，逻辑支持更新)
 */
public class SinaSocietyProcessor implements PageProcessor {
	private static Log logger = LogFactory.getLog(SinaSocietyProcessor.class);
	private List<String> urlLists = new ArrayList<String>();
	private String POSTURL = "http://news\\.sina\\.com\\.cn/.*";
	private String ENTRYREGURL = "http://news\\.sina\\.com\\.cn/society/$";
	private static String ENTRYURL = "http://news.sina.com.cn/society/";

	// public static void main(String[] args) {
	// Spider.create(new
	// SinaSocietyProcessor()).addUrl(ENTRYURL).thread(5).run();
	// System.out.println("爬取结束");
	// }

	@Override
	public void process(Page page) {
		if (page.getUrl().regex(ENTRYREGURL).match()) {
			urlLists = page.getHtml().xpath("//div[@class=\'blk122\']").links().regex(POSTURL).all();
			page.addTargetRequests(urlLists);
		} else {
			MDao mDao = new MDao();
			String title = page.getHtml().xpath("//h1[@id=\"artibodyTitle\"]/text()").get().toString();
			String author = page.getHtml().xpath("//span[@data-sudaclick =\'media_name\']/a/text()").get().toString();
			if (!mDao.findByNews(title)) {
				if (!(title.isEmpty()) && !(author.isEmpty())) {
					NewsBean bean = new NewsBean();
					bean.setTitle(title);
					bean.setAuthor(author);
					bean.setNewstime(page.getHtml().xpath("//span[@class =\'time-source\']/text()").get());
					bean.setCategory("社会");
					bean.setOrigin("新浪新闻中心");
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
