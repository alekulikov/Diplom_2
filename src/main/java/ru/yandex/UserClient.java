package ru.yandex;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import ru.yandex.model.*;

import static io.restassured.RestAssured.given;

public class UserClient extends BaseRestClient {

    private String accessToken;

    @Step("Создаем пользователя")
    public ValidatableResponse createUser(CreateUserRequest request) {
        return given()
                .spec(getBaseSpec())
                .body(request)
                .when()
                .post("api/auth/register")
                .then();
    }

    @Step("Авторизуемся пользователем")
    public ValidatableResponse loginUser(LoginUserRequest request) {
        return given()
                .spec(getBaseSpec())
                .body(request)
                .when()
                .post("api/auth/login")
                .then();
    }

    @Step("Получаем заказы пользователя")
    public ValidatableResponse getUsersOrders() {
        return accessToken != null
                ? given()
                .spec(getBaseSpec())
                .auth().oauth2(this.accessToken)
                .when()
                .get("api/orders")
                .then()
                : given()
                .spec(getBaseSpec())
                .when()
                .get("api/orders")
                .then();
    }

    @Step("Обновляем данные пользователя")
    public ValidatableResponse updateUserDate(UpdateUserRequest request) {
        return accessToken != null
                ? given()
                .spec(getBaseSpec())
                .auth().oauth2(this.accessToken)
                .body(request)
                .when()
                .patch("api/auth/user")
                .then()
                : given()
                .spec(getBaseSpec())
                .body(request)
                .when()
                .patch("api/auth/user")
                .then();
    }

    @Step("Удаляем пользователя")
    public ValidatableResponse deleteUser() {
        return accessToken != null
                ? given()
                .spec(getBaseSpec())
                .auth().oauth2(this.accessToken)
                .when()
                .delete("api/auth/user")
                .then()
                : given()
                .spec(getBaseSpec())
                .when()
                .delete("api/auth/user")
                .then();
    }

    @Step("Создаем заказ")
    public ValidatableResponse createOrder(CreateOrderRequest request) {
        return accessToken != null
                ? given()
                .spec(getBaseSpec())
                .auth().oauth2(this.accessToken)
                .body(request)
                .when()
                .post("api/orders")
                .then()
                : given()
                .spec(getBaseSpec())
                .body(request)
                .when()
                .post("api/orders")
                .then();
    }

    @Step("Получаем токен")
    public boolean getAccessToken(ValidatableResponse response) {
        try {
            this.accessToken = response.extract().path("accessToken").toString().substring(7);
            return true;
        } catch (NullPointerException e) {
            return false;
        }

    }

    @Step("Очищаем токен")
    public void flushAccessToken() {
        this.accessToken = null;
    }
}
