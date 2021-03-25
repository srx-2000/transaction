package com.srx.transaction.Entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Component;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Component
public class CommonUser extends User {
//    user_id  name    phone      sex  city    account
    private String commonUserId;
    private String name;
    private String phone;
    private String sex;
    private String city;
    private String account;

}
