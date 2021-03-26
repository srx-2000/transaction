package com.srx.transaction.Mapper;

import com.srx.transaction.Entities.Deal;
import org.springframework.stereotype.Repository;

@Repository
public interface DealMapper {
    /**
     * 插入一次交易，该方法应在用户点击购买后
     * @param deal
     * @return
     */
    Boolean insertDeal(Deal deal);
}
