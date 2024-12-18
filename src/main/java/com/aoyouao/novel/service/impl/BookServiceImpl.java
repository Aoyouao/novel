package com.aoyouao.novel.service.impl;

import com.aoyouao.novel.core.common.constant.ErrorCodeEnum;
import com.aoyouao.novel.core.common.req.PageReqDto;
import com.aoyouao.novel.core.common.resp.PageRestDto;
import com.aoyouao.novel.core.common.resp.RestResp;
import com.aoyouao.novel.core.constant.DatabaseConsts;
import com.aoyouao.novel.dao.entity.*;
import com.aoyouao.novel.dao.mapper.BookChapterMapper;
import com.aoyouao.novel.dao.mapper.BookCommentMapper;
import com.aoyouao.novel.dao.mapper.BookCommentReplyMapper;
import com.aoyouao.novel.dao.mapper.BookInfoMapper;
import com.aoyouao.novel.dto.req.UserCommentReqDto;
import com.aoyouao.novel.dto.resp.*;
import com.aoyouao.novel.manager.cache.*;
import com.aoyouao.novel.manager.dao.UserDaoManager;
import com.aoyouao.novel.service.BookService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    @Autowired
    private BookCategoryCacheManager bookCategoryCacheManager;
    @Autowired
    private BookChapterMapper bookChapterMapper;
    @Autowired
    private BookInfoMapper bookInfoMapper;
    @Autowired
    private BookCommentReplyMapper bookCommentReplyMapper;

    private static final Integer REC_BOOK_COUNT = 4;
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
    public RestResp<Void> saveReply(UserCommentReqDto userCommentReqDto) {
        //查询是否有根评论
        BookComment bookComment = bookCommentMapper.selectById(userCommentReqDto.getCommentId());
        if(bookComment == null){
            return RestResp.fail(ErrorCodeEnum.USER_COMMENT);
        }
        BookCommentReply bookCommentReply = new BookCommentReply();
        bookCommentReply.setUserId(userCommentReqDto.getUserId());
        bookCommentReply.setCommentId(userCommentReqDto.getCommentId());
        bookCommentReply.setReplyContent(userCommentReqDto.getReplyContent());
        bookCommentReply.setCreateTime(LocalDateTime.now());
        bookCommentReply.setUpdateTime(LocalDateTime.now());

        if(userCommentReqDto.getParentReplyId() != null){
            bookCommentReply.setParentReplyId(userCommentReqDto.getParentReplyId());
        }
        bookCommentReplyMapper.insert(bookCommentReply);
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
        BookComment bookComment = bookCommentMapper.selectById(commentId);
        bookCommentMapper.delete(queryWrapper);
        bookCommentMapper.desCommentCount(bookComment.getBookId());
        return RestResp.ok();
    }

    @Override
    public RestResp<BookCommentRespDto> listNewestComments(Long bookId) {
        //查询评论总数
        QueryWrapper<BookComment> commentCountQueryWrapper = new QueryWrapper<>();
        commentCountQueryWrapper.eq(DatabaseConsts.BookCommentTable.COLUMN_BOOK_ID,bookId);
        Long commentCount = bookCommentMapper.selectCount(commentCountQueryWrapper);

       /* BookCommentRespDto bookCommentRespDto = BookCommentRespDto.builder().commentTotal(commentCount).build();*/
        BookCommentRespDto bookCommentRespDto = new BookCommentRespDto();
        bookCommentRespDto.setCommentTotal(commentCount);

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
    public RestResp<List<BookCommentRespDto>> getComment(Long commentId) {

        QueryWrapper<BookComment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DatabaseConsts.CommonColumnEnum.ID.getName(), commentId);
        List<BookComment> bookCommentList = bookCommentMapper.selectList(queryWrapper);



        QueryWrapper<BookCommentReply> bookCommentReplyQueryWrapper = new QueryWrapper<>();
        bookCommentReplyQueryWrapper.eq(DatabaseConsts.BookCommentTable.COLUMN_COMMENT_ID,commentId);
        List<BookCommentReply> bookCommentReplies = bookCommentReplyMapper.selectList(bookCommentReplyQueryWrapper);

        List<Long> userIds = bookCommentList.stream().map(BookComment::getUserId).toList();
        List<UserInfo> userInfos = userDaoManager.listUsers(userIds);
        Map<Long, String> userInfoMap = userInfos.stream().collect(Collectors.toMap(UserInfo::getId, UserInfo::getUsername));


        return RestResp.ok(bookCommentList.stream().map(bookComment -> {
            BookCommentRespDto bookCommentRespDto = new BookCommentRespDto();
            bookCommentRespDto.setCommentTotal(bookCommentMapper.selectCount(queryWrapper));
            bookCommentRespDto.setComments(bookCommentList.stream().map(v-> BookCommentRespDto.CommentInfo.builder()
                    .id(v.getId())
                    .commentUserId(v.getUserId())
                    .commentUser(userInfoMap.get(v.getUserId()))
                    .commentContent(v.getCommentContent())
                    .commentTime(v.getCreateTime()).build()).toList());
                    List<BookCommentReplyRespDto> replyRespDtoList = bookCommentReplies.stream().filter(reply -> reply.getCommentId().equals(bookComment.getId()))
                            .map(reply -> {
                                BookCommentReplyRespDto replyRespDto = new BookCommentReplyRespDto();
                                replyRespDto.setId(reply.getId());
                                replyRespDto.setCommentId(reply.getCommentId());
                                replyRespDto.setReplyContent(reply.getReplyContent());
                                replyRespDto.setUserId(reply.getUserId());
                                replyRespDto.setCreateTime(reply.getCreateTime());
                                return replyRespDto;
                            }).collect(Collectors.toList());
                    bookCommentRespDto.setReplies(replyRespDtoList);
                    return bookCommentRespDto;
                }).collect(Collectors.toList()));
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
    public RestResp<List<BookCategoryRespDto>> listCategory(Integer workDirection) {
        return RestResp.ok(bookCategoryCacheManager.listCategory(workDirection));
    }

    @Override
    public RestResp<List<BookInfoRespDto>> listRecBooks(Long bookId) throws NoSuchAlgorithmException {
        //先根据该id得到类别id 再根据类别id查询出同类别的最新更新的id列表 随机生成一个索引 bookId 如果没有已选取过的索引集合里没有
        //则获取详细信息添加到返回集合里
        Long categoryId = bookInfoCacheManager.getBookInfo(bookId).getCategoryId();
        List<Long> lastUpdateIdList = bookInfoCacheManager.getLastUpdateIdList(categoryId);
        List<BookInfoRespDto> respDtoList = new ArrayList<>();
        List<Integer> recIdIndexList = new ArrayList<>();
        Random random = SecureRandom.getInstanceStrong();
        int count = 0;
        while(count < REC_BOOK_COUNT){
            int recIdIndex = random.nextInt(lastUpdateIdList.size());
            if(!recIdIndexList.contains(recIdIndex)){
                recIdIndexList.add(recIdIndex);
                Long id = lastUpdateIdList.get(recIdIndex);
                BookInfoRespDto bookInfo = bookInfoCacheManager.getBookInfo(id);
                respDtoList.add(bookInfo);
                count++;
            }
        }
        return RestResp.ok(respDtoList);
    }

    @Override
    public RestResp<BookLastChapterAboutRespDto> getLastChapterAbout(Long bookId) {
        //查询最新章节ID
        Long lastChapterId = bookInfoCacheManager.getBookInfo(bookId).getLastChapterId();
        //查询最新章节信息
        BookChapterRespDto chapter = bookChapterCacheManager.getChapter(lastChapterId);
        //查询最新章节内容
        String bookContent = bookContentCacheManager.getBookContent(chapter.getId());
        //查询章节总数
        QueryWrapper<BookChapter> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DatabaseConsts.BookChapterTable.COLUMN_BOOK_ID, bookId);
        Long chapterTotal = bookChapterMapper.selectCount(queryWrapper);
        return RestResp.ok(BookLastChapterAboutRespDto.builder().chapterInfo(chapter)
                .chapterTotal(chapterTotal)
                .chapterSummary(bookContent.substring(0,30)).build());
    }

    @Override
    public RestResp<Long> addVisitCount(Long bookId) {
        log.info("-----bookId:{}",bookId);
        bookInfoMapper.addVisitCount(bookId);
        Long visitCount = bookInfoCacheManager.getBookInfo(bookId).getVisitCount();
        return RestResp.ok(visitCount);
    }

    @Override
    public RestResp<List<BookChapterRespDto>> listChapters(Long bookId) {
        QueryWrapper<BookChapter> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DatabaseConsts.BookChapterTable.COLUMN_BOOK_ID,bookId)
                .orderByAsc(DatabaseConsts.BookChapterTable.COLUMN_CHAPTER_NUM);
        List<BookChapter> bookChapters = bookChapterMapper.selectList(queryWrapper);
        return RestResp.ok(bookChapters.stream().map(v->BookChapterRespDto.builder()
                .id(v.getId())
                .chapterName(v.getChapterName())
                .isVip(v.getIsVip())
                .build()).toList());
    }

    @Override
    public RestResp<BookInfoRespDto> getBookById(Long id) {
        return RestResp.ok(bookInfoCacheManager.getBookInfo(id));
    }

    @Override
    public RestResp<BookContentAboutRespDto> getBookContentAbout(Long chapterId) {

        BookChapterRespDto chapter = bookChapterCacheManager.getChapter(chapterId);

        String bookContent = bookContentCacheManager.getBookContent(chapterId);

        //bookInfoCacheManager.evictBookInfo(chapter.getBookId());
        BookInfoRespDto bookInfo = bookInfoCacheManager.getBookInfo(chapter.getBookId());

        return RestResp.ok(BookContentAboutRespDto.builder()
                .bookChapterRespDto(chapter)
                .chapterContent(bookContent)
                .bookInfoRespDto(bookInfo).build());
    }

    @Override
    public RestResp<Long> getPreChapterId(Long chapterId) {
        //查询小说id和章节号
        BookChapterRespDto chapter = bookChapterCacheManager.getChapter(chapterId);
        Long bookId = chapter.getBookId();
        Integer chapterNum = chapter.getChapterNum();

        //查询上一章节信息返回ID
        QueryWrapper<BookChapter> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DatabaseConsts.BookChapterTable.COLUMN_BOOK_ID,bookId)
                .lt(DatabaseConsts.BookChapterTable.COLUMN_CHAPTER_NUM,chapterNum)
                .orderByDesc(DatabaseConsts.BookChapterTable.COLUMN_CHAPTER_NUM)
                .last(DatabaseConsts.SqlEnum.LIMIT_1.getSql());
        BookChapter bookChapter = bookChapterMapper.selectOne(queryWrapper);
        return RestResp.ok(Optional.ofNullable(bookChapter).map(BookChapter::getId).orElse(null));
    }

    @Override
    public RestResp<Long> getNextChapterId(Long chapterId) {
        BookChapterRespDto chapter = bookChapterCacheManager.getChapter(chapterId);
        Long bookId = chapter.getBookId();
        Integer chapterNum = chapter.getChapterNum();

        QueryWrapper<BookChapter> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DatabaseConsts.BookChapterTable.COLUMN_BOOK_ID,bookId)
                .gt(DatabaseConsts.BookChapterTable.COLUMN_CHAPTER_NUM,chapterNum)
                .orderByAsc(DatabaseConsts.BookChapterTable.COLUMN_CHAPTER_NUM)
                .last(DatabaseConsts.SqlEnum.LIMIT_1.getSql());
        BookChapter bookChapter = bookChapterMapper.selectOne(queryWrapper);
        return RestResp.ok(Optional.ofNullable(bookChapter).map(BookChapter::getId).orElse(null));
    }

    @Override
    public RestResp<PageRestDto<UserCommentRespDto>> listComments(Long userId, PageReqDto pageReqDto) {
        //获得分页请求的页数和条数 根据分页请求和更新时间查找数据 得到评论数据
        //如果查询数据不为空 根据得到的评论数据得到小说id列表 再查询小说信息得到UserCommentRespDto里需要的评论小说名和评论小说封面
        IPage<BookComment> page = new Page<>();
        page.setCurrent(pageReqDto.getPageNum());
        pageReqDto.setPageSize(pageReqDto.getPageSize());
        QueryWrapper<BookComment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DatabaseConsts.BookCommentTable.COLUMN_USER_ID,userId)
                .orderByDesc(DatabaseConsts.CommonColumnEnum.UPDATE_TIME.getName());
        IPage<BookComment> bookCommentIPage = bookCommentMapper.selectPage(page, queryWrapper);
        List<BookComment> comments = bookCommentIPage.getRecords();
        if(!CollectionUtils.isEmpty(comments)){
            List<Long> bookIds = comments.stream().map(BookComment::getBookId).toList();
            QueryWrapper<BookInfo> bookInfoQueryWrapper = new QueryWrapper<>();
            bookInfoQueryWrapper.in(DatabaseConsts.CommonColumnEnum.ID.getName(),bookIds);
            List<BookInfo> bookInfos = bookInfoMapper.selectList(bookInfoQueryWrapper);
            Map<Long, BookInfo> bookInfoMap = bookInfos.stream().collect(Collectors.toMap(BookInfo::getId, Function.identity()));

            return RestResp.ok(PageRestDto.of(pageReqDto.getPageNum(), pageReqDto.getPageSize(), page.getTotal(),
                    comments.stream().map(v->
                        UserCommentRespDto.builder().commentContent(v.getCommentContent())
                                .commentBook(bookInfoMap.get(v.getBookId()).getBookName())
                                .commentBookPic(bookInfoMap.get(v.getBookId()).getPicUrl())
                                .commentTime(v.getCreateTime()).build()).toList()));
        }
        return RestResp.ok(PageRestDto.of(pageReqDto.getPageNum(), pageReqDto.getPageSize(), page.getTotal(),
                Collections.emptyList()));
    }


}
