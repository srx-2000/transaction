package com.srx.transaction.Mapper;

import com.srx.transaction.Entities.BusinessUser;
import com.srx.transaction.Entities.CommonUser;
import com.srx.transaction.Entities.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Repository
public interface UserMapper {
    /**
     * 登录
     *
     * @param username
     * @param password
     * @return
     */
    User login(@Param("username") String username, @Param("password") String password);

    /**
     * 插入一个用户
     *
     * @param user
     * @return
     */
    Boolean insertUser(User user);

    /**
     * 插入用户的同时如果该用户的权限为普通用户，则在common表中插入一个普通用户
     *
     * @param commonUser
     * @return
     */
    Boolean insertCommonUser(CommonUser commonUser);

    /**
     * 与插入普通用户同理
     *
     * @param businessUser
     * @return
     */
    Boolean insertBusinessUser(BusinessUser businessUser);

    /**
     * 查询用户名是否存在
     *
     * @param username
     * @return
     */
    String queryUsername(@Param("username") String username);

    /**
     * 查询邮箱是否存在
     *
     * @param email
     * @return
     */
    String queryEmail(@Param("email") String email);

    /**
     * 查询用户权限
     *
     * @param userId
     * @return
     */
    String queryUserRole(@Param("userId") String userId);

    /**
     * 根据userId查询普通用户的所有信息
     *
     * @param userId
     * @return
     */
    CommonUser queryCommonUserById(@Param("commonUserId") String userId);

    /**
     * 根据userId查询商家信息
     *
     * @param userId
     * @return
     */
    BusinessUser queryBusinessUserById(@Param("businessUserId") String userId);

    /**
     * 通过给出的起始以及限制个数获取普通用户列表
     *
     * @return
     */
    List<CommonUser> queryCommonUserList(@Param("begin") Integer begin, @Param("pageSize") Integer pageSize, @Param("status") String status);

    /**
     * 通过给出的起始以及限制个数获取普通用户列表
     *
     * @return
     */
    List<BusinessUser> queryBusinessUserList(@Param("begin") Integer begin, @Param("pageSize") Integer pageSize, @Param("status") String status);

    /**
     * 通过用户名查询userId
     *
     * @param username
     * @return
     */
    String queryUserIdByUsername(@Param("username") String username);

    /**
     * 修改密码
     *
     * @param email
     * @param password
     * @return
     */
    Boolean updatePassword(@Param("email") String email, @Param("password") String password);

    /**
     * @param username
     * @return
     */
    Boolean updateUserStatus(@Param("username") String username, @Param("status") String status);

    /**
     * 允许用户修改城市信息，即收货地址
     *
     * @param city
     * @param userId
     * @return
     */
    Boolean updateUserCity(@Param("userId") String userId, @Param("city") String city);

    /**
     * 查询所有用户的数量
     *
     * @return
     */
    Integer queryUserCount();

    /**
     * 查询普通用户的数量
     *
     * @return
     */
    Integer queryCommonUserCount();

    /**
     * 查询商家用户的数量
     *
     * @return
     */
    Integer queryBusinessUserCount();


}
