package com.aoyouao.novel.dto.resp;

import com.aoyouao.novel.core.json.serializer.UsernameSerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 小说评论响应DTO
 */
@Data

public class BookCommentRespDto {

    private Long commentTotal;
    private List<CommentInfo> comments;
    private List<BookCommentReplyRespDto> replies;
    @Data
    @Builder
    public static class CommentInfo{
        private Long id;
        private String commentContent;
        @JsonSerialize(using = UsernameSerializer.class)
        private String commentUser;
        private long commentUserId;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime commentTime;
    }
}
