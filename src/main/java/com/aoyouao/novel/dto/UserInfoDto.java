package com.aoyouao.novel.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户信息DTO
 * 通常用于在不同的层之间传输数据，尤其是需要将对象通过网络传输或者保存到数据库中
 */
@Data
@Builder
public class UserInfoDto implements Serializable {

    //用于标识类的版本号，它在反序列化过程中用于确保类的版本一致
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private Integer status;
}
