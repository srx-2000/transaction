package com.srx.transaction.Controller;

import com.srx.transaction.Entities.*;
import com.srx.transaction.Entities.DTO.ResultMessage;
import com.srx.transaction.Mapper.WalletMapper;
import com.srx.transaction.Serivce.DealService;
import com.srx.transaction.Serivce.GoodsService;
import com.srx.transaction.Serivce.ShopService;
import com.srx.transaction.Serivce.WalletService;
import com.srx.transaction.Util.CodeUtil;
import com.srx.transaction.Util.PaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.srx.transaction.Enum.ResultCode.*;

@RestController
@RequestMapping("/deal")
public class DealController {

    private final int autoInterval = 1000 * 60 * 60 * 24;
    @Autowired
    private DealService dealService;
    @Autowired
    private ShopService shopService;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private WalletService walletService;


    @PostMapping("/insertDeal")
    public ResultMessage insetDeal(Deal deal) {
        String dealUUID = CodeUtil.get_uuid();
        deal.setDealUUID(dealUUID);
        Integer dealCount = Integer.valueOf(deal.getDealCount());
        String goodsUUID = deal.getGoodsUUID();
        Goods goodsByUUID = goodsService.getGoodsByUUID(goodsUUID);
        if (goodsByUUID == null)
            return new ResultMessage(ERROR_NOFOUND_GOODS);
        double money = goodsByUUID.getGoodsDiscountPrice() * dealCount;
        MiddleWallet middleWallet = new MiddleWallet();
        middleWallet.setDealUUID(dealUUID);
        middleWallet.setSumMoney(money);
        Boolean aBoolean = dealService.insertDeal(deal);
        Boolean aBoolean1 = goodsService.insertMiddleWallet(middleWallet);
        if (aBoolean == null) {
            return new ResultMessage(ERROR_GOODS_STATUS);
        }
        if (aBoolean && aBoolean1) {
            return new ResultMessage(INSERT_DEAL_SUCCESS);
        } else {
            return new ResultMessage(INSERT_DEAL_FAIL);
        }
    }

    @GetMapping("/dealFilter")
    public ResultMessage dealFilter(Deal deal, @RequestParam(required = false) Integer currentPage,
                                    @RequestParam(required = false) Integer pageSize) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        if (currentPage == null || pageSize == null) {
            List<Deal> dealListByCondition = dealService.getDealListByCondition(deal, currentPage, pageSize);
            return new ResultMessage(DATA_RETURN_SUCCESS, dealListByCondition);
        } else {
            ResultMessage getDealListByCondition = PaginationUtil.getPaginationResult(currentPage, pageSize, dealService, "getDealListByCondition", deal);
            return getDealListByCondition;
        }
    }

    /**
     * 确认收货，此接口为用户手动确认收货，确认收货后需要用户给出评价
     * 并在此时更改奶订单的时间为当前确认收货的时间，并从此时开始计算24小时内，如果用户不退货
     * 订单状态将锁死成为无法退货的彻底完成。
     * <p>
     * 而用户没有点击确认收货的订单会一直处于正在交易的状态，直到系统会在7*24小时后自动将订单状态更新为已收货
     * 此时默认的用户评价为5星好评，并在此时更新订单时间，更新为已收货时的时间，并在此后的24小时内接受退货
     * 如果超过24小时则订单锁死，无法退货
     * <p>
     * 此方法为用户主动提交时的方法，下面还需要有以下几个方法：
     * 1.每隔24小时遍历订单数据库，查询订单状态以及时间的定时任务类，此方法为锁死退货方法（当订单状态为2,且数据库中时间与现在的时间相比相差大于24小时）
     * 2.每隔7*24小时遍历订单数据库，查询订单状态以及时间的定时任务类，此方法为自动签收方法（当订单状态为0,且数据库中时间与现在的时间相比相差大于7*24小时）
     *
     * @return
     */
    @GetMapping("/receipt")
    public ResultMessage receipt(@RequestParam String dealUUID, @RequestParam String assess) {
        Boolean aBoolean = dealService.updateAssess(dealUUID, assess);
        Boolean aBoolean1 = dealService.updateStatus(dealUUID, "2");
        Boolean aBoolean2 = dealService.updateDealTime(dealUUID);
        if (aBoolean && aBoolean1 && aBoolean2) {
            return new ResultMessage(RECEIPT_DEAL_SUCCESS);
        } else
            return new ResultMessage(RECEIPT_DEAL_FAIL);
    }

    /**
     * 该方法主要是提供给买家用来退货的接口，需要买家给出对于这次购买后退货时的评价
     *
     * @param dealUUID
     * @param assess
     * @return
     */
    @GetMapping("/returnGoods")
    public ResultMessage returnGoods(@RequestParam String dealUUID, @RequestParam String assess) {
        MiddleWallet middleWallet = walletService.getMiddleWallet(dealUUID, "0");
        if (middleWallet != null) {
            double sumMoney = middleWallet.getSumMoney();
            Deal deal = dealService.getDeal(dealUUID);
            String commonId = deal.getCommonId();
            Boolean aBoolean2 = walletService.addMoney(commonId, sumMoney);
            if (aBoolean2) {
                Boolean aBoolean3 = walletService.updateMiddleWalletStatus(dealUUID, "-1");
                Boolean aBoolean = dealService.updateStatus(dealUUID, "1");
                Boolean aBoolean1 = dealService.updateAssess(dealUUID, assess);
                if (aBoolean && aBoolean1 && aBoolean3) {
                    return new ResultMessage(RETURN_DEAL_SUCCESS);
                } else
                    return new ResultMessage(RETURN_DEAL_FAIL);
            } else {
                return new ResultMessage(RETURN_DEAL_FAIL);
            }
        } else {
            return new ResultMessage(RETURN_DEAL_FAIL);
        }
    }

    /**
     * 该接口主要有两个作用，第一个是用来修改退货时的状态，即将用户退货的订单在完成退货时修改为-1。
     * 第二个是用来给管理员用户修改订单详情用的。
     *
     * @param dealUUID
     * @param status
     * @return
     */
    @GetMapping("/updateDealStatus")
    public ResultMessage updateDealStatus(@RequestParam String dealUUID, @RequestParam String status) {
        Boolean aBoolean = dealService.updateStatus(dealUUID, status);
        if (aBoolean)
            return new ResultMessage(UPDATE_DEAL_STATUS_SUCCESS);
        return new ResultMessage(UPDATE_DEAL_STATUS_FAIL);
    }

    /**
     * 此接口会在系统启动后自动每隔1天一刷新，用来将用户未签收的订单自动签收
     *
     * @return
     */
    @Scheduled(fixedRate = autoInterval)
    @GetMapping("/autoReceipt")
    public ResultMessage autoReceipt() {
        Deal deal = new Deal();
        deal.setStatus("0");
        Date date = new Date();
        Date receiptDate = new Date(date.getTime() - 1000 * 60 * 60 * 24 * 7);
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String receiptTime = sd.format(receiptDate);
        deal.setDealTime(receiptTime);
        List<Deal> dealListByCondition = dealService.getDealListByCondition(deal, null, null);
        Integer count = 0;
        for (Deal d : dealListByCondition) {
            String dealUUID = d.getDealUUID();
//            String status = d.getStatus();
            Boolean aBoolean = dealService.updateStatus(dealUUID, "2");
            if (aBoolean) {
                count++;
            }
        }
        if (count == dealListByCondition.size()) {
            return new ResultMessage(RECEIPT_DEAL_SUCCESS);
        }
        return new ResultMessage(RECEIPT_DEAL_FAIL);
    }

    /**
     * 该接口依旧会在系统启动后自动完成，用来将用户已签收的订单在24小时后自动更改为已完成，即不可以进行退货
     *
     * @return
     */
    @Scheduled(fixedRate = autoInterval)
    @GetMapping("/autoComplete")
    public ResultMessage autocomplete() {
        Deal deal = new Deal();
        deal.setStatus("2");
        Date date = new Date();
        Date receiptDate = new Date(date.getTime() - 1000 * 60 * 60 * 24);
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String receiptTime = sd.format(receiptDate);
        deal.setDealTime(receiptTime);
        List<Deal> dealListByCondition = dealService.getDealListByCondition(deal, null, null);
        Integer count = 0;
        for (Deal d : dealListByCondition) {
            String dealUUID = d.getDealUUID();
            String goodsUUID = d.getGoodsUUID();
            String dealCount = d.getDealCount();
//            String status = d.getStatus();
            MiddleWallet middleWallet = walletService.getMiddleWallet(dealUUID, "0");
            if (middleWallet != null) {
                double sumMoney = middleWallet.getSumMoney();
                Deal deal1 = dealService.getDeal(dealUUID);
                String shopUUID = deal1.getShopUUID();
                Shop shopByUUID = shopService.getShopByUUID(shopUUID);
                String businessId = shopByUUID.getBusinessId();
                Boolean aBoolean2 = walletService.addMoney(businessId, sumMoney);
                if (aBoolean2) {
                    Boolean aBoolean1 = walletService.updateMiddleWalletStatus(dealUUID, "1");
                    if (aBoolean1) {
                        Boolean aBoolean = dealService.updateStatus(dealUUID, "-1");
                        if (aBoolean) {
                            if (Integer.valueOf(d.getAssess()) > 3) {
                                //这里有个隐形bug，就是将商品和店铺的交易量的更新放在了订单插入方法中，所以此时如果用户退货了，那么dealcount会出现多出的情况
                                Boolean aBoolean3 = goodsService.updateGoodsPraiseCount(goodsUUID, Integer.valueOf(dealCount));
                                Boolean aBoolean4 = shopService.updateShopPraiseCount(goodsUUID, Integer.valueOf(dealCount));
                                if (aBoolean3 && aBoolean4)
                                    count++;
                            }
                        }
                    }
                }
            }
        }
        if (count == dealListByCondition.size()) {
            return new ResultMessage(COMPLETE_DEAL_SUCCESS);
        }
        return new ResultMessage(COMPLETE_DEAL_FAIL);
    }

    @GetMapping("/getDealCount")
    public ResultMessage getDealCount(Deal deal) {
        Integer dealCount = dealService.queryDealCount(deal);
        Map<String, Integer> countMap = new HashMap<>();
        countMap.put("dealCount", dealCount);
        return new ResultMessage(DATA_RETURN_SUCCESS, countMap);
    }
}
