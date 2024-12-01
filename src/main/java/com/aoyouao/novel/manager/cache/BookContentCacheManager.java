package com.aoyouao.novel.manager.cache;

import com.aoyouao.novel.core.constant.CacheConsts;
import com.aoyouao.novel.core.constant.DatabaseConsts;
import com.aoyouao.novel.dao.entity.BookContent;
import com.aoyouao.novel.dao.mapper.BookContentMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

/**
 * 小说内容
 */
@Component
@RequiredArgsConstructor
public class BookContentCacheManager {

    @Autowired
    private final BookContentMapper bookContentMapper;

    //小说内容查询并放入缓存中
    @Cacheable(cacheManager = CacheConsts.REDIS_CACHE_MANAGER,
                value = CacheConsts.BOOK_CONTENT_CACHE_NAME)
    public String getBookContent(Long chapterId){
        QueryWrapper<BookContent> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DatabaseConsts.BookContentTable.COLUMN_CHAPTER_ID,chapterId)
                .last(DatabaseConsts.SqlEnum.LIMIT_1.getSql());
        BookContent bookContent = bookContentMapper.selectOne(queryWrapper);
        return bookContent.getContent();
    }

    @CacheEvict(cacheManager = CacheConsts.REDIS_CACHE_MANAGER,
                    value = CacheConsts.BOOK_CONTENT_CACHE_NAME)
    public void evictBookContent(Long chapterId){

    }
}
