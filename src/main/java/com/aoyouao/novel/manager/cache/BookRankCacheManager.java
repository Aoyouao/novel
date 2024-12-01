package com.aoyouao.novel.manager.cache;

import com.aoyouao.novel.core.common.resp.RestResp;
import com.aoyouao.novel.core.constant.CacheConsts;
import com.aoyouao.novel.core.constant.DatabaseConsts;
import com.aoyouao.novel.dao.entity.BookInfo;
import com.aoyouao.novel.dao.mapper.BookInfoMapper;
import com.aoyouao.novel.dto.resp.BookRankRespDto;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 小说排行榜 缓存管理类
 * */
@Component
@RequiredArgsConstructor
public class BookRankCacheManager {
    @Autowired
    private final BookInfoMapper bookInfoMapper;

    /**
     * 小说点击榜查询 并放入缓存中
     */
    @Cacheable(cacheManager = CacheConsts.REDIS_CACHE_MANAGER,
                value = CacheConsts.BOOK_VISIT_RANK_CACHE_NAME)
    public List<BookRankRespDto> listVisitRankBooks(){
        QueryWrapper<BookInfo> bookInfoQueryWrapper = new QueryWrapper<>();
        bookInfoQueryWrapper.orderByDesc(DatabaseConsts.BookTable.COLUMN_VISIT_COUNT);
        return listRankBooks(bookInfoQueryWrapper);
    }

    /**
     * 小说新书榜查询 并放入缓存中
     */
    @Cacheable(cacheManager = CacheConsts.REDIS_CACHE_MANAGER,
                value = CacheConsts.BOOK_NEWEST_RANK_CACHE_NAME)
    public List<BookRankRespDto> listNewestRankBooks(){
        QueryWrapper<BookInfo> bookInfoQueryWrapper = new QueryWrapper<>();
        bookInfoQueryWrapper.gt(DatabaseConsts.BookTable.COLUMN_WORD_COUNT,0)
                .orderByDesc(DatabaseConsts.CommonColumnEnum.CREATE_TIME.getName());
        return listRankBooks(bookInfoQueryWrapper);
    }

    /**
     * 小说更新榜查询 并放入缓存中
     */
    @Cacheable(cacheManager = CacheConsts.REDIS_CACHE_MANAGER,
                value = CacheConsts.BOOK_UPDATE_RANK_CACHE_NAME)
    public List<BookRankRespDto> listUpdateRankBooks(){
        QueryWrapper<BookInfo> bookInfoQueryWrapper = new QueryWrapper<>();
        bookInfoQueryWrapper.gt(DatabaseConsts.BookTable.COLUMN_WORD_COUNT,0)
                .orderByDesc(DatabaseConsts.CommonColumnEnum.CREATE_TIME.getName());
        return listRankBooks(bookInfoQueryWrapper);
    }

    public List<BookRankRespDto> listRankBooks(QueryWrapper<BookInfo> bookInfoQueryWrapper){
        bookInfoQueryWrapper.gt(DatabaseConsts.BookTable.COLUMN_WORD_COUNT,0)
                .last(DatabaseConsts.SqlEnum.LIMIT_30.getSql());
        return bookInfoMapper.selectList(bookInfoQueryWrapper).stream().map(v -> {
            BookRankRespDto bookRankRespDto = new BookRankRespDto();
            bookRankRespDto.setId(v.getId());
            bookRankRespDto.setCategoryId(v.getCategoryId());
            bookRankRespDto.setCategoryName(v.getCategoryName());
            bookRankRespDto.setPicUrl(v.getPicUrl());
            bookRankRespDto.setBookName(v.getBookName());
            bookRankRespDto.setAuthorName(v.getAuthorName());
            bookRankRespDto.setBookDesc(v.getBookDesc());
            bookRankRespDto.setLastChapterName(v.getLastChapterName());
            bookRankRespDto.setLastChapterUpdateTime(v.getLastChapterUpdateTime());
            return bookRankRespDto;
        }).toList();
    }
}
