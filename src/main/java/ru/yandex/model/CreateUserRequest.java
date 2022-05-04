package ru.yandex.model;

public class CreateUserRequest {

    private String email;
    private String password;
    private String name;

    public CreateUserRequest(UserData userData) {
        this(userData.getEmail(), userData.getPassword(), userData.getName());
    }

    public CreateUserRequest(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }
}
