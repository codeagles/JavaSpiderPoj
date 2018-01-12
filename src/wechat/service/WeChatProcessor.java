package wechat.service;

import com.maiget.dao.ESDao;
import com.maiget.dao.MDao;
import com.maiget.model.NewsBean;
import common.CommonVar;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import util.DateUtils;

import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by codeagles on 2017/12/22.
 * http://weixin.sogou.com/
 * 网站规则：
 * 前缀固定：pcindex/pc
 * 选项卡(分类页签):/pc_x  x:为数字 例如 /pc_1/
 * 每个选项数据拉取：pc_1.html、1.html、2.html.....
 * http://weixin.sogou.com/pcindex/pc/pc_0/pc_0.html
 *
 * 带视频链接：
 * https://mp.weixin.qq.com/s?src=11&timestamp=1514365142&ver=599&signature=MP5OWVuFAyklPYIhUWFiawQX1mGyFNkqIh4PNLhWj1NDoaR*WmX2b9E*kxX5BcvWtI9Ob02lQht-2e7yUafM6faziWQS1u5svU5iv7Q-eNCwN7DRm6Ew0XDevwnybpO*&new=1
 */
public class WeChatProcessor implements PageProcessor {
    private static String ENTRYURL = "http://weixin.sogou.com/pcindex/pc/";
    private String ENTRYREGURL = "http://weixin.sogou.com/pcindex/pc/";
    private List<String> urlLists = new ArrayList<>();
    private List<String> finalUrlLists = new ArrayList<>();
    private String POSTURL = "http://mp.weixin.qq.com/s";
    private MDao mdao = new MDao();
    private int categroy = 0;
    private List<String> categroyList = new ArrayList<>();
    @Override
    public void process(Page page) {
        categroyList.add(0,"热门");
        categroyList.add(1,"要闻");
        categroyList.add(2,"段子手");
        categroyList.add(3,"养生堂");
        categroyList.add(4,"私房话");
        categroyList.add(5,"八卦精");
        categroyList.add(6,"科技咖");
        categroyList.add(7,"财经");
        categroyList.add(8,"汽车");
        categroyList.add(9,"生活");
        categroyList.add(10,"时尚");
        categroyList.add(11,"育儿");
        categroyList.add(12,"旅游");
        categroyList.add(13,"职场");
        categroyList.add(14,"美食");
        categroyList.add(15,"历史");
        categroyList.add(16,"教育");
        categroyList.add(17,"星座");
        categroyList.add(18,"体育");
        categroyList.add(19,"军事");
        categroyList.add(20,"游戏");
        categroyList.add(21,"萌宠");


        if (page.getUrl().toString().contains(ENTRYREGURL)) {
            categroy = Integer.parseInt(page.getUrl().toString().split("/")[5].replace("pc_","").trim());
            urlLists = page.getHtml().xpath("//div[@class=\'txt-box\']").links().all();
            for (String url : urlLists) {
                if (!"".equals(url) && url.contains(POSTURL)) {
                    finalUrlLists.add(url);
                }
            }
            page.addTargetRequests(finalUrlLists);
            System.out.printf("finalUrlLists" + finalUrlLists.size());
        } else {
            try {

            String title = page.getHtml().xpath("//h2[@id=\'activity-name\']/text()").get();
            String author = page.getHtml().xpath("//div[@id=\'meta_content\']/span/text()").get();
            String newstime = page.getHtml().xpath("//div[@id=\'meta_content\']/em[@id=\'post-date\']/text()").get();
            String content = page.getHtml().xpath("//div[@id=\'js_content\']").get();//不带有头部标题
            //String content = page.getHtml().xpath("//div[@id=\'img-content\']").get(); //带有头部标题，缺点：多了一堆js代码，优势：可直接当做文章用。
            content += content.replace("data-src", "src");
            String img = page.getHtml().xpath("//div[@id=\'js_content\']/p").css("img", "data-src").get();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:ss:mm");
            if (!title.isEmpty() && !author.isEmpty()) {
                NewsBean bean = new NewsBean();
                bean.setAuthor(author);
                bean.setTitle(title);
                bean.setCategory(categroyList.get(categroy));
                bean.setOrigin("搜狗微信");
                try {
                    bean.setNewstime(DateUtils.dateToStamp(newstime));
                } catch (ParseException e) {
                    bean.setNewstime(String.valueOf(new Date().getTime()));
                }
                bean.setImg(img);
                bean.setContent(content);
                bean.setCreatetime(new Date().getTime());
                ESDao es = new ESDao();
                try {
                    es.insert(bean);
                } catch (UnknownHostException e) {
                    MDao mDao = new MDao();
                    int i = mDao.addInfo(bean);
                    if (i > 0) {
                        System.out.println("insert successed！");
                    } else {
                        System.out.println("failed！");
                    }
                }
            }

            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }

    @Override
    public Site getSite() {
        return new CommonVar().getSite();
    }

    public static void main(String[] args) {

        for(int categroy = 0; categroy <= 1; categroy++){
            for(int i = 0; i < 1; i++){
                if(i == 0){
                    Spider.create(new WeChatProcessor()).addUrl(ENTRYURL + "pc_" + categroy + "/pc_" + categroy + ".html").thread(2).run();//抓取页面
                }else{
                    Spider.create(new WeChatProcessor()).addUrl(ENTRYURL + "pc_" + categroy + "/" + i + ".html").thread(2).run();
                }

            }
        }

    }
}
