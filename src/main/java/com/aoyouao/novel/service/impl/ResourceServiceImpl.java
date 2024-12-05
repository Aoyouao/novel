package com.aoyouao.novel.service.impl;

import com.aoyouao.novel.core.common.constant.ErrorCodeEnum;
import com.aoyouao.novel.core.common.exception.BusinessException;
import com.aoyouao.novel.core.common.resp.RestResp;
import com.aoyouao.novel.core.constant.SystemConfigConsts;
import com.aoyouao.novel.dto.resp.ImgVerifyCodeRespDto;
import com.aoyouao.novel.manager.cache.VerifyCodeManager;
import com.aoyouao.novel.service.ResourceService;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ResourceServiceImpl implements ResourceService {

    @Autowired
    private final VerifyCodeManager verifyCodeManager;

    @Value("${novel.file.upload.path}")
    private String fileUploadPath;
    @Override
    public RestResp<ImgVerifyCodeRespDto> getImgVerifyCode() throws IOException {
        String sessionId = IdWorker.get32UUID();
        return RestResp.ok(ImgVerifyCodeRespDto.builder()
                .sessionId(sessionId)
                .img(verifyCodeManager.genImgVerifyCode(sessionId))
                .build());
    }

    @SneakyThrows
    @Override
    public RestResp<String> uploadImage(MultipartFile file) {
        //先根据当前时间生成一个存放路径 再从传过来的file中拿到文件名 拿到文件的后缀名拼接到随机生成的文件名后面
        //创建新文件路径 包括存储路径和文件名 判断文件路径的是否存在 不存在则创建该目录 创建失败抛异常
        //将上传的文件保存到新的文件路径下
        //判断上传的文件是否为图片
        LocalDateTime now = LocalDateTime.now();
        String savePath =
                SystemConfigConsts.IMAGE_UPLOAD_DIRECTORY
                +now.format(DateTimeFormatter.ofPattern("yyyy"))+ File.separator
                +now.format(DateTimeFormatter.ofPattern("MM"))+ File.separator
                +now.format(DateTimeFormatter.ofPattern("dd"));
        String oriName = file.getOriginalFilename();
        assert oriName != null;
        String saveFileName =  IdWorker.get32UUID()+oriName.substring(oriName.lastIndexOf("."));
        File saveFile = new File(fileUploadPath+savePath,saveFileName);
        if(!saveFile.getParentFile().exists()){
            boolean mkdirs = saveFile.getParentFile().mkdirs();
            if(!mkdirs){
                throw new BusinessException(ErrorCodeEnum.USER_UPLOAD_FILE_ERROR);
            }
        }
        file.transferTo(saveFile);
        if(Objects.isNull(ImageIO.read(saveFile))){
            Files.delete(saveFile.toPath());
            throw new BusinessException(ErrorCodeEnum.USER_UPLOAD_FILE_ERROR);
        }
        return RestResp.ok(savePath + File.separator + saveFileName);
    }


}
