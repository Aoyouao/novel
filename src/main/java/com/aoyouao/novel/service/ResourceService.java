package com.aoyouao.novel.service;

import com.aoyouao.novel.core.common.resp.RestResp;
import com.aoyouao.novel.dto.resp.ImgVerifyCodeRespDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ResourceService {

    RestResp<ImgVerifyCodeRespDto> getImgVerifyCode() throws IOException;

    /**
     * 上传图片
     */
    RestResp<String> uploadImage(MultipartFile file);
}
