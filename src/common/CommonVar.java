package common;

import us.codecraft.webmagic.Site;

public class CommonVar {
	
	private Site site = Site.me().setDomain("blog.sina.com.cn").setSleepTime(3000).setUserAgent(
			"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_2) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.65 Safari/537.31");
	//添加主机IP
	public static final String HOST = "127.0.0.1";
	public Site getSite() {
		return site;
	}
	
}
