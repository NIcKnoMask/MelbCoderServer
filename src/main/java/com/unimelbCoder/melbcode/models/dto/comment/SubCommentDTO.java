package com.unimelbCoder.melbcode.models.dto.comment;


import lombok.Data;
import org.jetbrains.annotations.NotNull;

@Data
public class SubCommentDTO extends BaseCommentDTO{

    /*
    父级评论内容
     */
    private String parentContent;

    @Override
    public int compareTo(@NotNull BaseCommentDTO o) {
        return Long.compare(o.getCommentTime(), this.getCommentTime());
    }
}
