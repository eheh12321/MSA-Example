package com.example.eureka_tcp_server.test_client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import lombok.extern.slf4j.Slf4j;

import java.net.SocketAddress;

@Slf4j
public class NettyClientOutboundHandler extends ChannelOutboundHandlerAdapter {

    @Override
    public void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) throws Exception {
        log.info("## connect() - remoteAddress: {}, localAddress: {}", remoteAddress, localAddress);
        super.connect(ctx, remoteAddress, localAddress, promise);
    }

    @Override
    public void close(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
        log.info("## close()");
        super.close(ctx, promise);
    }

    @Override
    public void read(ChannelHandlerContext ctx) throws Exception {
        log.info("## ---- recv() ----");
        super.read(ctx);
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        log.info("## send() - {}", (String) msg);
        super.write(ctx, msg, promise);
    }

    @Override
    public void flush(ChannelHandlerContext ctx) throws Exception {
        log.info("## flush()");
        super.flush(ctx);
    }
}
