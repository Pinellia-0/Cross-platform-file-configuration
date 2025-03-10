package com.pinellia.t3.utils;

import com.jcraft.jsch.*;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;

@Component
public class SftpUtils {
    //SFTP链接
    private ChannelSftp channel;
    //SFTP会话
    private Session session;

    public void connect(String host, int port, String username, String password) throws JSchException {
        // 创建JSch对象
        JSch jsch = new JSch();
        session = jsch.getSession(username, host, port);//创建会话
        session.setPassword(password);//秘密
        session.setConfig("StrictHostKeyChecking", "no");//忽略主机密钥检查
        session.connect();//连接会话
        channel = (ChannelSftp) session.openChannel("sftp");//打开通道
        channel.connect();//链接通道
    }

    //列出指定 SFTP 路径下的所有非隐藏文件和目录的名称。
    public List<String> listFiles(String path) throws SftpException {
        List<String> files = new ArrayList<>();
        Vector<ChannelSftp.LsEntry> entries = channel.ls(path);
        for (ChannelSftp.LsEntry entry : entries) {
            if (!entry.getFilename().startsWith(".")) {
                files.add(entry.getFilename());
            }
        }
        return files;
    }

    //读取指定 SFTP 路径下的文本文件内容，并将其作为一个字符串返回
    public String readTextFile(String path) throws IOException, SftpException {
        try (InputStream is = channel.get(path);
             BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            return reader.lines().collect(Collectors.joining("\n"));
        }
    }


    //指定的文本内容保存到指定的 SFTP 路径下的文件中
    public void saveTextFile(String path, String content) throws SftpException, IOException {
        try (OutputStream os = channel.put(path)) {
            os.write(content.getBytes(StandardCharsets.UTF_8));
        }
    }

    // 断开连接
    public void disconnect() {
        if (channel != null) channel.disconnect();//关闭通道
        if (session != null) session.disconnect();//关闭会话
    }

    public ChannelSftp getChannel() {
        return this.channel;
    }

}
