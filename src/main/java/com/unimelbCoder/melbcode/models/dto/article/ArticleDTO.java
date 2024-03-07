package com.unimelbCoder.melbcode.models.dto.article;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class ArticleDTO {

    private Integer articleId;

    /**
     * 作者ID
     */
    private String authorId;

    /**
     * 作者用户名
     */
    private String authorName;

    /**
     * 文章类型：1-文章，2-问答
     */
    private Integer articleType;

    /**
     * 文章标题
     */
    private String title;

    /**
     * 文章短标题
     */
    private String short_title;

    /**
     * 文章内容
     */
    private String content;

    /**
     * 发布状态：0-未发布，1-已发布
     */
    private Integer publicStatus;

    /**
     * 发布时间
     */
    private Timestamp create_time;

    /**
     * 最后更新时间
     */
    private Timestamp update_time;

    /**
     * 表示当前查看的用户是否已经点赞过
     */
    private Boolean praised;

    /**
     * 表示当前查看的用户是否已经收藏过
     */
    private Boolean collected;

    /**
     * 表示当前查看的用户是否已经评论过
     */
    private Boolean commented;

    /**
     * 文章对应的计数统计
     */
    private ArticleFootCountDTO count;

}
