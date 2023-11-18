package com.hmall.item.web;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hmall.common.dto.PageDTO;
import com.hmall.item.pojo.Item;
import com.hmall.item.service.IItemService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
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
    public PageDTO pageQuery(@RequestParam("page") Integer page, @RequestParam("size") Integer size) {
        log.info("分页参数：{}+{}", page, size);
        return itemService.selectPage(page, size);
    }

    /**
     * 根据 ID 查询
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Item getItem(@PathVariable("id") Long id) {
        log.info("根据ID查询：id{}", id);
        return itemService.getById(id);
    }

    @PostMapping
    public void addItem(@RequestBody Item item) {
        log.info("新增商品：{}", item);
        itemService.save(item);
        //todo id 创建时间更新时间
    }

    @PutMapping("/status/{id}/{status}")
    public void itemStatus(@PathVariable("id") Long id, @PathVariable("status") Integer status) {
        log.info("更新商品状态：{},{}", id, status == 1 ? "上架商品" : "下架商品");
        itemService.updateItemStatusById(id,status);
    }


    @PutMapping
    public void updateItem(@RequestBody Item item){
        log.info("更新商品：{}",item);
        itemService.updateItem(item);
    }

    @DeleteMapping("/{id}")
    public void deleteItemById(@PathVariable Long id){
        log.info("根据ID删除商品：{}",id);
        itemService.deleteItemById(id);
    }
}
