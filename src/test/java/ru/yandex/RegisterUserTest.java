package ru.yandex;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.model.CreateUserRequest;
import ru.yandex.model.UserData;

import static ru.yandex.Steps.checkStatusCode;
import static ru.yandex.Steps.parameterEqualsTo;

@DisplayName("Создание пользователя")
public class RegisterUserTest {

    UserClient client;
    UserData user;

    @Before
    public void setUp() {
        client = new UserClient();
        user = UserDataGenerator.getRandom();
    }

    @After
    public void tearDown() {
        client.deleteUser();
    }

    @Test
    @DisplayName("Пользователь может быть создан")
    public void userCanBeCreated() {
        ValidatableResponse createResponse = client.createUser(new CreateUserRequest(user));
        client.getAccessToken(createResponse);

        checkStatusCode(createResponse, 200);
        parameterEqualsTo(createResponse, "success", true);
    }

    @Test
    @DisplayName("Один и тот же пользователь не может быть создан дважды")
    public void courierMustBeUnique() {
        client.getAccessToken(client.createUser(new CreateUserRequest(user)));
        ValidatableResponse repeatCreateResponse = client.createUser(new CreateUserRequest(user));

        checkStatusCode(repeatCreateResponse, 403);
        parameterEqualsTo(repeatCreateResponse, "success", false);
        parameterEqualsTo(repeatCreateResponse, "message", "User already exists");
    }

    @Test
    @DisplayName("Пользователь не может быть создан без пароля")
    public void userCannotCreatedWithoutPassword() {
        user.setPassword(null);
        ValidatableResponse createResponse = client.createUser(new CreateUserRequest(user));
        client.getAccessToken(client.createUser(new CreateUserRequest(user)));

        checkStatusCode(createResponse, 403);
        parameterEqualsTo(createResponse, "success", false);
        parameterEqualsTo(createResponse, "message", "Email, password and name are required fields");
    }

    @Test
    @DisplayName("Пользователь не может быть создан без почты")
    public void userCannotCreatedWithoutEmail() {
        user.setEmail(null);
        ValidatableResponse createResponse = client.createUser(new CreateUserRequest(user));
        client.getAccessToken(client.createUser(new CreateUserRequest(user)));

        checkStatusCode(createResponse, 403);
        parameterEqualsTo(createResponse, "success", false);
        parameterEqualsTo(createResponse, "message", "Email, password and name are required fields");
    }

    @Test
    @DisplayName("Пользователь не может быть создан без имени")
    public void userCannotCreatedWithoutName() {
        user.setName(null);
        ValidatableResponse createResponse = client.createUser(new CreateUserRequest(user));
        client.getAccessToken(client.createUser(new CreateUserRequest(user)));

        checkStatusCode(createResponse, 403);
        parameterEqualsTo(createResponse, "success", false);
        parameterEqualsTo(createResponse, "message", "Email, password and name are required fields");
    }
}
