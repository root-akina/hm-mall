package com.hmall.item.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hmall.common.dto.PageDTO;
import com.hmall.item.pojo.Item;

public interface IItemService extends IService<Item> {

    PageDTO selectPage(Integer page, Integer size);

    void updateItemStatusById(Long id, Integer status);
}
