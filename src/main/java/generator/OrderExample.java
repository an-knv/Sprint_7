package generator;

import model.Order;

import static utils.Utils.randomString;

public class OrderExample {
    public static Order randomOrder() {
        return new Order(
                "Anna",
                "Konovalova",
                "Klenovaya 2",
                "Mitino",
                "+71234567899",
                3,
                "2026-01-04",
                "TestOrder",
                null
        );
    }
}
