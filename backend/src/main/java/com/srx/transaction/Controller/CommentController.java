package com.srx.transaction.Controller;

import com.srx.transaction.Entities.Comment;
import com.srx.transaction.Entities.DTO.ResultMessage;
import com.srx.transaction.Enum.ResultCode;
import com.srx.transaction.Serivce.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.srx.transaction.Enum.ResultCode.DATA_RETURN_SUCCESS;

@RestController
@RequestMapping("/goods/comment")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @PostMapping("/insertComment")
    public ResultMessage insertComment(Comment comment) {
        Boolean aBoolean = commentService.insertComment(comment);
        if (aBoolean) {
            return new ResultMessage(ResultCode.INSERT_COMMENT_SUCCESS);
        } else
            return new ResultMessage(ResultCode.INSERT_COMMENT_FAIL);
    }

    @GetMapping("/getCommentListByGoodsUUID")
    public ResultMessage getCommentListByGoodsUUID(@RequestParam String goodsUUID){
        List<Comment> commentListByUUID = commentService.getCommentListByUUID(goodsUUID);
        return new ResultMessage(DATA_RETURN_SUCCESS,commentListByUUID);
    }
}
