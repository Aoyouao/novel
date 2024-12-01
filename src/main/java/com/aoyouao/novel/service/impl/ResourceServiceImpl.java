package com.aoyouao.novel.service.impl;

import com.aoyouao.novel.core.common.resp.RestResp;
import com.aoyouao.novel.dto.resp.ImgVerifyCodeRespDto;
import com.aoyouao.novel.manager.cache.VerifyCodeManager;
import com.aoyouao.novel.service.ResourceService;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ResourceServiceImpl implements ResourceService {

    private final VerifyCodeManager verifyCodeManager;
    @Override
    public RestResp<ImgVerifyCodeRespDto> getImgVerifyCode() throws IOException {
        String sessionId = IdWorker.get32UUID();
        return RestResp.ok(ImgVerifyCodeRespDto.builder()
                .sessionId(sessionId)
                .img(verifyCodeManager.genImgVerifyCode(sessionId))
                .build());
    }
}
