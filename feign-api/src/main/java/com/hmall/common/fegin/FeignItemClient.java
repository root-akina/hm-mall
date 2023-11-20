package com.hmall.common.fegin;


import com.hmall.common.config.FeignRequestConfig;
import com.hmall.common.dto.Item;
import com.hmall.common.dto.PageDTO;
import org.springframework.cloud.openfeign.FeignClient;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "itemservice",path = "item",configuration = FeignRequestConfig.class)
public interface FeignItemClient {

    @GetMapping("/list")
    PageDTO<Item> pageQuery(@RequestParam("page") Integer page, @RequestParam("size") Integer size);

    @GetMapping("/{id}")
    Item getItem(@PathVariable("id") Long id);

    @PutMapping("/stock/{itemId}/{num}")
    public void stockUpdate(@PathVariable("itemId") Long itemId,@PathVariable("num") Integer num);
}