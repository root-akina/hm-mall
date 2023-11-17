package com.hmall.item.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hmall.common.dto.PageDTO;
import com.hmall.item.mapper.ItemMapper;
import com.hmall.item.pojo.Item;
import com.hmall.item.service.IItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemService extends ServiceImpl<ItemMapper, Item> implements IItemService {

    @Autowired
    private ItemMapper itemMapper;


    @Override
    public PageDTO selectPage(Integer pages, Integer size) {
        Page<Item> page = new Page<>(pages, size);
//2.构建条件对象
        LambdaQueryWrapper<Item> queryWrapper = new LambdaQueryWrapper<>();

//3.调用selectPage()方法
        Page<Item> pageResult = itemMapper.selectPage(page, queryWrapper);
        List<Item> itemList = pageResult.getRecords();
        long total = pageResult.getTotal();
        return new PageDTO<>(total,itemList);
    }
}
