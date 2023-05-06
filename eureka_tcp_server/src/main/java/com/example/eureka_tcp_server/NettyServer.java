package com.example.eureka_tcp_server;

import com.example.eureka_tcp_server.handler.ProcessingHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import lombok.RequiredArgsConstructor;

import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
public class NettyServer {

    private final int port;

    public void run() {
        // Non-blocking I/O
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(); // 외부에서 들어오는 클라이언트 연결 수락 (부모, 서버 소켓)
        NioEventLoopGroup workerGroup = new NioEventLoopGroup(); // 바탕으로 데이터 입출력 / 이벤트 처리 (자식, 클라이언트와 통신할 소켓)

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() { // 클라이언트와 통신할 소켓 생성할 때마다 수행
                        @Override
                        protected void initChannel(SocketChannel channel) {
                            // 핸들러가 여러개인 경우 앞에서부터 순서대로 적용 => 순서 중요 (뒤의 핸들러로 넘기기 가능)
                            channel.pipeline().addLast(
                                    new StringEncoder(StandardCharsets.UTF_8), // ChannelOutboundHandlerAdapter (송신)
                                    new StringDecoder(StandardCharsets.UTF_8), // ChannelInboundHandlerAdapter (수신)
                                    new ProcessingHandler()
                            );
                        }
                    }).option(ChannelOption.SO_BACKLOG, 128) // option -> 부모 설정 / 동시에 접속할 수 있는 소켓의 수
                    .childOption(ChannelOption.SO_KEEPALIVE, true); // childOption -> 자식 설정 / 주기적으로 KeepAlive 패킷 전송 (HeartBeat)

            ChannelFuture future = bootstrap.bind(port).sync(); // 클라이언트 연결이 들어올 때 까지 listen()
            future.channel().closeFuture().sync(); // 서버 소켓이 종료될 때 까지 대기
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            workerGroup.shutdownGracefully(); // 4-way handshake
            bossGroup.shutdownGracefully();
        }
    }
}
