package com.aoyouao.novel;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.Map;

@SpringBootApplication
@MapperScan("com.aoyouao.novel.dao.mapper")
@EnableCaching
@Slf4j
public class NovelApplication {

    public static void main(String[] args) {
        log.info("啦啦啦啦啦啦啦啦啦Application is starting...");
        SpringApplication.run(NovelApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext context){
        return args -> {
            Map<String, CacheManager> beans = context.getBeansOfType(CacheManager.class);
            log.info("加载了如下缓存管理器：");
            beans.forEach((k,v)->{
                log.info("{}:{}",k,v.getClass().getName());
                log.info("缓存：{}",v.getCacheNames());
            });

        };
    }
}
