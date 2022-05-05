package ru.yandex.model;

public class UpdateUserRequest {

    private String email;
    private String name;

    public UpdateUserRequest(UserData userData) {
        this(userData.getEmail(), userData.getName());
    }

    public UpdateUserRequest(String email, String name) {
        this.email = email;
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
