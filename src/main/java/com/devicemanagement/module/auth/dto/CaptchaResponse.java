package com.devicemanagement.module.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** 教学环境返回数字验证码，生产项目应替换为图片或短信验证码。 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "登录验证码响应")
public class CaptchaResponse {
    @Schema(description = "验证码唯一编号", example = "d0a3b5cc-8f50-4fd3-a0f0-123456789abc")
    private String captchaId;
    @Schema(description = "教学环境返回的数字验证码", example = "1234")
    private String captchaCode;
}
