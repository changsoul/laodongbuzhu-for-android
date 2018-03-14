package com.wudaosoft.laodongbuzhu.exception;

/**
 * Created 2018/3/14 13:52.
 *
 * @author Changsoul.Wu
 */
public class ServiceException extends RuntimeException {

    public ServiceException(String s) {
        super(s);
    }

    public ServiceException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public ServiceException(Throwable throwable) {
        super(throwable);
    }

    public ServiceException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
