package com.srx.transaction.Serivce.Impl;

import com.srx.transaction.Entities.Goods;
import com.srx.transaction.Entities.GoodsPicture;
import com.srx.transaction.Mapper.GoodsMapper;
import com.srx.transaction.Serivce.BaseService;
import com.srx.transaction.Serivce.GoodsService;
import com.srx.transaction.Util.PaginationUtil;
import jdk.nashorn.internal.ir.CallNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GoodsServiceImplement implements GoodsService, BaseService {

    @Autowired
    private GoodsMapper goodsMapper;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Boolean insertGoods(Goods goods, List<GoodsPicture> pictureList) {
        Integer flag = 0;
        for (GoodsPicture g : pictureList) {
            Boolean aBoolean = goodsMapper.insertGoodsPicture(g);
            if (aBoolean) {
                flag++;
            }
        }
        Boolean aBoolean = goodsMapper.insertGoods(goods);
        return (flag == pictureList.size()) && aBoolean;
    }

    /**
     * 与下面的updateGoodsCount方法一样，都需要验证goodsUUID的存在性，如果不存在需要给出错误提示
     *
     * @param goodsUUID
     * @param status
     * @return
     */
    @Override
    public Boolean updateGoodsStatus(String goodsUUID, String status) {
        Goods goods = goodsMapper.queryGoodsByUUID(goodsUUID);
        Integer goodsCount = goods.getGoodsCount();
        // 商品数量为0的商品不可以将状态设置为在出售
        if (goodsCount == 0 && status == "1") {
            return false;
        }
        Goods finalGoods = new Goods();
        finalGoods.setStatus(status);
        finalGoods.setGoodsUUID(goodsUUID);
        Boolean aBoolean = goodsMapper.updateGoods(goods);
        return aBoolean;
    }

    @Override
    public List<Goods> getGoodsByCondition(Goods goods, Integer currentPage, Integer pageSize) {
        Integer begin = (currentPage - 1) * pageSize;
        List<Goods> goodsList = goodsMapper.queryGoodsListByCondition(goods, begin, pageSize);
        for (Goods g : goodsList) {
            List<GoodsPicture> goodsPictures = goodsMapper.queryGoodsPictureListByUUID(g.getGoodsUUID(), 5);
            g.setPictureList(goodsPictures);
        }
        return goodsList;
    }

    /**
     * 这里最终返回null时，就证明用户输入的购买量大于了该商品的库存量，应给与ERROR_NUM_GOODS错误反馈
     * 同时需要记得在controller层加入对goodsUUID进行验证的步骤，确保输入的UUID是数据库中存在的，如果不存在
     * 应给予ERROR_NOFOUND_GOODS错误反馈
     *
     * @param goodsUUID
     * @param subtractCount
     * @return
     */
    @Override
    public Boolean updateGoodsCount(String goodsUUID, Integer subtractCount) {
        Goods goods = goodsMapper.queryGoodsByUUID(goodsUUID);
        Integer goodsCount = goods.getGoodsCount();
        if (goodsCount > subtractCount && subtractCount > 0) {
            int finalCount = goodsCount - subtractCount;
            Goods finalGoods = new Goods();
            finalGoods.setGoodsUUID(goodsUUID);
            if (finalCount == 0) {
                finalGoods.setGoodsCount(finalCount);
                finalGoods.setStatus("-1");
                Boolean aBoolean = goodsMapper.updateGoods(finalGoods);
                return aBoolean;
            } else {
                finalGoods.setGoodsCount(finalCount);
                Boolean aBoolean = goodsMapper.updateGoods(finalGoods);
                return aBoolean;
            }
        }
        return null;
    }

    @Override
    public Goods getGoodsByUUID(String goodsUUID) {
        return null;
    }

    @Override
    public Boolean updateGoodsPraiseCount(String goodsUUID, Integer count) {
        return null;
    }

    @Override
    public Boolean updateGoodsPraiseRate(String goodsUUID, Double rate) {
        return null;
    }

    @Override
    public Boolean updateGoodsDealCount(String goodsUUID, Integer count) {
        return null;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Boolean updateGoodsInfo(Goods goods, List<GoodsPicture> pictureList) {
        Integer flag = 0;
        for (GoodsPicture g : pictureList) {
            Boolean aBoolean = goodsMapper.insertGoodsPicture(g);
            if (aBoolean) {
                flag++;
            }
        }
        Boolean aBoolean = goodsMapper.updateGoods(goods);
        return (flag == pictureList.size()) && aBoolean;
    }
}
