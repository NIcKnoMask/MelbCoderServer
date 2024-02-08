package com.unimelbCoder.melbcode.models.enums;

import lombok.Getter;

@Getter
public enum OperateTypeEnum {

    EMPTY(0, "") {
        @Override
        public int getDbStatCode() {
            return 0;
        }
    },
    READ(1, "阅读") {
        @Override
        public int getDbStatCode() {
            return ReadStatEnum.READ.getCode();
        }
    },
    PRAISE(2, "点赞") {
        @Override
        public int getDbStatCode() {
            return PraiseStatEnum.PRAISE.getCode();
        }
    },
    COLLECTION(3, "收藏") {
        @Override
        public int getDbStatCode() {
            return CollectionStatEnum.COLLECT.getCode();
        }
    },
    COMMENT(4, "评论") {
        @Override
        public int getDbStatCode() {
            return CommentStatEnum.COMMENT.getCode();
        }
    },
    CANCEL_PRAISE(5, "取消点赞") {
        @Override
        public int getDbStatCode() {
            return PraiseStatEnum.CANCEL_PRAISE.getCode();
        }
    },
    CANCEL_COLLECTION(6, "取消收藏") {
        @Override
        public int getDbStatCode() {
            return CollectionStatEnum.CANCEL_COLLECT.getCode();
        }
    },
    DELETE_COMMENT(7, "删除评论") {
        @Override
        public int getDbStatCode() {
            return CommentStatEnum.DELETE_COMMENT.getCode();
        }
    }
    ;

    private final Integer code;
    private final String desc;

    OperateTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static OperateTypeEnum fromCode(Integer code) {
        for (OperateTypeEnum value : OperateTypeEnum.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return OperateTypeEnum.EMPTY;
    }

    public abstract int getDbStatCode();

    public static NotifyTypeEnum getNotifyType(OperateTypeEnum type) {
        switch (type) {
            case PRAISE:
                return NotifyTypeEnum.PRAISE;
            case CANCEL_PRAISE:
                return NotifyTypeEnum.CANCEL_PRAISE;
            case COLLECTION:
                return NotifyTypeEnum.COLLECT;
            case CANCEL_COLLECTION:
                return NotifyTypeEnum.CANCEL_COLLECT;
            default:
                return null;
        }
    }

}
