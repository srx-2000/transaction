package com.srx.transaction.Entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Component;

@Data
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Component
public class BusinessUser extends User {
//user_id  name    phone      sex  identification_front  identification_back  account  license
    private String businessUserId;
    private String name;
    private String phone;
    private String sex;
    private String identificationFront;
    private String identificationBack;
    private String account;
    private String license;
}
