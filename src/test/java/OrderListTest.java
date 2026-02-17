import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.response.Response;
import model.Courier;
import model.CourierAPI;
import model.OrderAPI;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static model.CourierAPI.*;
import static org.hamcrest.Matchers.*;

public class OrderListTest {

    private static CourierAPI courierClient;
    private OrderAPI orderClient;
    private final Courier testCourier = new Courier(LOGIN, PASSWORD, FIRSTNAME);
    private int courierId;

    @BeforeClass
    public static void setUpBaseUrl() {
        courierClient = new CourierAPI();
        RestAssured.baseURI = courierClient.getBaseUri();
        // Глобальная настройка Jackson: не сериализовать null-поля
        RestAssured.config = RestAssuredConfig.config()
                .objectMapperConfig(new ObjectMapperConfig().jackson2ObjectMapperFactory(
                        (cls, charset) -> new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL)
                ));
    }

    @Before
    @Step("Подготовка тестовых данных: создание и авторизация курьера")
    public void prepareTestData() {
        Response createResp = courierClient.createCourier(testCourier);
        createResp.then().statusCode(201);

        Response loginResp = courierClient.loginCourier(testCourier);
        loginResp.then().statusCode(200);
        courierId = loginResp.path("id");

        orderClient = new OrderAPI();
    }

    @Test
    @Description("Получение списка заказов для созданного курьера → статус 200")
    public void shouldReturnOrderListForCourier() {
        Response response = orderClient.getOrders(courierId);
        response.then().statusCode(200);
    }

    @After
    @Step("Очистка: удаление тестового курьера")
    public void cleanupCourier() {
        if (courierId != 0) {
            Response deleteResp = courierClient.deleteCourier(courierId);
            deleteResp.then().statusCode(200);
        }
    }
}