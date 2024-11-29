package com.aoyouao.novel.core.common.exception;

import com.aoyouao.novel.core.common.constant.ErrorCodeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 自定义业务异常，用于处理用户请求时，业务错误时抛出
 */

@EqualsAndHashCode(callSuper = true) // Lombok 生成的 equals() 和 hashCode() 方法也考虑父类的字段
@Data
public class BusinessException extends RuntimeException{

    private final ErrorCodeEnum errorCodeEnum;
    public BusinessException(ErrorCodeEnum errorCodeEnum){
        //null: 当前 BusinessException 没有链式的根本异常（即不需要追溯到另一个异常）
        //false: 禁用异常的“抑制”功能，表示在处理异常时不会抑制其他异常
        //false:  禁用堆栈追踪的生成，意味着该异常的堆栈信息不会被记录和保存，从而提高性能
        super(errorCodeEnum.getMessage(),null,false,false);
        this.errorCodeEnum = errorCodeEnum;
    }
}
