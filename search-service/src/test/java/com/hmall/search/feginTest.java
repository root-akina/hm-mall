package com.hmall.search;

import com.hmall.search.feign.ItemClient;
import com.hmall.search.pojo.PageDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.openfeign.FeignClient;

@SpringBootTest
public class feginTest {

    @Autowired
    private ItemClient itemClient;

    @Test
    public void sad() {
        PageDTO pageDTO = itemClient.pageQuery(100, 100);
        System.out.println(pageDTO.getTotal());
        System.out.println(pageDTO.getList().toString());

    }
}
