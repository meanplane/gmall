package com.mp.product.exception;

import com.mp.common.exception.GCommonExceptionEnum;
import com.mp.common.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;

/**
 * @Author: Xiaoer
 * @Date: 2020-06-18
 */

@Slf4j
@RestControllerAdvice(basePackages = "com.mp.product.controller")
public class ControllerExceptionAdvice {

    @ExceptionHandler(value = Exception.class)
    public R handleValidException(MethodArgumentNotValidException e){
        log.error("数据校验错误 : {} , 异常类型 : {} ",e.getMessage(),e.getClass());
        BindingResult bindingResult = e.getBindingResult();

        HashMap<String, String> errorMap = new HashMap<>();
        bindingResult.getFieldErrors().forEach(err -> {
            errorMap.put(err.getField(),err.getDefaultMessage());
        });
        return R.error(GCommonExceptionEnum.PARAM_ERROR).put("data",errorMap);
    }

    @ExceptionHandler(value = Throwable.class)
    public R handleException(Throwable throwable){
        log.error("错误 : ",throwable);
        return R.error(GCommonExceptionEnum.SYS_ERROR);
    }

}
