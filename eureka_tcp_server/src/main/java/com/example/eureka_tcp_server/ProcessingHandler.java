package com.example.eureka_tcp_server;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProcessingHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        log.info(">> 채널이 등록되었습니다");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String requestData = (String) msg;
        ChannelFuture future = ctx.writeAndFlush(requestData);
        future.addListener(ChannelFutureListener.CLOSE);
        log.info(">> 클라이언트로부터 메시지를 받았습니다: {}", requestData);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        log.info(">> 클라이언트로부터 모든 메시지를 받았습니다");
        ctx.close();
    }
}
