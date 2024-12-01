package com.aoyouao.novel.service;

import com.aoyouao.novel.core.common.resp.RestResp;
import com.aoyouao.novel.dto.resp.HomeBookRespDto;
import com.aoyouao.novel.dto.resp.HomeFriendLinkRespDto;


import java.util.List;

public interface HomeService {
    /**
     * 查询首页小说推荐列表
     */
    RestResp<List<HomeBookRespDto>> listHomeWorks();

    /**
     *首页友情链接列表查询
     */
    RestResp<List<HomeFriendLinkRespDto>> listHomeFriendLists();
}
