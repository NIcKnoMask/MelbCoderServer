package com.unimelbCoder.melbcode.models.dto;

import lombok.Data;
import lombok.experimental.Accessors;
import java.util.Date;

@Data
@Accessors(chain = true)
public class UserStarDTO {
    /**
     * 用户id
     */
    private String user_id;

    /**
     * 收藏对象id
     */
    private String object_id;

    /**
     * 收藏日期：文章-收藏时的时期 位置-规划的日期
     */
    private Date date;

    /**
     * 收藏类型：1-article 2-location
     */
    private String type;
}
