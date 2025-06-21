
package test.scooter;

import courier.CreateCourier;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;




class TestCourierCreate extends TestCourier {


    @Test
    @DisplayName("Успешное создание курьера")
    void shouldSuccessfullyCreateCourier() {
        CreateCourier courier = new CreateCourier("SuperPuperCourier1", "thebest12", "Petr");
        Response response = createCreateCourier(courier);
        verifyResponseStatusCode(response,201);
        verifyResponseBody(response,true);
        getCourierIdForCleanup(courier.getLogin(), courier.getPassword());
    }

    @Test
    @DisplayName("Проверка создания двух одинаковых курьеров")
    void createTwoAnswerCourier() {
        CreateCourier courier = new CreateCourier("SuperPuperCourier3", "thebest15", "Vasia");
        Response firstCreation = createCreateCourier(courier);
        verifyResponseStatusCode(firstCreation,201);
        verifyResponseBody(firstCreation,true);

        Response secondCreation = createCreateCourier(courier);
        verifyResponseStatusCode(secondCreation, 409);
        verifyErrorResponse(secondCreation, 409, "Этот логин уже используется. Попробуйте другой.");
        getCourierIdForCleanup(courier.getLogin(), courier.getPassword());
    }

    @Test
    @DisplayName("Проверка создания курьера без обязательных полей")
    void shouldFailWhenRequiredFieldsMissing() {
        testMissingField("login", new CreateCourier(null, "thebest7", "Pasha"));
        testMissingField("password", new CreateCourier("Gena", null, "Gena"));
    }

    @Step("Проверка отсутствия обязательного поля missingField")
    public void testMissingField(String missingField, CreateCourier courier) {
        Response response = createCreateCourier(courier);
        verifyResponseStatusCode(response, 400);
        verifyErrorResponse(response, 400, "Недостаточно данных для создания учетной записи");
    }
}
