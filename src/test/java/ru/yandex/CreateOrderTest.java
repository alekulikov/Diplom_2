package ru.yandex;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.model.CreateOrderRequest;
import ru.yandex.model.CreateUserRequest;
import ru.yandex.model.LoginUserRequest;
import ru.yandex.model.UserData;

import java.util.List;

import static ru.yandex.Steps.checkStatusCode;
import static ru.yandex.Steps.parameterEqualsTo;

@DisplayName("Создание заказа")
public class CreateOrderTest {

    UserClient client;
    UserData user;

    @Before
    public void setUp() {
        client = new UserClient();
        user = UserDataGenerator.getRandom();
        client.getAccessToken(client.createUser(new CreateUserRequest(user)));
    }

    @After
    public void tearDown() {
        client.getAccessToken(client.loginUser(new LoginUserRequest(user)));
        client.deleteUser();
    }

    @Test
    @DisplayName("Пользователь может создать заказ")
    public void orderCanBeCreated() {
        ValidatableResponse createOrderResponse = client.createOrder(new CreateOrderRequest(List.of(
                "61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa74")));

        checkStatusCode(createOrderResponse, 200);
        parameterEqualsTo(createOrderResponse, "success", true);
    }

    @Test
    @DisplayName("Заказ может быть создан без токена")
    public void orderCanBeCreatedWithoutToken() {
        client.flushAccessToken();
        ValidatableResponse createOrderResponse = client.createOrder(new CreateOrderRequest(List.of(
                "61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa74")));

        checkStatusCode(createOrderResponse, 200);
        parameterEqualsTo(createOrderResponse, "success", true);
    }

    @Test
    @DisplayName("Заказ не может быть создан без ингредиентов")
    public void orderCanNotBeCreatedWithoutIngredients() {
        ValidatableResponse createOrderResponse = client.createOrder(new CreateOrderRequest(List.of()));

        checkStatusCode(createOrderResponse, 400);
        parameterEqualsTo(createOrderResponse, "success", false);
        parameterEqualsTo(createOrderResponse, "message", "Ingredient ids must be provided");
    }

    @Test
    @DisplayName("Заказ не может быть создан с неверным хэшем ингредиентов")
    public void orderCanNotBeCreatedWithIncorrectIngredientsHash() {
        ValidatableResponse createOrderResponse = client.createOrder(new CreateOrderRequest(List.of(
                "_61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa74_")));

        checkStatusCode(createOrderResponse, 500);
    }
}
