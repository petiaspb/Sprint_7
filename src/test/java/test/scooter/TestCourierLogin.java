package test.scooter;

import com.google.gson.Gson;
import courier.AuthCourier;
import courier.CreateCourier;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

import static org.hamcrest.Matchers.notNullValue;

public class TestCourierLogin extends TestCourier {

    Gson gson = new Gson();

    @Test
    @DisplayName("Успешная авторизация курьера")
    void successfulAuthTest() {

        CreateCourier courier = new CreateCourier("Couriers", "thebest2", "Linas");
        Response createResponse = createCreateCourier(courier);
        verifyResponseStatusCode(createResponse, 201);
        verifyResponseBody(createResponse, true);

        Response authResponse = authCourier(courier.getLogin(), courier.getPassword());
        verifyResponseStatusCode(authResponse,200);
        authResponse.then().assertThat().body("id", notNullValue());

        getCourierIdForCleanup(courier.getLogin(), courier.getPassword());

    }

    @Test
    @DisplayName("Проверка авторизации без обязательных  полей")
    void testCourierAuthWithMissingRequiredFields(){

        CreateCourier courier = new CreateCourier("bulkan2055","thebest333","Гаврюша");
        Response response = createCreateCourier(courier);
        verifyResponseStatusCode(response,201);
        verifyResponseBody(response,true);
        getCourierIdForCleanup(courier.getLogin(), courier.getPassword());

        checkAuthWithMissingData(null, courier.getPassword());
        checkAuthWithMissingData(courier.getLogin(),null);
        checkAuthWithMissingData(null,null);



    }




    @Test
    @DisplayName("Авторизоваться под несуществующим пользователем")
    void authWithNonExistentCredentials(){
        Response response = authCourier("SSS","qwerty");
        verifyResponseStatusCode(response,404);
        verifyErrorResponse(response,404,"Учетная запись не найдена");
    }

    @Step("Авторизация")
    public Response authCourier(String login, String password) {
        AuthCourier auth = new AuthCourier(login, password);
        Response response = given()
                .header("Content-type", "application/json")
                .body(gson.toJson(auth))
                .post("/api/v1/courier/login");

        return response;
    }



}
