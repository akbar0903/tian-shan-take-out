package com.akbar.exception;

public class AddressBookBusinessException extends BaseException{
    public AddressBookBusinessException() {
    }

    public AddressBookBusinessException(String message) {
        super(message);
    }
}
