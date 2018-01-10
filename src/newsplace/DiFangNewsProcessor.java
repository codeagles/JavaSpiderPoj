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

    private String[] array ={"/hb/","/ah/","/bj/","/fj/","/gd/","/gs/","/gx/","/gz/","/ha/","/he/","/hi/","/hlj/","/hn/","/jl/","/js/","/jx/","/ln/","/nb/","/nx/","/qd/","/qh/","/sc/","/sd/","/sh/","/sn/","/sx/","/sz/","/tj/","/xiamen/","/nmg/","/yn/","/zj/"};

    @Override
    public void process(Page page) {
        page.addTargetRequest(ENTRYREGURL);
        String[] array = new String[34];
        urlLists = page.getHtml().xpath("//ul[@class=\'clearfix inner1000\']").links().all();
        int i =0;
        for(String str:urlLists){
             array[i] = str.substring(20);
             i++;
        }
        for(i =0;i<array.length;i++){
            System.out.printf("\""+array[i]+"\",");
        }
    }

    @Override
    public Site getSite() {
        return new CommonVar().getSite();
    }


    public static void main(String[] args) {
       Spider.create(new DiFangNewsProcessor()).addUrl(ENTRYURL).thread(2).run();//抓取页面
    }

}
