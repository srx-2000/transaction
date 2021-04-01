package com.srx.transaction.Controller;

import com.srx.transaction.Entities.DTO.ResultMessage;
import com.srx.transaction.Entities.ShopCar;
import com.srx.transaction.Enum.ResultCode;
import com.srx.transaction.Serivce.ShopCarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.srx.transaction.Enum.ResultCode.*;

@RestController
@RequestMapping("/shopCar")
public class ShopCarController {
    @Autowired
    private ShopCarService shopCarService;

    @PostMapping("/insertToCar")
    public ResultMessage insertToCar(ShopCar shopCar) {
        Boolean aBoolean = shopCarService.insertGoodsToCar(shopCar);
        if (aBoolean)
            return new ResultMessage(INSERT_SHOPCAR_SUCCESS);
        return new ResultMessage(INSERT_SHOPCAR_FAIL);
    }

    @GetMapping("/updateCount")
    public ResultMessage updateCount(@RequestParam String goodsUUID, @RequestParam String commonId, @RequestParam Boolean countUpper) {
        Boolean aBoolean = shopCarService.updateCount(goodsUUID, commonId, countUpper);
        if (aBoolean)
            return new ResultMessage(UPDATE_SHOPCAR_COUNT_SUCCESS);
        return new ResultMessage(UPDATE_SHOPCAR_COUNT_FAIL);
    }

    @GetMapping("/getGoodsListInCar")
    public ResultMessage getGoodsListInCar(@RequestParam String commonId, @RequestParam String status) {
        List<ShopCar> goodsListByCommonId = shopCarService.getGoodsListByCommonId(commonId, status);
        return new ResultMessage(DATA_RETURN_SUCCESS, goodsListByCommonId);
    }

    @GetMapping("/getGoodsByCommonIdAndUUID")
    public ResultMessage getGoodsByCommonIdAndUUID(@RequestParam String goodsUUID, @RequestParam String commonId) {
        ShopCar goodsByCommonIdAndUUID = shopCarService.getGoodsByCommonIdAndUUID(goodsUUID, commonId);
        return new ResultMessage(DATA_RETURN_SUCCESS, goodsByCommonIdAndUUID);
    }

    @GetMapping("/updateStatus")
    public ResultMessage updateStatus(@RequestParam String goodsUUID, @RequestParam String commonId, @RequestParam String status) {
        Boolean aBoolean = shopCarService.updateStatus(goodsUUID, commonId, status);
        if (aBoolean)
            return new ResultMessage(UPDATE_SHOPCAR_STATUS_SUCCESS);
        return new ResultMessage(UPDATE_SHOPCAR_STATUS_FAIL);

    }

}
