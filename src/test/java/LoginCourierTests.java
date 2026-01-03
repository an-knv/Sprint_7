import io.restassured.response.Response;
import model.Courier;
import model.CourierCreds;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import steps.CourierSteps;

import static generator.CourierGenerator.randomCourier;
import static org.apache.http.HttpStatus.*;
import static org.junit.jupiter.api.Assertions.*;

public class LoginCourierTests {
    private CourierSteps courierSteps = new CourierSteps();
    private String id;
    private Courier courier;


    @BeforeEach
    public void setUp() {
        courier = randomCourier();
        Response createResponse = courierSteps.createCourier(courier);

    }

    @Test
    @DisplayName("курьер может авторизоваться;")
    public void loginCourier() {


        // отправляем запрос на логин курьера
        Response loginResponse = courierSteps.loginCourier(CourierCreds.credsFrom(courier));
        // сравниваем код ответа логина
        courierSteps.compareResponseStatusCode(loginResponse, SC_OK);
        // печатаем тело ответа логина
        courierSteps.printResponseBody(loginResponse);
        // извлекаем id из ответа и сохраняем
        id = courierSteps.extractIdFromLoginResponse(loginResponse);

        System.out.println("\"Курьер успешно авторизовался. ID:" + id);
    }
    @Test
    @DisplayName("авторизация без логина")
    public void loginCourierWithoutLogin() {


        // Создаем учетные данные без логина (null)
        CourierCreds credsWithoutLogin = new CourierCreds(null, courier.getPassword());
        // отправляем запрос на логин курьера
        Response loginResponseSecond = courierSteps.loginCourier(credsWithoutLogin);
        // сравниваем код ответа логина
        courierSteps.compareResponseStatusCode(loginResponseSecond, 400);
        // печатаем тело ответа логина
        courierSteps.printResponseBody(loginResponseSecond);
        // проверяем текст
        String responseBody = loginResponseSecond.body().asString();
        assertTrue(responseBody.contains("Недостаточно данных для входа"));

    }
    @Test
    @DisplayName("авторизация без пароля")
    public void loginCourierWithoutPassword() {

        // отправляем запрос на логин курьера
        Response loginResponse= courierSteps.loginCourier(CourierCreds.credsFrom(courier));
        // извлекаем id из ответа и сохраняем
        id = courierSteps.extractIdFromLoginResponse(loginResponse);
        // Создаем учетные данные без пароля (null)
        CourierCreds credsWithoutPassword = new CourierCreds(courier.getLogin(), null);
        // отправляем запрос на логин курьера
        Response loginResponseSecond = courierSteps.loginCourier(credsWithoutPassword);
        // сравниваем код ответа логина
        courierSteps.compareResponseStatusCode(loginResponseSecond, 400);
        // печатаем тело ответа логина
        courierSteps.printResponseBody(loginResponseSecond);
        // проверяем текст
        String responseBody = loginResponseSecond.body().asString();
        assertTrue(responseBody.contains("Недостаточно данных для входа"));

    }
    @Test
    @DisplayName("авторизация с пустым логином")
    public void loginCourierWithEmptyLogin() {


        // Создаем учетные данные без логина (null)
        CourierCreds credsWithoutLogin = new CourierCreds("", courier.getPassword());
        // отправляем запрос на логин курьера
        Response loginResponseSecond = courierSteps.loginCourier(credsWithoutLogin);
        // сравниваем код ответа логина
        courierSteps.compareResponseStatusCode(loginResponseSecond, 400);
        // печатаем тело ответа логина
        courierSteps.printResponseBody(loginResponseSecond);
        // проверяем текст
        String responseBody = loginResponseSecond.body().asString();
        assertTrue(responseBody.contains("Недостаточно данных для входа"));

    }
    @Test
    @DisplayName("авторизация с пустым паролем")
    public void loginCourierWithEmptyPassword() {

        // отправляем запрос на логин курьера
        Response loginResponse= courierSteps.loginCourier(CourierCreds.credsFrom(courier));
        // извлекаем id из ответа и сохраняем
        id = courierSteps.extractIdFromLoginResponse(loginResponse);
        // Создаем учетные данные без пароля (null)
        CourierCreds credsWithoutPassword = new CourierCreds(courier.getLogin(), "");
        // отправляем запрос на логин курьера
        Response loginResponseSecond = courierSteps.loginCourier(credsWithoutPassword);
        // сравниваем код ответа логина
        courierSteps.compareResponseStatusCode(loginResponseSecond, 400);
        // печатаем тело ответа логина
        courierSteps.printResponseBody(loginResponseSecond);
        // проверяем текст
        String responseBody = loginResponseSecond.body().asString();
        assertTrue(responseBody.contains("Недостаточно данных для входа"));

    }
    @Test
    @DisplayName("Система вернёт ошибку, если неправильно указать логин")
    public void errorIfLoginIsIncorrect() {
        // отправляем запрос на логин курьера
        Response loginResponse= courierSteps.loginCourier(CourierCreds.credsFrom(courier));
        // извлекаем id из ответа и сохраняем
        id = courierSteps.extractIdFromLoginResponse(loginResponse);
        // Создаем учетные данные с неправильным логином
        String wrongLogin = courier.getLogin()+"1";
        CourierCreds incorrectLoginCreds = new CourierCreds(wrongLogin, courier.getPassword());
        // Отправляем запрос на логин
        Response loginResponseSecond = courierSteps.loginCourier(incorrectLoginCreds);
        // Проверяем код ответа
        courierSteps.compareResponseStatusCode(loginResponseSecond, SC_NOT_FOUND);
        // Печатаем тело ответа
        courierSteps.printResponseBody(loginResponseSecond);
        // Проверяем сообщение об ошибке
        String responseBody = loginResponseSecond.body().asString();
        assertTrue(responseBody.contains("Учетная запись не найдена"));

    }
    @Test
    @DisplayName("Система вернёт ошибку, если неправильно указать пароль")
    public void errorIfPasswordIsIncorrect() {
        // отправляем запрос на логин курьера
        Response loginResponse= courierSteps.loginCourier(CourierCreds.credsFrom(courier));
        // извлекаем id из ответа и сохраняем
        id = courierSteps.extractIdFromLoginResponse(loginResponse);
        // Создаем учетные данные с неправильным логином
        String wrongPassword = courier.getPassword()+"1";
        CourierCreds incorrectLoginCreds = new CourierCreds(courier.getLogin(), wrongPassword);
        // Отправляем запрос на логин
        Response loginResponseSecond = courierSteps.loginCourier(incorrectLoginCreds);
        // Проверяем код ответа
        courierSteps.compareResponseStatusCode(loginResponseSecond, SC_NOT_FOUND);
        // Печатаем тело ответа
        courierSteps.printResponseBody(loginResponseSecond);
        // Проверяем сообщение об ошибке
        String responseBody = loginResponseSecond.body().asString();
        assertTrue(responseBody.contains("Учетная запись не найдена"));

    }
    @Test
    @DisplayName("успешный запрос возвращает id")
    public void loginCourierReturnId() {


        // отправляем запрос на логин курьера
        Response loginResponse = courierSteps.loginCourier(CourierCreds.credsFrom(courier));
        // сравниваем код ответа логина
        courierSteps.compareResponseStatusCode(loginResponse, SC_OK);
        // печатаем тело ответа логина
        courierSteps.printResponseBody(loginResponse);
        // извлекаем id из ответа и сохраняем
        id = courierSteps.extractIdFromLoginResponse(loginResponse);
        assertFalse(id.isEmpty());

    }
    @AfterEach
    public void tearDown() {
        courierSteps.deleteCourier(id);
        System.out.println("Удален курьер ID: " + id);
    }

}
