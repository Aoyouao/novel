package com.aoyouao.novel.dao.mapper;

import com.aoyouao.novel.dao.entity.BookComment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 小说评论 Mapper 接口
 * </p>
 *
 * @author ${author}
 * @date 2024/11/23
 */
public interface BookCommentMapper extends BaseMapper<BookComment> {
    void addCommentCount(@Param("bookId") Long bookId);

    void desCommentCount(@Param("bookId") Long bookId);
}
