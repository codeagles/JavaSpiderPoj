package com.maiget.quartz;

import com.maiget.service.*;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import us.codecraft.webmagic.Spider;
import util.DateUtils;
import wechat.service.WeChatProcessor;

import java.util.Date;


public class SpiderJob implements Job{

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		System.out.println("爬取开始，当前时间："+DateUtils.formatDateToString(new Date()));
		Spider.create(new SinaSocietyProcessor()).addUrl("http://news.sina.com.cn/society/").thread(3).run();
		Spider.create(new SinaTechProcessor()).addUrl("http://tech.sina.com.cn").thread(3).run();
		Spider.create(new SinaEntProcessor()).addUrl("http://ent.sina.com.cn/weibo/").thread(3).run();
		Spider.create(new SinaSocietyProcessor()).addUrl("http://news.sina.com.cn/society/").thread(5).run();
		Spider.create(new TencentTechProcessor()).addUrl("http://ent.sina.com.cn/weibo/").thread(3).run();
		Spider.create(new TechHQProcessor()).addUrl("http://tech.huanqiu.com").thread(3).run();

		for(int categroy = 0; categroy <= 1; categroy++){
			for(int i = 0; i < 1; i++){
				if(i == 0){
					Spider.create(new WeChatProcessor()).addUrl("http://weixin.sogou.com/pcindex/pc/" + "pc_" + categroy + "/pc_" + categroy + ".html").thread(2).run();//抓取页面
				}else{
					Spider.create(new WeChatProcessor()).addUrl("http://weixin.sogou.com/pcindex/pc/" + "pc_" + categroy + "/" + i + ".html").thread(2).run();
				}

			}
		}

		System.out.println("爬取结束，结束时间："+DateUtils.formatDateToString(new Date()));
		
	}

}
