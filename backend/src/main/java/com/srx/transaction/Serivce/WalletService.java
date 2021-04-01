package com.srx.transaction.Serivce;

import com.srx.transaction.Entities.MiddleWallet;
import com.srx.transaction.Entities.Wallet;
import org.apache.ibatis.annotations.Param;

public interface WalletService extends BaseService {
//    /**
//     * 插入一个钱包,该方法已在userservice中使用mapper实现，无需再提供service层的方法
//     *
//     * @param userId
//     * @return
//     */
//    Boolean insertWallet(String userId);

//    /**
//     * 更新积分
//     *
//     * @param userId
//     * @param integral
//     * @return
//     */
//    Boolean updateIntegral(String userId, String integral);

    /**
     * 给用户添加钱，可以用在商家获利，也可以用在用户充值
     *
     * @param userId
     * @param addMoney
     * @return
     */
    Boolean addMoney(String userId, Double addMoney);

    /**
     * 通过用户id查询钱包
     *
     * @param userId
     * @return
     */
    Wallet getWallet(String userId);


    /**
     * 普通用户花钱
     *
     * @param userId
     * @param subMoney
     * @return
     */
    Boolean subMoney(String userId, Integer subMoney);

    /**
     * 查询一条中间钱包记录
     *
     * @param dealUUID
     * @return
     */
    MiddleWallet getMiddleWallet(String dealUUID,String status);

    /**
     * 更新中间钱包的状态
     *
     * @param dealUUID
     * @param status
     * @return
     */
    Boolean updateMiddleWalletStatus(String dealUUID, String status);
}
