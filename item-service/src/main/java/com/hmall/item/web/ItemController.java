package com.hmall.item.web;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hmall.common.dto.Order;
import com.hmall.common.dto.OrderDetail;
import com.hmall.common.dto.PageDTO;
import com.hmall.common.fegin.FeignItemClient;
import com.hmall.common.fegin.FeignOrderClient;
import com.hmall.item.pojo.Item;
import com.hmall.item.service.IItemService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("item")
public class ItemController {

    @Autowired
    private IItemService itemService;

    @Autowired
    private RabbitTemplate rabbitTemplate;



    @GetMapping("/list")
    public PageDTO<Item> pageQuery(@RequestParam("page") Integer page, @RequestParam("size") Integer size) {
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
        item.setUpdateTime(new Date());
        item.setCreateTime(new Date());
        itemService.save(item);
        //todo id 创建时间更新时间 使用AOP
    }

    @PutMapping("/status/{id}/{status}")
    public void itemStatus(@PathVariable("id") Long id, @PathVariable("status") Integer status) {
        log.info("更新商品状态：{},{}", id, status == 1 ? "上架商品" : "下架商品");
        itemService.updateItemStatusById(id, status);

    }


    @PutMapping
    public void updateItem(@RequestBody Item item) {
        log.info("更新商品：{}", item);
        itemService.updateItem(item);
    }

    @DeleteMapping("/{id}")
    public void deleteItemById(@PathVariable Long id) {
        log.info("根据ID删除商品：{}", id);
        itemService.deleteItemById(id);
    }

    //todo 修改库存，恢复库存
    @PutMapping("/stock/{itemId}/{num}")
    public void stockUpdate(@PathVariable("itemId") Long itemId, @PathVariable("num") Integer num) {
        log.info("item更新库存服务：id：{},num:{}", itemId, num);
        Item byId = itemService.getById(itemId);
        UpdateWrapper<Item> itemUpdateWrapper = new UpdateWrapper<>();
        Integer stock = byId.getStock() - num;
        itemUpdateWrapper.set("stock", stock);
        itemUpdateWrapper.eq("id", itemId);
        boolean update = itemService.update(itemUpdateWrapper);
        //修改ES文档
        Map<String, Object> msg = new HashMap<>();
        msg.put("itemId", itemId);
        msg.put("stock", stock);
        rabbitTemplate.convertAndSend("update.queue", msg);
    }
}
