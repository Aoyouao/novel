package com.aoyouao.novel.service.impl;

import com.aoyouao.novel.core.common.constant.CommonConsts;
import com.aoyouao.novel.core.common.constant.ErrorCodeEnum;
import com.aoyouao.novel.core.common.exception.BusinessException;
import com.aoyouao.novel.core.common.resp.RestResp;
import com.aoyouao.novel.core.constant.DatabaseConsts;
import com.aoyouao.novel.core.constant.SystemConfigConsts;
import com.aoyouao.novel.core.util.JwtUtils;
import com.aoyouao.novel.dao.entity.UserBookshelf;
import com.aoyouao.novel.dao.entity.UserFeedback;
import com.aoyouao.novel.dao.entity.UserInfo;
import com.aoyouao.novel.dao.mapper.UserBookshelfMapper;
import com.aoyouao.novel.dao.mapper.UserFeedbackMapper;
import com.aoyouao.novel.dao.mapper.UserInfoMapper;
import com.aoyouao.novel.dto.req.UserInfoUptReqDto;
import com.aoyouao.novel.dto.req.UserLoginReqDto;
import com.aoyouao.novel.dto.req.UserRegisterReqDto;
import com.aoyouao.novel.dto.resp.UserInfoRespDto;
import com.aoyouao.novel.dto.resp.UserLoginRespDto;
import com.aoyouao.novel.dto.resp.UserRegisterRespDto;
import com.aoyouao.novel.manager.cache.VerifyCodeManager;
import com.aoyouao.novel.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
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
    @Autowired
    private final VerifyCodeManager verifyCodeManager;
    @Autowired
    private final JwtUtils jwtUtils;
    @Autowired
    private final UserFeedbackMapper userFeedbackMapper;
    @Autowired
    private final UserBookshelfMapper userBookshelfMapper;

    @Override
    public RestResp<UserRegisterRespDto> register(UserRegisterReqDto userRegisterReqDto) {
        //校验图形验证码是否正确
        if(!verifyCodeManager.imgVerifyCodeOk(userRegisterReqDto.getSessionId(),
                userRegisterReqDto.getVelCode())){
            throw new BusinessException(ErrorCodeEnum.USER_VERIFY_CODE_ERROR);
        }

        //校验手机号是否注册
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DatabaseConsts.UserInfoTable.COLUMN_USERNAME,userRegisterReqDto.getUsername());
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
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DatabaseConsts.UserInfoTable.COLUMN_USERNAME,userLoginReqDto.getUsername());
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

    @Override
    public RestResp<Void> updateUserInfo(UserInfoUptReqDto userInfoUptReqDto) {
        UserInfo userInfo = new UserInfo();
        userInfo.setId(userInfoUptReqDto.getUserId());
        userInfo.setNickName(userInfoUptReqDto.getNickName());
        userInfo.setUserSex(userInfoUptReqDto.getUserSex());
        userInfoMapper.updateById(userInfo);
        return RestResp.ok();
    }

    @Override
    public RestResp<UserInfoRespDto> getUserInfo(Long userId) {
        UserInfo userInfo = userInfoMapper.selectById(userId);
        return RestResp.ok(
                UserInfoRespDto.builder().nickName(userInfo.getNickName())
                        .userSex(userInfo.getUserSex()).build());
    }

    @Override
    public RestResp<Void> saveFeedback(Long userId, String content) {
        UserFeedback userFeedback = new UserFeedback();
        userFeedback.setUserId(userId);
        userFeedback.setContent(content);
        userFeedback.setCreateTime(LocalDateTime.now());
        userFeedback.setUpdateTime(LocalDateTime.now());
        userFeedbackMapper.insert(userFeedback);
        return RestResp.ok();
    }

    @Override
    public RestResp<Void> deleteFeedback(Long userId, Long id) {
        QueryWrapper<UserFeedback> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DatabaseConsts.CommonColumnEnum.ID.getName(), userId)
                .eq(DatabaseConsts.UserFeedBackTable.COLUMN_USER_ID,id);
        userFeedbackMapper.delete(queryWrapper);
        return RestResp.ok();
    }

    @Override
    public RestResp<Integer> getBookshelfStatus(Long userId, Long bookId) {
        QueryWrapper<UserBookshelf> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DatabaseConsts.UserBookshelfTable.COLUMN_USER_ID,userId)
                .eq(DatabaseConsts.UserBookshelfTable.COLUMN_BOOK_ID,bookId);
        return RestResp.ok(userBookshelfMapper.selectCount(queryWrapper) > 0 ?
                CommonConsts.YES : CommonConsts.NO);
    }


}
