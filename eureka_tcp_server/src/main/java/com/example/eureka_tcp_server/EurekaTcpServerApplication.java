package com.example.eureka_tcp_server;

import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
public class EurekaTcpServerApplication {
    public static void main(String[] args) {
        new NettyServer(9000).run();
    }
}
