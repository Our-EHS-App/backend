package com.service;

public enum FormStatusEnum {
    New(1L),
    Submitted(4L),
    Expired(5L);

    public final Long id;
    FormStatusEnum(long id) {
        this.id = id;
    }
}
