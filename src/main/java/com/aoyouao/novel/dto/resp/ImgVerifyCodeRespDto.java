package com.aoyouao.novel.dto.resp;

/**
 * 图片验证码响应DTO
 */

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
@Data
@Builder
public class ImgVerifyCodeRespDto {

    //当前会话id 用于标识该图形验证码属于哪个会话
    @Schema(description = "sessionId")
    private String sessionId;

    //Base64编码后的图片
    @Schema(description = "img")
    private String img;
}
