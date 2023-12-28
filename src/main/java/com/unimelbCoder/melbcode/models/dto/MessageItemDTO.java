package com.unimelbCoder.melbcode.models.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class MessageItemDTO {

    /**
     * 是否系统消息
     */
    private Boolean isSystemMsg;

    /**
     * 发送者
     */
    private String sender;

    /**
     * 接收者
     */
    private String receiver;

    /**
     * 消息内容
     */
    private String content;

}
