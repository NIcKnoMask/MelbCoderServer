package com.unimelbCoder.melbcode.models.enums;

import lombok.Getter;

@Getter
public enum CollectionStatEnum {

    NOT_COLLECT(0, "未收藏"),
    COLLECT(1, "已收藏"),
    CANCEL_COLLECT(2, "取消收藏")
    ;

    private final Integer code;
    private final String desc;

    CollectionStatEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private static CollectionStatEnum fromCode(Integer code) {
        for (CollectionStatEnum value : CollectionStatEnum.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return CollectionStatEnum.NOT_COLLECT;
    }

}
