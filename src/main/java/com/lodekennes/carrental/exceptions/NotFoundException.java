package com.lodekennes.carrental.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends javassist.NotFoundException {
    public NotFoundException(String msg) {
        super(msg);
    }
}
