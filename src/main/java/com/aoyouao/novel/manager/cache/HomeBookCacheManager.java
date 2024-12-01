package com.aoyouao.novel.manager.cache;

import com.aoyouao.novel.core.common.resp.RestResp;
import com.aoyouao.novel.core.constant.CacheConsts;
import com.aoyouao.novel.core.constant.DatabaseConsts;
import com.aoyouao.novel.dao.entity.BookInfo;
import com.aoyouao.novel.dao.entity.HomeBook;
import com.aoyouao.novel.dao.mapper.BookInfoMapper;
import com.aoyouao.novel.dao.mapper.HomeBookMapper;
import com.aoyouao.novel.dto.resp.HomeBookRespDto;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.reactivex.rxjava3.core.Maybe;
import jodd.util.CollectionUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 首页推荐小说 缓存管理类
 */
@Component
@RequiredArgsConstructor
public class HomeBookCacheManager {

    @Autowired
    private final HomeBookMapper homeBookMapper;

    @Autowired
    private final BookInfoMapper bookInfoMapper;

    //查询首页推荐的小说 放入缓存中
    @Cacheable(cacheManager = CacheConsts.CAFFEINE_CACHE_MANAGER, value = CacheConsts.HOME_BOOK_CACHE_NAME)
    public List<HomeBookRespDto> listHomeBooks(){

        //先查询首页推荐的小说
        QueryWrapper<HomeBook> bookQueryWrapper = new QueryWrapper<>();
        bookQueryWrapper.orderByAsc(DatabaseConsts.CommonColumnEnum.SORT.getName());
        List<HomeBook> homeBooks = homeBookMapper.selectList(bookQueryWrapper);

        //遍历homeBooks获取首页推荐小说的id
        if(!CollectionUtils.isEmpty(homeBooks)) {
            List<Long> bookId = homeBooks.stream().map(HomeBook::getBookId).toList();


            //根据id查询小说具体信息
            QueryWrapper<BookInfo> queryWrapper = new QueryWrapper<>();
            queryWrapper.in(DatabaseConsts.CommonColumnEnum.ID.getName(), bookId);
            List<BookInfo> bookInfos = bookInfoMapper.selectList(queryWrapper);

            //将bookInfos封装到HomeBookRespDto
            if(!CollectionUtils.isEmpty(bookInfos)){
                //Map<Long,BookInfo> 书籍的 id（Long 类型）作为键，将每本书的信息（BookInfo 类型）作为值存储
                Map<Long, BookInfo> bookInfoMap =
                        bookInfos.stream().collect(Collectors.toMap(BookInfo::getId, Function.identity()));

                //存放转化后的HomeBookRespDto对象
                return homeBooks.stream().map(v->{
                    BookInfo bookInfo = bookInfoMap.get(v.getBookId());
                    HomeBookRespDto homeBookRespDto = new HomeBookRespDto();
                    homeBookRespDto.setType(v.getType());
                    homeBookRespDto.setBookId(v.getBookId());
                    homeBookRespDto.setBookName(bookInfo.getBookName());
                    homeBookRespDto.setPicUrl(bookInfo.getPicUrl());
                    homeBookRespDto.setAuthorName(bookInfo.getAuthorName());
                    homeBookRespDto.setBookDesc(bookInfo.getBookDesc());
                    return homeBookRespDto;
                }).toList();
            }
        }
        return Collections.emptyList();
    }

}
