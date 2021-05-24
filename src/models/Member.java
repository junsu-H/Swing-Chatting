package models;

public class Member {
    private Long id;
    private String nickname;
    private String password;

    public Member(String nickname, String password) {
        this.nickname = nickname;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public String getNickname() {
        return nickname;
    }

    public String getPassword() {
        return password;
    }
}