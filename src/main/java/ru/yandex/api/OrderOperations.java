package ru.yandex.api;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import ru.yandex.model.CreateOrderRequest;

import static io.restassured.RestAssured.given;

public class OrderOperations extends BaseRestClient {

    private static final String ORDERS_PATH = "api/orders";

    @Step("Получаем заказы пользователя")
    public ValidatableResponse getUsersOrders(String accessToken) {
        return accessToken != null
                ? given()
                .spec(getBaseSpec())
                .auth().oauth2(accessToken)
                .when()
                .get(ORDERS_PATH)
                .then()
                : given()
                .spec(getBaseSpec())
                .when()
                .get(ORDERS_PATH)
                .then();
    }

    @Step("Создаем заказ")
    public ValidatableResponse createOrder(CreateOrderRequest request, String accessToken) {
        return accessToken != null
                ? given()
                .spec(getBaseSpec())
                .auth().oauth2(accessToken)
                .body(request)
                .when()
                .post(ORDERS_PATH)
                .then()
                : given()
                .spec(getBaseSpec())
                .body(request)
                .when()
                .post(ORDERS_PATH)
                .then();
    }
}
