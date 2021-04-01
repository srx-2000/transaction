package com.srx.transaction.Mapper;

import com.srx.transaction.Entities.Comment;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentMapper {
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
    List<Comment> queryCommentListByUUID(@Param("goodsUUID")String goodsUUID);







}
