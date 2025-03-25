package tests.api;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import tests.models.CourierData;
import tests.json.jsonDeleteCourier;

import static io.restassured.RestAssured.given;


public class CourierApi extends BaseApi
{
    final String URL = BASE_URL + "/courier";

    @Step("Создание курьера")
    public Response createCourier(CourierData courierData) {
        return given()
                .contentType(ContentType.JSON)
                .body(courierData) // Автоматическая сериализация CourierData
                .post(URL);
    }

    @Step("Логин курьера")
    public Response loginCourier(CourierData courierData) {
        return given()
                .contentType(ContentType.JSON)
                .body(courierData)
                .post(URL + "/login");
    }

    @Step("Удаление курьера по ID")
    public Response deleteCourier(Integer courierId) {
        jsonDeleteCourier json = new jsonDeleteCourier(courierId);
        return given()
                .contentType(ContentType.JSON)
                .body(json)
                .delete(URL);
    }
}
