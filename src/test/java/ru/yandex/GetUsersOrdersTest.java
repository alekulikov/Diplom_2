package ru.yandex;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.model.*;

import java.util.List;

import static ru.yandex.Steps.*;

@DisplayName("Получение списка заказов пользователя")
public class GetUsersOrdersTest {

    UserClient client;
    UserData user;

    @Before
    public void setUp() {
        client = new UserClient();
        user = UserDataGenerator.getRandom();
        client.getAccessToken(client.createUser(new CreateUserRequest(user)));
        client.createOrder(new CreateOrderRequest(List.of("61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa74")));
    }

    @After
    public void tearDown() {
        client.getAccessToken(client.loginUser(new LoginUserRequest(user)));
        client.deleteUser();
    }

    @Test
    @DisplayName("Список заказов пользователя может быть получен")
    public void ordersListMayBeReceived() {
        ValidatableResponse ordersListResponse = client.getUsersOrders();
        GetOrdersListResponse orderList = ordersListResponse.extract().body().as(GetOrdersListResponse.class);

        checkStatusCode(ordersListResponse, 200);
        parameterEqualsTo(ordersListResponse, "success", true);
        parameterIsNot(ordersListResponse, "totalToday", 0);
        checkArrayListIsNotEmpty(orderList.getOrders());
    }

    @Test
    @DisplayName("Список заказов пользователя не может быть получен без авторизации")
    public void ordersListNotMayBeReceivedWithoutToken() {
        client.flushAccessToken();
        ValidatableResponse ordersListResponse = client.getUsersOrders();

        checkStatusCode(ordersListResponse, 401);
        parameterEqualsTo(ordersListResponse, "success", false);
        parameterEqualsTo(ordersListResponse, "message", "You should be authorised");
    }
}
