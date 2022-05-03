package ru.yandex;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.model.CreateUserRequest;
import ru.yandex.model.LoginUserRequest;
import ru.yandex.model.UserData;

import static ru.yandex.Steps.checkStatusCode;
import static ru.yandex.Steps.parameterEqualsTo;

@DisplayName("Авторизация пользователя")
public class LoginUserTest {
    
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
        client.deleteUser();
    }

    @Test
    @DisplayName("Пользователь может авторизоваться с корректными данными")
    public void userCanLoginWithValidCredentials() {
        ValidatableResponse loginResponse = client.loginUser(new LoginUserRequest(user));
        client.getAccessToken(loginResponse);

        checkStatusCode(loginResponse, 200);
        parameterEqualsTo(loginResponse, "success", true);
    }

    @Test
    @DisplayName("Пользователь не может авторизоваться, если такой почты не существует")
    public void userCannotLoginWithIncorrectEmail() {
        user.setEmail("incorrectMail@mail.com");
        ValidatableResponse loginResponse = client.loginUser(new LoginUserRequest(user));
        client.getAccessToken(loginResponse);

        checkStatusCode(loginResponse, 401);
        parameterEqualsTo(loginResponse, "success", false);
        parameterEqualsTo(loginResponse, "message", "email or password are incorrect");
    }

    @Test
    @DisplayName("Пользователь не может авторизоваться, если пароль не подходит")
    public void userCannotLoginWithIncorrectPassword() {
        user.setPassword("incorrectPassword");
        ValidatableResponse loginResponse = client.loginUser(new LoginUserRequest(user));
        client.getAccessToken(loginResponse);

        checkStatusCode(loginResponse, 401);
        parameterEqualsTo(loginResponse, "success", false);
        parameterEqualsTo(loginResponse, "message", "email or password are incorrect");
    }
}
