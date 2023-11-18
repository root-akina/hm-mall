package com.hmall.search.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hmall.search.pojo.Item;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 商品表 Mapper 接口
 * </p>
 *
 * @author root-akina
 * @since 2023-11-18
 */
@Mapper
public interface TbItemMapper extends BaseMapper<Item> {

}
