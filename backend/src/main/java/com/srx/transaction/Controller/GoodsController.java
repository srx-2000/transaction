package com.srx.transaction.Controller;

import com.srx.transaction.Entities.DTO.ResultMessage;
import com.srx.transaction.Entities.Goods;
import com.srx.transaction.Entities.GoodsPicture;
import com.srx.transaction.Serivce.GoodsService;
import com.srx.transaction.Util.CodeUtil;
import com.srx.transaction.Util.PaginationUtil;
import com.srx.transaction.Util.PictureUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import static com.srx.transaction.Enum.ResultCode.*;

@RestController
@RequestMapping("/Goods")
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    @GetMapping("/goodsFilter")
    public ResultMessage goodsFilter(Goods goods,
                                     @RequestParam Integer currentPage,
                                     @RequestParam Integer pageSize) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        ResultMessage getGoodsByCondition = PaginationUtil.getPaginationResult(currentPage, pageSize, goodsService, "getGoodsByCondition", goods);
        return getGoodsByCondition;
    }

    @PostMapping("/insertGoods")
    public ResultMessage insertGoods(Goods goods,
                                     @RequestPart MultipartFile[] goodsPictures) {
        if (goods != null || goodsPictures.length == 0) {
            try {
                List<GoodsPicture> pictureUrlList = new ArrayList<>();
                String goodsUUID = CodeUtil.get_uuid();
                String shopUUID = goods.getShopUUID();
                goods.setGoodsUUID(goodsUUID);
                Integer discount = goods.getDiscount();
                Double goodsPrice = goods.getGoodsPrice();
                goods.setGoodsDiscountPrice(goodsPrice * discount / 100);
                for (int i = 0; i < goodsPictures.length; i++) {
                    Boolean aBoolean = PictureUtil.uploadPicture(goodsPictures[i], shopUUID, goodsUUID, null);
                    if (aBoolean) {
                        String path = PictureUtil.getUrl(shopUUID, goodsUUID, null);
                        GoodsPicture goodsPicture = new GoodsPicture();
                        goodsPicture.setGoodsUUID(goodsUUID);
                        goodsPicture.setPicturePath(path);
                        pictureUrlList.add(goodsPicture);
                    } else
                        return new ResultMessage(ERROR_NETWORK);
                }
                Boolean aBoolean = goodsService.insertGoods(goods, pictureUrlList);
                if (aBoolean) {
                    return new ResultMessage(GOODS_INSERT_SUCCESS);
                } else {
                    return new ResultMessage(GOODS_INSERT_FAIL);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return new ResultMessage(ERROR_PARAM);
            }
        } else {
            return new ResultMessage(ERROR_NULL);
        }
    }

    @GetMapping("/updateGoodsInfo")
    public ResultMessage updateGoodsInfo(Goods goods, @RequestPart MultipartFile[] goodsPictures) throws Exception {
        String goodsUUID = goods.getGoodsUUID();
        Goods goodsByUUID = goodsService.getGoodsByUUID(goodsUUID);
        String shopUUID = goodsByUUID.getShopUUID();
        List<GoodsPicture> pictureUrlList = new ArrayList<>();
        if (goodsByUUID != null) {
            Integer discount = goods.getDiscount();
            goods.setGoodsDiscountPrice(goods.getGoodsPrice() * discount / 100);
            try {
                for (int i = 0; i < goodsPictures.length; i++) {
                    Boolean aBoolean = PictureUtil.uploadPicture(goodsPictures[i], shopUUID, goodsUUID, null);
                    if (aBoolean) {
                        String path = PictureUtil.getUrl(shopUUID, goodsUUID, null);
                        GoodsPicture goodsPicture = new GoodsPicture();
                        goodsPicture.setGoodsUUID(goodsUUID);
                        goodsPicture.setPicturePath(path);
                        pictureUrlList.add(goodsPicture);
                    } else
                        return new ResultMessage(ERROR_NETWORK);
                }
            } catch (Exception e) {
                return new ResultMessage(ERROR_NETWORK);
            }
            Boolean aBoolean = goodsService.updateGoodsInfo(goods, pictureUrlList);
            if (aBoolean) {
                return new ResultMessage(UPDATE_GOODS_INFO_SUCCESS);
            } else {
                return new ResultMessage(UPDATE_GOODS_INFO_FAIL);
            }
        } else {
            return new ResultMessage(ERROR_NOFOUND_GOODS);
        }

    }

    @GetMapping("/getGoodsByUUID")
    public ResultMessage getGoodsByUUID(@RequestParam String goodsUUID) {
        Goods goodsByUUID = goodsService.getGoodsByUUID(goodsUUID);
        return new ResultMessage(DATA_RETURN_SUCCESS, goodsByUUID);
    }

//    @GetMapping("/changeGoodsStatus")
//    public ResultMessage changeGoodStatus(){
//
//    }
}
