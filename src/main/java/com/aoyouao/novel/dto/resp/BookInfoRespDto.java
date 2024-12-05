package com.aoyouao.novel.dto.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 小说信息响应DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookInfoRespDto {

    private Long id;

    private Long categoryId;

    private String categoryName;

    private String picUrl;

    private String bookName;

    private Long authorId;

    private String authorName;

    private String bookDesc;

    private Integer bookStatus;

    private Long visitCount;

    private Integer wordCount;

    private Integer commentCount;

    private Long firstChapterId;

    private Long lastChapterId;

    private String lastChapterName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime updateTime;

}
