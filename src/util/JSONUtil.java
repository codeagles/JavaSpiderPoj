package util;

import com.maiget.model.NewsBean;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;

import java.io.IOException;

/**
 * Created by codeagles on 2018/1/10.
 */
public class JSONUtil {

    // Java实体对象转json对象
    public static String model2Json(NewsBean bean) {
        String jsonData = null;
        try {
            XContentBuilder jsonBuild = XContentFactory.jsonBuilder();
            jsonBuild.startObject()
                    .field("title", bean.getTitle())
                    .field("newstime", bean.getNewstime())
                    .field("author", bean.getAuthor())
                    .field("content", bean.getContent())
                    .field("img",bean.getImg())
                    .field("category", bean.getCategory())
                    .field("place", bean.getPlace())
                    .field("origin", bean.getOrigin())
                    .field("createtime", bean.getCreatetime())
                    .endObject();

            jsonData = jsonBuild.string();
            //System.out.println(jsonData);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return jsonData;
    }

}
