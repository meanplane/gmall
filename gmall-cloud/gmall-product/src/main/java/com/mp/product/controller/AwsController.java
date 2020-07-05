package com.mp.product.controller;

import com.github.tobato.fastdfs.domain.conn.FdfsWebServer;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.mp.common.utils.R;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

/**
 * Author: Xiaoer
 * Date: 2020-06-17
 */
@RestController
@RequestMapping("/aws")
public class AwsController {

    @Autowired
    private FastFileStorageClient storageClient;

    @Autowired
    private FdfsWebServer fdfsWebServer;


    @PostMapping("/imgUpload")
    public R imgUpload(@RequestParam("file") MultipartFile file) throws IOException {

        StorePath storePath = storageClient.uploadFile(file.getInputStream(), file.getSize(), FilenameUtils.getExtension(file.getOriginalFilename()), null);
        String url = getResAccessUrl(storePath);

        return R.ok().put("data", url);
    }

    // 封装图片完整URL地址
    private String getResAccessUrl(StorePath storePath) {
        return fdfsWebServer.getWebServerUrl() + storePath.getFullPath();
    }
}
