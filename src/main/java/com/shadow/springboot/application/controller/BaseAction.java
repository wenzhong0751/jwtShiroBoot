package com.shadow.springboot.application.controller;

import com.shadow.springboot.application.util.RequestResponseUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * controller基础抽象类
 *
 * @author tomsun28
 * @date 11:09 2018/3/20
 */
public abstract class BaseAction {
    /**
     * description 获得来的request中的 key value数据封装到map返回
     *
     * @param request 1
     * @return java.util.Map<java.lang.String       ,       java.lang.String>
     */
    @SuppressWarnings("rawtypes")
    protected Map<String, String> getRequestParameter(HttpServletRequest request) {

        return RequestResponseUtil.getRequestParameters(request);
    }

    protected Map<String, String> getRequestBody(HttpServletRequest request) {
        return RequestResponseUtil.getRequestBodyMap(request);
    }

    protected Map<String, String> getRequestHeader(HttpServletRequest request) {
        return RequestResponseUtil.getRequestHeaders(request);
    }
}
