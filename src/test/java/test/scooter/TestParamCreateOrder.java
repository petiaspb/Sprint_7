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
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

public class TestParamCreateOrder {
    private final Gson gson = new Gson();

    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @ParameterizedTest
    @MethodSource("orderParameters")
    @DisplayName("Тест с разными параметрами заказа")
    public void createOrder(String firstName,
                            String lastName,
                            String address,
                            String metroStation,
                            String phone,
                            int rentTime,
                            String deliveryDate,
                            String comment,
                            String[] color) {
        CreateOrder order = new CreateOrder(firstName,
                lastName,
                address,
                metroStation,
                phone,
                rentTime,
                deliveryDate,
                comment,
                color);
        Response response = createOrder(order);
        verifyOrderResponse(response);


    }

    static Stream<Arguments> orderParameters() {
        return Stream.of(
                Arguments.of("Джон",
                        "Уик",
                        "ул. Кукушкина, 5",
                        "4",
                        "+79123456789",
                        3,
                        "2021-11-31",
                        "тра та та",
                        new String[]{"BLACK"}
                ),
                Arguments.of("Вин",
                        "Дизель",
                        "ул. Пупкина, 18",
                        "5",
                        "+79987654321",
                        5,
                        "2022-11-01",
                        "",
                        new String[]{"GREY"}
                ),
                Arguments.of("Гарри",
                        "Поттер",
                        "ул. Замковая, 33",
                        "10",
                        "+79999999999",
                        1,
                        "2024-09-05",
                        "Чтобы был быстрее метлы",
                        new String[]{"BLACK", "GREY"}
                ),
                Arguments.of( "Анна",
                        "Каренина",
                        "пр. Паровозов, 15",
                        "7",
                        "+79111111111",
                        2,
                        "2025-03-20",
                        null,
                        new String[]{})

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


        int trackNumber = response.then().extract().path("track");
        assertThat(trackNumber, notNullValue());
    }


}