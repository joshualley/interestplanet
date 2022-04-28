package com.example.interestplanet.model;

public class User extends AbstractModel {
    private String username;
    private String password;
    private String sign;
    private String avatarUrl;

    public User() {}

    public User(String username, String password, String sign, String avatarUrl) {
        this.username = username;
        this.password = password;
        this.sign = sign;
        this.avatarUrl = avatarUrl;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", sign='" + sign + '\'' +
                ", avatarUrl='" + avatarUrl + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
