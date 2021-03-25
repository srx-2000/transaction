package com.srx.transaction;

import com.srx.transaction.Entities.CommonUser;
import com.srx.transaction.Entities.DTO.ResultMessage;
import com.srx.transaction.Entities.User;
import com.srx.transaction.Util.PictureUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.srx.transaction.Enum.ResultCode.ERROR_NO_DATA;

@SpringBootTest
class SrxApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    public void test1(){
        User user = new User("1", "srx", "srx62600", "1601684622@qq.com", "0","-1");
        CommonUser commonUser = new CommonUser("1", "石荣兴", "1378322331", "男", "北京", "123");
        List<Object> list=new ArrayList<>();
        list.add(user);
        list.add(commonUser);
        ResultMessage message=new ResultMessage(ERROR_NO_DATA,user,commonUser);
        System.out.println(message.toString());
    }
//    @Test
//    public void test2(){
//        Map<String,Object> map=new HashMap<>();
//        map.put("user",new User());
//        map.put("commonUser",new CommonUser());
//        List<Object> conversion = GsonUtil.Conversion(map, new String[]{"user", "commonUser"}, new Class[]{User.class, CommonUser.class});
//        for (Object o :conversion) {
//            System.out.println(o);
//        }
//    }

    @Test
    public void test2(){
        String goodsUUID="ce79d103-f2ce-4b8d-9478-edc7eab399be";
        String shopUUID="f653b418-8970-47df-9462-e43d1dfb1a78";
        String url1 = PictureUtil.getUrl(shopUUID, goodsUUID, null);
        System.out.println(url1);
    }
}
