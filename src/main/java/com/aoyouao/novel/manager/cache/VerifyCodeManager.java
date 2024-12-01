package com.aoyouao.novel.manager.cache;

import com.aoyouao.novel.core.common.util.ImgVerifyCodeUtils;
import com.aoyouao.novel.core.constant.CacheConsts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;
import java.util.Objects;

/**
 * 验证码管理类
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class VerifyCodeManager {

    private final StringRedisTemplate stringRedisTemplate;

    //生成图形验证码 并放入redis中
    public String genImgVerifyCode(String sessionId) throws IOException {
        String verifyCode = ImgVerifyCodeUtils.getRandomVerifyCode(4);
        String img = ImgVerifyCodeUtils.genVerifyCodeImg(verifyCode);
        log.debug("Storing code: {}", verifyCode);
        stringRedisTemplate.opsForValue().set(
                CacheConsts.IMG_VERIFY_CODE_CACHE_KEY+sessionId,verifyCode, Duration.ofMinutes(5));
        return img;
    }

    //校验图形验证码
    public boolean imgVerifyCodeOk(String sessionId, String verifyCode) {
        String storedCode = stringRedisTemplate.opsForValue().get(CacheConsts.IMG_VERIFY_CODE_CACHE_KEY + sessionId);
        log.debug("Retrieved code: {}", storedCode);  // 检查是否正确读取
        return Objects.equals(storedCode, verifyCode);
    }

    //从redis中删除验证码
    public void removeImgVerifyCode(String sessionId){
        stringRedisTemplate.delete(CacheConsts.IMG_VERIFY_CODE_CACHE_KEY+sessionId);
    }
}
