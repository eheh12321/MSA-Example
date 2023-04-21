package com.example.eureka_kafka_consumer;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class MessageConsumer {

    @Value("${my.netty.host}")
    private String host;

    @Value("${my.netty.port}")
    private int port;

    // TODO: 소켓 서버가 죽으면 어떻게 retry하는지, 예외처리, kafka 설정 더 알아보기,
    // consume한 데이터에 대해 재처리, 서버로부터 응답(성공/실패)을 받는 코드 추가
    // retryTemplate, kafka 파티션, consumer 이중화
    // kafka listener concurrency. 실패 났을 경우에 어떻게 고려해야 하는가. 로깅.
    @KafkaListener(topics = "myTopic", groupId = "myGroup")
    public void consume(String message) {
        log.info(">> Kafka Consume Message: {}", message);
        tcpSender(message); // Consume 데이터를 전송
    }

    private void tcpSender(String message) {
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(workerGroup);
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel channel) throws Exception {
                    channel.pipeline().addLast(
                            new StringEncoder(StandardCharsets.UTF_8),
                            new ClientHandler(message)
                    );
                }
            });
            System.out.printf(">> 연결할 TCP 서버 정보- Host: {%s}, Port: {%s}", host, port);
            ChannelFuture future = bootstrap.connect(host, port).sync();
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            workerGroup.shutdownGracefully();
        }
    }
}
