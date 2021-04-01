package com.srx.transaction.Serivce.Impl;

import com.srx.transaction.Entities.*;
import com.srx.transaction.Mapper.*;
import com.srx.transaction.Serivce.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImplement implements CommentService {
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private GoodsMapper goodsMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private DealMapper dealMapper;
    @Autowired
    private ShopMapper shopMapper;

    @Override
    public Boolean insertComment(Comment comment) {
        List<Deal> deals = dealMapper.queryDealListByGoodsUUID(comment.getGoodsUUID());
        Goods goods = goodsMapper.queryGoodsByUUID(comment.getGoodsUUID());
        CommonUser commonUser = userMapper.queryCommonUserById(comment.getUserId());
        String shopUUID = goods.getShopUUID();
        Shop shop = shopMapper.queryShopByUUID(shopUUID);
        String businessId = shop.getBusinessId();
        Boolean flag = false;
        for (Deal deal : deals) {
            if (deal.getCommonId().equals(comment.getUserId())) {
                flag = true;
            }
        }
        if (goods == null) {
            return false;
        } else if (businessId.equals(comment.getUserId())) {//判定是不是卖家
            String replyId = comment.getReplyId();
            if (replyId != null) {//商家只可以回复买家的评论
                Boolean aBoolean = commentMapper.insertComment(comment);
                return aBoolean;
            }
            return false;
        } else if (commonUser == null) {
            return false;
        } else if (!flag) {//判定用户是否已购买该商品
            return false;
        }
        Boolean aBoolean = commentMapper.insertComment(comment);
        return aBoolean;
    }

    @Override
    public List<Comment> getCommentListByUUID(String goodsUUID) {
        List<Comment> comments = commentMapper.queryCommentListByUUID(goodsUUID);
        return comments;
    }
}
