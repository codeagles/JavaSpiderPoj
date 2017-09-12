package com.maiget.action;


import java.util.Date;

import org.springframework.stereotype.Component;

import com.maiget.service.SinaEntProcessor;
import com.maiget.service.SinaSocietyProcessor;
import com.maiget.service.SinaTechProcessor;

import us.codecraft.webmagic.Spider;
import util.DateUtils;



/**
 * 
 * @author codeagles
 *
 */
@Component
public class ExecAction {

	public static void main(String[] args) {
		System.out.println("爬取时间"+DateUtils.formatDateToString(new Date()));
		Spider.create(new SinaSocietyProcessor()).addUrl("http://news.sina.com.cn/society/").run();
		Spider.create(new SinaTechProcessor()).addUrl("http://tech.sina.com.cn").run();
		Spider.create(new SinaEntProcessor()).addUrl("http://ent.sina.com.cn/weibo/").run();
		System.out.println("爬取结束");
	}

}
