package com.aoyouao.novel.dto.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 小说评论响应DTO
 */
@Data
@Builder
public class BookCommentRespDto {
    private Long commentTotal;
    private List<CommentInfo> comments;

    @Data
    @Builder
    public static class CommentInfo{
        private Long id;
        private String commentContent;

        private String commentUser;
        private long commentUserId;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime commentTime;
    }
}
