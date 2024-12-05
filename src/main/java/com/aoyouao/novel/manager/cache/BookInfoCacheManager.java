package com.aoyouao.novel.manager.cache;

import com.aoyouao.novel.core.constant.CacheConsts;
import com.aoyouao.novel.core.constant.DatabaseConsts;
import com.aoyouao.novel.dao.entity.BookChapter;
import com.aoyouao.novel.dao.entity.BookInfo;
import com.aoyouao.novel.dao.mapper.BookChapterMapper;
import com.aoyouao.novel.dao.mapper.BookInfoMapper;
import com.aoyouao.novel.dto.resp.BookInfoRespDto;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * 小说信息缓存管理类
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class BookInfoCacheManager {

    @Autowired
    private final BookInfoMapper bookInfoMapper;
    @Autowired
    private final BookChapterMapper bookChapterMapper;

    //从缓存中查询小说信息
    @Cacheable(cacheManager = CacheConsts.CAFFEINE_CACHE_MANAGER,value = CacheConsts.BOOK_INFO_CACHE_NAME)
    public BookInfoRespDto getBookInfo(Long id){
        return cachePutBookInfo(id);
    }

    //缓存小说信息
    @CachePut(cacheManager = CacheConsts.CAFFEINE_CACHE_MANAGER,value = CacheConsts.BOOK_INFO_CACHE_NAME)
    public BookInfoRespDto cachePutBookInfo(Long id){
        BookInfo bookInfo = bookInfoMapper.selectById(id);
        //查询首章ID
        QueryWrapper<BookChapter> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DatabaseConsts.BookChapterTable.COLUMN_BOOK_ID,id)
                .orderByAsc(DatabaseConsts.BookChapterTable.COLUMN_CHAPTER_NUM)
                .last(DatabaseConsts.SqlEnum.LIMIT_1.getSql());
        BookChapter firstBookChapter = bookChapterMapper.selectOne(queryWrapper);
        log.info("=======visitCount:{}",bookInfo.getVisitCount());
        return BookInfoRespDto.builder()
                .id(bookInfo.getId())
                .bookName(bookInfo.getBookName())
                .bookDesc(bookInfo.getBookDesc())
                .bookStatus(bookInfo.getBookStatus())
                .authorId(bookInfo.getAuthorId())
                .authorName(bookInfo.getAuthorName())
                .categoryId(bookInfo.getCategoryId())
                .categoryName(bookInfo.getCategoryName())
                .commentCount(bookInfo.getCommentCount())
                .firstChapterId(firstBookChapter.getId())
                .lastChapterId(bookInfo.getLastChapterId())
                .picUrl(bookInfo.getPicUrl())
                .visitCount(bookInfo.getVisitCount())
                .wordCount(bookInfo.getWordCount())
                .lastChapterName(bookInfo.getLastChapterName())
                .updateTime(bookInfo.getLastChapterUpdateTime())
                .build();
    }

    @CacheEvict(cacheManager = CacheConsts.CAFFEINE_CACHE_MANAGER,
                    value = CacheConsts.BOOK_INFO_CACHE_NAME)
    public void evictBookInfo(Long id){
        log.info("清除缓存");
    }

    //根据类别id查询出最新更新的500个ID列表
    @Cacheable(cacheManager = CacheConsts.CAFFEINE_CACHE_MANAGER,
            value = CacheConsts.LAST_UPDATE_BOOK_ID_LIST_CACHE_NAME)
    public List<Long> getLastUpdateIdList(Long categoryId){
        QueryWrapper<BookInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DatabaseConsts.BookTable.COLUMN_CATEGORY_ID,categoryId)
                .orderByDesc(DatabaseConsts.CommonColumnEnum.UPDATE_TIME.getName());
        List<BookInfo> bookInfos = bookInfoMapper.selectList(queryWrapper);
        return bookInfos.stream().map(BookInfo::getId).toList();
    }
}
