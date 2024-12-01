package com.aoyouao.novel.manager.dao;

import com.aoyouao.novel.core.constant.DatabaseConsts;
import com.aoyouao.novel.dao.entity.UserInfo;
import com.aoyouao.novel.dao.mapper.UserInfoMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 用户模块dao管理类
 */
@Component
@RequiredArgsConstructor
public class UserDaoManager {

    private final UserInfoMapper userInfoMapper;
    //根据用户id集合批量查询用户信息
    public List<UserInfo> listUsers(List<Long> userIds){
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.in(DatabaseConsts.CommonColumnEnum.ID.getName(),userIds);
        return userInfoMapper.selectList(queryWrapper);
    }

}
