package com.example.eureka_tcp_server.test_client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

@Slf4j
public class NettyClient {

    private final EventLoopGroup group;
    private final Bootstrap bootstrap;
    private static int retryCnt = 0;

    public NettyClient() {
        group = new NioEventLoopGroup();
        bootstrap = new Bootstrap().group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) {
                        // Inbound = Bottom-Up (앞에서부터 실행)
                        // Outbound = Top-down (뒤에서부터 실행)
                        socketChannel.pipeline().addLast(
                                new StringEncoder(StandardCharsets.UTF_8), // OutBound - 4
                                new NettyClientOutboundHandler(), // Outbound - 3
                                new StringDecoder(StandardCharsets.UTF_8), // InBound - 1
                                new NettyClientInboundHandler() // Inbound - 2
                        );
                    }
                })
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000);
    }

    public Channel connect(final String host, final int port) throws InterruptedException {
        ChannelFuture future = bootstrap.connect(host, port)
                .addListener(new ChannelFutureListener() {
                    @Override
                    public void operationComplete(ChannelFuture f) throws Exception {
                        if (f.isSuccess()) {
                            log.info(">> 연결 성공!");
                        } else {
                            log.error(">> 연결 실패... (재시도 {}/5)", ++retryCnt);
                            f.channel().close();
                            if (retryCnt == 5) {
                                log.error(">> 연결을 종료합니다...");
                                close();
                            } else {
                                Thread.sleep(3000);
                                connect(host, port);
                            }
                        }
                    }
                });
        future.sync(); // 성공 할 때 까지 block
        return future.channel();
    }

    public void close() {
        group.shutdownGracefully();
    }

    public static void main(String[] args) throws InterruptedException {
        NettyClient client = new NettyClient();

        Channel channel = client.connect("localhost", 9090);
        ChannelFuture future;
        Scanner sc = new Scanner(System.in);
        while(true) {
            String str = sc.nextLine();
            future = channel.writeAndFlush(str);
            if ("exit".equals(str) || future.isDone()) {
                future = channel.close().sync();
                break;
            }
        }
        if (future.isSuccess()) {
            log.info("연결을 종료합니다.");
            client.close();
        } else {
            log.info("비정상 종료");
        }
    }
}
