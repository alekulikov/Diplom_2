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
import ru.yandex.model.*;

import java.util.List;

import static ru.yandex.Steps.*;

@DisplayName("Получение списка заказов пользователя")
public class GetUsersOrdersTest {

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
        orderOperations.createOrder(new CreateOrderRequest(List.of("61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa74")),
                authentication.getAccessToken());
    }

    @After
    public void tearDown() {
        userUpdater = new UserUpdater();
        userUpdater.deleteUser(authentication.getAccessToken());
    }

    @Test
    @DisplayName("Список заказов пользователя может быть получен")
    public void ordersListMayBeReceived() {
        ValidatableResponse ordersListResponse = orderOperations.getUsersOrders(authentication.getAccessToken());
        GetOrdersListResponse orderList = ordersListResponse.extract().body().as(GetOrdersListResponse.class);

        checkStatusCode(ordersListResponse, 200);
        parameterEqualsTo(ordersListResponse, "success", true);
        parameterIsNot(ordersListResponse, "totalToday", 0);
        checkArrayListIsNotEmpty(orderList.getOrders());
    }

    @Test
    @DisplayName("Список заказов пользователя не может быть получен без авторизации")
    public void ordersListNotMayBeReceivedWithoutToken() {
        ValidatableResponse ordersListResponse = orderOperations.getUsersOrders(null);

        checkStatusCode(ordersListResponse, 401);
        parameterEqualsTo(ordersListResponse, "success", false);
        parameterEqualsTo(ordersListResponse, "message", "You should be authorised");
    }
}
