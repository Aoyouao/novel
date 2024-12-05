package com.aoyouao.novel.controller.front;

import com.aoyouao.novel.core.common.resp.RestResp;
import com.aoyouao.novel.core.constant.ApiRouterConsts;
import com.aoyouao.novel.dao.mapper.BookCommentMapper;
import com.aoyouao.novel.dto.req.BookVisitReqDto;
import com.aoyouao.novel.dto.req.UserCommentReqDto;
import com.aoyouao.novel.dto.resp.*;
import com.aoyouao.novel.manager.cache.BookRankCacheManager;
import com.aoyouao.novel.service.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * 小说模块
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(ApiRouterConsts.API_FRONT_BOOK_URL_PREFIX)
@Slf4j
public class BookController {

    @Autowired
    private final BookService bookService;

    private final BookRankCacheManager bookRankCacheManager;

    //小说最新评论查询接口
    @GetMapping("comment/newest_list")
    public RestResp<BookCommentRespDto> listNewestComments(@RequestParam Long bookId){
        return bookService.listNewestComments(bookId);
    }

    //小说点击榜查询接口
    @GetMapping("visit_rank")
    public RestResp<List<BookRankRespDto>> listVisitRankBooks(){
        log.info("Controller,listVisitRankBooks开始执行");
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

    //小说分类列表查询接口
    @GetMapping("category/list")
    public RestResp<List<BookCategoryRespDto>> listCategory(Integer workDirection){
        return bookService.listCategory(workDirection);
    }

    //小说推荐列表查询接口
    @GetMapping("rec_list")
    public RestResp<List<BookInfoRespDto>> listRecBooks(Long bookId) throws NoSuchAlgorithmException {
        return bookService.listRecBooks(bookId);
    }

    //小说最新章节查询接口
    @GetMapping("last_chapter/about")
    public RestResp<BookLastChapterAboutRespDto> getLastChapterAbout(Long bookId){
        return bookService.getLastChapterAbout(bookId);
    }

    //增加小说点击量接口
    @PostMapping("visit")
    public RestResp<Long> addVisitCount(@RequestBody BookVisitReqDto bookVisitReqDto){
        String bookId = bookVisitReqDto.getBookId();
        log.info("-----bookId:{}",Long.parseLong(bookId));
        return bookService.addVisitCount(Long.parseLong(bookId));
    }

    //小说章节列表查询接口
    @GetMapping("chapter/list")
    public RestResp<List<BookChapterRespDto>> listChapters(Long bookId){
        return bookService.listChapters(bookId);
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

    //查询上一章节ID
    @GetMapping("pre_chapter_id/{chapterId}")
    public RestResp<Long> getPreChapterId(@PathVariable Long chapterId){
        return bookService.getPreChapterId(chapterId);
    }

    //获取下一章节ID
    @GetMapping("next_chapter_id/{chapterId}")
    public RestResp<Long> getNextChapterId(@PathVariable Long chapterId){
        return bookService.getNextChapterId(chapterId);
    }
}
