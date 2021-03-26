package com.srx.transaction.Mapper;

import com.srx.transaction.Entities.Goods;
import com.srx.transaction.Entities.GoodsPicture;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

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
     *
     * @param goods
     * @return
     */
    Boolean updateGoods(Goods goods);

    /**
     * 根据给出的goods中的非空项对goods进行筛选
     *
     * @param goods
     * @param begin
     * @param pageSize
     * @return
     */
    List<Goods> queryGoodsListByCondition(Goods goods, @Param("begin") Integer begin, @Param("pageSize") Integer pageSize);

    /**
     * 插入一个商品图片
     *
     * @param goodsPicture
     * @return
     */
    Boolean insertGoodsPicture(GoodsPicture goodsPicture);

    /**
     * 根据商品uuid查询最新的5张图片的地址
     *
     * @param goodsUUID
     * @return
     */
    List<GoodsPicture> queryGoodsPictureListByUUID(@Param("goodsUUID") String goodsUUID, @Param("pageSize") Integer pageSize);

    /**
     * 通过商品uuid查询商品,这里就不做和goodsPicture表的连接了，直接返回基本信息，
     * 商品图片通过上面的方法获取
     * @param goodsUUID
     * @return
     */
    Goods queryGoodsByUUID(@Param("goodsUUID") String goodsUUID);

}

