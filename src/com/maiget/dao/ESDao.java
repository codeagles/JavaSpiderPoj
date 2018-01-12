package com.maiget.dao;

import com.maiget.model.NewsBean;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import util.JSONUtil;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by codeagles on 2018/1/10.
 */
public class ESDao {
    private Client client = null;

    public void insert(NewsBean bean) throws UnknownHostException {
        client = TransportClient.builder().build()
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("10.11.8.250"), 9300));
        String json = JSONUtil.model2Json(bean);
        IndexResponse response = client.prepareIndex("mfu_toutiao", "mg_article").setSource(json).get();
        if (response.isCreated()) {
            System.out.println("ES创建成功!");
        }
        client.close();
    }
}
