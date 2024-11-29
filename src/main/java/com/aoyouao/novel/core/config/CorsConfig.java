package com.aoyouao.novel.core.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableConfigurationProperties(CorsProperties.class)
@RequiredArgsConstructor //用于自动生成一个构造函数，该构造函数包含所有 final 字段和 @NonNull 注解的字段
public class CorsConfig {

    private final CorsProperties corsProperties;

    @Bean
    public CorsFilter corsFilter(){
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        //允许的域
        for(String allowOrigin : corsProperties.getAllowOrigins()){
            corsConfiguration.addAllowedOrigin(allowOrigin);
        }
        //允许请求的头
        corsConfiguration.addAllowedHeader("*");
        //允许的请求方式
        corsConfiguration.addAllowedMethod("*");
        //是否允许携带cookie信息
        corsConfiguration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource corsConfigurationSource = new UrlBasedCorsConfigurationSource();
        //跨域配置适用于所有的API路径
        corsConfigurationSource.registerCorsConfiguration("/**",corsConfiguration);
        return new CorsFilter(corsConfigurationSource);
    }
}
