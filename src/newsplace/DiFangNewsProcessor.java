package newsplace;

import com.maiget.dao.ESDao;
import com.maiget.dao.MDao;
import com.maiget.model.NewsBean;
import common.CommonVar;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import util.DateUtils;
import util.MD5Util;

import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by codeagles on 2018/1/8.
 */
public class DiFangNewsProcessor implements PageProcessor {
    private static String ENTRYURL = "http://difang.gmw.cn/";
    private String ENTRYREGURL = "http://difang.gmw.cn/";
    private List<String> urlLists = new ArrayList<>();
    private String POSTURL = "http://difang\\.gmw\\.cn/.*";
    private MDao mdao = new MDao();
    private String[] array = {"/hb/", "/ah/", "/bj/", "/fj/", "/gd/", "/gs/", "/gx/", "/gz/", "/ha/", "/he/", "/hi/", "/hlj/", "/hn/", "/jl/", "/js/", "/jx/", "/ln/", "/nx/", "/qh/", "/sc/", "/sd/", "/sh/", "/sn/", "/sx/", "/sz/", "/tj/", "/nmg/", "/yn/", "/zj/"};
    private List<String> lists = new ArrayList<>();
    @Override
    public void process(Page page) {
        if(!page.getUrl().toString().contains(".htm")){
            lists = page.getHtml().xpath("//div[@class =\'layoutRight\']/ul[@class=\'newsList\']").links().regex(POSTURL).all();
            page.addTargetRequests(lists);
        }else{
            String title = page.getHtml().xpath("//h1[@id=\'articleTitle\']/text()").get();
            String newstime = page.getHtml().xpath("//span[@id=\'pubTime\']/text()").get();
            String author = page.getHtml().xpath("//span[@id=\'source\']/a/text()").get();
            List<String> place = page.getHtml().xpath("//div[@id=\'contentBreadcrumbs2\']/a/text()").all();
            String titlemd5 = MD5Util.md5Str(title);
            if (!(title.isEmpty()) && !(author.isEmpty())) {
                NewsBean bean = new NewsBean();
                bean.setTitle(title);
                bean.setAuthor(author);
                try {
                    bean.setNewstime(DateUtils.dateToStamp(newstime));
                } catch (ParseException e) {
                    bean.setNewstime(String.valueOf(new Date().getTime()));
                }
                bean.setPlace(place.get(2));
                bean.setCategory("要闻");
                bean.setOrigin("光明网");
                bean.setContent(page.getHtml().xpath("//div[@id=\'contentMain\']").get());
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
        }
    }

    @Override
    public Site getSite() {
        return new CommonVar().getSite();
    }


    public static void main(String[] args) {
        String[] array = {"/hb/", "/ah/", "/bj/", "/fj/", "/gd/", "/gs/", "/gx/", "/gz/", "/ha/", "/he/", "/hi/", "/hlj/", "/hn/", "/jl/", "/js/", "/jx/", "/ln/", "/nx/", "/qh/", "/sc/", "/sd/", "/sh/", "/sn/", "/sx/", "/sz/", "/tj/", "/nmg/", "/yn/", "/zj/"};

        for(int i =0;i<array.length;i++){
            Spider.create(new DiFangNewsProcessor()).addUrl("http://difang.gmw.cn"+array[i]).thread(2).run();//抓取页面

        }
    }

}
