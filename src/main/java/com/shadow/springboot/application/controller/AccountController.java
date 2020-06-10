package com.shadow.springboot.application.controller;

import com.alibaba.fastjson.JSON;
import com.shadow.springboot.application.domain.bo.User;
import com.shadow.springboot.application.domain.vo.Message;
import com.shadow.springboot.application.service.AccountService;
import com.shadow.springboot.application.service.UserService;
import com.shadow.springboot.application.support.factory.LogTaskFactory;
import com.shadow.springboot.application.support.manager.LogExeManager;
import com.shadow.springboot.application.util.*;
import io.jsonwebtoken.SignatureAlgorithm;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * post新增,get读取,put完整更新,patch部分更新,delete删除
 *
 * @author tomsun28
 * @date 14:40 2018/3/8
 */
@RestController
@RequestMapping("/account")
public class AccountController extends BaseAction {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountController.class);
    private static final String STR_USERNAME = "username";
    private static final String STR_EMAIL = "email";
    private static final String STR_NICKNAME = "nickname";
    private static final String STR_TEL = "tel";
    private static final String STR_ADDR = "addr";
//    private static final String STR_SEX = "sex";
//    private static final String STR_WHERE = "createWhere";


    @Autowired
    private StringRedisTemplate redisTemplate;

    @Resource(name = "accountServiceImpl")
    private AccountService accountService;

    @Resource(name = "userServiceImpl")
    private UserService userService;

    /**
     * description 登录签发 JWT ,这里已经在 passwordFilter 进行了登录认证
     *
     * @param request  1
     * @param response 2
     * @return com.usthe.bootshiro.domain.vo.Message
     */
    @ApiOperation(value = "用户登录", notes = "POST用户登录签发JWT")
    @PostMapping("/login")
    public Message accountLogin(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String> params = RequestResponseUtil.getRequestBodyMap(request);
        String appId = params.get("appId");
        // 根据appId获取其对应所拥有的角色(这里设计为角色对应资源，没有权限对应资源)
        String roles = accountService.loadAccountRole(appId);
        // 时间以秒计算,token有效刷新时间是token有效过期时间的2倍
//        long refreshPeriodTime = 36000L;
        long refreshPeriodTime = JsonWebTokenUtil.PERIOD;

        String jwt = JsonWebTokenUtil.issueJWT(UUID.randomUUID().toString(), appId,
                "token-server", refreshPeriodTime >> 1, roles, null, SignatureAlgorithm.HS512);
        // 将签发的JWT存储到Redis： {JWT-SESSION-{appID} , jwt}
        redisTemplate.opsForValue().set("JWT-SESSION-" + appId, jwt, refreshPeriodTime, TimeUnit.SECONDS);
        User authUser = userService.getByUsername(appId);
        authUser.setPassword(null);
        authUser.setSalt(null);

        LogExeManager.getInstance().executeLogTask(LogTaskFactory.loginLog(authUser.getUsername(), IpUtil.getIpFromRequest(WebUtils.toHttp(request)), (short) 1, "登录成功"));

        return new Message().ok(1003, "issue jwt success").addData("jwt", jwt).addData("user", authUser);
    }

    /**
     * description 用户账号的注册
     *
     * @param request  1
     * @param response 2
     * @return com.usthe.bootshiro.domain.vo.Message
     */
    @ApiOperation(value = "用户注册", notes = "POST用户注册")
    @PostMapping("/register")
    public Message accountRegister(HttpServletRequest request, HttpServletResponse response) {

        Map<String, String> params = RequestResponseUtil.getRequestBodyMap(request);
        User authUser = new User();
        String username = params.get("uid");
        String password = params.get("password");
        String userKey = params.get("userKey");
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            // 必须信息缺一不可,返回注册账号信息缺失
            return new Message().error(1111, "账户信息缺失");
        }
        if (accountService.isAccountExistByUid(username)) {
            // 账户已存在
            return new Message().error(1111, "账户已存在");
        }

        authUser.setUsername(username);

        // 从Redis取出密码传输加密解密秘钥
        String tokenKey = redisTemplate.opsForValue().get("TOKEN_KEY_" + IpUtil.getIpFromRequest(WebUtils.toHttp(request)).toUpperCase() + userKey);
        String realPassword = AesUtil.aesDecode(password, tokenKey);
        String salt = CommonUtil.getRandomString(6);
        // 存储到数据库的密码为 MD5(原密码+盐值)
        authUser.setPassword(Md5Util.md5(realPassword + salt));
        authUser.setSalt(salt);
        authUser.setRegtime(new Date());
        if (!StringUtils.isEmpty(params.get(STR_USERNAME))) {
            authUser.setUsername(params.get(STR_USERNAME));
        }
        if (!StringUtils.isEmpty(params.get(STR_NICKNAME))) {
            authUser.setNickname(params.get(STR_NICKNAME));
        }
        if (!StringUtils.isEmpty(params.get(STR_ADDR))) {
            authUser.setAddr(params.get(STR_ADDR));
        }
        if (!StringUtils.isEmpty(params.get(STR_TEL))) {
            authUser.setTel(params.get(STR_TEL));
        }
        if (!StringUtils.isEmpty(params.get(STR_EMAIL))) {
            authUser.setEmail(params.get(STR_EMAIL));
        }
        authUser.setDisabled(false);

        if (accountService.registerAccount(authUser)) {
            LogExeManager.getInstance().executeLogTask(LogTaskFactory.registerLog(username, IpUtil.getIpFromRequest(WebUtils.toHttp(request)), (short) 1, "注册成功"));
            return new Message().ok(2002, "注册成功");
        } else {
            LogExeManager.getInstance().executeLogTask(LogTaskFactory.registerLog(username, IpUtil.getIpFromRequest(WebUtils.toHttp(request)), (short) 0, "注册失败"));
            return new Message().ok(1111, "注册失败");
        }
    }

}
