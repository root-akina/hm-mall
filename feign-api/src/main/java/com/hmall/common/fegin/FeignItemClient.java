package com.hmall.common.fegin;


import com.hmall.common.dto.Item;
import com.hmall.common.dto.PageDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@Component
@FeignClient(value = "itemservice",path = "item")
public interface FeignItemClient {

    @GetMapping("/list")
    PageDTO<Item> pageQuery(@RequestHeader("authorization:2") @RequestParam("page") Integer page, @RequestParam("size") Integer size);

    @GetMapping("/{id}")
    Item getItem(@PathVariable("id") Long id);
}