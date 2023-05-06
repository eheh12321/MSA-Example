package com.example.eureka_tcp_server.test_client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NettyClientInboundHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) {
        log.info(">> 이벤트 루프 등록");
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) {
        log.info(">> 이벤트 루프 제거");
        ctx.close();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        log.info(">> 입출력 준비 완료");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        log.info(">> 채널 비활성화");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        log.info(">> 데이터 수신: {}", (String) msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        log.info(">> 데이터 수신 완료");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.info("Exception: {}", cause.getMessage());
        ctx.close();
    }
}
