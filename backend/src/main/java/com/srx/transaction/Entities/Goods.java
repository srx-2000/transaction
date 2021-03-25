package com.srx.transaction.Entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
public class Goods {
//    goods_id  goods_uuid  shop_uuid  goods_name  goods_price  goods_description  goods_count  goods_praise_count  goods_praise_rate  goods_deal_count  goods_type  goods_discount_price  size  is_bargain  damage_level  status
    private Integer goodsId;
    private String goodsUUID;
    private String shopUUID;
    private String goodsName;
    private String goodsDescription;
    private String size;
    private String goodsType;
    private String isBargain;
    private String status;
    private Double goodsPrice;
    private Integer goodsCount;
    private Integer goodsPraiseCount;
    private Double goodsPraiseRate;
    private Integer goodsDealCount;
    private Integer damageLevel;

}
