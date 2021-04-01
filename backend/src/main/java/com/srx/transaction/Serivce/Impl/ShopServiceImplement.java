package com.srx.transaction.Serivce.Impl;

import com.srx.transaction.Entities.Shop;
import com.srx.transaction.Mapper.ShopMapper;
import com.srx.transaction.Serivce.BaseService;
import com.srx.transaction.Serivce.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShopServiceImplement implements ShopService{

    @Autowired
    private ShopMapper shopMapper;

    @Override
    public Boolean insertShop(Shop shop) {
        return shopMapper.insertShop(shop);
    }

    @Override
    public Boolean updateShopName(String shopUUID, String shopName) {
        return shopMapper.updateShopName(shopUUID, shopName);
    }

    @Override
    public Boolean updateShopStatus(String shopUUID, String status) {
        return shopMapper.updateShopStatus(shopUUID, status);
    }

    @Override
    public Shop getShopByUserId(String businessId) {
        return shopMapper.queryShopByUserId(businessId);
    }

    @Override
    public Boolean updateShopLevel(String shopUUID, Boolean upper) {
        Shop shop = shopMapper.queryShopByUUID(shopUUID);
        Integer nowLevel = shop.getLevel();
        Integer newLevel;
        if (upper) {
            newLevel = nowLevel + 1;
            if (newLevel > 5) {
                return false;
            }
        } else {
            newLevel = nowLevel - 1;
            if (newLevel < 1) {
                return false;
            }
        }
        Boolean aBoolean = shopMapper.updateShopLevel(shopUUID, newLevel);
        return aBoolean;
    }

    @Override
    public List<Shop> getShopList(Integer currentPage, Integer pageSize) {
        int begin = (currentPage - 1) * pageSize;
        List<Shop> shops = shopMapper.queryShopList(begin, pageSize);
        return shops;
    }

    @Override
    public Shop getShopByUUID(String shopUUID) {
        return shopMapper.queryShopByUUID(shopUUID);
    }

    @Override
    public Boolean updateShopDealCount(String shopUUID, Integer addDealCount) {
        Shop shop = shopMapper.queryShopByUUID(shopUUID);
        Integer nowDealCount = shop.getDealCount();
        Integer newDealCount = nowDealCount + addDealCount;
        Boolean aBoolean = shopMapper.updateShopDealCount(shopUUID, newDealCount);
        return aBoolean;
    }

    @Override
    public Boolean updateShopPraiseCount(String shopUUID, Integer praiseCount) {
        Shop shop = shopMapper.queryShopByUUID(shopUUID);
        Integer nowPraiseCount = shop.getPraiseCount();
        Integer newPraiseCount = nowPraiseCount++;
        return shopMapper.updateShopPraiseCount(shopUUID, newPraiseCount);
    }

    @Override
    public Boolean updateShopPraiseRate(String shopUUID) {
        Shop shop = shopMapper.queryShopByUUID(shopUUID);
        Integer nowDealCount = shop.getDealCount();
        Integer nowPraiseCount = shop.getPraiseCount();
        double rate;
        if (nowDealCount == 0) {
            rate = 0.0;
        } else {
            rate = Double.valueOf(nowPraiseCount * 100 / nowDealCount);
        }
        Boolean aBoolean = shopMapper.updateShopPraiseRate(shopUUID, rate);
        return aBoolean;
    }

    @Override
    public List<Shop> getShopListByCondition(Shop shop, Integer currentPage, Integer pageSize) {
        int begin = (currentPage - 1) * pageSize;
        return shopMapper.queryShopListByCondition(shop, begin, pageSize);
    }
}
