package com.akbar.controller.admin;

import com.akbar.constant.MessageConstant;
import com.akbar.result.Result;
import com.akbar.utils.AliOssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

/**
 * 通用接口
 */
@RestController
@RequestMapping("/admin/common")
@Slf4j
public class CommonController {

    @Autowired
    private AliOssUtil aliOssUtil;


    /**
     * 文件上传接口
     */
    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file) {
        log.info("要上传的文件：{}", file);

        try {
            // 原始文件名
            String originalFilename = file.getOriginalFilename();
            // 获取后缀
            String extension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
            // 生成新的文件名
            String objectName = UUID.randomUUID() + "." + extension;

            // 上传文件到阿里云oss
            String fileUrl = aliOssUtil.upload(file.getBytes(), objectName);
            if (fileUrl == null) {
                return Result.error(MessageConstant.UPLOAD_FAILED);
            }

            return Result.success(fileUrl);
        } catch (IOException e) {
            log.error("上传文件失败", e);
        }

        return Result.error(MessageConstant.UPLOAD_FAILED);
    }
}
