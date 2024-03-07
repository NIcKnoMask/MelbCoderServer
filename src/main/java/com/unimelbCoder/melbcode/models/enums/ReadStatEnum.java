package com.unimelbCoder.melbcode.models.enums;

import lombok.Getter;

@Getter
public enum ReadStatEnum {

    NOT_READ(0, "未读"),
    READ(1, "已读")
    ;

    private final Integer code;
    private final String desc;

    ReadStatEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static ReadStatEnum fromCode(Integer code) {
        for (ReadStatEnum value : ReadStatEnum.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return ReadStatEnum.NOT_READ;
    }
}
