package com.srx.transaction.Enum;

public enum ResultCode {
    //成功码
    LOGIN_SUCCESS("登录成功", 204),
    FILE_UPLOAD_SUCCESS("文件上传成功", 205),
    DATA_RETURN_SUCCESS("数据返回成功", 206),
    UPDATE_PASSWORD_SUCCESS("密码更改成功", 207),
    REGISTER_SUCCESS("用户注册成功", 208),
    VALIDATION_SUCCESS("验证码正确", 209),
    UPDATE_USER_STATUS_SUCCESS("用户状态更新成功", 210),
    UPDATE_SHOP_LEVEL_SUCCESS("更新店铺等级成功", 211),
    UPDATE_SHOP_STATUS_SUCCESS("更新店铺状态成功", 212),
    UPDATE_SHOP_NAME_SUCCESS("更改店铺名称成功", 213),
    UPDATE_USER_ADDRESS_SUCCESS("更改用户地址成功", 214),
    UPDATE_GOODS_INFO_SUCCESS("更改商品信息成功", 216),
    UPDATE_GOODS_STATUS_SUCCESS("更改商品状态成功", 217),
    GOODS_INSERT_SUCCESS("商品信息上传成功", 215),
    UPDATE_SHOP_INFO_SUCCESS("更改店铺信息成功", 218),
    INSERT_DEAL_SUCCESS("订单提交成功", 219),
    RECEIPT_DEAL_SUCCESS("订单签收成功", 220),
    COMPLETE_DEAL_SUCCESS("订单完成成功", 221),
    RETURN_DEAL_SUCCESS("订单退货成功", 222),
    UPDATE_DEAL_STATUS_SUCCESS("订单状态更新成功", 223),
    INSERT_SHOPCAR_SUCCESS("插入一个商品进入购物车", 224),
    UPDATE_SHOPCAR_COUNT_SUCCESS("购物车商品数量更新成功", 225),
    UPDATE_SHOPCAR_STATUS_SUCCESS("购物车商品状态更新成功", 226),
    ADD_MONEY_SUCCESS("账户金额添加成功", 227),
    SUB_MONEY_SUCCESS("账户金额扣除成功", 228),
    INSERT_COMMENT_SUCCESS("评论成功", 229),




    USERNAME_EXIT("该用户名已被占用", 437),
    USERNAME_NOT_EXIT("该用户名可以使用", 214),
    EMAIL_EXIT("该邮箱已注册", 438),
    EMAIL_NOT_EXIT("该邮箱可以使用", 215),


    // 失败码
    ERROR_NOLOGIN("请先登录", 423),
    ERROR_NOFOUND_USER("密码或用户名错误，或账号正在审批中", 424),
    ERROR_ROLE("请输入正确的角色信息", 441),
    FAIL_VALIDATION("验证码错误", 440),
    ERROR_NULL("请确保正确填写了表单", 425),
    ERROR_PARAM("传入参数有误", 427),
    ERROR_NO_MORE_DATA("已经没有数据了", 428),
    ERROR_NO_DATA("没有查询到数据", 429),
    ERROR_INDEX("索引错误，请输入正确的索引", 478),
    ERROR_NETWORK("网络错误...请稍后重试", 479),
    ERROR_NOFOUND_GOODS("商品未找到", 480),
    ERROR_GOODS_STATUS("商品状态异常", 483),
    ERROR_NOFOUND_SHOP("店铺未找到", 482),
    ERROR_UPDATE_SHOP_STATUS("店铺现状态与更改状态相同", 464),
    ERROR_UPDATE_SHOP_NAME("店铺名称与更改名称相同", 465),
    // 这里需要前端对商品的数量进行约束，使用户不可以输入大于库存总数量的商品数量
    // 而此时该错误就可以处理，商品已售空或商品已下架的异常情况
    // 此处的商品已售空是的触发原因是：
    // 用户将商品加入了购物车，但没有下单，而在他下单时该商品已售空，那么此时就会出现商品售空的异常
    ERROR_NUM_GOODS("商品已售空，或已下架，", 481),
    UPDATE_PASSWORD_FAIL("更改密码失败", 435),
    REGISTER_FAIL("注册失败", 436),
    UPDATE_USER_STATUS_FAIL("用户状态更新失败", 456),
    UPDATE_SHOP_LEVEL_FAIL("更新店铺等级失败", 455),
    UPDATE_SHOP_STATUS_FAIL("更新店铺状态失败", 457),
    UPDATE_SHOP_NAME_FAIL("更改店铺名称失败", 458),
    UPDATE_USER_ADDRESS_FAIL("更改用户地址失败", 459),
    UPDATE_GOODS_INFO_FAIL("更改商品信息失败", 461),
    UPDATE_GOODS_STATUS_FAIL("更改商品状态失败", 462),
    GOODS_INSERT_FAIL("商品信息上传失败，请重试", 460),
    UPDATE_SHOP_INFO_FAIL("更改店铺信息失败", 463),
    INSERT_DEAL_FAIL("订单提交失败", 467),
    RECEIPT_DEAL_FAIL("订单签收失败", 468),
    COMPLETE_DEAL_FAIL("订单完成失败", 469),
    RETURN_DEAL_FAIL("订单退货失败", 470),
    UPDATE_DEAL_STATUS_FAIL("订单状态更新失败", 471),
    INSERT_SHOPCAR_FAIL("插入一个商品进入购物车失败", 472),
    UPDATE_SHOPCAR_COUNT_FAIL("购物车商品数量更新失败", 473),
    UPDATE_SHOPCAR_STATUS_FAIL("购物车商品状态更新成功", 474),
    ADD_MONEY_FAIL("账户金额添加失败", 475),
    SUB_MONEY_FAIL("账户金额扣除失败", 476),
    INSERT_COMMENT_FAIL("评论失败", 477),


    ;
    private final String message;
    private final Integer code;

    ResultCode(String message, Integer code) {
        this.message = message;
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public Integer getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "ResultCode{" +
                "message='" + message + '\'' +
                ", code=" + code +
                '}';
    }
}
