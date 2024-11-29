package com.aoyouao.novel.core.common.req;

import lombok.Data;

/**
 * 分页请求数据格式封装
 */

@Data
public class PageReqDto {

    private Integer pageNum = 1;

    private Integer pageSize = 10;

    //是否查询所有
    private Boolean fetchAll = false;
}
