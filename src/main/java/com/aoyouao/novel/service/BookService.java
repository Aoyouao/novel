package com.aoyouao.novel.service;

import com.aoyouao.novel.core.common.resp.RestResp;
import com.aoyouao.novel.dto.req.UserCommentReqDto;
import com.aoyouao.novel.dto.resp.BookCommentRespDto;
import com.aoyouao.novel.dto.resp.BookContentAboutRespDto;
import com.aoyouao.novel.dto.resp.BookInfoRespDto;
import com.aoyouao.novel.dto.resp.BookRankRespDto;

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
     * 小说信息查询
     */
    RestResp<BookInfoRespDto> getBookById(Long id);

    /**
     * 小说内容相关信息查询
     */
    RestResp<BookContentAboutRespDto> getBookContentAbout(Long chapterId);
}
