package models;

public class Member {
    private Long id;
    private String email;
    private String password;

    public Member(String nickname, String password) {
        this.email = nickname;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String nickname){
        this.email = nickname;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public String getPassword() {
        return password;
    }
}