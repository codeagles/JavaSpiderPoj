package wechat.service;

import common.CommonVar;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by codeagles on 2017/12/22.
 * http://weixin.sogou.com/pcindex/pc/pc_6/pc_6.html
 */
public class TechKaProcessor implements PageProcessor{
    private static String ENTRYURL = "http://weixin.sogou.com/pcindex/pc/pc_6/pc_6.html";
    private String ENTRYREGURL = "http://weixin\\.sogou\\.com";
    private List<String> urlLists = new ArrayList<>();
    private String POSTURL = "http://mp\\.weixin\\.qq.com/s?";


    @Override
    public void process(Page page) {
        if(page.getUrl().regex(ENTRYREGURL).match()){
            urlLists = page.getHtml().xpath("//div[@class=\'txt-box\']").links().regex(POSTURL).all();
            page.addTargetRequests(urlLists);
            System.out.printf("urlLists"+urlLists.size());
        }else{
//            MDao mdao = new MDao();
            String title = page.getHtml().xpath("//h1/text()").get().toString();
            String author = page.getHtml().xpath("//span[@class=\'a_source\']/text()").get();
            String newstime = page.getHtml().xpath("//span[@class=\'a_time\']/text()").get();
            String content = page.getHtml().xpath("//div[@class=\'Cnt-Main-Article-QQ\']").get();
            String img = page.getHtml().xpath("//div[@class=\'Cnt-Main-Article-QQ\']/p").css("img", "src").get();
//            if(!title.isEmpty()&&!author.isEmpty()){
//                NewsBean bean = new NewsBean();
//                bean.setAuthor(author);
//                bean.setTitle(title);
//                bean.setCategory("科技");
//                bean.setOrigin("腾讯科技");
//                bean.setNewstime(newstime);
//                bean.setImg(img);
//                bean.setContent(content);
//                int i = mdao.addInfo(bean);
//                if (i > 0) {
//                    System.out.println("insert successed！");
//                } else {
//                    System.out.println("insert failed!");
//                }
//            }


        }
    }

    @Override
    public Site getSite() {
        return new CommonVar().getSite();
    }

    public static void main(String[] args) {
        Spider.create(new TechKaProcessor()).addUrl(ENTRYURL).run();
    }
}
