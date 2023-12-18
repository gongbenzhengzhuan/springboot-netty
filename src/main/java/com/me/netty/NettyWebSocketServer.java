package com.me.netty;

import com.me.config.NettyConfigure;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;

/**
 * netty服务器
 */
@Component
public class NettyWebSocketServer implements Runnable{
    @Autowired
    NettyConfigure nettyConfigure;
    @Autowired
    WebSocketChannelInit webSocketChannelInit;
    private EventLoopGroup bossGroup = new NioEventLoopGroup(1);
    private EventLoopGroup workerGroup = new NioEventLoopGroup();

    /**
     * 资源关闭--在容器销毁时关闭
     */
    @PreDestroy
    public void close() {
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }

    @Override
    public void run() {
        try {// 创建服务端启动助手
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            // 设置线程组
            serverBootstrap.group(bossGroup, workerGroup);
            // 设置参数
            serverBootstrap.channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.DEBUG))
                    .childHandler(webSocketChannelInit);
            // 启动
            ChannelFuture channelFuture = serverBootstrap.bind(nettyConfigure.getPort()).sync();
            System.out.println("--netty服务端启动成功--");
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
