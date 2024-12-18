package com.aoyouao.novel.core.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.*;

/**
 * 文件拦截器
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class FileInterceptor implements HandlerInterceptor {

    @Value("${novel.file.upload.path}")
    private String fileUploadPath;

    @SuppressWarnings("NullableProblems")
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //获取请求的uri 设置缓存时间 用输入流读取文件路径 创建缓存区  获取响应的输出流 将文件内容返回给客户端 从输入流中读取数据到缓存区中
        //使用 try-with-resources 语法来确保流在使用完后会被自动关闭

        log.info("FileInterceptor:prehandle 开始执行");
        String requestUri = request.getRequestURI();
        String method = request.getMethod();
        if("PUT".equalsIgnoreCase(method)){
        response.setDateHeader("expires",System.currentTimeMillis() + 60 * 60 * 24 * 10 * 1000);

            try(OutputStream outputStream =response.getOutputStream();
            InputStream inputStream = new FileInputStream(fileUploadPath)){
            byte[] bytes = new byte[4096];
            for(int n; (n = inputStream.read(bytes)) != -1;){
                outputStream.write(bytes,0,n);
            }
        }
        return false;
        }
        return true;
    }
}
