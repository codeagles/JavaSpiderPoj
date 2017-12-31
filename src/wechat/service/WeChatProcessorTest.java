package wechat.service;

import com.maiget.dao.MDao;
import com.maiget.model.NewsBean;
import common.CommonVar;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

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
 * <p>
 * 带视频链接：
 * https://mp.weixin.qq.com/s?src=11&timestamp=1514365142&ver=599&signature=MP5OWVuFAyklPYIhUWFiawQX1mGyFNkqIh4PNLhWj1NDoaR*WmX2b9E*kxX5BcvWtI9Ob02lQht-2e7yUafM6faziWQS1u5svU5iv7Q-eNCwN7DRm6Ew0XDevwnybpO*&new=1
 */
public class WeChatProcessorTest implements PageProcessor {
    private static String ENTRYURL = "http://weixin.sogou.com/pcindex/pc/";
    private String ENTRYREGURL = "http://weixin.sogou.com/pcindex/pc/";
    private List<String> urlLists = new ArrayList<>();
    private List<String> finalUrlLists = new ArrayList<>();
    private String POSTURL = "http://mp.weixin.qq.com/s";
    private MDao mdao = new MDao();

    @Override
    public void process(Page page) {

        if (page.getUrl().toString().contains(ENTRYREGURL)) {
            page.addTargetRequest("https://mp.weixin.qq.com/s?src=11&timestamp=1514365142&ver=599&signature=MP5OWVuFAyklPYIhUWFiawQX1mGyFNkqIh4PNLhWj1NDoaR*WmX2b9E*kxX5BcvWtI9Ob02lQht-2e7yUafM6faziWQS1u5svU5iv7Q-eNCwN7DRm6Ew0XDevwnybpO*&new=1\n");
        } else {
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
                bean.setCategory("aa");
                bean.setOrigin("搜狗微信");
                bean.setNewstime(newstime);
                bean.setImg(img);
                bean.setContent(content);
                bean.setCreatetime(sdf.format(new Date()));
                int i = mdao.addInfo(bean);
                if (i > 0) {
                    System.out.println("insert successed！");
                } else {
                    System.out.println("insert failed!");
                }
            }


        }
    }

    @Override
    public Site getSite() {
        return new CommonVar().getSite();
    }

    public static void main(String[] args) {


        Spider.create(new WeChatProcessorTest()).addUrl(ENTRYURL + "pc_0/1.html").thread(2).run();
    }
}
