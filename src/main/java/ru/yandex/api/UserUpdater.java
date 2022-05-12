package ru.yandex.api;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import ru.yandex.model.UpdateUserRequest;

import static io.restassured.RestAssured.given;

public class UserUpdater extends BaseRestClient {

    private static final String UPDATE_PATH = "api/auth/user";

    @Step("Обновляем данные пользователя")
    public ValidatableResponse updateUserDate(UpdateUserRequest request, String accessToken) {
        return accessToken != null
                ? given()
                .spec(getBaseSpec())
                .auth().oauth2(accessToken)
                .body(request)
                .when()
                .patch(UPDATE_PATH)
                .then()
                : given()
                .spec(getBaseSpec())
                .body(request)
                .when()
                .patch(UPDATE_PATH)
                .then();
    }

    @Step("Удаляем пользователя")
    public ValidatableResponse deleteUser(String accessToken) {
        return accessToken != null
                ? given()
                .spec(getBaseSpec())
                .auth().oauth2(accessToken)
                .when()
                .delete(UPDATE_PATH)
                .then()
                : given()
                .spec(getBaseSpec())
                .when()
                .delete(UPDATE_PATH)
                .then();
    }
}
