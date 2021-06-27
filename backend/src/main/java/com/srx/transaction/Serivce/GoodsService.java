package com.srx.transaction.Serivce;

import com.srx.transaction.Entities.Goods;
import com.srx.transaction.Entities.GoodsPicture;
import com.srx.transaction.Entities.MiddleWallet;
import com.srx.transaction.Entities.Shop;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface GoodsService extends BaseService {
    /**
     * 插入一个商品，这里需要注意的是给goodsDiscountPrice附上初值与goodsPrice相等
     * 同时还需要在此方法中向goodsPicture表中插入商品图片信息
     *
     * @param goods
     * @param pictureList 商品图片列表
     * @return
     */
    Boolean insertGoods(Goods goods, List<GoodsPicture> pictureList);

    /**
     * 更新商品状态,这里的商品状态指的是商品处于上架或下架状态，而非是否被用户购买
     *
     * @param goodsUUID
     * @param status
     * @return
     */
    Boolean updateGoodsStatus(String goodsUUID, String status);

    /**
     * 查询商品列表
     *
     * @param goods
     * @param currentPage
     * @param pageSize
     * @return
     */
    List<Goods> getGoodsByCondition(Goods goods, Integer currentPage, Integer pageSize);

    /**
     * 这里的count仅仅指普通用户购买商品后，商品数量减少
     * 商家更新商品数量的方法请调用updateGoodsInfo方法
     *
     * @param goodsUUID
     * @param subtractCount
     * @return
     */
    Boolean updateGoodsCount(String goodsUUID, Integer subtractCount);

    /**
     * 这里是通过uuid获取一个商品，此方法不对外提供接口
     *
     * @param goodsUUID
     * @return
     */
    Goods getGoodsByUUID(String goodsUUID);

    /**
     * 更新商品好评数量
     *
     * @param goodsUUID
     * @param addCount
     * @return
     */
    Boolean updateGoodsPraiseCount(String goodsUUID, Integer addCount);

    /**
     * 更新商品好评率,每次调用该方法，系统会从数据库中读取现在的数据进行计算，无需传入修改值
     *
     * @param goodsUUID
     * @return
     */
    Boolean updateGoodsPraiseRate(String goodsUUID);

    /**
     * 更新商品交易数量,该方法与updateGoodsPraiseCount都不支持对数量进行减法运算
     *
     * @param goodsUUID
     * @param addCount
     * @return
     */
    Boolean updateGoodsDealCount(String goodsUUID, Integer addCount);

    /**
     * 更新商品信息，此方法主要是给商家用户使用，用来修改商品信息使用，
     * 而上面的更新方法多是给系统内部调用，用来级联更新一个商品的状态，数量等。
     * @param goods（goodsName,goodsDescription,size,goodsType,isBargain,goodsPrice,goodsCount,damageLevel,goodsDiscountPrice）
     * @param pictureList
     * @return
     */
    Boolean updateGoodsInfo(Goods goods,List<GoodsPicture> pictureList);

    /**
     * 插入一个订单记录
     * @param middleWallet
     * @return
     */
    Boolean insertMiddleWallet(MiddleWallet middleWallet);

//    /**
//     * 更新商品价格
//     *
//     * @param goodsUUID
//     * @param price
//     * @return
//     */
//    Boolean updateGoodsPrice(String goodsUUID, Double price);
//
//    /**
//     * 更新商品打折价格
//     *
//     * @param goodsUUID
//     * @param count
//     * @return
//     */
//    Boolean updateGoodsDiscountPrice(String goodsUUID, Integer count);
//
//    /**
//     * 更新商品磨损率
//     *
//     * @param goodsUUID
//     * @param level
//     * @return
//     */
//    Boolean updateGoodsDamageLevel(String goodsUUID, Integer level);
//
//    /**
//     * 更新商品描述
//     * @param goodsUUID
//     * @param description
//     * @return
//     */
//    Boolean updateGoodsDescription(String goodsUUID, String description);
//    /**
//     * 更新商品大小
//     * @param goodsUUID
//     * @param size
//     * @return
//     */
//    Boolean updateGoodsSize(String goodsUUID, String size);
//
//    /**
//     * 更新商品是否可以讨价还价
//     * @param goodsUUID
//     * @param isBargain
//     * @return
//     */
//    Boolean updateGoodsIsBargain(String goodsUUID,String isBargain);

    /**
     * 通过传入的goods查询相应的商品的数量
     * @param goods
     * @return
     */
    Integer queryGoodsCount(Goods goods);


}
