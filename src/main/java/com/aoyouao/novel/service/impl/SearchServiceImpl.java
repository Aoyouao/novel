package com.aoyouao.novel.service.impl;

import com.aoyouao.novel.core.common.resp.PageRestDto;
import com.aoyouao.novel.core.common.resp.RestResp;
import com.aoyouao.novel.dao.entity.BookInfo;
import com.aoyouao.novel.dao.mapper.BookInfoMapper;
import com.aoyouao.novel.dto.req.BookSearchReqDto;
import com.aoyouao.novel.dto.resp.BookInfoRespDto;
import com.aoyouao.novel.service.SearchService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SearchServiceImpl implements SearchService {

    @Autowired
    private final BookInfoMapper bookInfoMapper;
    @Override
    public RestResp<PageRestDto<BookInfoRespDto>> searchBooks(BookSearchReqDto condition) {
        Page<BookInfoRespDto> page = new Page<>();
        page.setCurrent(condition.getPageNum());
        page.setSize(condition.getPageSize());
        List<BookInfo> bookInfos = bookInfoMapper.searchBooks(page, condition);
        log.info("condition:keyword:{}",condition.getKeyword());
        return RestResp.ok(
                PageRestDto.of(condition.getPageNum(),condition.getPageSize(), page.getTotal(),
                        bookInfos.stream().map(v->BookInfoRespDto.builder()
                                .id(v.getId())
                                .bookName(v.getBookName())
                                .categoryId(v.getCategoryId())
                                .categoryName(v.getCategoryName())
                                .authorId(v.getAuthorId())
                                .authorName(v.getAuthorName())
                                .wordCount(v.getWordCount())
                                .lastChapterName(v.getLastChapterName()).build()).toList()
                ));
    }
}
