package com.srx.transaction.Entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class Comment {
//    comment_id  goods_uuid  user_id  comment_content  comment_time  reply_id
    private Integer commentId;
    private String goodsUUID;
    private String userId;
    private String commentContent;
    private String commentTime;
    private String replyId;
}
