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


    @NotNull(message = "User ID cannot be null")
    private Long userId;

    @Schema(description = "小说ID")
    @NotNull(message="小说ID不能为空！")
    private Long bookId;

    @Schema(description = "评论内容")
    @NotNull(message = "评论内容不能为空")
    @Length(min=10,max=512)
    private String commentContent;
}
