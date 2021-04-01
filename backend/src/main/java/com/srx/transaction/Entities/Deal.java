package com.srx.transaction.Entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
public class Deal {
//  deal_uuid  goods_uuid  shop_uuid  common_id  deal_time  deal_count assess status
    private String dealUUID;
    private String goodsUUID;
    private String shopUUID;
    private String commonId;
    private String dealTime;
    private String dealCount;
    private String assess;
    private String status;
}
