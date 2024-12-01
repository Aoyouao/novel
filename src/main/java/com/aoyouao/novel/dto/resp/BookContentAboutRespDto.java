package com.aoyouao.novel.dto.resp;

import lombok.Builder;
import lombok.Data;

/**
 * 小说内容相关响应DTO
 */
@Data
@Builder
public class BookContentAboutRespDto {

    private BookInfoRespDto bookInfoRespDto;

    private BookChapterRespDto bookChapterRespDto;

    private String chapterContent;
}
