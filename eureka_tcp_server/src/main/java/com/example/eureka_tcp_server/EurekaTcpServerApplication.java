package com.example.eureka_tcp_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EurekaTcpServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(EurekaTcpServerApplication.class, args);
        new NettyServer(9000).run();
    }

}
