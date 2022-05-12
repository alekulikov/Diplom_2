package ru.yandex;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class Steps {

    @Step("Проверяем код ответа")
    public static void checkStatusCode(ValidatableResponse response, int expectedCode) {
        response.assertThat().statusCode(expectedCode);
    }

    @Step("Проверяем, что параметр {jsonPath} равен {expected}")
    public static void parameterEqualsTo(ValidatableResponse response, String jsonPath, Object expected) {
        assertThat(response.extract().path(jsonPath), equalTo(expected));
    }

    @Step("Проверяем, что параметр {jsonPath} не равен {unexpected}")
    public static void parameterIsNot(ValidatableResponse response, String jsonPath, Object unexpected) {
        assertThat(response.extract().path(jsonPath), is(not(unexpected)));
    }

    @Step("Проверяем, что список не пустой")
    public static <T> void checkArrayListIsNotEmpty(List<T> arrayList) {
        assertThat(arrayList.isEmpty(), equalTo(false));
    }
}
