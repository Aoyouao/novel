package com.aoyouao.novel.dto.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 *新闻信息响应DTO
 */
@Data
@Builder
public class NewsInfoRespDto {
    private Long id;

    private Long categoryId;

    private String categoryName;

    private String sourceName;

    private String title;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime updateTime;

    private String content;
}
