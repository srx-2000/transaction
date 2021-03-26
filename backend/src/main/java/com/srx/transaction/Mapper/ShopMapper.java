package com.srx.transaction.Mapper;

import com.srx.transaction.Entities.Shop;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShopMapper {
    /**
     * 插入一个商店，该方法应该在商家用户被管理员审批同意成为一个正常用户时调用
     *
     * @param shop
     * @return
     */
    Boolean insertShop(Shop shop);

    /**
     * 该方法是给商家用户使用的，用来更改该店铺的名称
     * 计划中，商家只允许更改这个属性和status这个属性，其他属性皆为自动更新或是管理员用户手动更改
     *
     * @param shopName
     * @param shopUUID
     * @return
     */
    Boolean updateShopName(@Param("shopUUID") String shopUUID, @Param("shopName") String shopName);

    /**
     * 该方法有两个主要用途
     * 1.管理员封禁商家用户的时候顺便把商家的店给关了，或恢复商家时给商家恢复
     * 2.商家自己关了，或开了
     * 3.店铺在商家账号未被审核成功时，默认为-1的状态，即审核中
     *
     * @param shopUUID
     * @param status
     * @return
     */
    Boolean updateShopStatus(@Param("shopUUID") String shopUUID, @Param("status") String status);

    /**
     * 根据商家的id查询商家店铺信息
     *
     * @param businessId
     * @return
     */
    Shop queryShopByUserId(@Param("businessId") String businessId);

    /**
     * 更新商铺等级
     *
     * @param shopUUID
     * @param level
     * @return
     */
    Boolean updateShopLevel(@Param("shopUUID") String shopUUID, @Param("level") Integer level);

    /**
     * 获取所有商铺信息
     *
     * @return
     */
    List<Shop> queryShopList(@Param("begin") Integer begin, @Param("pageSize") Integer pageSize);

    /**
     * 通过商铺的UUID查询商铺信息
     *
     * @param shopUUID
     * @return
     */
    Shop queryShopByUUID(@Param("shopUUID") String shopUUID);

    /**
     * 更新商铺成交量
     *
     * @param shopUUID
     * @return
     */
    Boolean updateShopDealCount(@Param("shopUUID") String shopUUID, @Param("dealCount") Integer dealCount);

    /**
     * 更新商铺好评量
     *
     * @param shopUUID
     * @return
     */
    Boolean updateShopPraiseCount(@Param("shopUUID") String shopUUID, @Param("praiseCount") Integer praiseCount);

    /**
     * 更新商铺好评率
     *
     * @param shopUUID
     * @return
     */
    Boolean updateShopPraiseRate(@Param("shopUUID") String shopUUID, @Param("praiseRate") Double praiseRate);

    /**
     * 通过传入的shop对象，检查shop对象中的属性，将不为空的加入到搜索条件中
     *
     * @param shop
     * @param begin
     * @param pageSize
     * @return
     */
    List<Shop> queryShopListByCondition(Shop shop, @Param("begin") Integer begin, @Param("pageSize") Integer pageSize);

}
