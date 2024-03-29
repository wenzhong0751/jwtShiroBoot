package com.shadow.springboot.application.controller;

import com.shadow.springboot.application.domain.bo.AccountLog;
import com.shadow.springboot.application.domain.bo.OperationLog;
import com.shadow.springboot.application.domain.vo.Message;
import com.shadow.springboot.application.service.AccountLogService;
import com.shadow.springboot.application.service.OperationLogService;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/log")
@CrossOrigin
public class LogController extends BaseAction {

    @Resource(name = "accountLogServiceImpl")
    private AccountLogService accountLogService;

    @Resource(name = "operationLogServiceImpl")
    private OperationLogService operationLogService;

    @SuppressWarnings("unchecked")
    @ApiOperation(value = "获取日志记录", httpMethod = "GET")
    @GetMapping("/accountLog/{currentPage}/{pageSize}")
    public Message getAccountLogList(@PathVariable Integer currentPage, @PathVariable Integer pageSize) {
        Page<AccountLog> pageInfo = accountLogService.getAccountLogList(currentPage, pageSize);
        return new Message().ok(6666, "return accountLogs success").addData("data", pageInfo);
    }

    @SuppressWarnings("unchecked")
    @ApiOperation(value = "获取用户操作api日志列表", httpMethod = "GET")
    @GetMapping("/operationLog/{currentPage}/{pageSize}")
    public Message getOperationLogList(@PathVariable Integer currentPage, @PathVariable Integer pageSize) {
        Page<OperationLog> pageInfo = operationLogService.getOperationList(currentPage, pageSize);
        return new Message().ok(6666, "return operationLogList success").addData("data", pageInfo);
    }
}
