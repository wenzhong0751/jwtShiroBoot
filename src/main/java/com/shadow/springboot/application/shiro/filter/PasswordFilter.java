package com.shadow.springboot.application.shiro.filter;

import com.alibaba.fastjson.JSON;
import com.shadow.springboot.application.domain.vo.Message;
import com.shadow.springboot.application.shiro.token.PasswordToken;
import com.shadow.springboot.application.util.CommonUtil;
import com.shadow.springboot.application.util.IpUtil;
import com.shadow.springboot.application.util.RequestResponseUtil;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 基于 用户名密码 的认证过滤器
 *
 * @author tomsun28
 * @date 20:18 2018/2/10
 */
public class PasswordFilter extends AccessControlFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(PasswordFilter.class);

    private StringRedisTemplate redisTemplate;

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {

        Subject subject = getSubject(request, response);
        // 如果其已经登录，再此发送登录请求
        //  拒绝，统一交给 onAccessDenied 处理
        return null != subject && subject.isAuthenticated();
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {

        // 判断若为获取登录注册加密动态秘钥请求
        if (isPasswordTokenGet(request)) {
            //动态生成秘钥，redis存储秘钥供之后秘钥验证使用，设置有效期5秒用完即丢弃
            String tokenKey = CommonUtil.getRandomString(16);
            String userKey = CommonUtil.getRandomString(6);
            try {
                redisTemplate.opsForValue().set("TOKEN_KEY_" + IpUtil.getIpFromRequest(WebUtils.toHttp(request)).toUpperCase() + userKey.toUpperCase(), tokenKey, 5, TimeUnit.SECONDS);
                // 动态秘钥response返回给前端
                Message message = new Message();
                LOGGER.info("tokenGet");
                message.ok(1000, "issued tokenKey success")
                        .addData("tokenKey", tokenKey).addData("userKey", userKey.toUpperCase());
                RequestResponseUtil.responseWrite(JSON.toJSONString(message), response);

            } catch (Exception e) {
                LOGGER.warn("签发动态秘钥失败" + e.getMessage(), e);
                Message message = new Message();
                message.ok(1000, "issued tokenKey fail");
                RequestResponseUtil.responseWrite(JSON.toJSONString(message), response);
            }
            return false;
        }

        // 判断是否是登录请求
        if (isPasswordLoginPost(request)) {

            AuthenticationToken authenticationToken = null;
            try {
                authenticationToken = createPasswordToken(request);
            } catch (Exception e) {
                // response 告知无效请求
                Message message = new Message().error(1111, "error request");
                RequestResponseUtil.responseWrite(JSON.toJSONString(message), response);
                return false;
            }

            Subject subject = getSubject(request, response);
            try {
                subject.login(authenticationToken);
                //登录认证成功,进入请求派发json web token url资源内
                return true;
            } catch (AuthenticationException e) {
                LOGGER.warn(authenticationToken.getPrincipal() + "::" + e.getMessage());
                // 返回response告诉客户端认证失败
                Message message = new Message().error(1002, "login fail");
                RequestResponseUtil.responseWrite(JSON.toJSONString(message), response);
                return false;
            } catch (Exception e) {
                LOGGER.error(authenticationToken.getPrincipal() + "::认证异常::" + e.getMessage(), e);
                // 返回response告诉客户端认证失败
                Message message = new Message().error(1002, "login fail");
                RequestResponseUtil.responseWrite(JSON.toJSONString(message), response);
                return false;
            }
        }
        // 判断是否为注册请求,若是通过过滤链进入controller注册
        if (isAccountRegisterPost(request)) {
            return true;
        }

        // 之后添加对账户的找回等
        // response 告知无效请求
        Message message = new Message().error(1111, "error request");
        RequestResponseUtil.responseWrite(JSON.toJSONString(message), response);
        return false;
    }

    private boolean isPasswordTokenGet(ServletRequest request) {

        String tokenKey = RequestResponseUtil.getParameter(request, "tokenKey");

        return (request instanceof HttpServletRequest)
                && "GET".equals(((HttpServletRequest) request).getMethod().toUpperCase())
                && "get".equals(tokenKey);
    }

    private boolean isPasswordLoginPost(ServletRequest request) {

        Map<String, String> map = RequestResponseUtil.getRequestBodyMap(request);
        String password = map.get("password");
        String timestamp = map.get("timestamp");
        String methodName = map.get("methodName");
        String appId = map.get("appId");
        return (request instanceof HttpServletRequest)
                && "POST".equals(((HttpServletRequest) request).getMethod().toUpperCase())
                && null != password
                && null != timestamp
                && null != appId
                && "login".equals(methodName);
    }

    private boolean isAccountRegisterPost(ServletRequest request) {

        Map<String, String> map = RequestResponseUtil.getRequestBodyMap(request);
        String uid = map.get("uid");
        String username = map.get("username");
        String methodName = map.get("methodName");
        String password = map.get("password");
        return (request instanceof HttpServletRequest)
                && "POST".equals(((HttpServletRequest) request).getMethod().toUpperCase())
                && null != username
                && null != password
                && null != uid
                && "register".equals(methodName);
    }

    private AuthenticationToken createPasswordToken(ServletRequest request) throws Exception {

        Map<String, String> map = RequestResponseUtil.getRequestBodyMap(request);
        String appId = map.get("appId");
        String timestamp = map.get("timestamp");
        String password = map.get("password");
        String host = IpUtil.getIpFromRequest(WebUtils.toHttp(request));
        String userKey = map.get("userKey");
        String tokenKey = redisTemplate.opsForValue().get("TOKEN_KEY_" + host.toUpperCase() + userKey);
        return new PasswordToken(appId, password, timestamp, host, tokenKey);
    }

    public void setRedisTemplate(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

}
