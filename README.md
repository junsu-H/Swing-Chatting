# Swing-Chatting

## DDL

```
DROP database IF EXISTS chatting;
create database chatting;
```

```
create table `chatting`.`member`(
    id INT(11) AUTO_INCREMENT  NOT NULL,
    nickname VARCHAR(10) NOT NULL ,
    password VARCHAR(100) NOT NULL,
    create_date  TIMESTAMP NOT NULL DEFAULT NOW(),
    PRIMARY KEY (id),
    UNIQUE KEY (nickname)
    
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
```