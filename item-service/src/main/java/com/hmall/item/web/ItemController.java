package com.hmall.item.web;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hmall.common.dto.PageDTO;
import com.hmall.item.pojo.Item;
import com.hmall.item.service.IItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("item")
public class ItemController {

    @Autowired
    private IItemService itemService;

    @GetMapping("/list")
    public PageDTO pageQuery(@RequestParam("page") Integer page,@RequestParam("size") Integer size){
        log.info("分页参数：{}+{}",page,size);
        return itemService.selectPage(page,size);
    }
}
