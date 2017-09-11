package com.maiget.quartz;

import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.maiget.service.SinaEntProcessor;
import com.maiget.service.SinaSocietyProcessor;
import com.maiget.service.SinaTechProcessor;

import us.codecraft.webmagic.Spider;
import util.DateUtils;


public class SpiderJob implements Job{

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		System.out.println("爬取开始，当前时间："+DateUtils.formatDateToString(new Date()));
		Spider.create(new SinaSocietyProcessor()).addUrl("http://news.sina.com.cn/society/").run();
		Spider.create(new SinaTechProcessor()).addUrl("http://tech.sina.com.cn").run();
		Spider.create(new SinaEntProcessor()).addUrl("http://ent.sina.com.cn/weibo/").run();
		System.out.println("爬取结束，结束时间："+DateUtils.formatDateToString(new Date()));
		
	}

}
