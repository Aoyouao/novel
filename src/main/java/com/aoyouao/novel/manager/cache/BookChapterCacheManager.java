package com.aoyouao.novel.manager.cache;

/**
 * 小说章节 缓存管理类
 */

import com.aoyouao.novel.core.constant.CacheConsts;
import com.aoyouao.novel.dao.entity.BookChapter;
import com.aoyouao.novel.dao.mapper.BookChapterMapper;
import com.aoyouao.novel.dto.resp.BookChapterRespDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookChapterCacheManager {

    @Autowired
    private final BookChapterMapper bookChapterMapper;
    //查询章节信息 放入缓存中
    @Cacheable(cacheManager = CacheConsts.CAFFEINE_CACHE_MANAGER,
                value = CacheConsts.BOOK_CHAPTER_CACHE_NAME)
    public BookChapterRespDto getChapter(Long chapterId){
        BookChapter bookChapter = bookChapterMapper.selectById(chapterId);
        return BookChapterRespDto.builder()
                .id(bookChapter.getId())
                .bookId(bookChapter.getBookId())
                .chapterNum(bookChapter.getChapterNum())
                .chapterName(bookChapter.getChapterName())
                .wordCount(bookChapter.getWordCount())
                .isVip(bookChapter.getIsVip())
                .chapterUpdateTime(bookChapter.getUpdateTime()).build();
    }

    @CacheEvict(cacheManager = CacheConsts.CAFFEINE_CACHE_MANAGER,
                    value = CacheConsts.BOOK_CHAPTER_CACHE_NAME)
    public void evictBookChapterCache(Long chapterId){
        //调用此方法自动清楚小说章节信息的缓存
    }
}
