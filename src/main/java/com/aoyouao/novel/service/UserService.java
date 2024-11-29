package com.aoyouao.novel.service;

import com.aoyouao.novel.core.common.resp.RestResp;
import com.aoyouao.novel.dto.req.UserLoginReqDto;
import com.aoyouao.novel.dto.req.UserRegisterReqDto;
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
}
