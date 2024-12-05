package com.aoyouao.novel.dto.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class BookRankRespDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private Long categoryId;

    private String categoryName;

    private String picUrl;

    private String bookName;

    private String authorName;

    private String bookDesc;

    private Integer wordCount;

    private String lastChapterName;

    @JsonFormat(pattern = "MM/dd HH:mm")
    private LocalDateTime lastChapterUpdateTime;
}
