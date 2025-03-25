package tests;

import org.junit.After;
import org.junit.Before;
import tests.api.CourierApi;
import tests.api.OrderApi;
import tests.models.OrderData;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.List;

import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(Parameterized.class)
public class CreateOrderTest {

    private OrderApi orderApi;
    private final OrderData orderData;
    private Response resp;

    public CreateOrderTest(List<String> color) {
        this.orderData = new OrderData(color);
    }

    @Before
    public void setUp() {
        orderApi = new OrderApi();

        resp = createOrderWithColor(orderData);
    }

    @After
    public void tearDown() {
        cancelOrderIfExists();
    }

    @Parameterized.Parameters(name = "Тестовые данные: {0}")
    public static Object[] getOrderData() {
        return new Object[]{
                List.of("BLACK"),
                List.of("GREY"),
                List.of("BLACK", "GREY"),
                List.of() // без указания цвета
        };
    }

    @Test
    @DisplayName("Создание заказа с параметризацией")
    @Description("Проверка создания заказа с выбором цвета самоката")
    public void createOrderTest() {
        Response response = createOrderWithColor(orderData);
        response.then().statusCode(SC_CREATED).body("track", notNullValue());
    }

    @Step("Создание заказа с параметрами: {orderData}")
    private Response createOrderWithColor(OrderData orderData) {
        return orderApi.createOrder(orderData);
    }

    @Step("Отмена заказа, если он был создан")
    private void cancelOrderIfExists() {
        int track = resp.path("track");
        if (track != 0) {
            orderApi.cancelOrder(track).then().statusCode(SC_OK);
            resp = null;
        }
    }

}
