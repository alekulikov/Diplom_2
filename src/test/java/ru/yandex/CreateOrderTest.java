package ru.yandex;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.api.OrderOperations;
import ru.yandex.api.UserAuthentication;
import ru.yandex.api.UserCreator;
import ru.yandex.api.UserUpdater;
import ru.yandex.model.CreateOrderRequest;
import ru.yandex.model.CreateUserRequest;
import ru.yandex.model.LoginUserRequest;
import ru.yandex.model.UserData;

import java.util.List;

import static ru.yandex.Steps.checkStatusCode;
import static ru.yandex.Steps.parameterEqualsTo;

@DisplayName("Создание заказа")
public class CreateOrderTest {

    UserCreator user;
    UserAuthentication authentication;
    UserUpdater userUpdater;
    OrderOperations orderOperations;
    UserData userData;

    @Before
    public void setUp() {
        userData = UserDataGenerator.getRandom();
        user = new UserCreator();
        user.createUser(new CreateUserRequest(userData));
        authentication = new UserAuthentication();
        authentication.loginUser(new LoginUserRequest(userData));
        orderOperations = new OrderOperations();
    }

    @After
    public void tearDown() {
        userUpdater = new UserUpdater();
        userUpdater.deleteUser(authentication.getAccessToken());
    }

    @Test
    @DisplayName("Пользователь может создать заказ")
    public void orderCanBeCreated() {
        ValidatableResponse createOrderResponse = orderOperations.createOrder(new CreateOrderRequest(List.of(
                "61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa74")), authentication.getAccessToken());

        checkStatusCode(createOrderResponse, 200);
        parameterEqualsTo(createOrderResponse, "success", true);
    }

    @Test
    @DisplayName("Заказ может быть создан без токена")
    public void orderCanBeCreatedWithoutToken() {
        ValidatableResponse createOrderResponse = orderOperations.createOrder(new CreateOrderRequest(List.of(
                "61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa74")), null);

        checkStatusCode(createOrderResponse, 200);
        parameterEqualsTo(createOrderResponse, "success", true);
    }

    @Test
    @DisplayName("Заказ не может быть создан без ингредиентов")
    public void orderCanNotBeCreatedWithoutIngredients() {
        ValidatableResponse createOrderResponse = orderOperations.createOrder(new CreateOrderRequest(List.of()),
                authentication.getAccessToken());

        checkStatusCode(createOrderResponse, 400);
        parameterEqualsTo(createOrderResponse, "success", false);
        parameterEqualsTo(createOrderResponse, "message", "Ingredient ids must be provided");
    }

    @Test
    @DisplayName("Заказ не может быть создан с неверным хэшем ингредиентов")
    public void orderCanNotBeCreatedWithIncorrectIngredientsHash() {
        ValidatableResponse createOrderResponse = orderOperations.createOrder(new CreateOrderRequest(List.of(
                "_61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa74_")), authentication.getAccessToken());

        checkStatusCode(createOrderResponse, 500);
    }
}
