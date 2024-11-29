package com.aoyouao.novel.service;

import com.aoyouao.novel.core.common.resp.RestResp;
import com.aoyouao.novel.dto.resp.HomeBookRespDto;


import java.util.List;

public interface HomeService {
    /**
     * 查询首页小说推荐列表
     */
    RestResp<List<HomeBookRespDto>> listHomeWorks();
}
