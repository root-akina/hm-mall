package com.hmall.search.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hmall.search.pojo.EsDTO;
import com.hmall.search.pojo.Item;
import com.hmall.search.pojo.PageDTO;
import com.hmall.search.service.ITbItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 商品表 前端控制器
 * </p>
 *
 * @author root-akina
 * @since 2023-11-18
 */
@RestController
@RequestMapping("/search")
public class TbItemController {

    @Autowired
    private ITbItemService esService;

    /**
     * ES搜索
     */
    @PostMapping("/list")
    public PageDTO<Item> searchES(@RequestBody EsDTO esDTO){
        return esService.searchEs(esDTO);
    }

    /**
     * RabbitMQ远程调用
     */


}
