package com.aoyouao.novel.service.impl;

import com.aoyouao.novel.core.common.resp.RestResp;
import com.aoyouao.novel.core.constant.DatabaseConsts;
import com.aoyouao.novel.dao.entity.NewsContent;
import com.aoyouao.novel.dao.entity.NewsInfo;
import com.aoyouao.novel.dao.mapper.NewsContentMapper;
import com.aoyouao.novel.dao.mapper.NewsInfoMapper;
import com.aoyouao.novel.dto.resp.NewsInfoRespDto;
import com.aoyouao.novel.manager.cache.NewsCacheManager;
import com.aoyouao.novel.service.NewsService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
@RequiredArgsConstructor
public class NewsServiceImpl implements NewsService {

    @Autowired
    private final NewsCacheManager newsCacheManager;
    @Autowired
    private final NewsInfoMapper newsInfoMapper;
    @Autowired
    private final NewsContentMapper newsContentMapper;
    @Override
    public RestResp<List<NewsInfoRespDto>> listLatestNews() {
        return RestResp.ok(newsCacheManager.listLatestNews());
    }

    @Override
    public RestResp<NewsInfoRespDto> getNews(Long id) {
        NewsInfo newsInfo = newsInfoMapper.selectById(id);
        QueryWrapper<NewsContent> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DatabaseConsts.NewsContentTable.COLUMN_NEWS_ID, id)
                .last(DatabaseConsts.SqlEnum.LIMIT_1.getSql());
        NewsContent newsContent = newsContentMapper.selectOne(queryWrapper);
        return RestResp.ok(NewsInfoRespDto.builder()
                .id(newsInfo.getId())
                .categoryId(newsInfo.getCategoryId())
                .categoryName(newsInfo.getCategoryName())
                .title(newsInfo.getTitle())
                .sourceName(newsInfo.getSourceName())
                .content(newsContent.getContent())
                .updateTime(newsInfo.getUpdateTime()).build());
    }
}
