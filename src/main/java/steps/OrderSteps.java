package steps;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import model.*;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;

public class OrderSteps {
    private static final String API_V1_ORDER = "/api/v1/orders";
    private static final String API_V1_ORDER_CANCEL = "/api/v1/orders/cancel";

    public OrderSteps() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
    }

    @Step("Создание заказа")
    public Response createOrder(Order order) {
        return given()
                .contentType(JSON)
                .body(order)
                .when()
                .post(API_V1_ORDER);
    }
    @Step("Проверить код ответа создания заказа")
    public void checkCreateOrderStatusCode(Response response, int expectedStatusCode) {
        response.then().assertThat().statusCode(expectedStatusCode);
    }

    @Step("Печать тела ответа")
    public void printResponseBody(Response response) {
        System.out.println("Тело ответа:");
        response.then().log().body();
    }
    @Step("Извлечь track из ответа")
    public int extractTrackFromResponse(Response response) {
        OrderResponse orderResponse = response.as(OrderResponse.class);
        return orderResponse.getTrack();
    }


    @Step("Отмена заказа")
    public Response cancelOrder(int track) {
        return given()
                .contentType(JSON)
                .body("{\"track\": "+track+"}")
                .when()
                .put(API_V1_ORDER_CANCEL);
    }
    @Step("Проверить код ответа отмены")
    public static void compareResponseCancelOrderStatusCode(Response responseCancel, int expectedStatusCode) {
        responseCancel.then().assertThat().statusCode(expectedStatusCode);
    }
    @Step("Получить список заказов")
    public Response getOrders() {
        return given()
                .when()
                .get(API_V1_ORDER);
    }

    @Step("Проверить код ответа получения списка заказов")
    public void checkGetOrderStatusCode(Response response, int expectedStatusCode) {
        response.then().assertThat().statusCode(expectedStatusCode);
    }


}
