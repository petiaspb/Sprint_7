package test.scooter;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import order.Order;
import order.OrdersListResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class TestGetOrdersList {

    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Test
    @DisplayName("Получение списка заказов")
    @Description("Проверка, что ручка возвращает валидный список заказов")
    public void getOrdersListShouldReturnValidResponse() {
        Response response = sendGetOrdersRequest();
        verifyOrdersListResponse(response);
    }

    @Step("Отправка GETа для получения списка заказов")
    private Response sendGetOrdersRequest() {
        return given()
                .header("Content-type", "application/json") // Устанавливаем заголовок
                .when()
                .get("/api/v1/orders"); // Отправляем GET-запрос
    }

    @Step("Проверка ответа со списком заказов")
    private void verifyOrdersListResponse(Response response) {

        response.then().statusCode(200);


        OrdersListResponse ordersResponse = response.as(OrdersListResponse.class);


        assertThat("Список заказов должен содержать элементы",
                ordersResponse.getOrders(), not(empty()));


        Order firstOrder = ordersResponse.getOrders().get(0);


        assertThat("ID заказа должен быть положительным числом",
                firstOrder.getId(), greaterThan(0));
        assertThat("Трек-номер должен быть заполнен",
                firstOrder.getTrack(), notNullValue());
        assertThat("Имя клиента должно быть заполнено",
                firstOrder.getFirstName(), not(emptyOrNullString()));
        assertThat("Фамилия клиента должна быть заполнена",
                firstOrder.getLastName(), not(emptyOrNullString()));
        assertThat("Адрес должен быть заполнен",
                firstOrder.getAddress(), not(emptyOrNullString()));
        assertThat("Телефон должен быть заполнен",
                firstOrder.getPhone(), not(emptyOrNullString()));

    }
}
