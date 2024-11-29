package com.aoyouao.novel.service.impl;

import com.aoyouao.novel.core.common.resp.RestResp;
import com.aoyouao.novel.dto.resp.HomeBookRespDto;
import com.aoyouao.novel.manager.HomeBookCacheManager;
import com.aoyouao.novel.service.HomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 首页模块实现类
 */
@Service
@RequiredArgsConstructor
public class HomeServiceImpl implements HomeService {

    @Autowired
    private final HomeBookCacheManager homeBookCacheManager;

    @Override
    public RestResp<List<HomeBookRespDto>> listHomeWorks() {
        List<HomeBookRespDto> respDtos = homeBookCacheManager.listHomeBooks();
        return RestResp.ok(respDtos);
    }
}
