package com.pinellia.t3.controller;

import org.springframework.http.MediaType;
import org.springframework.ui.Model;
import com.pinellia.t3.service.SftpService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@Controller
public class FileController {
    private final SftpService sftpService;

    public FileController(SftpService sftpService) {
        this.sftpService = sftpService;
    }

    // 显示文件列表
    @GetMapping("/")
    public String index(Model model) throws Exception {
        model.addAttribute("files", sftpService.getFileList());// 将文件列表传递给前端
        return "index";// 返回index.html
    }

    // 获取图片
    @GetMapping(value = "/image", produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    @ResponseBody
    public byte[] getImage(@RequestParam String filename) throws Exception {
        return sftpService.getImageContent(filename);// 返回图片二进制数据
    }

    // 获取文本内容
    @GetMapping("/content")
    @ResponseBody
    public String getContent(@RequestParam String filename) throws Exception {
        return sftpService.getFileContent(filename);// 返回文本内容
    }

    // 保存文件
    @PostMapping("/save")
    @ResponseBody
    public ResponseEntity<?> saveFile(@RequestBody Map<String, String> request) {
        try {
            String filename = request.get("filename");
            String content = request.get("content");
            if (filename == null || content == null) {
                return ResponseEntity.badRequest().body("文件名或内容不能为空");
            }
            sftpService.saveFile(filename, content);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).body("保存失败：" + e.getMessage());
        }
    }
}
