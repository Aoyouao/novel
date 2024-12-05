package com.aoyouao.novel.dto.resp;

import lombok.Builder;
import lombok.Data;

/**
 * 小说分类 响应DTO
 */
@Data
@Builder
public class BookCategoryRespDto {
    //类别Id
    private Long id;
    //类别名
    private String name;
}
