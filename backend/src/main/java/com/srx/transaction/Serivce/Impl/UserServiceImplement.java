package com.srx.transaction.Serivce.Impl;

import com.srx.transaction.Entities.BusinessUser;
import com.srx.transaction.Entities.CommonUser;
import com.srx.transaction.Entities.Shop;
import com.srx.transaction.Entities.User;
import com.srx.transaction.Mapper.ShopMapper;
import com.srx.transaction.Mapper.UserMapper;
import com.srx.transaction.Serivce.BaseService;
import com.srx.transaction.Serivce.UserService;
import com.srx.transaction.Util.CodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImplement implements UserService, BaseService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ShopMapper shopMapper;

    @Override
    public User login(String username, String password) {
        if (username != null && password != null)
            return userMapper.login(username, CodeUtil.get_MD5_code(password));
        return null;
    }

    @Override
    public Boolean registerAdminUser(User user) {
        if (user.getStatus().equals("2")) {
            user.setPassword(CodeUtil.get_MD5_code(user.getPassword()));
            Boolean flag = userMapper.insertUser(user);
            return flag;
        } else return false;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Boolean registerCommonUser(User user, CommonUser commonUser) {
        if (user.getRole().equals("0")) {
            user.setPassword(CodeUtil.get_MD5_code(user.getPassword()));
            Boolean flag = userMapper.insertUser(user);
            commonUser.setCommonUserId(user.getUserId());
            Boolean flag1 = userMapper.insertCommonUser(commonUser);
            return flag && flag1;
        } else return false;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Boolean registerBusinessUser(User user, BusinessUser businessUser, String shopUUID) {
        if (user.getRole().equals("1")) {
            user.setPassword(CodeUtil.get_MD5_code(user.getPassword()));
            Boolean flag = userMapper.insertUser(user);
            businessUser.setBusinessUserId(user.getUserId());
            Boolean flag1 = userMapper.insertBusinessUser(businessUser);
            String username = user.getUsername();
            String id = userMapper.queryUserIdByUsername(username);
            Shop shop = new Shop();
            shop.setShopName(businessUser.getName() + "的小店");
            shop.setBusinessId(id);
            shop.setShopUUID(shopUUID);
            shop.setBusinessId(businessUser.getBusinessUserId());
            Boolean flag2 = shopMapper.insertShop(shop);
            return flag && flag1 && flag2;
        } else return false;

    }

    @Override
    public Boolean isUsernameExist(String username) {
        String s = userMapper.queryUsername(username);
        if (!s.equals("0"))
            return true;
        else
            return false;
    }

    @Override
    public Boolean isEmailExist(String email) {
        String s = userMapper.queryEmail(email);
        if (!email.equals("0")) {
            return true;
        }
        return false;
    }

    @Override
    public String getAuthority(User user) {
        if (user != null) {
            String username = user.getUsername();
            if (username != null) {
                String s = userMapper.queryUserRole(user);
                return s;
            }
        }
        return null;
    }

    @Override
    public CommonUser getCommonUserById(String userId) {
        if (!userId.equals("0") && userId != null) {
            CommonUser user = userMapper.queryCommonUserById(userId);
            return user;
        }
        return null;
    }

    @Override
    public BusinessUser getBusinessUserById(String userId) {
        if (!userId.equals("0") && userId != null) {
            BusinessUser user = userMapper.queryBusinessUserById(userId);
            return user;
        }
        return null;
    }

    @Override
    public List<CommonUser> getCommonUserList(Integer currentPage, Integer pageSize, String status) {
        Integer begin = (currentPage - 1) * pageSize;
        List<CommonUser> commonUsers = userMapper.queryCommonUserList(begin, pageSize, status);
        return commonUsers;
    }

    @Override
    public List<BusinessUser> getBusinessUserList(Integer currentPage, Integer pageSize, String status) {
        Integer begin = (currentPage - 1) * pageSize;
        List<BusinessUser> businessUsers = userMapper.queryBusinessUserList(begin, pageSize, status);
        return businessUsers;
    }

    @Override
    public Boolean updatePassword(String email, String oldPassword, String newPassword) {
        User login = userMapper.login(email, CodeUtil.get_MD5_code(oldPassword));
        if (login != null) {
            Boolean flag = userMapper.updatePassword(email, CodeUtil.get_MD5_code(newPassword));
            return flag;
        }
        return false;
    }

    /**
     * 管理员用户更新将用户状态更新为正常时，如果该用户是商家用户则，在此时为商家创建一个初始店铺
     * 店铺名为商家 ==>姓名+的小店。该名称商家可以自行修改
     *
     * @param username
     * @param status
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Boolean updateUserStatus(String username, String status) {
        User user = new User();
        user.setUsername(username);
        String s = userMapper.queryUserRole(user);
        Boolean flag = userMapper.updateUserStatus(username, status);
        String id = userMapper.queryUserIdByUsername(username);
        if (s.equals("1") && flag && status.equals("0")) {
            Shop shop = shopMapper.queryShopByUserId(id);
            Boolean aBoolean = shopMapper.updateShopStatus(shop.getShopUUID(), "1");
            return aBoolean;
        } else if (s.equals("1") && flag && status.equals("1")) {
            Shop shop = shopMapper.queryShopByUserId(id);
            Boolean aBoolean = shopMapper.updateShopStatus(shop.getShopUUID(), "0");
            return aBoolean;
        }
        return flag;
    }

}
