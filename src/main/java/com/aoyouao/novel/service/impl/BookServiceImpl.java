package com.aoyouao.novel.service.impl;

import com.aoyouao.novel.core.common.constant.ErrorCodeEnum;
import com.aoyouao.novel.core.common.resp.RestResp;
import com.aoyouao.novel.core.constant.DatabaseConsts;
import com.aoyouao.novel.dao.entity.BookChapter;
import com.aoyouao.novel.dao.entity.BookComment;
import com.aoyouao.novel.dao.entity.UserInfo;
import com.aoyouao.novel.dao.mapper.BookChapterMapper;
import com.aoyouao.novel.dao.mapper.BookCommentMapper;
import com.aoyouao.novel.dto.req.UserCommentReqDto;
import com.aoyouao.novel.dto.resp.*;
import com.aoyouao.novel.manager.cache.BookChapterCacheManager;
import com.aoyouao.novel.manager.cache.BookContentCacheManager;
import com.aoyouao.novel.manager.cache.BookInfoCacheManager;
import com.aoyouao.novel.manager.cache.BookRankCacheManager;
import com.aoyouao.novel.manager.dao.UserDaoManager;
import com.aoyouao.novel.service.BookService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookServiceImpl implements BookService {

    @Autowired
    private BookCommentMapper bookCommentMapper;
    @Autowired
    private UserDaoManager userDaoManager;
    @Autowired
    private BookRankCacheManager bookRankCacheManager;
    @Autowired
    private BookInfoCacheManager bookInfoCacheManager;
    @Autowired
    private BookChapterCacheManager bookChapterCacheManager;
    @Autowired
    private BookContentCacheManager bookContentCacheManager;
    @Override
    public RestResp<Void> saveComment(UserCommentReqDto userCommentReqDto) {
        //检验用户是否已发布过评论
        QueryWrapper<BookComment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DatabaseConsts.BookCommentTable.COLUMN_USER_ID,userCommentReqDto.getUserId())
                .eq(DatabaseConsts.BookCommentTable.COLUMN_BOOK_ID,userCommentReqDto.getBookId());
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
        bookCommentMapper.addCommentCount(userCommentReqDto.getBookId());
        return RestResp.ok();
    }

    @Override
    public RestResp<Void> updateComment(Long userId, Long id, String content) {
        QueryWrapper<BookComment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DatabaseConsts.CommonColumnEnum.ID.getName(), id)
                .eq(DatabaseConsts.BookCommentTable.COLUMN_USER_ID,userId);
        BookComment bookComment = new BookComment();
        bookComment.setCommentContent(content);
        bookCommentMapper.update(bookComment,queryWrapper);
        return RestResp.ok();

    }

    @Override
    public RestResp<Void> deleteComment(Long userId, Long commentId) {
        QueryWrapper<BookComment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DatabaseConsts.CommonColumnEnum.ID.getName(), commentId)
                .eq(DatabaseConsts.BookCommentTable.COLUMN_USER_ID,userId);
        bookCommentMapper.delete(queryWrapper);
        return RestResp.ok();
    }

    @Override
    public RestResp<BookCommentRespDto> listNewestComments(Long bookId) {
        //查询评论总数
        QueryWrapper<BookComment> commentCountQueryWrapper = new QueryWrapper<>();
        commentCountQueryWrapper.eq(DatabaseConsts.BookCommentTable.COLUMN_BOOK_ID,bookId);
        Long commentCount = bookCommentMapper.selectCount(commentCountQueryWrapper);

        BookCommentRespDto bookCommentRespDto = BookCommentRespDto.builder().commentTotal(commentCount).build();

        if(commentCount > 0){
            //查询最新评论列表
            QueryWrapper<BookComment> commentQueryWrapper = new QueryWrapper<>();
            commentQueryWrapper.eq(DatabaseConsts.BookCommentTable.COLUMN_BOOK_ID,bookId)
                    .orderByDesc(DatabaseConsts.CommonColumnEnum.CREATE_TIME.getName())
                    .last(DatabaseConsts.SqlEnum.LIMIT_5.getSql());
            List<BookComment> bookCommentList = bookCommentMapper.selectList(commentQueryWrapper);

            //查询评论用户信息，并设置需要返回的评论用户名
            List<Long> userIds = bookCommentList.stream().map(BookComment::getUserId).toList();
            List<UserInfo> userInfos = userDaoManager.listUsers(userIds);
            Map<Long, String> userInfoMap = userInfos.stream().collect(Collectors.toMap(UserInfo::getId, UserInfo::getUsername));
            List<BookCommentRespDto.CommentInfo> commentInfos = bookCommentList.stream().map(v -> BookCommentRespDto.CommentInfo.builder()
                    .id(v.getId())
                    .commentUserId(v.getUserId())
                    .commentUser(userInfoMap.get(v.getUserId()))
                    .commentContent(v.getCommentContent())
                    .commentTime(v.getCreateTime()).build()).toList();
            bookCommentRespDto.setComments(commentInfos);
        }else{
            bookCommentRespDto.setComments(Collections.emptyList());
        }
        return RestResp.ok(bookCommentRespDto);
    }

    @Override
    public RestResp<List<BookRankRespDto>> listVisitRankBooks() {
        return RestResp.ok(bookRankCacheManager.listVisitRankBooks());
    }

    @Override
    public RestResp<List<BookRankRespDto>> listNewestRankBooks() {
        return RestResp.ok(bookRankCacheManager.listNewestRankBooks());
    }

    @Override
    public RestResp<List<BookRankRespDto>> listUpdateRankBooks() {
        return RestResp.ok(bookRankCacheManager.listUpdateRankBooks());
    }

    @Override
    public RestResp<BookInfoRespDto> getBookById(Long id) {
        return RestResp.ok(bookInfoCacheManager.getBookInfo(id));
    }

    @Override
    public RestResp<BookContentAboutRespDto> getBookContentAbout(Long chapterId) {
        BookChapterRespDto chapter = bookChapterCacheManager.getChapter(chapterId);

        String bookContent = bookContentCacheManager.getBookContent(chapterId);

        BookInfoRespDto bookInfo = bookInfoCacheManager.getBookInfo(chapter.getBookId());

        return RestResp.ok(BookContentAboutRespDto.builder()
                .bookChapterRespDto(chapter)
                .chapterContent(bookContent)
                .bookInfoRespDto(bookInfo).build());
    }

}
