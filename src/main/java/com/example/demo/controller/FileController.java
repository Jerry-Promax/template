package com.example.demo.controller;

import cn.hutool.core.io.FileUtil;
import com.example.demo.common.Ignore;
import com.example.demo.common.Result;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.UUID;

/**
 * 类功能描述
 * <p>
 * 作者：jerry
 * 日期：2025-07-03
 */
@RestController
@RequestMapping("/file")
public class FileController {
    @Value("${ip}")
    String ip;
    @Value("${server.port}")
    String port;
    private static final String ROOT_PATH = System.getProperty("user.dir") + File.separator + "files";

    @Ignore
    @PostMapping("/upload")
    public Result upload(MultipartFile file) throws IOException {
        // 确保存储目录存在
        File parentFile = new File(ROOT_PATH);
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }

        String originalFilename = file.getOriginalFilename();
        String mainName = FileUtil.mainName(originalFilename);
        String extName = FileUtil.extName(originalFilename);

        // 修复点1：使用ROOT_PATH常量而不是重新拼接路径
        File saveFile = new File(ROOT_PATH + File.separator + originalFilename);

        // 如果文件存在，生成唯一文件名
        if (saveFile.exists()) {
            // 修复点2：使用更可靠的UUID而不是时间戳
            String uniqueName = UUID.randomUUID().toString().replace("-", "") + "." + extName;
            saveFile = new File(ROOT_PATH + File.separator + uniqueName);
            originalFilename = uniqueName; // 更新文件名用于URL生成
        }

        file.transferTo(saveFile);

        // 修复点3：确保URL编码特殊字符
        String encodedFilename = URLEncoder.encode(originalFilename, "UTF-8");
        String url = "http://" + ip + ":" + port + "/file/download/" + encodedFilename;
        return Result.success(url);
    }

    @Ignore
    @GetMapping("/download/{fileName}")
    public void download(@PathVariable String fileName,
                         HttpServletResponse httpServletResponse
    ) throws IOException {
        // 修复点4：解码URL参数
        String decodedFilename = URLDecoder.decode(fileName, "UTF-8");
        String filePath = ROOT_PATH + File.separator + decodedFilename;

        if (!FileUtil.exist(filePath)) {
            httpServletResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        byte[] bytes = FileUtil.readBytes(filePath);
        try (ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream()) {
            servletOutputStream.write(bytes);
            servletOutputStream.flush();
        }
    }
}