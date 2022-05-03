package ru.yandex;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.apache.commons.lang3.RandomStringUtils;
import ru.yandex.model.UserData;

public class UserDataGenerator {

    public static UserData getRandom() {
        return getRandom(10, 10, 10);
    }

    @Step("Готовим новые данные для создания курьера")
    public static UserData getRandom(int lengthEmail, int lengthPassword, int lengthName) {
        String email = RandomStringUtils.randomAlphabetic(lengthEmail) + "@yandex.ru";
        String password = RandomStringUtils.randomAlphabetic(lengthPassword);
        String name = RandomStringUtils.randomAlphabetic(lengthName);

        Allure.addAttachment("email", email);
        Allure.addAttachment("password", password);
        Allure.addAttachment("firstName", name);

        return new UserData(email, password, name);
    }
}
