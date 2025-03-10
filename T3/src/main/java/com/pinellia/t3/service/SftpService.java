package com.pinellia.t3.service;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import com.pinellia.t3.config.SftpConfig;
import com.pinellia.t3.utils.SftpUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;


@Service
public class SftpService {

    // 定义 SFTP 操作
    private final SftpUtils sftpUtils;
    // 定义 SFTP 配置信息
    private final SftpConfig config;

    // 构造函数
    public SftpService(SftpUtils sftpUtils, SftpConfig config) {
        this.sftpUtils = sftpUtils;
        this.config = config;
        try {
            sftpUtils.connect(config.getHost(), config.getPort(),
                    config.getUsername(), config.getPassword());
        } catch (JSchException erro) {
            throw new RuntimeException("SFTP connection failed", erro);
        }
    }

    // 获取 SFTP 服务器指定根路径下的文件列表
    public List<String> getFileList() throws SftpException {
        return sftpUtils.listFiles(config.getRootPath());
    }

    // 获取文本文件内容
    public String getFileContent(String filename) throws Exception {
        return sftpUtils.readTextFile(config.getRootPath() + "/" + filename);
    }

    // 保存到 SFTP 服务器指定根路径下
    public void saveFile(String filename, String content) throws Exception {
        sftpUtils.saveTextFile(config.getRootPath() + "/" + filename, content);
    }

    // 获取图片二进制数据
    public byte[] getImageContent(String filename) throws Exception {
        try (InputStream is = sftpUtils.getChannel().get(config.getRootPath() + "/" + filename)) {
            return IOUtils.toByteArray(is);// 将输入流转换为字节数组
        }
    }
}
