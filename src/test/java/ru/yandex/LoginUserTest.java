package ru.yandex;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.api.UserAuthentication;
import ru.yandex.api.UserCreator;
import ru.yandex.api.UserUpdater;
import ru.yandex.model.CreateUserRequest;
import ru.yandex.model.LoginUserRequest;
import ru.yandex.model.UserData;

import static ru.yandex.Steps.checkStatusCode;
import static ru.yandex.Steps.parameterEqualsTo;

@DisplayName("Авторизация пользователя")
public class LoginUserTest {

    UserCreator user;
    UserAuthentication authentication;
    UserUpdater userUpdater;
    UserData userData;

    @Before
    public void setUp() {
        userData = UserDataGenerator.getRandom();
        user = new UserCreator();
        user.createUser(new CreateUserRequest(userData));
        authentication = new UserAuthentication();
        authentication.loginUser(new LoginUserRequest(userData));
    }

    @After
    public void tearDown() {
        userUpdater = new UserUpdater();
        userUpdater.deleteUser(authentication.getAccessToken());
    }

    @Test
    @DisplayName("Пользователь может авторизоваться с корректными данными")
    public void userCanLoginWithValidCredentials() {
        ValidatableResponse loginResponse = authentication.loginUser(new LoginUserRequest(userData));

        checkStatusCode(loginResponse, 200);
        parameterEqualsTo(loginResponse, "success", true);
    }

    @Test
    @DisplayName("Пользователь не может авторизоваться, если такой почты не существует")
    public void userCannotLoginWithIncorrectEmail() {
        userData.setEmail("incorrectMail@mail.com");
        ValidatableResponse loginResponse = authentication.loginUser(new LoginUserRequest(userData));

        checkStatusCode(loginResponse, 401);
        parameterEqualsTo(loginResponse, "success", false);
        parameterEqualsTo(loginResponse, "message", "email or password are incorrect");
    }

    @Test
    @DisplayName("Пользователь не может авторизоваться, если пароль не подходит")
    public void userCannotLoginWithIncorrectPassword() {
        userData.setPassword("incorrectPassword");
        ValidatableResponse loginResponse = authentication.loginUser(new LoginUserRequest(userData));

        checkStatusCode(loginResponse, 401);
        parameterEqualsTo(loginResponse, "success", false);
        parameterEqualsTo(loginResponse, "message", "email or password are incorrect");
    }
}
