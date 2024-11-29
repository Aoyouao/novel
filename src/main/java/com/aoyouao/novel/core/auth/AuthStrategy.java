package com.aoyouao.novel.core.auth;

import com.aoyouao.novel.core.common.constant.ErrorCodeEnum;
import com.aoyouao.novel.core.common.exception.BusinessException;
import java.util.Objects;

import com.aoyouao.novel.core.constant.SystemConfigConsts;
import com.aoyouao.novel.core.util.JwtUtils;
import com.aoyouao.novel.dto.UserInfoDto;
import com.aoyouao.novel.manager.UserInfoCacheManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

/**
 * 策略模式实现用户认证授权功能
 */
public interface AuthStrategy {

    /**
     * 用户认证授权
     *
     * @param token      登录 token
     * @param requestUri 请求的 URI
     * @throws BusinessException 认证失败则抛出业务异常
     */
    void auth(String token, String requestUri) throws BusinessException;

    /**
     * 前台多系统单点登录统一账号认证授权（门户系统、作家系统以及后面会扩展的漫画系统和视频系统等）
     *
     * @param jwtUtils             jwt 工具
     * @param userInfoCacheManager 用户缓存管理对象
     * @param token                token 登录 token
     * @return 用户ID
     */
    default Long authSSO(JwtUtils jwtUtils, UserInfoCacheManager userInfoCacheManager,
                         String token) {
        System.out.println("authSSO method entered");
        if (!StringUtils.hasText(token)) {
            // token 为空
            throw new BusinessException(ErrorCodeEnum.USER_LOGIN_EXPIRED);
        }
        System.out.println("Token is valid, parsing userId from token.");
        Long userId = jwtUtils.parseToken(token, SystemConfigConsts.NOVEL_FRONT_KEY);
        if (Objects.isNull(userId)) {
            // token 解析失败
            throw new BusinessException(ErrorCodeEnum.USER_LOGIN_EXPIRED);
        }
        System.out.println("Parsed userId: " + userId);
        UserInfoDto userInfo = userInfoCacheManager.getUser(userId);
        if (Objects.isNull(userInfo)) {
            // 用户不存在
            throw new BusinessException(ErrorCodeEnum.USER_ACCOUNT_NOT_EXIST);
        }


        // 打印日志，确保 userId 已经正确设置
        System.out.println("Setting userId to thread local: " + userId);
        // 设置 userId 到当前线程
        UserHolder.setUserId(userId);
        // 返回 userId
        return userId;
    }

}
