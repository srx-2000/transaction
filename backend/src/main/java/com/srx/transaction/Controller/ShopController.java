package com.srx.transaction.Controller;

import com.srx.transaction.Entities.DTO.ResultMessage;
import com.srx.transaction.Entities.Shop;
import com.srx.transaction.Enum.ResultCode;
import com.srx.transaction.Serivce.ShopService;
import com.srx.transaction.Util.PaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.srx.transaction.Enum.ResultCode.*;

@RestController
@RequestMapping("/shop")
public class ShopController {

    @Autowired
    private ShopService shopService;

    /**
     * 该接口提供对店铺的筛选，如果shop参数不填的话，就是查询所有店铺
     *
     * @param shop
     * @param currentPage
     * @param pageSize
     * @return
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    @GetMapping("/shopFilter")
    public ResultMessage shopFilter(Shop shop,
                                    @RequestParam("currentPage") Integer currentPage,
                                    @RequestParam("pageSize") Integer pageSize) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {

        ResultMessage getShopList = PaginationUtil.getPaginationResult(currentPage, pageSize, shopService, "getShopListByCondition", shop);
        return getShopList;
    }

    @GetMapping("/updateShopLevel")
    public ResultMessage updateShopLevel(@RequestParam String shopUUID, @RequestParam Boolean upper) {
        Boolean aBoolean = shopService.updateShopLevel(shopUUID, upper);
        if (aBoolean) {
            return new ResultMessage(UPDATE_SHOP_LEVEL_SUCCESS, true);
        } else {
            return new ResultMessage(UPDATE_SHOP_LEVEL_FAIL, false);
        }
    }

    /**
     * isOpen为true，店铺状态将更新为营业中
     * isOpen为false，店铺状态将更新为已打烊
     *
     * @param shopUUID
     * @param isOpen
     * @return
     */
    @GetMapping("/updateShopStatus")
    public ResultMessage updateShopStatus(@RequestParam String shopUUID, @RequestParam Boolean isOpen) {
        String status;
        if (isOpen) {
            status = "1";
        } else {
            status = "0";
        }
        Shop shopByUUID = shopService.getShopByUUID(shopUUID);
        String nowStatus = shopByUUID.getStatus();
        if (status.equals(nowStatus)){
            return new ResultMessage(ERROR_UPDATE_SHOP_STATUS);
        }
        Boolean aBoolean = shopService.updateShopStatus(shopUUID, status);
        if (aBoolean)
            return new ResultMessage(UPDATE_SHOP_STATUS_SUCCESS);
        return new ResultMessage(UPDATE_SHOP_STATUS_FAIL);
    }

    @GetMapping("/updateShopName")
    public ResultMessage updateShopName(@RequestParam String shopUUID, @RequestParam String shopName) {
        Shop shopByUUID = shopService.getShopByUUID(shopUUID);
        String name = shopByUUID.getShopName();
        if (shopName.equals(name)){
            return new ResultMessage(ERROR_UPDATE_SHOP_NAME);
        }
        Boolean aBoolean = shopService.updateShopName(shopUUID, shopName);
        if (aBoolean)
            return new ResultMessage(UPDATE_SHOP_NAME_SUCCESS, true);
        return new ResultMessage(UPDATE_SHOP_NAME_FAIL, false);
    }

    @GetMapping("/updateShopPraiseRate")
    public ResultMessage updateShopPraiseRate(@RequestParam String shopUUID) {
        Shop shopByUUID = shopService.getShopByUUID(shopUUID);
        if (shopByUUID != null) {
            Boolean aBoolean = shopService.updateShopPraiseRate(shopUUID);
            if (aBoolean)
                return new ResultMessage(UPDATE_SHOP_INFO_SUCCESS);
            else
                return new ResultMessage(UPDATE_SHOP_INFO_FAIL);
        }
        return new ResultMessage(ERROR_NOFOUND_SHOP);
    }


    @GetMapping("/getShopCount")
    public ResultMessage getShopCount(Shop shop){
        Integer shopCount = shopService.getShopCount(shop);
        Map<String,Integer> countMap=new HashMap<>();
        countMap.put("shopCount",shopCount);
        return new ResultMessage(DATA_RETURN_SUCCESS,countMap);
    }
}
