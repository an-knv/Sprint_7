import io.restassured.response.Response;
import model.Courier;
import model.CourierCreds;
import model.CourierLoginResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import steps.CourierSteps;

import static generator.CourierGenerator.randomCourier;
import static model.CourierCreds.credsFrom;
import static org.apache.http.HttpStatus.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CreateCourierTests {
    private CourierSteps courierSteps = new CourierSteps();
    private String id;

    @Test
    @DisplayName("Курьера можно создать")
    public void createCourier() {
        //создаем случайного курьера
        Courier courier = randomCourier();
        //отправляем запрос на создание курьера
        Response response = courierSteps.createCourier(courier);
        //сравниваем ответ
        courierSteps.compareResponseStatusCode(response, 201);
        //печатаем тело ответа
        courierSteps.printResponseBody(response);
        // отправляем запрос на логин курьера
        Response loginResponse = courierSteps.loginCourier(CourierCreds.credsFrom(courier));
        // сравниваем код ответа логина
        courierSteps.compareResponseStatusCode(loginResponse, 200);
        // печатаем тело ответа логина
        courierSteps.printResponseBody(loginResponse);
        // извлекаем id из ответа и сохраняем
        id = courierSteps.extractIdFromLoginResponse(loginResponse);

        System.out.println("Курьер успешно создан с ID: " + id);
    }

     @Test
    @DisplayName("Нельзя создать двух одинаковых курьеров")
    public void cannotCreateTheSameCourier() {
         //создаем первого курьера
         Courier courier = randomCourier();
         //отправляем запрос на создание курьера
         Response response = courierSteps.createCourier(courier);
         //сравниваем ответ
         courierSteps.compareResponseStatusCode(response, SC_CREATED);
         //печатаем тело ответа
         courierSteps.printResponseBody(response);
         //отправляем повторный запрос на создание курьера
         Response secondResponse = courierSteps.createCourier(courier);
         //сравниваем ответ
         courierSteps.compareResponseStatusCode(secondResponse, SC_CONFLICT);
         //печатаем тело ответа
         courierSteps.printResponseBody(secondResponse);
         Response loginResponse = courierSteps.loginCourier(CourierCreds.credsFrom(courier));
         // извлекаем id из ответа и сохраняем
         id = courierSteps.extractIdFromLoginResponse(loginResponse);

    }
    @Test
    @DisplayName("Создание курьера без логина возвращает ошибку")
    public void createCourierWithoutLoginReturnsError() {
        // Создаем курьера без логина
        Courier courier = new Courier(null, "password123", "FirstName");
        //отправляем запрос на создание курьера
        Response response = courierSteps.createCourier(courier);
        //сравниваем ответ
        courierSteps.compareResponseStatusCode(response, SC_BAD_REQUEST);
        //печатаем тело ответа
        courierSteps.printResponseBody(response);

    }

    @Test
    @DisplayName("Создание курьера с пустым логином возвращает ошибку")
    public void createCourierWithEmptyLoginReturnsError() {
        // Создаем курьера с пустым логином
        Courier courier = new Courier("", "password123", "FirstName");
        //отправляем запрос на создание курьера
        Response response = courierSteps.createCourier(courier);
        //сравниваем ответ
        courierSteps.compareResponseStatusCode(response, SC_BAD_REQUEST);
        //печатаем тело ответа
        courierSteps.printResponseBody(response);

    }
    @Test
    @DisplayName("Создание курьера с пустым паролем возвращает ошибку")
    public void createCourierWithEmptyPasswordReturnsError() {
        // Создаем курьера с пустым паролем
        Courier courier = new Courier("Анна", "", "FirstName");
        //отправляем запрос на создание курьера
        Response response = courierSteps.createCourier(courier);
        //сравниваем ответ
        courierSteps.compareResponseStatusCode(response, SC_BAD_REQUEST);
        //печатаем тело ответа
        courierSteps.printResponseBody(response);

    }

    @Test
    @DisplayName("Создание курьера без пароля возвращает ошибку")
    public void createCourierWithoutPasswordReturnsError() {
        // Создаем курьера без пароля
        Courier courier = new Courier("login123", null, "FirstName");
        //отправляем запрос на создание курьера
        Response response = courierSteps.createCourier(courier);
        //сравниваем ответ
        courierSteps.compareResponseStatusCode(response, SC_BAD_REQUEST);
        //печатаем тело ответа
        courierSteps.printResponseBody(response);
    }
    @Test
    @DisplayName("успешный запрос возвращает ok: true")
    public void createCourierReturnsOkTrue() {
        //создаем случайного курьера
        Courier courier = randomCourier();
        //отправляем запрос на создание курьера
        Response response = courierSteps.createCourier(courier);
        //сравниваем ответ
        courierSteps.compareResponseStatusCode(response, 201);
        //печатаем тело ответа
        courierSteps.printResponseBody(response);
        //поверяем ответ
        String responseBody = response.body().asString();
        assertTrue(responseBody.contains("\"ok\":true"), "ответ содержит ok: true");
        Response loginResponse = courierSteps.loginCourier(CourierCreds.credsFrom(courier));
        // извлекаем id из ответа и сохраняем
        id = courierSteps.extractIdFromLoginResponse(loginResponse);
    }

    @Test
    @DisplayName("запрос возвращает правильный код ответа")
    public void createCourierReturns201() {
        //создаем случайного курьера
        Courier courier = randomCourier();
        //отправляем запрос на создание курьера
        Response response = courierSteps.createCourier(courier);
        //сравниваем ответ
        courierSteps.compareResponseStatusCode(response, 201);
        //печатаем тело ответа
        assertEquals(SC_CREATED, response.statusCode(),
                "При успешном создании должен возвращаться код 201");
        Response loginResponse = courierSteps.loginCourier(CourierCreds.credsFrom(courier));
        // извлекаем id из ответа и сохраняем
        id = courierSteps.extractIdFromLoginResponse(loginResponse);

    }
    @Test
    @DisplayName("если создать пользователя с логином, который уже есть, возвращается ошибка.")
    public void createCourierWithTheSameLogin() {
        //создаем случайного курьера
        Courier courier = randomCourier();
        //отправляем запрос на создание курьера
        Response response = courierSteps.createCourier(courier);
        //сравниваем ответ
        courierSteps.compareResponseStatusCode(response, 201);
        //печатаем тело ответа
        courierSteps.printResponseBody(response);
        Courier secondCourier = new Courier(
                courier.getLogin(),
                "DifferentPassword",
                "DifferentName"
        );
        //отправляем запрос на создание курьера с тем же логином
        Response secondResponse = courierSteps.createCourier(secondCourier);
        //сравниваем ответ
        courierSteps.compareResponseStatusCode(secondResponse, SC_CONFLICT);
        //печатаем тело ответа
        courierSteps.printResponseBody(secondResponse);
        // отправляем запрос на логин курьера
        Response loginResponse = courierSteps.loginCourier(CourierCreds.credsFrom(courier));
        // сравниваем код ответа логина
        courierSteps.compareResponseStatusCode(loginResponse, 200);
        // печатаем тело ответа логина
        courierSteps.printResponseBody(loginResponse);
        // извлекаем id из ответа и сохраняем
        id = courierSteps.extractIdFromLoginResponse(loginResponse);

    }

        @AfterEach
        public void tearDown() {
            courierSteps.deleteCourier(id);
        }
    }


