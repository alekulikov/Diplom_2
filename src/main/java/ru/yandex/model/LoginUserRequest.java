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
}
