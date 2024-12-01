package com.aoyouao.novel.service;

import com.aoyouao.novel.core.common.resp.RestResp;
import com.aoyouao.novel.dto.resp.NewsInfoRespDto;

import java.util.List;

public interface NewsService {

    //新闻最新列表查询
    RestResp<List<NewsInfoRespDto>> listLatestNews();

    //新闻信息查询接口
    RestResp<NewsInfoRespDto> getNews(Long id);
}
