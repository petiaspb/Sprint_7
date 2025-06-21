package test.scooter;

import com.google.gson.Gson;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import order.CreateOrder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestParamCreateOrder {
    private final Gson gson = new Gson();

    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @ParameterizedTest
    @MethodSource("colorParameters")
    @DisplayName("Тест с разными цветами заказа")
    public void createOrderWithDifferentColors(String[] colors) {
        // Общие параметры заказа
        String firstName = "Иван";
        String lastName = "Иванов";
        String address = "ул. Пушкина, д. 10";
        String metroStation = "3";
        String phone = "+79123456789";
        int rentTime = 3;
        String deliveryDate = "2024-11-01";
        String comment = "";
        CreateOrder order = new CreateOrder(firstName,
                lastName,
                address,
                metroStation,
                phone,
                rentTime,
                deliveryDate,
                comment,
                colors);

        Response response = createOrder(order);
        verifyOrderResponse(response);
    }

    static Stream<Arguments> colorParameters() {
        return Stream.of(
                Arguments.of((Object) new String[] {"BLACK"}),
                Arguments.of((Object) new String[] {"GREY"}),
                Arguments.of((Object) new String[] {"BLACK", "GREY"}),
                Arguments.of((Object) new String[] {})
        );
    }

    @Step("Выполнить запрос на создание заказа")

    public Response createOrder(CreateOrder order) {
        return given()
                .header("Content-type", "application/json")
                .body(gson.toJson(order))
                .post("/api/v1/orders");
    }

    @Step("Проверка ответа на создание заказа")
    private void verifyOrderResponse(Response response) {

        response.then().statusCode(201);


        Integer trackNumber = response.path("track");
        assertThat(trackNumber, allOf(notNullValue(), instanceOf(Integer.class)));
        assertTrue(trackNumber > 0, "Трэк номер-положительное число");
    }


}