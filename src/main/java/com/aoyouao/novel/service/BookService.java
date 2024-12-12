package com.aoyouao.novel.service;

import com.aoyouao.novel.core.common.req.PageReqDto;
import com.aoyouao.novel.core.common.resp.PageRestDto;
import com.aoyouao.novel.core.common.resp.RestResp;
import com.aoyouao.novel.dto.req.UserCommentReqDto;
import com.aoyouao.novel.dto.resp.*;

import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * 小说模块
 */
public interface BookService {
    /**
     * 发表评论
     */
    RestResp<Void> saveComment(UserCommentReqDto userCommentReqDto);

    /**
     * 回复评论
     */
    RestResp<Void> saveReply(UserCommentReqDto userCommentReqDto);

    /**
     * 修改评论
     */
    RestResp<Void> updateComment(Long userId,Long id,String content);

    /**
     * 删除评论
     */
    RestResp<Void> deleteComment(Long userId,Long commentId);


    /**
     * 展示最新评论列表
     */
    RestResp<BookCommentRespDto> listNewestComments(Long bookId);

    /**
     * 展示所有评论
     */
    RestResp<List<BookCommentRespDto>> getComment(Long commentId);

    /**
     * 小说点击榜查询
     */
    RestResp<List<BookRankRespDto>> listVisitRankBooks();

    /**
     * 小说新书榜查询
     */
    RestResp<List<BookRankRespDto>> listNewestRankBooks();

    /**
     * 小说更新榜查询
     */
    RestResp<List<BookRankRespDto>> listUpdateRankBooks();

    /**
     * 小说分类列表查询
     */
    RestResp<List<BookCategoryRespDto>> listCategory(Integer workDirection);

    /**
     * 小说推荐列表查询
     */
    RestResp<List<BookInfoRespDto>> listRecBooks(Long bookId) throws NoSuchAlgorithmException;

    /**
     * 小说最新章节相关信息查询
     */
    RestResp<BookLastChapterAboutRespDto> getLastChapterAbout(Long bookId);

    /**
     * 增加小说点击量
     */
    RestResp<Long> addVisitCount(Long bookId);

    /**
     * 小说章节列表查询
     */
    RestResp<List<BookChapterRespDto>> listChapters(Long bookId);

    /**
     * 小说信息查询
     */
    RestResp<BookInfoRespDto> getBookById(Long id);

    /**
     * 小说内容相关信息查询
     */
    RestResp<BookContentAboutRespDto> getBookContentAbout(Long chapterId);

    /**
     * 查询上一章节ID
     */
    RestResp<Long> getPreChapterId(Long chapterId);

    /**
     * 查询下一章节ID
     */
    RestResp<Long> getNextChapterId(Long chapterId);

    /**
     * 分页查询评论
     */
    RestResp<PageRestDto<UserCommentRespDto>> listComments(Long userId, PageReqDto pageReqDto);
}
