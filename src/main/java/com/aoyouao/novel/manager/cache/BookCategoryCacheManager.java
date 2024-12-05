package com.aoyouao.novel.manager.cache;

import com.aoyouao.novel.core.constant.CacheConsts;
import com.aoyouao.novel.core.constant.DatabaseConsts;
import com.aoyouao.novel.dao.entity.BookCategory;
import com.aoyouao.novel.dao.mapper.BookCategoryMapper;
import com.aoyouao.novel.dto.resp.BookCategoryRespDto;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BookCategoryCacheManager {

    @Autowired
    private final BookCategoryMapper bookCategoryMapper;

    //根据作品方向查询小说分类列表，并放入缓存中
    @Cacheable(cacheManager = CacheConsts.CAFFEINE_CACHE_MANAGER,value = CacheConsts.BOOK_CATEGORY_LIST_CACHE_NAME)
    public List<BookCategoryRespDto> listCategory(Integer workDirection){
        QueryWrapper<BookCategory> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DatabaseConsts.BookCategoryTable.COLUMN_WORK_DIRECTION,workDirection);
        return bookCategoryMapper.selectList(queryWrapper).stream()
                .map(v->BookCategoryRespDto.builder()
                        .id(v.getId())
                        .name(v.getName()).build()).toList();
    }
}
