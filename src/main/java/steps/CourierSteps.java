package steps;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import model.Courier;
import model.CourierCreds;
import model.CourierLoginResponse;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static model.CourierCreds.credsFrom;

public class CourierSteps {
    private static final String API_V1_COURIER = "/api/v1/courier";
    private static final String API_V1_COURIER_LOGIN = "/api/v1/courier/login";

    public CourierSteps() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
    }

    @Step("Создание курьера")
    public Response createCourier(Courier courier) {
        return given()
                .contentType(JSON)
                .body(courier)
                .when()
                .post(API_V1_COURIER);
    }
    @Step("Проверить код ответа")
    public void compareResponseStatusCode(Response response, int expectedStatusCode) {
        response.then().assertThat().statusCode(expectedStatusCode);
    }
    @Step("Вывести тело ответа в консоль")
    public void printResponseBody(Response response) {
        System.out.println("Тело ответа:");
        System.out.println(response.body().asString());
    }

    @Step("Извлечь ID из ответа логина")
    public String extractIdFromLoginResponse(Response loginResponse) {
        String id = loginResponse.as(CourierLoginResponse.class).getId();
        System.out.println("Извлечен ID курьера: " + id);
        return id;
    }

    @Step("Логин курьера в системе")
    public Response loginCourier(CourierCreds creds) {
        return given()
                .contentType(JSON)
                .body(creds)
                .when()
                .post(API_V1_COURIER_LOGIN);
    }
    @Step("Удаление курьера")
    public Response deleteCourier(String id) {
        return given()
                .header("Content-type", "application/json")
                .when()
                .delete(API_V1_COURIER + "/" + id);

    }




}
