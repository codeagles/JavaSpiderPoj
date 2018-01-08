package newsplace;

import com.maiget.dao.MDao;
import common.CommonVar;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by codeagles on 2018/1/8.
 */
public class DiFangNewsProcessor implements PageProcessor {
    private static String ENTRYURL = "http://difang.gmw.cn/";
    private String ENTRYREGURL = "http://difang.gmw.cn/";
    private List<String> urlLists = new ArrayList<>();
    private String POSTURL = "http://difang.gmw.cn/";
    private MDao mdao = new MDao();

    @Override
    public void process(Page page) {
        page.addTargetRequest(ENTRYREGURL);
        urlLists = page.getHtml().xpath("//ul[@class=\'clearfix inner1000\']").links().all();
        System.out.println("aaaa"+ urlLists.size());
    }

    @Override
    public Site getSite() {
        return new CommonVar().getSite();
    }




    public static void main(String[] args) {
       Spider.create(new DiFangNewsProcessor()).addUrl(ENTRYURL).thread(2).run();//抓取页面
    }



}
