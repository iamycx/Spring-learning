package com.iamycx.exception;

/**
 * Created by iamycx on 21:34 2017/11/29
 *
 * 秒杀关闭异常
 */
public class SeckillCloseException extends SeckillException{

    public SeckillCloseException(String message) {
        super(message);
    }

    public SeckillCloseException(String message, Throwable cause) {
        super(message, cause);
    }
}
