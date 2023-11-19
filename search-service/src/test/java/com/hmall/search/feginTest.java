package com.hmall.search;

import com.alibaba.fastjson.JSON;
import com.hmall.search.feign.ItemClient;
import com.hmall.search.pojo.Item;
import com.hmall.search.pojo.PageDTO;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.openfeign.FeignClient;

import java.io.IOException;

@Slf4j
@SpringBootTest
public class feginTest {

    @Autowired
    private RestHighLevelClient client;

    @Autowired
    private ItemClient itemClient;

    @Test
    public void sad() {
        PageDTO pageDTO = itemClient.pageQuery(100, 100);
        System.out.println(pageDTO.getTotal());
        System.out.println(pageDTO.getList().toString());
    }

    @Test
    public void searchES(){
        int size = 100;
        int i=1;
        int page = 0;
        while (true){
//            page = (i-1)*size;
            PageDTO<Item> itemsDTO = itemClient.pageQuery(i, size);

            Long total = itemsDTO.getTotal();

            BulkRequest bulkRequest = new BulkRequest();
            for (Item item : itemsDTO.getList()) {
                if (item.getStatus()==1){
                    IndexRequest indexRequest = new IndexRequest("hmall")
                            .id(item.getId().toString())
                            .source(JSON.toJSONString(item), XContentType.JSON);
                    bulkRequest.add(indexRequest);
                    System.out.println("Added request: " + indexRequest.toString());
                }
            }
            if (bulkRequest.numberOfActions()>0){
                try {
                    client.bulk(bulkRequest, RequestOptions.DEFAULT);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }else {
                log.info("搜不到数据了{}",bulkRequest.getDescription());
                return;
            }

            log.info("第{}页，本页总条数：{}， 导入完毕",i,total);
            i++;
        }
    }
}
