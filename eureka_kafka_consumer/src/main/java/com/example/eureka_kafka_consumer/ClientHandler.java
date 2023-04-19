package com.example.eureka_kafka_consumer;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ClientHandler extends ChannelInboundHandlerAdapter {

    private String message;

    public ClientHandler() {
    }

    public ClientHandler(String message) {
        this.message = message;
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        log.info(">> 채널이 등록되었습니다");
    }

    @Override // 채널이 입출력이 가능해짐
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info(">> 데이터를 전송했습니다: {}", message);
        ctx.writeAndFlush(message);
        ctx.close();
    }
}
