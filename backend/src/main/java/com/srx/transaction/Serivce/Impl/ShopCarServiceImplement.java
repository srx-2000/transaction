package com.srx.transaction.Serivce.Impl;

import com.srx.transaction.Entities.Goods;
import com.srx.transaction.Entities.ShopCar;
import com.srx.transaction.Mapper.GoodsMapper;
import com.srx.transaction.Mapper.ShopCarMapper;
import com.srx.transaction.Serivce.ShopCarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShopCarServiceImplement implements ShopCarService {

    @Autowired
    private ShopCarMapper shopCarMapper;
    @Autowired
    private GoodsMapper goodsMapper;

    @Override
    public Boolean insertGoodsToCar(ShopCar shopCar) {
        Goods goods = goodsMapper.queryGoodsByUUID(shopCar.getGoodsUUID());
        String goodsStatus = goods.getStatus();
        Integer goodsCount = goods.getGoodsCount();
        Integer carCount = shopCar.getGoodsCount();
        if (goods != null && goodsStatus.equals("1") && goodsCount >= carCount) {
            Boolean aBoolean = shopCarMapper.insertGoodsToCar(shopCar);
            return aBoolean;
        }
        return false;
    }

    @Override
    public Boolean updateCount(String goodsUUID, String commonId, Boolean countUpper) {
        ShopCar shopCar = shopCarMapper.queryGoodsByCommonIdAndUUID(goodsUUID, commonId);
        Integer goodsCount = shopCar.getGoodsCount();
        Goods goods = goodsMapper.queryGoodsByUUID(goodsUUID);
        Integer goodsCount1 = goods.getGoodsCount();
        if (countUpper) {
            goodsCount++;
        } else {
            goodsCount--;
        }
        if (goodsCount <= 0) {
            shopCarMapper.updateStatus(goodsUUID, commonId, "-1");
            goodsCount = 0;
        } else if (goodsCount > goodsCount1) {
            goodsCount = goodsCount1;
        }
        Boolean flag = shopCarMapper.updateCount(goodsUUID, commonId, goodsCount);
        return flag;
    }

    @Override
    public Boolean updateStatus(String goodsUUID, String commonId, String status) {
        ShopCar shopCar = shopCarMapper.queryGoodsByCommonIdAndUUID(goodsUUID, commonId);
        String status1 = shopCar.getStatus();
        //如果用户将已购买的物品重新添加到购物车中，那么就不是更改购物车中商品状态了，而是重新向表中插入一个新的数据
        //此时商品的数量与原来的商品数量相同。
        if (status1.equals("1")){
            ShopCar shopCar1 = new ShopCar();
            shopCar1.setCommonId(commonId);
            shopCar1.setGoodsUUID(goodsUUID);
            shopCar1.setGoodsCount(shopCar.getGoodsCount());
            shopCarMapper.insertGoodsToCar(shopCar1);
            return true;
        }
        Boolean aBoolean = shopCarMapper.updateStatus(goodsUUID, commonId, status);
        return aBoolean;
    }

    @Override
    public ShopCar getGoodsByCommonIdAndUUID(String goodsUUID, String commonId) {
        ShopCar shopCar = shopCarMapper.queryGoodsByCommonIdAndUUID(goodsUUID, commonId);
        return shopCar;
    }

    @Override
    public List<ShopCar> getGoodsListByCommonId(String commonId, String status) {
        return shopCarMapper.queryGoodsListByCommonId(commonId, status);
    }
}
