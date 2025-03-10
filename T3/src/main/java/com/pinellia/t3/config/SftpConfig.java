package com.pinellia.t3.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Configuration
@ConfigurationProperties(prefix = "sftp")
public class SftpConfig {
    // 服务器地址
    private String host;
    // 端口号（默认22）
    private int port;
    // 登录用户名
    private String username;
    // 登录密码
    private String password;
    // 要访问的服务器路径
    private String rootPath;

    // Getters and setters
    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRootPath(String rootPath) {
        this.rootPath = rootPath;
    }

    public String getHost() {
        return host;
    }

    public String getRootPath() {
        return rootPath;
    }

    public String getUsername() {
        return username;
    }

    public int getPort() {
        return port;
    }

    public String getPassword() {
        return password;
    }
}