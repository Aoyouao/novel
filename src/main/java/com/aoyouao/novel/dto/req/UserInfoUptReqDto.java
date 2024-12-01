package com.aoyouao.novel.dto.req;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * 用户信息更改请求dto
 */
@Data
public class UserInfoUptReqDto {

    private Long userId;

    @Length(min = 2,max = 10)
    private String nickName;

    @Pattern(regexp="^/[^\s]{10,}\\.(png|PNG|jpg|JPG|jpeg|JPEG|gif|GIF|bpm|BPM)$")
    private String userPhoto;

    @Min(value = 0)
    @Max(value = 1)
    private Integer userSex;
}
