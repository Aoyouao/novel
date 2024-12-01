package com.aoyouao.novel.service;

import com.aoyouao.novel.core.common.resp.RestResp;
import com.aoyouao.novel.dto.req.UserInfoUptReqDto;
import com.aoyouao.novel.dto.req.UserLoginReqDto;
import com.aoyouao.novel.dto.req.UserRegisterReqDto;
import com.aoyouao.novel.dto.resp.UserInfoRespDto;
import com.aoyouao.novel.dto.resp.UserLoginRespDto;
import com.aoyouao.novel.dto.resp.UserRegisterRespDto;

/**
 * 用户注册
 */
public interface UserService {

    //用户注册
    RestResp<UserRegisterRespDto> register(UserRegisterReqDto userRegisterReqDto);

    //用户登录
    RestResp<UserLoginRespDto> login(UserLoginReqDto userLoginReqDto);

    //用户修改信息
    RestResp<Void> updateUserInfo(UserInfoUptReqDto userInfoUptReqDto);

    //用户查询信息
    RestResp<UserInfoRespDto> getUserInfo(Long userId);

    //用户反馈
    RestResp<Void> saveFeedback(Long userId,String content);

    //用户反馈删除
    RestResp<Void> deleteFeedback(Long userId,Long id);

    //查询书架状态
    RestResp<Integer> getBookshelfStatus(Long userId,Long bookId);
}
