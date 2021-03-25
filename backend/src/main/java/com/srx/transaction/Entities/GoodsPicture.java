package com.srx.transaction.Entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class GoodsPicture {
//    goods_uuid  picture_path  join_time
    private String goodsUUID;
    private String picturePath;
    private String joinTime;
}
