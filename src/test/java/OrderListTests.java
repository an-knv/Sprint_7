import io.restassured.response.Response;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import steps.OrderSteps;

import static org.apache.http.HttpStatus.SC_OK;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class OrderListTests {
    private final OrderSteps orderSteps = new OrderSteps();


    @Test
    @DisplayName("в тело ответа возвращается список заказов")
    public void getOrderList() {


        // отправляем запрос на логин курьера
        Response response = orderSteps.getOrders();
        // сравниваем код ответа
        orderSteps.checkGetOrderStatusCode(response, SC_OK);
        // Печатаем тело ответа
        orderSteps.printResponseBody(response);
        String responseBody = response.getBody().asString();
        // Проверяем, что ответ содержит поле 'orders'
        assertTrue(responseBody.contains("\"orders\""), "Ответ должен содержать поле 'orders'");
    }
}
