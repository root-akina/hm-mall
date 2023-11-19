package com.hmall.search.feign;

import com.hmall.search.pojo.Item;
import com.hmall.search.pojo.PageDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "itemservice",path = "item")
public interface ItemClient {
    @GetMapping("/list")
    PageDTO<Item> pageQuery(@RequestParam("page") Integer page, @RequestParam("size") Integer size);
}
