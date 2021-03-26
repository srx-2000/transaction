package com.srx.transaction.Serivce;

import com.srx.transaction.Entities.BusinessUser;
import com.srx.transaction.Entities.CommonUser;
import com.srx.transaction.Entities.User;

import java.util.List;

public interface UserService extends BaseService{

    /**
     * 登录
     *
     * @param username
     * @param password
     * @return
     */
    User login(String username, String password);


    /**
     * 插入一个管理员用户
     * @param user
     * @return
     */
    Boolean registerAdminUser(User user);

    /**
     * 插入一个普通用户
     * @param commonUser
     * @param user
     * @return
     */
    Boolean registerCommonUser(User user,CommonUser commonUser);

    /**
     * 插入一个商家用户
     * @param businessUser
     * @param user
     * @param shopUUID
     * @return
     */
    Boolean registerBusinessUser(User user,BusinessUser businessUser,String shopUUID);

    /**
     * 查询用户名是否存在
     * @param username
     * @return
     */
    Boolean isUsernameExist(String username);

    /**
     * 查询邮箱是否存在
     * @param email
     * @return
     */
    Boolean isEmailExist(String email);

    /**
     * 查询用户权限
     * @param user
     * @return
     */
    String getAuthority(User user);

    /**
     * 根据userId查询普通用户的所有信息
     * @param userId
     * @return
     */
    CommonUser getCommonUserById(String userId);

    /**
     * 根据userId查询商家信息
     * @param userId
     * @return
     */
    BusinessUser getBusinessUserById(String userId);

    /**
     * 通过给出的起始以及限制个数获取普通用户列表
     * @return
     */
    List<CommonUser> getCommonUserList(String status,Integer currentPage, Integer pageSize);

    /**
     * 通过给出的起始以及限制个数获取普通用户列表
     * @return
     */
    List<BusinessUser> getBusinessUserList(String status,Integer currentPage, Integer pageSize);

    /**
     * 修改密码
     * @param email
     * @param oldPassword
     * @param newPassword
     * @return
     */
    Boolean updatePassword(String email,String oldPassword,String newPassword);


    /**
     * 更新用户状态，为管理员用户的接口
     * @param username
     * @return
     */
    Boolean updateUserStatus(String username,String status);

    /**
     * 更新用户地址
     * @param username
     * @param city
     * @return
     */
    Boolean updateUserCity(String username, String city);

//    /**
//     * 将用户状态更新为异常
//     * @param username
//     * @return
//     */
//    Boolean updateUserStatusToAbnormal(String username);

}
