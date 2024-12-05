package com.aoyouao.novel.service;

import com.aoyouao.novel.core.common.resp.PageRestDto;
import com.aoyouao.novel.core.common.resp.RestResp;
import com.aoyouao.novel.dto.req.BookSearchReqDto;
import com.aoyouao.novel.dto.resp.BookInfoRespDto;

/**
 * 搜索服务类
 */
public interface SearchService {

    RestResp<PageRestDto<BookInfoRespDto>> searchBooks(BookSearchReqDto condition);
}
