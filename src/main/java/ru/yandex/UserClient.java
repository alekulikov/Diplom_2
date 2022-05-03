package ru.yandex;

import io.restassured.response.ValidatableResponse;
import ru.yandex.model.*;

import static io.restassured.RestAssured.given;

public class UserClient extends BaseRestClient {

    private String accessToken;

    public ValidatableResponse createUser(CreateUserRequest request) {
        return given()
                .spec(getBaseSpec())
                .body(request)
                .log().all(true)
                .when()
                .post("api/auth/register")
                .then()
                .log().all(true);
    }

    public ValidatableResponse loginUser(LoginUserRequest request) {
        return given()
                .spec(getBaseSpec())
                .body(request)
                .log().all(true)
                .when()
                .post("api/auth/login")
                .then()
                .log().all(true);
    }

    public ValidatableResponse getUsersOrders() {
        return accessToken != null
                ? given()
                .spec(getBaseSpec())
                .auth().oauth2(this.accessToken)
                .log().all(true)
                .when()
                .get("api/orders")
                .then()
                .log().all(true)
                : given()
                .spec(getBaseSpec())
                .log().all(true)
                .when()
                .get("api/orders")
                .then()
                .log().all(true);
    }

    public ValidatableResponse updateUserDate(UpdateUserRequest request) {
        return accessToken != null
                ? given()
                .spec(getBaseSpec())
                .auth().oauth2(this.accessToken)
                .body(request)
                .log().all(true)
                .when()
                .patch("api/auth/user")
                .then()
                .log().all(true)
                : given()
                .spec(getBaseSpec())
                .body(request)
                .log().all(true)
                .when()
                .patch("api/auth/user")
                .then()
                .log().all(true);
    }

    public ValidatableResponse deleteUser() {
        return accessToken != null
                ? given()
                .spec(getBaseSpec())
                .auth().oauth2(this.accessToken)
                .log().all(true)
                .when()
                .delete("api/auth/user")
                .then()
                .log().all(true)
                : given()
                .spec(getBaseSpec())
                .log().all(true)
                .when()
                .delete("api/auth/user")
                .then()
                .log().all(true);
    }

    public ValidatableResponse createOrder(CreateOrderRequest request) {
        return accessToken != null
                ? given()
                .spec(getBaseSpec())
                .auth().oauth2(this.accessToken)
                .body(request)
                .log().all(true)
                .when()
                .post("api/orders")
                .then()
                .log().all(true)
                : given()
                .spec(getBaseSpec())
                .body(request)
                .log().all(true)
                .when()
                .post("api/orders")
                .then()
                .log().all(true);
    }

    public boolean getAccessToken(ValidatableResponse response) {
        try {
            this.accessToken = response.extract().path("accessToken").toString().substring(7);
            return true;
        } catch (NullPointerException e) {
            return false;
        }

    }

    public void flushAccessToken() {
        this.accessToken = null;
    }
}
