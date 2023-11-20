package com.hmall.search.service.impl;


import com.alibaba.fastjson.JSON;
import com.hmall.search.mapper.TbItemMapper;
import com.hmall.search.pojo.EsDTO;
import com.hmall.search.pojo.Item;
import com.hmall.search.pojo.PageDTO;
import com.hmall.search.service.ITbItemService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.lucene.search.TotalHits;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 商品表 服务实现类
 * </p>
 *
 * @author root-akina
 * @since 2023-11-18
 */
@Service
public class TbItemServiceImpl extends ServiceImpl<TbItemMapper, Item> implements ITbItemService {

    @Autowired
    private RestHighLevelClient client;

    @Override
    public PageDTO<Item> searchEs(EsDTO esDTO) {
        SearchRequest request = new SearchRequest("hmall");
        BoolQueryBuilder searchKey = new BoolQueryBuilder();

        //关键字搜索
        String key = esDTO.getKey();
        if (StringUtils.isEmpty(key)) {
            searchKey.must(QueryBuilders.matchAllQuery());
        } else {
            searchKey.must(QueryBuilders.matchQuery("all", key));
        }
        //过滤条件 key-s
        //key category
        String category = esDTO.getCategory();
        if (!StringUtils.isEmpty(category)) {
            searchKey.filter(QueryBuilders.termQuery("category", category));
        }
        //key brand
        String brandy = esDTO.getBrand();
        if (!StringUtils.isEmpty(brandy)) {
            searchKey.filter(QueryBuilders.termQuery("brand", brandy));
        }
        //key价格范围
        Integer maxPrice = esDTO.getMaxPrice();
        Integer minPrice = esDTO.getMinPrice();
        if (minPrice != null && maxPrice != null) {
            searchKey.filter(QueryBuilders.rangeQuery("price").gte(minPrice).lt(maxPrice));
        }

        //分页查询
        Integer page = esDTO.getPage();
        Integer size = esDTO.getSize();
        request.source().from((page - 1) * size).size(size);

        //排序soryby
        String sortBy = esDTO.getSortBy();
        if (!StringUtils.isEmpty(sortBy)) {
            if (esDTO.getSortBy().equals("sold")) {
                request.source().sort("sold", SortOrder.DESC);
            } else if (esDTO.getSortBy().equals("price")) {
                request.source().sort("price", SortOrder.DESC);
            }else if (esDTO.getSortBy().equals("default")){
                request.source().sort("id",SortOrder.ASC);
            }
        }

        try {
            request.source().query(searchKey);
            SearchResponse search = client.search(request, RequestOptions.DEFAULT);
            return requestHandler(search);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private PageDTO<Item> requestHandler(SearchResponse search) {
        SearchHits searchHits = search.getHits();
        Long totalHits = searchHits.getTotalHits().value;

        SearchHit[] hits = searchHits.getHits();
        List<Item> itemList = new ArrayList<>();
        for (SearchHit hit : hits) {

            String sourceAsString = hit.getSourceAsString();
            Item item = JSON.parseObject(sourceAsString, Item.class);
            itemList.add(item);
        }

        return new PageDTO<Item>(totalHits,itemList);
    }


}
