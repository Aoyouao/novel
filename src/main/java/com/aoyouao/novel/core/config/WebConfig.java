package com.aoyouao.novel.core.config;

import com.aoyouao.novel.core.constant.ApiRouterConsts;
import com.aoyouao.novel.core.constant.SystemConfigConsts;
import com.aoyouao.novel.core.interceptor.AuthInterceptor;
import com.aoyouao.novel.core.interceptor.FileInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Spring Web Mvc 相关配置不要加 @EnableWebMvc 注解，否则会导致 jackson 的全局配置失效。因为 @EnableWebMvc 注解会导致
 * WebMvcAutoConfiguration 自动配置失效
 */
@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final AuthInterceptor authInterceptor;

    private final FileInterceptor fileInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        //文件访问拦截
        registry.addInterceptor(fileInterceptor)
                .addPathPatterns(ApiRouterConsts.API_FRONT_RESOURCE_URL_PREFIX +SystemConfigConsts.IMAGE_UPLOAD_DIRECTORY+"**")
                .order(1);

        // 权限认证拦截
        registry.addInterceptor(authInterceptor)
            // 拦截会员中心相关请求接口
            .addPathPatterns(ApiRouterConsts.API_FRONT_URL_PREFIX + "/**",
                // 拦截作家后台相关请求接口
                ApiRouterConsts.API_AUTHOR_URL_PREFIX + "/**",
                // 拦截平台后台相关请求接口
                ApiRouterConsts.API_ADMIN_URL_PREFIX + "/**")
            // 放行登录注册相关请求接口
            .excludePathPatterns(
                    ApiRouterConsts.API_FRONT_USER_URL_PREFIX + "/register",
                    ApiRouterConsts.API_FRONT_USER_URL_PREFIX + "/login",
                    ApiRouterConsts.API_ADMIN_URL_PREFIX + "/login",
                    ApiRouterConsts.API_FRONT_RESOURCE_URL_PREFIX+"/img_verify_code",
                    ApiRouterConsts.API_FRONT_HOME_URL_PREFIX + "/**",
                    ApiRouterConsts.API_FRONT_RESOURCE_URL_PREFIX+"/**")
            .order(2);

    }
}
