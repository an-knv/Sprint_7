import io.restassured.response.Response;
import model.Courier;
import model.CourierCreds;
import model.Order;
import model.OrderResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import steps.CourierSteps;
import steps.OrderSteps;


import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static generator.OrderExample.randomOrder;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.junit.jupiter.api.Assertions.*;

public class CreateOrderTests {

    private OrderSteps orderSteps = new OrderSteps();


    @Test
    @DisplayName("Создание заказа")
    public void createOrder() {
        //создаем случайного курьера
        Order order = randomOrder();
        //отправляем запрос на создание заказа
        Response response = orderSteps.createOrder(order);
        // проверяем ответ
        orderSteps.checkCreateOrderStatusCode(response, SC_CREATED);
        orderSteps.printResponseBody(response);
        //извлекаем трек
        int track = orderSteps.extractTrackFromResponse(response);
        assertTrue(track > 0, "В ответе положительный track");
        System.out.println("Заказ успешно создан с track: " + track);
        orderSteps.cancelOrder(track);
        Response responseCancel = orderSteps.cancelOrder(track);

        System.out.println("Отменен заказ: " + track);
    }

    @ParameterizedTest(name = "Создание заказа с разными цветами")
    @MethodSource("colorCombinations")
    @DisplayName("Проверка создания заказа с разными цветами")
    public void createOrderWithDifferentColors(List<String> colors) {
        // Создаем заказ с указанными цветами
        Order order = new Order(
                "Anna",
                "Konovalova",
                "Klenovaya 2",
                "Mitino",
                "+71234567899",
                3,
                "2026-01-04",
                "TestOrder",
                colors
        );

        // Отправляем запрос на создание заказа
        Response response = orderSteps.createOrder(order);
        orderSteps.checkCreateOrderStatusCode(response, SC_CREATED);
        orderSteps.printResponseBody(response);
        int track = orderSteps.extractTrackFromResponse(response);
        System.out.println("Заказ успешно создан с track: " + track + ", цвета: " + colors);
        // Отменяем заказ
        Response cancelResponse = orderSteps.cancelOrder(track);
        System.out.println("Отменен заказ с track: " + track);
    }

    // Тестовые данные
    private static Stream<Arguments> colorCombinations() {
        return Stream.of(
                Arguments.of(Arrays.asList("BLACK")),
                Arguments.of(Arrays.asList("GREY")),
                Arguments.of(Arrays.asList("BLACK", "GREY")),
                Arguments.of(Arrays.asList())
        );
    }

}
