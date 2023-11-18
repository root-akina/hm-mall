package com.hmall.item.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hmall.common.dto.PageDTO;
import com.hmall.item.mapper.ItemMapper;
import com.hmall.item.pojo.Item;
import com.hmall.item.service.IItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import static java.time.LocalDateTime.now;

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
        return new PageDTO<>(total, itemList);
    }

    @Override
    public void updateItemStatusById(Long id, Integer status) {
        //根据ID获取商品
        Item item = itemMapper.selectById(id);
        //更改状态
        item.setStatus(status);
        Date date = new Date();
        item.setUpdateTime(date);
        //更新商品
        itemMapper.updateById(item);
        //todo ....

    }

    @Override
    public void updateItem(Item item) {
        Long id = item.getId();
        Item item1 = itemMapper.selectById(id);

        UpdateWrapper<Item> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set(item.getName()!=null,"name",item.getName())
                .set(item.getCategory()!=null,"category",item.getCategory())
                .set(item.getBrand()!= null,"brand",item.getBrand())
                .set(item.getPrice()!=null,"price",item.getPrice())
                .set(item.getStock()!=null,"stock",item.getStock())
                .set(item.getSpec()!=null,"spec",item.getSpec())
                .set(item.getImage()!=null,"image",item.getImage())
                .set(item.getIsAD()!=null,"isAD",item.getIsAD())
                .eq("id",item.getId());
        itemMapper.update(item1,updateWrapper);
    }

    @Override
    public void deleteItemById(Long id) {
        //查询商品状态，1，2 上架不能删除
        Item item = itemMapper.selectById(id);
        if (item.getStatus()==1){
            throw new RuntimeException("上架商品不能删除");
        }else {
            itemMapper.deleteById(id);
        }
    }
}
