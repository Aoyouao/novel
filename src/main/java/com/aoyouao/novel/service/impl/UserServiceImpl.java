package com.aoyouao.novel.service.impl;

import com.aoyouao.novel.core.common.constant.ErrorCodeEnum;
import com.aoyouao.novel.core.common.exception.BusinessException;
import com.aoyouao.novel.core.common.resp.RestResp;
import com.aoyouao.novel.core.constant.CacheConsts;
import com.aoyouao.novel.core.constant.SystemConfigConsts;
import com.aoyouao.novel.core.util.JwtUtils;
import com.aoyouao.novel.dao.entity.UserInfo;
import com.aoyouao.novel.dao.mapper.UserInfoMapper;
import com.aoyouao.novel.dto.req.UserLoginReqDto;
import com.aoyouao.novel.dto.req.UserRegisterReqDto;
import com.aoyouao.novel.dto.resp.UserLoginRespDto;
import com.aoyouao.novel.dto.resp.UserRegisterRespDto;
import com.aoyouao.novel.manager.VerifyCodeManager;
import com.aoyouao.novel.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.eclipse.angus.mail.smtp.DigestMD5;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Autowired
    private final UserInfoMapper userInfoMapper;

    private final VerifyCodeManager verifyCodeManager;

    private final JwtUtils jwtUtils;

    @Override
    public RestResp<UserRegisterRespDto> register(UserRegisterReqDto userRegisterReqDto) {
        //校验图形验证码是否正确
        if(!verifyCodeManager.imgVerifyCodeOk(userRegisterReqDto.getSessionId(),
                userRegisterReqDto.getVelCode())){
            throw new BusinessException(ErrorCodeEnum.USER_VERIFY_CODE_ERROR);
        }

        //校验手机号是否注册
        LambdaQueryWrapper<UserInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserInfo::getUsername,userRegisterReqDto.getUsername());
        if(userInfoMapper.selectCount(queryWrapper)>0){
            throw new BusinessException(ErrorCodeEnum.USER_NAME_EXIST);
        }

        //注册成功，保护用户信息
        UserInfo userInfo = new UserInfo();
        userInfo.setUsername(userRegisterReqDto.getUsername());
        userInfo.setNickName(userRegisterReqDto.getUsername());
        userInfo.setPassword(DigestUtils.md5DigestAsHex(userRegisterReqDto.getPassword().getBytes(StandardCharsets.UTF_8)));
        userInfo.setCreateTime(LocalDateTime.now());
        userInfo.setUpdateTime(LocalDateTime.now());
        userInfo.setSalt("0");
        userInfoMapper.insert(userInfo);

        //生成JWT并返回
        return RestResp.ok(
                UserRegisterRespDto.builder()
                        .uid(userInfo.getId())
                        .token(jwtUtils.generateToken(userInfo.getId(), SystemConfigConsts.NOVEL_FRONT_KEY))
                        .build()
        );
    }

    @Override
    public RestResp<UserLoginRespDto> login(UserLoginReqDto userLoginReqDto) {
        //查询用户信息是否存在
        LambdaQueryWrapper<UserInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserInfo::getUsername,userLoginReqDto.getUsername());
        UserInfo userInfo = userInfoMapper.selectOne(queryWrapper);
        if(Objects.isNull(userInfo)){
            throw new BusinessException(ErrorCodeEnum.USER_ACCOUNT_NOT_EXIST);
        }

        //校验密码
        if(!Objects.equals(userInfo.getPassword(),
                DigestUtils.md5DigestAsHex(userLoginReqDto.getPassword().getBytes(StandardCharsets.UTF_8)))){
            throw new BusinessException(ErrorCodeEnum.USER_PASSWORD_ERROR);
        }

        //登陆成功 生成jwt并返回
        return RestResp.ok(UserLoginRespDto.builder()
                .token(jwtUtils.generateToken(userInfo.getId(),SystemConfigConsts.NOVEL_FRONT_KEY))
                .uid(userInfo.getId())
                .nickName(userInfo.getNickName())
                .build());
    }
}
