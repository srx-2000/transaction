package com.srx.transaction.Serivce;

import com.srx.transaction.Entities.Deal;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DealService extends BaseService{
    /**
     * 插入一次交易，该方法应在用户点击购买后
     * @param deal
     * @return
     */
    Boolean insertDeal(Deal deal);

    /**
     * 查询交易列表
     * @param deal
     * @return
     */
    List<Deal> getDealListByCondition(Deal deal, Integer currentPage, Integer pageSize);

    /**
     * 将给出的dealUUID的订单的时间同步到现在，一般在订单被确认收货后调用，以提供24小时计时功能
     * 这里要注意，一开始插入的时间是用户提交订单的时间，而非用户收货的时间，所以这里需要更新订单交易时间
     * 而退货的时限范围是确认收货后的24小时
     * @param dealUUID
     * @return
     */
    Boolean updateDealTime(String dealUUID);

    /**
     * 该方法为用户评价某一单时调用，因为默认插入时assess为5，所以这里如果用户真的评价了，需要更改原先的assess
     * @param dealUUID
     * @param assess
     * @return
     */
    Boolean updateAssess(String dealUUID,String assess);

    /**
     * 更新订单状态
     * @param dealUUID
     * @param status
     * @return
     */
    Boolean updateStatus(String dealUUID,String status);

    /**
     * 根据订单号查询单个订单
     * @param dealUUID
     * @return
     */
    Deal getDeal(String dealUUID);

    /**
     * 根据goodsUUID查询特定商品的所有订单
     * @param goodsUUID
     * @return
     */
    List<Deal> getDealListByGoodsUUID(String goodsUUID);

}
