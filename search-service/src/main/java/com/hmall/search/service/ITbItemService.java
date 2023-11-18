package com.hmall.search.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.hmall.search.pojo.EsDTO;
import com.hmall.search.pojo.Item;
import com.hmall.search.pojo.PageDTO;

/**
 * <p>
 * 商品表 服务类
 * </p>
 *
 * @author root-akina
 * @since 2023-11-18
 */
public interface ITbItemService extends IService<Item> {

    PageDTO<Item> searchEs(EsDTO esDTO);
}
