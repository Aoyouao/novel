package com.aoyouao.novel.manager.cache;

import com.aoyouao.novel.core.common.resp.RestResp;
import com.aoyouao.novel.core.constant.CacheConsts;
import com.aoyouao.novel.core.constant.DatabaseConsts;
import com.aoyouao.novel.dao.entity.NewsInfo;
import com.aoyouao.novel.dao.mapper.NewsInfoMapper;
import com.aoyouao.novel.dto.resp.NewsInfoRespDto;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
@RequiredArgsConstructor
public class NewsCacheManager {

    @Autowired
    private final NewsInfoMapper newsInfoMapper;

    //最新新闻列表查询 并放入缓存中
    @Cacheable(cacheManager = CacheConsts.CAFFEINE_CACHE_MANAGER,
            value = CacheConsts.LATEST_NEWS_CACHE_NAME)
    public List<NewsInfoRespDto> listLatestNews(){
        QueryWrapper<NewsInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc(DatabaseConsts.CommonColumnEnum.CREATE_TIME.getName())
                .last(DatabaseConsts.SqlEnum.LIMIT_2.getSql());
        List<NewsInfo> newsInfos = newsInfoMapper.selectList(queryWrapper);
        return newsInfos.stream().map(v->NewsInfoRespDto.builder()
                .id(v.getId())
                .categoryId(v.getCategoryId())
                .categoryName(v.getCategoryName())
                .sourceName(v.getSourceName())
                .title(v.getTitle())
                .updateTime(v.getUpdateTime())
                .build()).toList();
    }
}
