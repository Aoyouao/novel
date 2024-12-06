package com.aoyouao.novel.dto.resp;

import com.aoyouao.novel.core.common.resp.PageRestDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

/**
 * 用户评论响应 dto
 */
@Data
@Builder
public class UserCommentRespDto{
    private String commentContent;
    private String commentBookPic;
    private String commentBook;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm;ss")
    private LocalDateTime commentTime;
}
