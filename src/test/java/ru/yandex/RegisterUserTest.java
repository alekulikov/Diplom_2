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

@DisplayName("Создание пользователя")
public class RegisterUserTest {

    UserCreator user;
    UserAuthentication authentication;
    UserUpdater userUpdater;
    UserData userData;

    @Before
    public void setUp() {
        userData = UserDataGenerator.getRandom();
        user = new UserCreator();
    }

    @After
    public void tearDown() {
        authentication = new UserAuthentication();
        authentication.loginUser(new LoginUserRequest(userData));
        userUpdater = new UserUpdater();
        userUpdater.deleteUser(authentication.getAccessToken());
    }

    @Test
    @DisplayName("Пользователь может быть создан")
    public void userCanBeCreated() {
        ValidatableResponse createResponse = user.createUser(new CreateUserRequest(userData));

        checkStatusCode(createResponse, 200);
        parameterEqualsTo(createResponse, "success", true);
    }

    @Test
    @DisplayName("Один и тот же пользователь не может быть создан дважды")
    public void courierMustBeUnique() {
        user.createUser(new CreateUserRequest(userData));
        ValidatableResponse repeatCreateResponse = user.createUser(new CreateUserRequest(userData));

        checkStatusCode(repeatCreateResponse, 403);
        parameterEqualsTo(repeatCreateResponse, "success", false);
        parameterEqualsTo(repeatCreateResponse, "message", "User already exists");
    }

    @Test
    @DisplayName("Пользователь не может быть создан без пароля")
    public void userCannotCreatedWithoutPassword() {
        userData.setPassword(null);
        ValidatableResponse createResponse = user.createUser(new CreateUserRequest(userData));

        checkStatusCode(createResponse, 403);
        parameterEqualsTo(createResponse, "success", false);
        parameterEqualsTo(createResponse, "message", "Email, password and name are required fields");
    }

    @Test
    @DisplayName("Пользователь не может быть создан без почты")
    public void userCannotCreatedWithoutEmail() {
        userData.setEmail(null);
        ValidatableResponse createResponse = user.createUser(new CreateUserRequest(userData));

        checkStatusCode(createResponse, 403);
        parameterEqualsTo(createResponse, "success", false);
        parameterEqualsTo(createResponse, "message", "Email, password and name are required fields");
    }

    @Test
    @DisplayName("Пользователь не может быть создан без имени")
    public void userCannotCreatedWithoutName() {
        userData.setName(null);
        ValidatableResponse createResponse = user.createUser(new CreateUserRequest(userData));

        checkStatusCode(createResponse, 403);
        parameterEqualsTo(createResponse, "success", false);
        parameterEqualsTo(createResponse, "message", "Email, password and name are required fields");
    }
}
