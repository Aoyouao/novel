package com.aoyouao.novel.dto.req;

/**
 * 用户发表评论请求DTO
 */

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class UserCommentReqDto {


    private Long userId;

    @NotNull(message="小说ID不能为空！")
    private Long bookId;

    //@NotNull(message = "评论内容不能为空")
    @Length(min=10,max=512)
    private String commentContent;

    /**
     * 根评论ID
     */
    private Long commentId;
    private String replyContent;
    private Long parentReplyId;

}
