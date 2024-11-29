package com.aoyouao.novel.service;

import com.aoyouao.novel.core.common.resp.RestResp;
import com.aoyouao.novel.dto.req.UserCommentReqDto;
import com.aoyouao.novel.dto.resp.BookCommentRespDto;

/**
 * 小说模块
 */
public interface BookService {
    RestResp<Void> saveComment(UserCommentReqDto userCommentReqDto);
    RestResp<Void> updateComment(Long userId,Long id,String content);
    RestResp<Void> deleteComment(Long userId,Long commentId);
    RestResp<BookCommentRespDto> listNewestComments(Long bookId);
}
