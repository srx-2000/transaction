package com.srx.transaction.Serivce;

import com.srx.transaction.Entities.ShopCar;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ShopCarService extends BaseService {
    /**
     * 插入一个商品到购物车中
     *
     * @param shopCar
     * @return
     */
    Boolean insertGoodsToCar(ShopCar shopCar);

    /**
     * 更新某用户在购物车中的某个商品的数量，这里需要注意，当用户将商品的数量改为0时，需要将status改为-1
     *
     * @param goodsUUID
     * @param commonId
     * @param countUpper 数量是否是增加
     * @return
     */
    Boolean updateCount(String goodsUUID, String commonId, Boolean countUpper);

    /**
     * 更新购物车中商品状态，主要用在插入deal和删除购物车中物品，以及检测用户是否曾将物品放入购物车然后删除，然后又想恢复
     * <p>
     * 这里需要说明，当一个商品在购物车中的状态为-1时，那么再向购物车中插入该商品时则需要插入一个新的，否则则直接将状态更新为0即可
     *
     * @param goodsUUID
     * @param commonId
     * @param status
     * @return
     */
    Boolean updateStatus(String goodsUUID, String commonId, String status);


    /**
     * 通过goods_uuid以及common_id，查询购物车中指定商品
     *
     * @param goodsUUID
     * @param commonId
     * @return
     */
    ShopCar getGoodsByCommonIdAndUUID(String goodsUUID, String commonId);


    /**
     * 通过用户的id查询该用户的购物车
     * 如果给出了商品在购物车中的具体状态，则根据状态查询
     *
     * @param commonId
     * @param status
     * @return
     */
    List<ShopCar> getGoodsListByCommonId(String commonId, String status);

}
