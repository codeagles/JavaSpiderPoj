package newsplace;

import com.maiget.dao.MDao;
import common.CommonVar;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by codeagles on 2018/1/8.
 */
public class DiFangNewsProcessor implements PageProcessor {
    private static String ENTRYURL = "http://difang.gmw.cn/";
    private String ENTRYREGURL = "http://difang.gmw.cn/";
    private List<String> urlLists = new ArrayList<>();
    private String POSTURL = "http://difang.gmw.cn/";
    private MDao mdao = new MDao();

    private String[] array ={"/hb/","/ah/","/bj/","/fj/","/gd/","/gs/","/gx/","/gz/","/ha/","/he/","/hi/","/hlj/","/hn/","/jl/","/js/","/jx/","/ln/","/nb/","/nx/","/qd/","/qh/","/sc/","/sd/","/sh/","/sn/","/sx/","/sz/","/tj/","/nmg/","/yn/","/zj/"};
    private Map<String,String> map = new HashMap<>();
    public DiFangNewsProcessor(){
        map.put("/hb/","河北");
        map.put("/ah/","安徽");
        map.put("/bj/","北京");
        map.put("/fj/","福建");
        map.put("/gd/","广东");
        map.put("/gs/","甘肃");
        map.put("/gx/","广西");
        map.put("/gz/","贵州");
        map.put("/ha/","");
        map.put("","");
        map.put("","");
        map.put("","");
        map.put("","");
        map.put("","");
        map.put("","");
        map.put("","");
        map.put("","");
    }

    @Override
    public void process(Page page) {
        page.addTargetRequest(ENTRYREGURL);

    }

    @Override
    public Site getSite() {
        return new CommonVar().getSite();
    }


    public static void main(String[] args) {
       Spider.create(new DiFangNewsProcessor()).addUrl(ENTRYURL).thread(2).run();//抓取页面
    }

}
