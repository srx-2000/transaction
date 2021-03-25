package com.srx.transaction.Entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class User {
    //user_id  username  password  email   status
    private String userId;
    private String username;
    private String password;
    private String email;
    private String role;
    private String status;
}
