package com.aoyouao.novel.dto.req;

import com.aoyouao.novel.core.common.req.PageReqDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * 搜索请求DTO
 */
@Data
public class BookSearchReqDto extends PageReqDto {

    private String keyword;

    private Integer workDirection;

    private Integer categoryId;

    private Integer isVip;

    private Integer bookStatus;

    private Integer wordCountMin;

    private Integer wordCountMax;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date updateTimeMin;

    private String sort;


}
