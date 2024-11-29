package com.aoyouao.novel.core.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import java.util.List;

/**
 * 跨域配置属性
 */
@ConfigurationProperties(prefix = "novel.cors")
@Data
public class CorsProperties {

    //允许跨域的域名的集合
    public List<String> allowOrigins;
}
