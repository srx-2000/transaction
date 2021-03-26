package com.srx.transaction.Entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ShopCar {
//    goods_uuid  common_id  goods_count  status
    private String goodsUUID;
    private String commonId;
    private Integer goodsCount;
    private String status;
}
