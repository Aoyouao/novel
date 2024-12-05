package com.aoyouao.novel.controller.front;

import com.aoyouao.novel.core.common.resp.RestResp;
import com.aoyouao.novel.core.constant.ApiRouterConsts;
import com.aoyouao.novel.dto.resp.ImgVerifyCodeRespDto;
import com.aoyouao.novel.service.ResourceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 前台门户-资源(图片/视频/文档)模块 API 控制器
 */
@Tag(name = "ResourceController", description = "前台门户-资源模块")
@RestController
@RequiredArgsConstructor
@RequestMapping(ApiRouterConsts.API_FRONT_RESOURCE_URL_PREFIX)
public class ResourceController {

    @Autowired
    private final ResourceService resourceService;


    //获取图片验证码接口
    @GetMapping("img_verify_code")
    public RestResp<ImgVerifyCodeRespDto> getImgVerifyCode() throws IOException {
        return resourceService.getImgVerifyCode();
    }

    //上传图片
    @PostMapping("/image")
    public RestResp<String> uploadImage(@RequestParam("file") MultipartFile file){
        return resourceService.uploadImage(file);
    }

}

