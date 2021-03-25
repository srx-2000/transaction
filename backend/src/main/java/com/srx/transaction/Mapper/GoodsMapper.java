package com.srx.transaction.Mapper;

import com.srx.transaction.Entities.Goods;
import org.springframework.stereotype.Repository;

@Repository
public interface GoodsMapper {

    /**
     * 插入一种商品
     *
     * @param goods
     * @return
     */
    Boolean insertGoods(Goods goods);

    /**
     * 更新商品信息
     * @param goods
     * @return
     */
    Boolean updateGoods(Goods goods);



}
