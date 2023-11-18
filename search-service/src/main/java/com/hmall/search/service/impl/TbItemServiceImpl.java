package com.hmall.search.service.impl;


import com.hmall.search.mapper.TbItemMapper;
import com.hmall.search.pojo.Item;
import com.hmall.search.service.ITbItemService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 商品表 服务实现类
 * </p>
 *
 * @author root-akina
 * @since 2023-11-18
 */
@Service
public class TbItemServiceImpl extends ServiceImpl<TbItemMapper, Item> implements ITbItemService {

}
