package com.me.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("netty")
@Data
public class NettyConfigure {
    private int port; // netty监听的端口
    private String path; // websocket访问路径
}
