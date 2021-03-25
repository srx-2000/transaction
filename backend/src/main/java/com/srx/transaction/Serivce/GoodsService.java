package com.srx.transaction.Serivce;

import com.srx.transaction.Entities.Goods;

public interface GoodsService extends BaseService {
    /**
     * 插入一个商品
     * @param goods
     * @return
     */
    Boolean insertGoods(Goods goods);
    /**
     * 记得给mapper中的updateGoods方法进行拆分
     */
}
