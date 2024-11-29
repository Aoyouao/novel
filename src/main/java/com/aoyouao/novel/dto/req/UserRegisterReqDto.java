package com.aoyouao.novel.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * 用户注册请求DTO
 */
@Data
public class UserRegisterReqDto {

    @Schema(description = "手机号")
    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp="^1[3|4|5|6|7|8|9][0-9]{9}$",message="手机号格式不正确！")
    private String username;

    @Schema(description = "密码")
    @NotBlank(message="密码不能为空！")
    private String password;

    @Schema(description = "验证码")
    @NotBlank(message="验证码不能为空！")
    @Pattern(regexp="^\\d{4}$",message="验证码格式不正确！")
    private String velCode;

    @Schema(description = "sessionId")
    @NotBlank
    @Length(min = 32,max = 32)
    //请求会话标识，用来标识图形验证码属于哪个会话
    private String sessionId;
}