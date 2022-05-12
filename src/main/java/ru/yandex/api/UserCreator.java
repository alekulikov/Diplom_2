package ru.yandex.api;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import ru.yandex.model.*;

import static io.restassured.RestAssured.given;

public class UserCreator extends BaseRestClient {

    private static final String REGISTRY_PATH = "api/auth/register";

    @Step("Создаем пользователя")
    public ValidatableResponse createUser(CreateUserRequest request) {
        return given()
                .spec(getBaseSpec())
                .body(request)
                .when()
                .post(REGISTRY_PATH)
                .then();
    }

    @Step("Авторизуемся пользователем")
    public ValidatableResponse loginUser(LoginUserRequest request) {
        return given()
                .spec(getBaseSpec())
                .body(request)
                .when()
                .post(REGISTRY_PATH)
                .then();
    }
}
