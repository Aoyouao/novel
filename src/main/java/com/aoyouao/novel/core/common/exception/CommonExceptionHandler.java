package com.aoyouao.novel.core.common.exception;

/**
 * 通用异常处理类
 */
import com.aoyouao.novel.core.common.constant.ErrorCodeEnum;
import com.aoyouao.novel.core.common.resp.RestResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.BindException;

@RestControllerAdvice //直接返回json字符串
@Slf4j
public class CommonExceptionHandler {

    //处理数据校验异常
    @ExceptionHandler(BindException.class)//捕获controller抛出的通用异常 做统一处理
    public RestResp<Void> handlerBindException(BindException e){
        log.error(e.getMessage());
        return RestResp.fail(ErrorCodeEnum.USER_REQUEST_PARAM_ERROR);
    }

    //处理业务异常
    @ExceptionHandler(BusinessException.class)
    public RestResp<Void> handlerBusinessException(BusinessException e){
        log.error(e.getMessage());
        return RestResp.fail(e.getErrorCodeEnum());
    }

    //处理系统异常
    @ExceptionHandler(Exception.class)
    public RestResp<Void> handlerException(Exception e){
        log.error(e.getMessage());
        return RestResp.error();
    }
}
