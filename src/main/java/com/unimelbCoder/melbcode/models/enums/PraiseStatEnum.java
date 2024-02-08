package com.unimelbCoder.melbcode.models.enums;

import lombok.Getter;

@Getter
public enum PraiseStatEnum {

    NOT_PRAISE(0, "未点赞"),
    PRAISE(1, "已点赞"),
    CANCEL_PRAISE(2, "取消点赞")
    ;

    private final Integer code;
    private final String desc;

    PraiseStatEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static PraiseStatEnum fromCode(Integer code) {
        for (PraiseStatEnum value : PraiseStatEnum.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return PraiseStatEnum.NOT_PRAISE;
    }
}
