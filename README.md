# Swing-Chatting

## 1. 프로젝트 소개
자바 소켓을 사용해 개발한 채팅 프로그램이다.


## 2. 프로젝트 기능
- MySQL과 연동하여 회원가입 및 로그인
- AES/OFB 를 통한 메시지 암호화
- 영어 <-> 한국어 메시지 번역 제공
- 보내는 사람 오른쪽 정렬, 받는 사람 왼쪽 정렬
- 쪽지 기능 제공
- 음성 채팅 제공


## 3. 사용 프로그램
- JDK 11.0.9
- MySQL Workbench 8.0 CE
- IntelliJ IDEA Ultimate 2021.1.1.2
- 파파고 API
- mysql-connector-java-8.0.25.jar
- json-simple-1.1.1.jar


## 4. DDL

```
DROP database IF EXISTS chatting;
create database chatting;
```

```
create table `chatting`.`user`(
    id INT(11) AUTO_INCREMENT  NOT NULL,
    email VARCHAR(30) NOT NULL ,
    password VARCHAR(100) NOT NULL,
    nickname VARCHAR(30) DEFAULT "GUEST",
    create_date  TIMESTAMP NOT NULL DEFAULT NOW(),
    PRIMARY KEY (id),
    UNIQUE KEY (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
```


## 5. 실행 방법
### 5-1. MySQL Workbench 8.0 CE 실행 후 root 로그인
### 5-2. MySQL에 DDL문 붙여넣기
### 5-3. 인텔리제이에서 Multirun 플러그인 다운받기
### 5-4. Multirun을 통해 ChatServer, LoginGui, LoginGui 코드 순차적으로 실행 (LoginGui은 채팅에 참여할 유저 수만큼 실행)
### 5-5. GUI가 열리면 REGISTER -> LOGIN
---

## 6. 실행 화면
![Swing-Chatting](https://user-images.githubusercontent.com/63388678/127656534-ea0b24c8-c84b-4a31-a41f-c72dee7df3b4.png)