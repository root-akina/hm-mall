package com.hmall.search.controller;


import com.alibaba.fastjson.JSON;

import com.hmall.common.context.BaseContext;
import com.hmall.common.fegin.FeignItemClient;

import com.hmall.search.feign.ItemClient;
import com.hmall.search.pojo.EsDTO;
import com.hmall.search.pojo.Item;
import com.hmall.search.pojo.PageDTO;
import com.hmall.search.service.ITbItemService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

/**
 * <p>
 * 商品表 前端控制器
 * </p>
 *
 * @author root-akina
 * @since 2023-11-18
 */
@Slf4j
@RestController
@RequestMapping("/search")
public class TbItemController {

    @Autowired
    private ITbItemService esService;
    @Autowired
    private ItemClient itemClient;
//    private FeignItemClient itemClient;

    @Autowired
    private RestHighLevelClient client;

    /**
     * ES搜索
     */
    @PostMapping("/list")
    public PageDTO<Item> searchES(@RequestBody EsDTO esDTO) {
        return esService.searchEs(esDTO);
    }

    /**
     * RabbitMQ远程调用
     * 上架或下架商品
     */
    @RabbitListener(queues = "item.status")
    public void itemConsumer(Map<String, Object> msg) throws IOException {
        Integer status = (Integer) msg.get("status");
        Integer id = (Integer) msg.get("id");

        if (status == 1) {
            //上架操作，根据id新增
            IndexRequest hmall = new IndexRequest("hmall").id(String.valueOf(id));
            Item item = itemClient.getItem(Long.valueOf(id));
            String jsonString = JSON.toJSONString(item);
            hmall.source(jsonString, XContentType.JSON);
            Long currentId = BaseContext.getCurrentId();
            if (currentId != null) {
                log.info("search id {}",id);
            }else {
                log.error("search controller-itemConsumer-ID null");
            }
            client.index(hmall, RequestOptions.DEFAULT);
        } else {
            //下架操作，根据ID删除
            DeleteRequest hmall = new DeleteRequest("hmall").id(String.valueOf(id));
            Long currentId = BaseContext.getCurrentId();
            if (currentId != null) {
                log.info("search id {}",id);
            }else {
                log.error("search controller-itemConsumer-ID null");
            }
            client.delete(hmall, RequestOptions.DEFAULT);
        }

    }


}
