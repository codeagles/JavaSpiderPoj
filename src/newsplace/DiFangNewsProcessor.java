package newsplace;

import com.maiget.dao.ESDao;
import com.maiget.dao.JedisCache;
import com.maiget.dao.MDao;
import com.maiget.model.NewsBean;
import common.CommonVar;
import redis.clients.jedis.Jedis;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
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
    private String POSTURL = "http://difang\\.gmw\\.cn/.*";
    private String[] array = {"/hb/", "/ah/", "/bj/", "/fj/", "/gd/", "/gs/", "/gx/", "/gz/", "/ha/", "/he/", "/hi/", "/hlj/", "/hn/", "/jl/", "/js/", "/jx/", "/ln/", "/nx/", "/qh/", "/sc/", "/sd/", "/sh/", "/sn/", "/sx/", "/sz/", "/tj/", "/nmg/", "/yn/", "/zj/"};
    private List<String> lists = new ArrayList<>();
    private Jedis jedis = JedisCache.getJedis(CommonVar.HOST);

    @Override
    public void process(Page page) {
        if (!page.getUrl().toString().contains(".htm")) {
            lists = page.getHtml().xpath("//div[@class =\'layoutRight\']/ul[@class=\'newsList\']").links().regex(POSTURL).all();
            page.addTargetRequests(lists);
        } else {
            try {
                String title = page.getHtml().xpath("//h1[@id=\'articleTitle\']/text()").get();
                String newstime = page.getHtml().xpath("//span[@id=\'pubTime\']/text()").get();
                String author = page.getHtml().xpath("//span[@id=\'source\']/a/text()").get();
                List<String> place = page.getHtml().xpath("//div[@id=\'contentBreadcrumbs2\']/a/text()").all();
                if (!(title == null ) && !(author == null)) {
                    String titlemd5 = MD5Util.md5Str(title);
                    if (!jedis.sismember("md5title", titlemd5)) {//添加redis set集合去重
                        jedis.sadd("md5title", titlemd5);//不存在则添加
                        System.out.println("redis written");
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
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public Site getSite() {
        return new CommonVar().getSite();
    }


//    public static void main(String[] args) {
//
//        String[] array = {"/hb/", "/ah/", "/bj/", "/fj/", "/gd/", "/gs/", "/gx/", "/gz/", "/ha/", "/he/", "/hi/", "/hlj/", "/hn/", "/jl/", "/js/", "/jx/", "/ln/", "/nx/", "/qh/", "/sc/", "/sd/", "/sh/", "/sn/", "/sx/", "/sz/", "/tj/", "/nmg/", "/yn/", "/zj/"};
//
//        for(int i =0;i<array.length;i++){
//            Spider.create(new DiFangNewsProcessor()).addUrl("http://difang.gmw.cn"+array[i]).thread(2).run();//抓取页面
//        }
//    }

}
