package ru.yandex.api;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import ru.yandex.model.LoginUserRequest;

import static io.restassured.RestAssured.given;

public class UserAuthentication extends BaseRestClient {

    private static final String LOGIN_PATH = "api/auth/login";
    private String accessToken;

    @Step("Авторизуемся пользователем")
    public ValidatableResponse loginUser(LoginUserRequest request) {
        ValidatableResponse response = given()
                .spec(getBaseSpec())
                .body(request)
                .when()
                .post(LOGIN_PATH)
                .then();
        saveAccessToken(response);
        return response;
    }

    @Step("Сохраняем токен")
    public void saveAccessToken(ValidatableResponse response) {
        try {
            this.accessToken = response.extract().path("accessToken").toString().substring(7);
        } catch (NullPointerException ignored) {
        }
    }

    @Step("Получаем токен")
    public String getAccessToken() {
        return accessToken;
    }

    @Step("Очищаем токен")
    public void flushAccessToken() {
        this.accessToken = null;
    }
}
