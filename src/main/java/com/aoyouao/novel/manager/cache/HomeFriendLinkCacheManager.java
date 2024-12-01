package com.aoyouao.novel.manager.cache;

import com.aoyouao.novel.core.constant.CacheConsts;
import com.aoyouao.novel.core.constant.DatabaseConsts;
import com.aoyouao.novel.dao.entity.HomeFriendLink;
import com.aoyouao.novel.dao.mapper.HomeFriendLinkMapper;
import com.aoyouao.novel.dto.resp.HomeFriendLinkRespDto;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class HomeFriendLinkCacheManager {

    @Autowired
    private final HomeFriendLinkMapper homeFriendLinkMapper;

    @Cacheable(cacheManager = CacheConsts.REDIS_CACHE_MANAGER,
            value = CacheConsts.HOME_FRIEND_LINK_CACHE_NAME)
    public List<HomeFriendLinkRespDto> listHomeFriendLists(){
        QueryWrapper<HomeFriendLink> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc(DatabaseConsts.CommonColumnEnum.SORT.getName());
        return homeFriendLinkMapper.selectList(queryWrapper).stream().map(v ->{
            HomeFriendLinkRespDto homeFriendLinkRespDto = new HomeFriendLinkRespDto();
            homeFriendLinkRespDto.setLinkName(v.getLinkName());
            homeFriendLinkRespDto.setLinkUrl(v.getLinkUrl());
            return homeFriendLinkRespDto;
        }).toList();
    }
}
