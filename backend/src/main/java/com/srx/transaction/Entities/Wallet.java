package com.srx.transaction.Entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
public class Wallet {
//    user_id  integral  sum_money  current_money
    private String userId;
    private Integer  integral;
    private Integer sumMoney;
    private Integer currentMoney;

}
