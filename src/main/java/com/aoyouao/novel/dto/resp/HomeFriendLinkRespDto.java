package com.aoyouao.novel.dto.resp;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 首页友情链接响应DTO
 */

@Data
public class HomeFriendLinkRespDto implements Serializable {

    @Serial
    private final static long serialVersionUID = 1L;

    //链接名
    private String linkName;

    //链接url
    private String linkUrl;
}
