package com.srx.transaction.Entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@Component
@AllArgsConstructor
@NoArgsConstructor
public class MiddleWallet {
//    deal_uuid  sum_money status
    private String dealUUID;
    private double sumMoney;
    private String status;
}
