package ru.yandex;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.yandex.api.UserAuthentication;
import ru.yandex.api.UserCreator;
import ru.yandex.api.UserUpdater;
import ru.yandex.model.CreateUserRequest;
import ru.yandex.model.LoginUserRequest;
import ru.yandex.model.UpdateUserRequest;
import ru.yandex.model.UserData;

import java.util.Collection;
import java.util.List;

import static ru.yandex.Steps.checkStatusCode;
import static ru.yandex.Steps.parameterEqualsTo;

@RunWith(Parameterized.class)
@DisplayName("Обновление данных пользователя")
public class UpdateUserTest {

    UserCreator user;
    UserAuthentication authentication;
    UserUpdater userUpdater;
    UserData userData;

    private final UpdateUserRequest request;
    private final String field;
    private final String newValue;

    public UpdateUserTest(UpdateUserRequest request, String field, String newValue) {
        this.request = request;
        this.field = field;
        this.newValue = newValue;
    }

    @Parameterized.Parameters(name = "updated field = \"{1}\"")
    public static Collection<Object[]> data() {
        return List.of(
                new Object[]{new UpdateUserRequest(null, "updatedName"), "user.name", "updatedName"},
                new Object[]{new UpdateUserRequest("updatedemail@mail.com", null),"user.email", "updatedemail@mail.com"});
    }

    @Before
    public void setUp() {
        userData = UserDataGenerator.getRandom();
        user = new UserCreator();
        user.createUser(new CreateUserRequest(userData));
        authentication = new UserAuthentication();
        authentication.loginUser(new LoginUserRequest(userData));
        userUpdater = new UserUpdater();
    }

    @After
    public void tearDown() {
        userUpdater.deleteUser(authentication.getAccessToken());
    }

    @Test
    public void userDataCanBeUpdated() {
        ValidatableResponse updateResponse = userUpdater.updateUserDate(request, authentication.getAccessToken());

        checkStatusCode(updateResponse, 200);
        parameterEqualsTo(updateResponse, "success", true);
        parameterEqualsTo(updateResponse, field, newValue);
    }

    @Test
    public void userDataCanNotBeUpdatedWithoutToken() {
        ValidatableResponse updateResponse = userUpdater.updateUserDate(request, null);

        checkStatusCode(updateResponse, 401);
        parameterEqualsTo(updateResponse, "success", false);
        parameterEqualsTo(updateResponse, "message", "You should be authorised");
    }
}
