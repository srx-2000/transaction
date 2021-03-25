package com.srx.transaction.Enum;

public enum ResultCode {
    LOGIN_SUCCESS("登录成功", 204),
    FILE_UPLOAD_SUCCESS("文件上传成功", 205),
    DATA_RETURN_SUCCESS("数据返回成功", 206),
    UPDATE_PASSWORD_SUCCESS("密码更改成功", 207),
    REGISTER_SUCCESS("用户注册成功", 208),
    VALIDATION_SUCCESS("验证码正确",209),
    UPDATE_USER_STATUS_SUCCESS("用户状态更新成功",210),
    UPDATE_SHOP_LEVEL_SUCCESS("更新店铺等级成功",211),
    UPDATE_SHOP_STATUS_SUCCESS("更新店铺状态成功",212),
    UPDATE_SHOP_NAME_SUCCESS("更改店铺名称成功",212),


    ENV_EXIT("环境已存在", 434),

    USERNAME_EXIT("该用户名已被占用", 437),
    USERNAME_NOT_EXIT("该用户名可以使用", 214),
    EMAIL_EXIT("该邮箱已注册", 438),
    EMAIL_NOT_EXIT("该邮箱可以使用", 215),


    // 失败码
    ERROR_NOLOGIN("请先登录", 423),
    ERROR_NOFOUND_USER("密码或用户名错误，或账号正在审批中", 424),
    ERROR_ROLE("请输入正确的角色信息",441),
    FAIL_VALIDATION("验证码错误",440),
    ERROR_NULL("请确保正确填写了表单", 425),
    ERROR_PARAM("传入参数有误", 427),
    ERROR_NO_MORE_DATA("已经没有数据了", 428),
    ERROR_NO_DATA("没有查询到数据", 429),
    ERROR_INDEX("索引错误，请输入正确的索引",478),
    UPDATE_PASSWORD_FAIL("更改密码失败", 435),
    REGISTER_FAIL("注册失败", 436),
    UPDATE_USER_STATUS_FAIL("用户状态更新失败",456),
    UPDATE_SHOP_LEVEL_FAIL("更新店铺等级失败",455),
    UPDATE_SHOP_STATUS_FAIL("更新店铺状态失败",457),
    UPDATE_SHOP_NAME_FAIL("更改店铺名称失败",458),
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
