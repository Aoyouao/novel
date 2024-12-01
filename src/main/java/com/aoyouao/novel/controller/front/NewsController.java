package com.aoyouao.novel.controller.front;

import com.aoyouao.novel.core.common.resp.RestResp;
import com.aoyouao.novel.core.constant.ApiRouterConsts;
import com.aoyouao.novel.dto.resp.NewsInfoRespDto;
import com.aoyouao.novel.service.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiRouterConsts.API_FRONT_NEWS_URL_PREFIX)
public class NewsController {

    @Autowired
    private final NewsService newsService;

    //最新新闻列表查询接口
    @GetMapping("latest_list")
    public RestResp<List<NewsInfoRespDto>> listLatestNews(){
        return newsService.listLatestNews();
    }

    //新闻信息查询接口
    @GetMapping("{id}")
    public RestResp<NewsInfoRespDto> getNews(@PathVariable Long id){
        return newsService.getNews(id);
    }
}
