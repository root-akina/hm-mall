package com.hmall.common.fegin;

import com.hmall.common.config.FeignRequestConfig;
import com.hmall.common.dto.Address;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(value = "userservice",path = "address",configuration = FeignRequestConfig.class)
public interface FeignAddressClient {
    @GetMapping("{id}")
    Address findAddressById(@PathVariable("id") Long id);

    @GetMapping("/uid/{userId}")
    public List<Address> findAddressByUserId(@PathVariable("userId") Long userId);

}
