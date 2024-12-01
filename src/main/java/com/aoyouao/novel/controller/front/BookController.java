package com.aoyouao.novel.controller.front;

import com.aoyouao.novel.core.common.resp.RestResp;
import com.aoyouao.novel.core.constant.ApiRouterConsts;
import com.aoyouao.novel.dao.mapper.BookCommentMapper;
import com.aoyouao.novel.dto.req.UserCommentReqDto;
import com.aoyouao.novel.dto.resp.BookCommentRespDto;
import com.aoyouao.novel.dto.resp.BookContentAboutRespDto;
import com.aoyouao.novel.dto.resp.BookInfoRespDto;
import com.aoyouao.novel.dto.resp.BookRankRespDto;
import com.aoyouao.novel.service.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 小说模块
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(ApiRouterConsts.API_FRONT_BOOK_URL_PREFIX)
public class BookController {

    @Autowired
    private final BookService bookService;

    //小说最新评论查询接口
    @GetMapping("comment/newest_lit")
    public RestResp<BookCommentRespDto> listNewestComments(@RequestParam Long bookId){
        return bookService.listNewestComments(bookId);
    }

    //小说点击榜查询接口
    @GetMapping("visit_rank")
    public RestResp<List<BookRankRespDto>> listVisitRankBooks(){
        return bookService.listVisitRankBooks();
    }

    //小说新书榜查询接口
    @GetMapping("newest_rank")
    public RestResp<List<BookRankRespDto>> listNewestRankBooks(){
        return bookService.listNewestRankBooks();
    }

    //小说更新榜查询接口
    @GetMapping("update_rank")
    public RestResp<List<BookRankRespDto>> listUpdateRankBooks(){
        return bookService.listUpdateRankBooks();
    }

    //小说信息查询接口
    @GetMapping("/{id}")
    public RestResp<BookInfoRespDto> getBookById(@PathVariable Long id){
        return bookService.getBookById(id);
    }

    //小说内容相关信息查询接口
    @GetMapping("content/{chapterId}")
    public RestResp<BookContentAboutRespDto> getBookContentAbout(@PathVariable Long chapterId){
        return bookService.getBookContentAbout(chapterId);
    }
}
