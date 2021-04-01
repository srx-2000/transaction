package com.srx.transaction.Mapper;

import com.srx.transaction.Entities.MiddleWallet;
import com.srx.transaction.Entities.Wallet;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletMapper {
    /**
     * 插入一个钱包
     *
     * @param userId
     * @return
     */
    Boolean insertWallet(String userId);

    /**
     * 更新积分
     *
     * @param userId
     * @param integral
     * @return
     */
    Boolean updateIntegral(@Param("userId") String userId, @Param("integral") Integer integral);

    /**
     * 更新总钱数，可加可减，减少总钱数的应用场景为商家退钱给买家，此方法在service层的实现应该给两个账户都添加钱
     *
     * @param userId
     * @param sumMoney
     * @return
     */
    Boolean updateSumMoney(@Param("userId") String userId, @Param("sumMoney") Double sumMoney);

    /**
     * 通过用户id查询钱包
     *
     * @param userId
     * @return
     */
    Wallet queryWallet(@Param("userId") String userId);

    /**
     * 更新当前钱数，可加可减
     * @param userId
     * @param currentMoney
     * @return
     */
    Boolean updateCurrentMoney(@Param("userId") String userId, @Param("currentMoney") Double currentMoney);

    /**
     * 插入一个
     * @param middleWallet
     * @return
     */
    Boolean insertMiddleWallet(MiddleWallet middleWallet);

    /**
     * 更新中间钱包的状态
     * @param dealUUID
     * @param status
     * @return
     */
    Boolean updateMiddleWalletStatus(@Param("dealUUID")String dealUUID,@Param("status")String status);

    /**
     * 通过订单号查询某一次订单中间钱包的信息
     * @param dealUUID
     * @return
     */
    MiddleWallet queryMiddleWalletByUUID(@Param("dealUUID")String dealUUID,@Param("status") String status);
}
