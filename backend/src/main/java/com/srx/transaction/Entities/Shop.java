package com.srx.transaction.Entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class Shop {
//    shop_id  shop_uuid  shop_name  business_id   level  praise_rate  praise_count  deal_count  create_time  status
    private String shopId;
    private String shopUUID;
    private String shopName;
    private String businessId;
    private Integer level;
    private Double praiseRate;
    private Integer praiseCount;
    private Integer dealCount;
    private String createTime;
    private String status;
}
