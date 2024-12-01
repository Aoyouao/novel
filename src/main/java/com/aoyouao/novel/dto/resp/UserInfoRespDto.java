package com.aoyouao.novel.dto.resp;

import lombok.Builder;
import lombok.Data;

/**
 * 用户信息响应DTO
 */
@Data
@Builder
public class UserInfoRespDto {

    private String nickName;

    private String userPhoto;

    private Integer userSex;
}
