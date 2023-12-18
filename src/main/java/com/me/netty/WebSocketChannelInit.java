package com.me.netty;

import com.me.config.NettyConfigure;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 通道初始化对象
 */
@Component
public class WebSocketChannelInit extends ChannelInitializer {
    @Autowired
    NettyConfigure nettyConfigure;
    @Autowired
    WebSocketHandler webSocketHandler;

    @Override
    protected void initChannel(Channel channel) throws Exception {
        ChannelPipeline pipeline = channel.pipeline();
        // 对Http协议的支持
        pipeline.addLast(new HttpServerCodec());

        // 对大数据流的支持
        pipeline.addLast(new ChunkedWriteHandler());

         // post请求三部分 request line / request header/ message body
        // HttpObjectAggregator将多个信息转化成单一的request或者response对象
        pipeline.addLast(new HttpObjectAggregator(8000));

        // 将Http协议升级为ws协议，websocket的支持
        pipeline.addLast(new WebSocketServerProtocolHandler(nettyConfigure.getPath()));

        // 自定义处理handler
        pipeline.addLast(webSocketHandler);
    }
}
