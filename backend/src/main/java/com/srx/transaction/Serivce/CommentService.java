package com.srx.transaction.Serivce;

import com.srx.transaction.Entities.Comment;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CommentService extends BaseService {
    /**
     * 插入一条评论
     *
     * @param comment
     * @return
     */
    Boolean insertComment(Comment comment);

    /**
     * 根据goodsUUID查询评论列表
     * @param goodsUUID
     * @return
     */
    List<Comment> getCommentListByUUID(String goodsUUID);

}
