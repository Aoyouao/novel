package com.aoyouao.novel.controller.front;

/**
 * 前台门户-首页模块 API 控制器
 */

import com.aoyouao.novel.core.common.resp.RestResp;
import com.aoyouao.novel.core.constant.ApiRouterConsts;
import com.aoyouao.novel.dao.entity.HomeFriendLink;
import com.aoyouao.novel.dto.resp.HomeBookRespDto;
import com.aoyouao.novel.dto.resp.HomeFriendLinkRespDto;
import com.aoyouao.novel.service.HomeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "HomeController", description = "前台门户-首页模块")
@RestController
@RequestMapping(ApiRouterConsts.API_FRONT_HOME_URL_PREFIX)
@RequiredArgsConstructor
public class HomeController {

    @Autowired
    private final HomeService homeService;

    @GetMapping("books")
    public RestResp<List<HomeBookRespDto>> listHomeBooks(){
        return homeService.listHomeWorks();
    }

    @GetMapping("friend_Link/list")
    public RestResp<List<HomeFriendLinkRespDto>> listHomeFriendLists(){
        return homeService.listHomeFriendLists();
    }
}
