## 사용된 기술 스택
- Jdk 17
- Spring Boot 3.x
- Maven
- Spring Cloud gateway
- eureka
- Kafka 3.x
- netty
- JWT
- Swagger(openApi 3.x)

## 각 서비스의 역할
### 1. eureka_server (8761)
- eureka clients 관리자

### 2. eureka_gateway (8000)
- 수문장 역할
- 요청 경로에 따라 타 서비스에게 요청 전달
- GlobalFilter를 적용시켜 Authorization 헤더의 JWT 토큰 검증

### 3. eureka_auth_server (9000)
- 인증 서버 (`/auth`)
- `POST /login` 요청을 하면 인증 절차를 거친 후 JWT 토큰을 만들어준다

### 4. eureka_kafka_producer (8082)
- 포스트맨에서 메시지를 받아줄 웹 클라이언트 (`/send`)
- `POST /send` 요청을 하면 해당 Json 데이터를 string 형식으로 만든 다음에 카프카에 적재

### 5. eureka_kafka_consumer (8083)
- 카프카에서 메시지를 수신하는 역할
- 카프카에서 데이터를 받은 다음에 tcp 통신을 통해 그대로 넘긴다

### 6. eureka_tcp_server (8085)
- tcp 서버
- 데이터를 수신하면 로그를 남긴다