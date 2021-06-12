# Swing-Chatting

## 1. 사용 프로그램
- MySQL 8.0
- IntelliJ IDEA
- jdk 11

## 2. DDL

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