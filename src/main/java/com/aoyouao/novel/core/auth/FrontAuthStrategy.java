package com.aoyouao.novel.core.auth;

import com.aoyouao.novel.core.common.exception.BusinessException;
import com.aoyouao.novel.core.util.JwtUtils;
import com.aoyouao.novel.manager.UserInfoCacheManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 前台门户系统 认证授权策略
 *
 * @author xiongxiaoyang
 * @date 2022/5/18
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class FrontAuthStrategy implements AuthStrategy {

    private final JwtUtils jwtUtils;

    private final UserInfoCacheManager userInfoCacheManager;

    @Override
    public void auth(String token, String requestUri) throws BusinessException {
        log.info("FrontAuthStrategy auth方法被调用");

        // 在调用 authSSO 前增加日志
        log.info("Calling authSSO with token: {}", token);

        // 统一账号认证
        authSSO(jwtUtils, userInfoCacheManager, token);

        // 方法调用后再加日志
        log.info("authSSO method called successfully.");
    }

}