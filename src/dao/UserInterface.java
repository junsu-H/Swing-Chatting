package dao;

import models.User;

public interface UserInterface {
    public String findByEmail(User user);

    public String findByPassword(User user);

    public int checkEmail(User user);

    public int save(User user);

    public int updateNickname(String nickname, User user);

    public int delete(User user);
}
