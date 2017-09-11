package com.maiget.action;


import org.springframework.stereotype.Component;

import com.maiget.service.SinaEntProcessor;
import com.maiget.service.SinaSocietyProcessor;
import com.maiget.service.SinaTechProcessor;
import com.maiget.service.SinaTechSeoProcessor;

import us.codecraft.webmagic.Spider;



/**
 * 
 * @author codeagles
 *
 */
@Component
public class ExecAction {

	public static void main(String[] args) {
		Spider.create(new SinaSocietyProcessor()).addUrl("http://news.sina.com.cn/society/").run();
		Spider.create(new SinaTechProcessor()).addUrl("http://tech.sina.com.cn").run();
		Spider.create(new SinaEntProcessor()).addUrl("ttp://ent.sina.com.cn/weibo/").run();
		System.out.println("爬取结束");
	}

}
