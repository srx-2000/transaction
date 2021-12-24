package com.srx.transaction.Controller;

import com.google.gson.Gson;
import com.srx.transaction.Entities.BusinessUser;
import com.srx.transaction.Entities.CommonUser;
import com.srx.transaction.Entities.DTO.ResultMessage;
import com.srx.transaction.Entities.User;
import com.srx.transaction.Entities.Wallet;
import com.srx.transaction.Serivce.Impl.UserServiceImplement;
import com.srx.transaction.Serivce.UserService;
import com.srx.transaction.Serivce.WalletService;
import com.srx.transaction.Util.CodeUtil;
import com.srx.transaction.Util.PaginationUtil;
import com.srx.transaction.Util.PictureUtil;
import com.srx.transaction.Util.ValidationCodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.srx.transaction.Enum.ResultCode.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private WalletService walletService;


    @PostMapping("/login")
    public ResultMessage login(@RequestBody User user, HttpServletRequest session) {
        if (user != null) {
            String role = user.getRole();
            String username = user.getUsername();
            String password = user.getPassword();
            if (username != null && password != null) {
                User login = userService.login(username, password);
                if (login != null) {
                    session.getSession().setAttribute("user", login);
                    if (login.getRole().equals(role)&& login.getRole().equals("0")) {
                        CommonUser commonUserById = userService.getCommonUserById(login.getUserId());
                        return new ResultMessage(LOGIN_SUCCESS, commonUserById);
                    } else if (login.getRole().equals(role) && login.getRole().equals("1")) {
                        BusinessUser businessUser = userService.getBusinessUserById(login.getUserId());
                        return new ResultMessage(LOGIN_SUCCESS, businessUser);
                    } else if (login.getRole().equals(role) && login.getRole().equals("2")) {
                        return new ResultMessage(LOGIN_SUCCESS, login);
                    } else
                        return new ResultMessage(ERROR_ROLE);
                }
                return new ResultMessage(ERROR_NOFOUND_USER);
            }
            return new ResultMessage(ERROR_NOLOGIN);
        }
        return new ResultMessage(ERROR_NOLOGIN);
    }

    /**
     * 图片验证码测试类
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @GetMapping("/getCode")
    public void createImg(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            response.setContentType("image/jpeg");//设置相应类型,告诉浏览器输出的内容为图片
            response.setHeader("Pragma", "No-cache");//设置响应头信息，告诉浏览器不要缓存此内容
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expire", 0);
            ValidationCodeUtil randomValidateCode = new ValidationCodeUtil();
            randomValidateCode.getRandcode(request, response);//    输出验证码图片
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/validationCode")
    public ResultMessage validationCode(HttpServletRequest request, @RequestParam String code) {
        String key = CodeUtil.codeUpper((String) request.getSession().getAttribute("RANDOMREDISKEY"));
        System.out.println(code);
        System.out.println(key);
        if (key.equals(code)) {
            return new ResultMessage(VALIDATION_SUCCESS);
        } else {
            return new ResultMessage(FAIL_VALIDATION);
        }
    }


    @PostMapping("/registerCommon")
    public ResultMessage registerCommon(@RequestPart("user") String user, @RequestPart("commonUser") String commonuser) {
        Gson gson = new Gson();
        User baseUser = gson.fromJson(user, User.class);
        CommonUser commonUser = gson.fromJson(commonuser, CommonUser.class);
        if (commonUser != null && baseUser != null) {
            User user1 = new User();
            user1.setUsername(baseUser.getUsername());
            user1.setPassword(CodeUtil.get_MD5_code(baseUser.getPassword()));
            user1.setRole(baseUser.getRole());
            user1.setEmail(baseUser.getEmail());
            Boolean register = userService.registerCommonUser(baseUser, commonUser);
            if (register)
                return new ResultMessage(REGISTER_SUCCESS, register);
            else return new ResultMessage(REGISTER_FAIL, register);
        } else return new ResultMessage(REGISTER_FAIL, false);
    }

    @PostMapping("/registerBusiness")
    public ResultMessage registerBusiness(@RequestPart("user") String user, @RequestPart("businessUser") String businessuser,
                                          @RequestPart("licence") MultipartFile license, @RequestPart("identityFront") MultipartFile identityFront,
                                          @RequestPart("identityBack") MultipartFile identityBack) throws Exception {
        Gson gson = new Gson();
        User baseUser = gson.fromJson(user, User.class);
        BusinessUser businessUser = gson.fromJson(businessuser, BusinessUser.class);
        String shopUUID = CodeUtil.get_uuid();
        String licenseUrl = "";
        String identityFrontUrl = "";
        String identityBackUrl = "";
        Boolean flag = PictureUtil.uploadPicture(license, shopUUID, null, PictureUtil.LICENSE);
        Boolean flag1 = PictureUtil.uploadPicture(identityFront, shopUUID, null, PictureUtil.IDENTITY_FRONT);
        Boolean flag2 = PictureUtil.uploadPicture(identityBack, shopUUID, null, PictureUtil.IDENTITY_BACK);
        if (flag) {
            licenseUrl = PictureUtil.getShopUrl(shopUUID, PictureUtil.LICENSE);
            businessUser.setLicense(licenseUrl);
        }
        if (flag1) {
            identityFrontUrl = PictureUtil.getShopUrl(shopUUID, PictureUtil.IDENTITY_FRONT);
            businessUser.setIdentificationFront(identityFrontUrl);
        }
        if (flag2) {
            identityBackUrl = PictureUtil.getShopUrl(shopUUID, PictureUtil.IDENTITY_BACK);
            businessUser.setIdentificationBack(identityBackUrl);
        }
        if (businessUser != null && baseUser != null) {
            User user1 = new User();
            user1.setUsername(baseUser.getUsername());
            user1.setPassword(CodeUtil.get_MD5_code(baseUser.getPassword()));
            user1.setRole(baseUser.getRole());
            user1.setEmail(baseUser.getEmail());
            Boolean register = userService.registerBusinessUser(baseUser, businessUser, shopUUID);
            if (register)
                return new ResultMessage(REGISTER_SUCCESS, register);
            else return new ResultMessage(REGISTER_FAIL, register);
        } else return new ResultMessage(REGISTER_FAIL, false);
    }

    @GetMapping("/getUserById")
    public ResultMessage getUserById(@RequestParam String userId) {
        CommonUser commonUserById = userService.getCommonUserById(userId);
        BusinessUser businessUser = userService.getBusinessUserById(userId);
        if (commonUserById != null) {
            return new ResultMessage(DATA_RETURN_SUCCESS, commonUserById);
        } else if (businessUser != null) {
            return new ResultMessage(DATA_RETURN_SUCCESS, businessUser);
        } else {
            return new ResultMessage(ERROR_NO_DATA);
        }
    }

    /**
     * @param mode        选择查询的用户角色，1为商家用户，0位普通用户
     * @param currentPage
     * @param pageSize
     * @param status      选择查询用户的状态，0为正常用户，-1为未批准用户，1为异常账号
     * @return
     */
    @GetMapping("/getUserList")
    public ResultMessage getUserList(@RequestParam String mode, @RequestParam Integer currentPage,
                                     @RequestParam Integer pageSize, @RequestParam(required = false) String status) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        if (mode.equals("0")) {
            ResultMessage paginationResult = PaginationUtil.getPaginationResult(currentPage, pageSize, userService, "getCommonUserList", status);
            return paginationResult;
        } else if (mode.equals("1")) {
            ResultMessage paginationResult = PaginationUtil.getPaginationResult(currentPage, pageSize, userService, "getBusinessUserList", status);
            return paginationResult;
        } else
            return new ResultMessage(ERROR_ROLE);
    }

    /**
     * 该接口是给管理员用户批准或，封禁用户账号时使用
     * 用户注册后，账号的状态为待批准，需要调用此接口批准用户创建账号的申请
     * 用户有违规行为后，也可使用该接口对用户进行封禁
     *
     * @param username
     * @param status
     * @return
     */
    @GetMapping("/changeStatus")
    public ResultMessage changeStatus(@RequestParam String username, @RequestParam String status) {
        Boolean flag = userService.updateUserStatus(username, status);
        if (flag) {
            return new ResultMessage(UPDATE_USER_STATUS_SUCCESS);
        }
        return new ResultMessage(UPDATE_USER_STATUS_FAIL);
    }

    @PostMapping("/updatePassword")
    public ResultMessage updatePassword(@RequestParam String email, @RequestParam String oldPassword,
                                        @RequestParam String newPassword) {
        Boolean flag = userService.updatePassword(email, oldPassword, newPassword);
        if (flag) {
            return new ResultMessage(UPDATE_PASSWORD_SUCCESS);
        }
        return new ResultMessage(UPDATE_PASSWORD_FAIL);
    }

    @GetMapping("/updateAddress")
    public ResultMessage updateCity(@RequestParam String username, @RequestParam String address) {
        Boolean aBoolean = userService.updateUserCity(username, address);
        if (aBoolean) {
            return new ResultMessage(UPDATE_USER_ADDRESS_SUCCESS);
        } else
            return new ResultMessage(UPDATE_USER_ADDRESS_FAIL);
    }

    @GetMapping("/isUsernameExist")
    public ResultMessage isUsernameExist(@RequestParam String username) {
        Boolean usernameExist = userService.isUsernameExist(username);
        if (usernameExist)
            return new ResultMessage(USERNAME_EXIT);
        else
            return new ResultMessage(USERNAME_NOT_EXIT);
    }

    @GetMapping("/isEmailExist")
    public ResultMessage isEmailExist(@RequestParam String email) {
        Boolean emailExist = userService.isEmailExist(email);
        if (emailExist)
            return new ResultMessage(EMAIL_EXIT);
        else
            return new ResultMessage(EMAIL_NOT_EXIT);
    }

    @GetMapping("/addMoney")
    public ResultMessage addMoney(@RequestParam String username, @RequestParam Double addMoney) {
        String userId = userService.getUserIdByUsername(username);
        Boolean aBoolean = walletService.addMoney(userId, addMoney);
        if (aBoolean)
            return new ResultMessage(ADD_MONEY_SUCCESS);
        return new ResultMessage(ADD_MONEY_FAIL);
    }

    @GetMapping("/subMoney")
    public ResultMessage subMoney(@RequestParam String username, @RequestParam Integer subMoney) {
        String userId = userService.getUserIdByUsername(username);
        Boolean aBoolean = walletService.subMoney(userId, subMoney);
        if (aBoolean)
            return new ResultMessage(SUB_MONEY_SUCCESS);
        return new ResultMessage(SUB_MONEY_FAIL);
    }

    @GetMapping("/getWalletById")
    public ResultMessage getWalletById(@RequestParam String userId) {
        String authority = userService.getAuthority(userId);
        if (authority == null) {
            return new ResultMessage(ERROR_NO_DATA);
        } else if (authority.equals("2")) {
            return new ResultMessage(WALLET_NO_FOUND);
        }
        Wallet wallet = walletService.getWallet(userId);
        if (wallet != null)
            return new ResultMessage(DATA_RETURN_SUCCESS, wallet);
        else
            return new ResultMessage(ERROR_NO_DATA);
    }


    @GetMapping("/getUserCount")
    public ResultMessage getUserCount(){
        Integer integer = userService.queryUserCount();
        Map<String,Integer> countMap=new HashMap<>();
        countMap.put("userCount",integer);
        return new ResultMessage(DATA_RETURN_SUCCESS, countMap);
    }

    @GetMapping("/getCommonUserCount")
    public ResultMessage getCommonUserCount(){
        Integer integer = userService.queryCommonUserCount();
        Map<String,Integer> countMap=new HashMap<>();
        countMap.put("commonUserCount",integer);
        return new ResultMessage(DATA_RETURN_SUCCESS, countMap);
    }

    @GetMapping("/getBusinessUserCount")
    public ResultMessage getBusinessUserCount(){
        Integer integer = userService.queryBusinessUserCount();
        Map<String,Integer> countMap=new HashMap<>();
        countMap.put("businessUserCount",integer);
        return new ResultMessage(DATA_RETURN_SUCCESS, countMap);
    }

}
