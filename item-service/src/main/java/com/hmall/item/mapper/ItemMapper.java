package com.hmall.item.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hmall.item.pojo.Item;
import org.apache.ibatis.annotations.Select;

public interface ItemMapper extends BaseMapper<Item> {

    @Select("select count(id) from test02.tb_item")
    Long selectAll();

}
