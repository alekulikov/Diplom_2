package ru.yandex.model;

public class LoginUserRequest {

    private String email;
    private String password;

    public LoginUserRequest(UserData userData) {
        this(userData.getEmail(), userData.getPassword());
    }

    public LoginUserRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
