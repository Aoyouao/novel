package com.aoyouao.novel.dao.mapper;

import com.aoyouao.novel.dao.entity.BookInfo;
import com.aoyouao.novel.dto.req.BookSearchReqDto;
import com.aoyouao.novel.dto.resp.BookInfoRespDto;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 小说信息 Mapper 接口
 * </p>
 *
 * @author ${author}
 * @date 2024/11/23
 */
public interface BookInfoMapper extends BaseMapper<BookInfo> {

    //小说搜索
    List<BookInfo> searchBooks(IPage<BookInfoRespDto> page, BookSearchReqDto condition);

    void addVisitCount(@Param("bookId") Long bookId);
}
