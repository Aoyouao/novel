package com.aoyouao.novel.controller.front;

import com.aoyouao.novel.core.common.resp.PageRestDto;
import com.aoyouao.novel.core.common.resp.RestResp;
import com.aoyouao.novel.core.constant.ApiRouterConsts;
import com.aoyouao.novel.dto.req.BookSearchReqDto;
import com.aoyouao.novel.dto.resp.BookInfoRespDto;
import com.aoyouao.novel.service.SearchService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiRouterConsts.API_FRONT_SEARCH_URL_PREFIX)
@Slf4j
public class SearchController {

    private final SearchService searchService;
    //小说搜索接口
    @GetMapping("books")
    public RestResp<PageRestDto<BookInfoRespDto>> searchBooks(BookSearchReqDto condition, HttpServletRequest request){
        return searchService.searchBooks(condition);
    }
}
