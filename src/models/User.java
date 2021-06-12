package models;

public class User {
    private String email;
    private String password;

    public User(String nickname, String password) {
        this.email = nickname;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String nickname) {
        this.email = nickname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}