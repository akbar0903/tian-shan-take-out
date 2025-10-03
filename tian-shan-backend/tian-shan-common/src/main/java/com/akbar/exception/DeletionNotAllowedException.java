package com.akbar.exception;

/**
 * 不允许删除的异常
 */
public class DeletionNotAllowedException extends BaseException{

    public DeletionNotAllowedException() {
    }

    public DeletionNotAllowedException(String message) {
        super(message);
    }
}
