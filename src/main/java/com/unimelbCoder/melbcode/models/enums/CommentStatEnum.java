package com.unimelbCoder.melbcode.models.enums;

import lombok.Getter;

@Getter
public enum CommentStatEnum {

    NOT_COMMENT(0, "未评论"),
    COMMENT(1, "已评论"),
    DELETE_COMMENT(2, "删除评论")
    ;

    private final Integer code;
    private final String desc;

    CommentStatEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static CommentStatEnum fromCode(Integer code) {
        for (CommentStatEnum value : CommentStatEnum.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return CommentStatEnum.NOT_COMMENT;
    }
}
