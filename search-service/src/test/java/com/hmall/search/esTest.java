package com.hmall.search;

import com.hmall.search.constans.HmallTableIndex;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;


@SpringBootTest
public class esTest {

    @Autowired
    private RestHighLevelClient client;

    /**
     * 创建库
     * @throws IOException
     */
    @Test
    public void hmallMapping() throws IOException {
        // 1.创建Request对象
        CreateIndexRequest request = new CreateIndexRequest("hmall");
        // 2.准备请求的参数：DSL语句
        request.source(HmallTableIndex.HMALL_MAPPING, XContentType.JSON);
        // 3.发送请求
        client.indices().create(request, RequestOptions.DEFAULT);

    }
}

