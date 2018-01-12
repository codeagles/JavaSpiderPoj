package com.maiget.quartz;

import com.maiget.service.*;
import newsplace.DiFangNewsProcessor;
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

		System.out.printf("新浪社会SinaSocietyProcessor start------");
		Spider.create(new SinaSocietyProcessor()).addUrl("http://news.sina.com.cn/society/").thread(3).run();
		System.out.printf("新浪社会SinaSocietyProcessor end------");

		System.out.printf("新浪科技SinaTechProcessor start------");
		Spider.create(new SinaTechProcessor()).addUrl("http://tech.sina.com.cn").thread(3).run();
		System.out.printf("新浪科技SinaTechProcessor end------");

		System.out.printf("新浪娱乐SinaEntProcessor start------");
		Spider.create(new SinaEntProcessor()).addUrl("http://ent.sina.com.cn/weibo/").thread(3).run();
		System.out.printf("新浪娱乐SinaEntProcessor end------");

		System.out.printf("腾讯科技TencentTechProcessor start------");
		Spider.create(new TencentTechProcessor()).addUrl("http://ent.sina.com.cn/weibo/").thread(3).run();
		System.out.printf("腾讯科技TencentTechProcessor end------");

		System.out.printf("环球科技TechHQProcessor start------");
		Spider.create(new TechHQProcessor()).addUrl("http://tech.huanqiu.com").thread(3).run();
		System.out.printf("环球科技TechHQProcessor end------");

		System.out.printf("微信WeChatProcessor start------");
		for(int categroy = 0; categroy <= 1; categroy++){
			for(int i = 0; i < 1; i++){
				if(i == 0){
					Spider.create(new WeChatProcessor()).addUrl("http://weixin.sogou.com/pcindex/pc/" + "pc_" + categroy + "/pc_" + categroy + ".html").thread(2).run();//抓取页面
				}else{
					Spider.create(new WeChatProcessor()).addUrl("http://weixin.sogou.com/pcindex/pc/" + "pc_" + categroy + "/" + i + ".html").thread(2).run();
				}

			}
		}
		System.out.printf("微信WeChatProcessor end------");

		System.out.printf("地方新闻DiFangNewsProcessor start------");
		String[] array = {"/hb/", "/ah/", "/bj/", "/fj/", "/gd/", "/gs/", "/gx/", "/gz/", "/ha/", "/he/", "/hi/", "/hlj/", "/hn/", "/jl/", "/js/", "/jx/", "/ln/", "/nx/", "/qh/", "/sc/", "/sd/", "/sh/", "/sn/", "/sx/", "/sz/", "/tj/", "/nmg/", "/yn/", "/zj/"};
		for(int i =0;i<array.length;i++){
			Spider.create(new DiFangNewsProcessor()).addUrl("http://difang.gmw.cn"+array[i]).thread(2).run();//抓取页面
		}
		System.out.printf("地方新闻DiFangNewsProcessor end------");

		System.out.println("爬取结束，结束时间："+DateUtils.formatDateToString(new Date()));
		
	}

}
