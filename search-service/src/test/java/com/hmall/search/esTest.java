package com.hmall.search;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hmall.search.constans.HmallTableIndex;
import com.hmall.search.mapper.TbItemMapper;
import com.hmall.search.pojo.Item;
import com.hmall.search.pojo.PageDTO;
import com.hmall.search.service.ITbItemService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@Slf4j
@SpringBootTest
public class esTest {

    @Autowired
    private RestHighLevelClient client;

    /**
     * 创建库
     *
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

    @Autowired
    private ITbItemService service;

    /**
     * 新增到库
     */
    @Test
    public void addOne() throws IOException {
        Item item = service.getById(100002672304L);

        String jsonString = JSON.toJSONString(item);

        IndexRequest hmall = new IndexRequest("hmall").id(item.getId().toString());

        hmall.source(jsonString, XContentType.JSON);

        client.index(hmall, RequestOptions.DEFAULT);
    }

    /**
     * 查询语句
     */
    @Test
    public void searchId() throws IOException {
        GetRequest getRequest = new GetRequest("hmall", "100002672304");

        GetResponse documentFields = client.get(getRequest, RequestOptions.DEFAULT);

        String sourceAsString = documentFields.getSourceAsString();
        System.out.println(sourceAsString);
    }

    /**
     * 删除语句
     */
    @Test
    public void deleteById() throws IOException {
        DeleteRequest hmall = new DeleteRequest("hmall", "100002672304");

        DeleteResponse delete = client.delete(hmall, RequestOptions.DEFAULT);

    }

    @Autowired
    private TbItemMapper itemMapper;

    /**
     * 批量插入
     */
    @Test
    public void bulkAdd() throws IOException {
        boolean flag = true;
        int size = 100;
        int page = 0;
        int i = 1;
        while (flag) {
            page = (i - 1) * 10;
            Page<Item> items = new Page<>(page, size);
            //2.构建条件对象
            LambdaQueryWrapper<Item> queryWrapper = new LambdaQueryWrapper<>();

//3.调用selectPage()方法
            Page<Item> pageResult = itemMapper.selectPage(items, queryWrapper);
            long total = pageResult.getTotal();
            BulkRequest bulkRequest = new BulkRequest();
            for (Item record : pageResult.getRecords()) {
                if (record.getStatus() == 1) {
                    bulkRequest.add(
                            new IndexRequest("hmall")
                                    .id(record.getId().toString())
                                    .source(JSON.toJSONString(record), XContentType.JSON));
                }
            }
            client.bulk(bulkRequest, RequestOptions.DEFAULT);
            log.info("第{}页，本页总条数：， 导入完毕", i);
            i++;
            if (page - (page % 1000) >= total - (total % 1000)) {
                flag = false;
            }
        }

    }

    @Test
    public void sadasd(){
        Page<Item> asd= new Page<>(0,10000);
        QueryWrapper<Item> asddd = new QueryWrapper();
        Page<Item> pageResult = itemMapper.selectPage(asd, asddd);
        System.out.println(pageResult.getTotal());
        System.out.println(pageResult.getRecords().toString());

    }




}

