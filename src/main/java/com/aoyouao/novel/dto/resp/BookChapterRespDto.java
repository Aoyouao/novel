package com.aoyouao.novel.dto.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 小说章节响应DTO
 */
@Data
@Builder
public class BookChapterRespDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private Long id;

    private Long bookId;

    private Integer chapterNum;

    private String chapterName;

    private Integer wordCount;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime chapterUpdateTime;

    private Integer isVip;

}
