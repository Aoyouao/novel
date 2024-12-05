package com.aoyouao.novel.dto.resp;

import lombok.Builder;
import lombok.Data;

/**
 * 小说最新章节信息响应DTO
 */
@Data
@Builder
public class BookLastChapterAboutRespDto {

    private BookChapterRespDto chapterInfo;

    private Long chapterTotal;

    private String chapterSummary;

}
