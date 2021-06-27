package com.srx.transaction.Controller;

import com.srx.transaction.Entities.DTO.ResultMessage;
import com.srx.transaction.Entities.Goods;
import com.srx.transaction.Entities.GoodsPicture;
import com.srx.transaction.Enum.GoodsType;
import com.srx.transaction.Serivce.GoodsService;
import com.srx.transaction.Util.CodeUtil;
import com.srx.transaction.Util.PaginationUtil;
import com.srx.transaction.Util.PictureUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    /**
     * 这里的goodsType参数，应为typeCode
     *
     * @param goods
     * @param goodsPictures
     * @return
     */
    @PostMapping("/insertGoods")
    public ResultMessage insertGoods(Goods goods, @RequestPart MultipartFile[] goodsPictures) {
        if (goods != null || goodsPictures.length == 0) {
            try {
                List<GoodsPicture> pictureUrlList = new ArrayList<>();
                String goodsUUID = CodeUtil.get_uuid();
                String shopUUID = goods.getShopUUID();
                goods.setGoodsUUID(goodsUUID);
                Integer discount = goods.getDiscount();
                Double goodsPrice = goods.getGoodsPrice();
                goods.setGoodsDiscountPrice(goodsPrice * discount / 100);
                Integer count = 0;
                for (int i = 0; i < goodsPictures.length; i++) {
                    Boolean aBoolean = PictureUtil.uploadPicture(goodsPictures[i], shopUUID, goodsUUID, null);
                    if (aBoolean) {
                        count++;
                    } else
                        return new ResultMessage(ERROR_NETWORK);
                }
                if (count == goodsPictures.length) {
                    List<String> goodsUrl = PictureUtil.getGoodsUrl(shopUUID, goodsUUID);
                    for (String path : goodsUrl) {
                        GoodsPicture goodsPicture = new GoodsPicture();
                        goodsPicture.setGoodsUUID(goodsUUID);
                        goodsPicture.setPicturePath(path);
                        pictureUrlList.add(goodsPicture);
                    }
                } else
                    return new ResultMessage(ERROR_NETWORK);
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

    /**
     * typeCode与上面方法一样
     *
     * @param goods
     * @param goodsPictures
     * @return
     * @throws Exception
     */
    @PostMapping("/updateGoodsInfo")
    public ResultMessage updateGoodsInfo(Goods goods, @RequestPart(required = false) MultipartFile[] goodsPictures) throws Exception {
        String goodsUUID = goods.getGoodsUUID();
        Goods goodsByUUID = goodsService.getGoodsByUUID(goodsUUID);
        String shopUUID = goodsByUUID.getShopUUID();
        List<GoodsPicture> pictureUrlList = new ArrayList<>();
        if (goodsByUUID != null) {
            Integer discount = goods.getDiscount();
            goods.setGoodsDiscountPrice(goods.getGoodsPrice() * discount / 100);
            try {
                Integer count = 0;
                if (goodsPictures != null) {
                    for (int i = 0; i < goodsPictures.length; i++) {
                        Boolean aBoolean = PictureUtil.uploadPicture(goodsPictures[i], shopUUID, goodsUUID, null);
                        if (aBoolean) {
                            count++;
                        } else
                            return new ResultMessage(ERROR_NETWORK);
                    }
                    if (count == goodsPictures.length) {
                        List<String> goodsUrl = PictureUtil.getGoodsUrl(shopUUID, goodsUUID);
                        for (String path : goodsUrl) {
                            GoodsPicture goodsPicture = new GoodsPicture();
                            goodsPicture.setGoodsUUID(goodsUUID);
                            goodsPicture.setPicturePath(path);
                            pictureUrlList.add(goodsPicture);
                        }
                    } else
                        return new ResultMessage(ERROR_NETWORK);
                }
            } catch (Exception e) {
                e.printStackTrace();
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
        if (goodsByUUID != null) {
            return new ResultMessage(DATA_RETURN_SUCCESS, goodsByUUID);
        }
        return new ResultMessage(ERROR_NOFOUND_GOODS);
    }

    @GetMapping("/changeGoodsStatus")
    public ResultMessage changeGoodStatus(@RequestParam String goodsUUID, @RequestParam String status) {
        Goods goodsByUUID = goodsService.getGoodsByUUID(goodsUUID);
        if (goodsByUUID != null) {
            Boolean aBoolean = goodsService.updateGoodsStatus(goodsUUID, status);
            if (aBoolean)
                return new ResultMessage(UPDATE_GOODS_STATUS_SUCCESS);
            else
                return new ResultMessage(UPDATE_GOODS_STATUS_FAIL);
        } else
            return new ResultMessage(ERROR_NOFOUND_GOODS);

    }

    /**
     * 需要手动调用对好评率进行更新，也可以使用springboot自带的计时器进行自动更新
     *
     * @param goodsUUID
     * @return
     */
    @GetMapping("/updateGoodsPraiseRate")
    public ResultMessage updateGoodsPraiseRate(@RequestParam String goodsUUID) {
        Goods goodsByUUID = goodsService.getGoodsByUUID(goodsUUID);
        if (goodsByUUID != null) {
            Boolean aBoolean = goodsService.updateGoodsPraiseRate(goodsUUID);
            if (aBoolean)
                return new ResultMessage(UPDATE_GOODS_INFO_SUCCESS);
            else
                return new ResultMessage(UPDATE_GOODS_INFO_FAIL);
        } else
            return new ResultMessage(ERROR_NOFOUND_GOODS);
    }

    @GetMapping("/getGoodsType")
    public ResultMessage getGoodsType() {
        List<Map<String, Object>> typeList = new ArrayList<>();
        GoodsType[] enumConstants = GoodsType.class.getEnumConstants();
        for (int i = 0; i < enumConstants.length; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("type", enumConstants[i].getType());
            map.put("typeCode", enumConstants[i].getTypeCode());
            typeList.add(map);
        }
        if (enumConstants.length != 0) {
            return new ResultMessage(DATA_RETURN_SUCCESS, typeList);
        } else return new ResultMessage(ERROR_NO_DATA);
    }

    @GetMapping("/getGoodsCount")
    public ResultMessage getGoodsCount(Goods goods){
        Map<String,Integer> countMap=new HashMap<>();
        Integer integer = goodsService.queryGoodsCount(goods);
        countMap.put("GoodsCount",integer);
        return new ResultMessage(DATA_RETURN_SUCCESS,countMap);
    }
}
