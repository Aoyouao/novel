package com.aoyouao.novel.service.impl;

import com.aoyouao.novel.core.common.constant.ErrorCodeEnum;
import com.aoyouao.novel.core.common.resp.RestResp;
import com.aoyouao.novel.dao.entity.BookComment;
import com.aoyouao.novel.dao.mapper.BookCommentMapper;
import com.aoyouao.novel.dto.req.UserCommentReqDto;
import com.aoyouao.novel.dto.resp.BookCommentRespDto;
import com.aoyouao.novel.service.BookService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookServiceImpl implements BookService {

    @Autowired
    private BookCommentMapper bookCommentMapper;
    @Override
    public RestResp<Void> saveComment(UserCommentReqDto userCommentReqDto) {
        //检验用户是否已发布过评论
        LambdaQueryWrapper<BookComment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BookComment::getUserId,userCommentReqDto.getUserId())
                .eq(BookComment::getBookId,userCommentReqDto.getBookId());
        if(bookCommentMapper.selectCount(queryWrapper)>0){
            return RestResp.fail(ErrorCodeEnum.USER_COMMENTED);
        }
        log.info("Saving comment for userId: {} and bookId: {}", userCommentReqDto.getUserId(), userCommentReqDto.getBookId());

        BookComment bookComment = new BookComment();
        bookComment.setUserId(userCommentReqDto.getUserId());
        bookComment.setBookId(userCommentReqDto.getBookId());
        bookComment.setCommentContent(userCommentReqDto.getCommentContent());
        bookComment.setCreateTime(LocalDateTime.now());
        bookComment.setUpdateTime(LocalDateTime.now());
        bookCommentMapper.insert(bookComment);
        return RestResp.ok();
    }

    @Override
    public RestResp<Void> updateComment(Long userId, Long id, String content) {
        LambdaUpdateWrapper<BookComment> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(BookComment::getUserId,userId)
                .eq(BookComment::getBookId,id)
                        .set(BookComment::getCommentContent,content);
        bookCommentMapper.update(null,updateWrapper);
        return RestResp.ok();

    }

    @Override
    public RestResp<Void> deleteComment(Long userId, Long commentId) {
        LambdaQueryWrapper<BookComment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BookComment::getBookId,commentId)
                .eq(BookComment::getUserId,userId);
        bookCommentMapper.delete(queryWrapper);
        return RestResp.ok();
    }

    @Override
    public RestResp<BookCommentRespDto> listNewestComments(Long bookId) {

        return null;
    }
}
