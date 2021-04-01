package com.srx.transaction.Serivce.Impl;

import com.srx.transaction.Entities.*;
import com.srx.transaction.Mapper.DealMapper;
import com.srx.transaction.Mapper.GoodsMapper;
import com.srx.transaction.Mapper.WalletMapper;
import com.srx.transaction.Serivce.BaseService;
import com.srx.transaction.Serivce.GoodsService;
import com.srx.transaction.Util.PaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GoodsServiceImplement implements GoodsService {

    @Autowired
    private GoodsMapper goodsMapper;
    @Autowired
    private WalletMapper walletMapper;
    @Autowired
    private DealMapper dealMapper;

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
        Boolean aBoolean = goodsMapper.updateGoods(finalGoods);
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
     * 该方法主要作用是给用户购买时减少商品数量的
     * <p>
     * 应在deal数据插入时调用
     * <p>
     * 这里最终返回null时，就证明用户输入的购买量大于了该商品的库存量，应给与ERROR_NUM_GOODS错误反馈
     * 这里返回null的情况还包括商品已售空，或商品已下架
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
        if (goodsCount >= subtractCount && subtractCount > 0) {
            int finalCount = goodsCount - subtractCount;
            Goods finalGoods = new Goods();
            finalGoods.setGoodsUUID(goodsUUID);
            if (finalCount == 0) {
                finalGoods.setGoodsCount(finalCount);
                finalGoods.setStatus("-1");
                Boolean aBoolean = goodsMapper.updateGoods(finalGoods);
                return aBoolean;
            } else if (finalCount < 0 || !goods.getStatus().equals("1")) {// 这里是考虑到有人可能将商品放入了购物车中但是没有提交deal，而当他提交deal时该商品已售罄了，那么就返回null，给出商品已售空，或提交数量大于库存
                return null;
            } else {
                finalGoods.setGoodsCount(finalCount);
                Boolean aBoolean = goodsMapper.updateGoods(finalGoods);
                return aBoolean;
            }
        }
        return null;
    }

    /**
     * 可用于goodsUUID的存在性验证
     *
     * @param goodsUUID
     * @return
     */
    @Override
    public Goods getGoodsByUUID(String goodsUUID) {
        Goods goods = goodsMapper.queryGoodsByUUID(goodsUUID);
        List<GoodsPicture> goodsPictures = goodsMapper.queryGoodsPictureListByUUID(goodsUUID, 5);
        if (goods==null&&goodsPictures.size()==0){
            return null;
        }
        goods.setPictureList(goodsPictures);
        return goods;
    }

    /**
     * 该方法不对外提供接口，仅在程序内部自动调用
     *
     * @param goodsUUID
     * @param addCount
     * @return
     */
    @Override
    public Boolean updateGoodsPraiseCount(String goodsUUID, Integer addCount) {
        Goods goods = goodsMapper.queryGoodsByUUID(goodsUUID);
        Integer goodsDealCount = goods.getGoodsDealCount();
        Integer goodsPraiseCount = goods.getGoodsPraiseCount();
        // 如果传入的是负数，返回false
        if (addCount <= 0) {
            return false;
        }
        // 如果好评数大于了交易量，就返回false
        if ((goodsDealCount + addCount) < goodsPraiseCount + addCount) {
            return false;
        }
        Goods finalGoods = new Goods();
        finalGoods.setGoodsUUID(goodsUUID);
        finalGoods.setGoodsPraiseCount(goodsPraiseCount);
        Boolean aBoolean = goodsMapper.updateGoods(finalGoods);
        return aBoolean;
    }

    /**
     * 需要手动调用对好评率进行更新，也可以使用springboot自带的计时器进行自动更新
     *
     * @param goodsUUID
     * @return
     */
    @Override
    public Boolean updateGoodsPraiseRate(String goodsUUID) {
        Goods goods = goodsMapper.queryGoodsByUUID(goodsUUID);
        Integer goodsPraiseCount = goods.getGoodsPraiseCount();
        Integer goodsDealCount = goods.getGoodsDealCount();
        if (goodsPraiseCount == null || goodsDealCount == null) {
            return false;
        }
        Double praiseRate;
        if (goodsDealCount == 0) {
            praiseRate = 0.0;
        } else {
            praiseRate = Double.valueOf(goodsPraiseCount * 100 / goodsDealCount);
        }
        Goods finalGoods = new Goods();
        finalGoods.setGoodsUUID(goodsUUID);
        finalGoods.setGoodsPraiseRate(praiseRate);
        Boolean aBoolean = goodsMapper.updateGoods(finalGoods);
        return aBoolean;
    }

    /**
     * 与updateGoodsPraiseCount方法同理
     *
     * @param goodsUUID
     * @param addCount
     * @return
     */
    @Override
    public Boolean updateGoodsDealCount(String goodsUUID, Integer addCount) {
        Goods goods = goodsMapper.queryGoodsByUUID(goodsUUID);
        Integer goodsDealCount = goods.getGoodsDealCount();
        int count = goodsDealCount + addCount;
        Goods finalGoods = new Goods();
        finalGoods.setGoodsUUID(goodsUUID);
        finalGoods.setGoodsDealCount(count);
        Boolean aBoolean = goodsMapper.updateGoods(finalGoods);
        return aBoolean;
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

    @Override
    public Boolean insertMiddleWallet(MiddleWallet middleWallet) {
        Deal deal = dealMapper.queryDealByUUID(middleWallet.getDealUUID());
        if (deal == null)
            return false;
        Boolean aBoolean = walletMapper.insertMiddleWallet(middleWallet);
        return aBoolean;
    }
}
