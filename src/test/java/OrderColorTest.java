import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.response.Response;
import model.CourierAPI;
import model.Order;
import model.OrderAPI;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.hamcrest.Matchers.*;

@RunWith(Parameterized.class)
public class OrderColorTest {

    private final OrderAPI orderClient;
    private final Order testOrder;
    private int track;

    public OrderColorTest(String testDescription, Order order) {
        this.testOrder = order;
        this.orderClient = new OrderAPI();
    }

    @BeforeClass
    public static void setUp() {
        RestAssured.baseURI = new CourierAPI().getBaseUri();
        // Глобальная настройка Jackson: не сериализовать null-поля
        RestAssured.config = RestAssuredConfig.config()
                .objectMapperConfig(new ObjectMapperConfig().jackson2ObjectMapperFactory(
                        (cls, charset) -> new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL)
                ));
    }

    @Parameterized.Parameters(name = "{0}")
    public static Object[][] testData() {
        return new Object[][]{
                {"Заказ только с чёрным цветом (BLACK)", new Order(
                        OrderAPI.FIRSTNAME,
                        OrderAPI.LASTNAME,
                        OrderAPI.ADDRES,
                        OrderAPI.METROSTATION,
                        OrderAPI.PHONE,
                        OrderAPI.RENTTIME,
                        OrderAPI.DELIVERYDATE,
                        OrderAPI.COMMENT,
                        OrderAPI.COLOR_BLACK
                )},
                {"Заказ только с серым цветом (GREY)", new Order(
                        OrderAPI.FIRSTNAME,
                        OrderAPI.LASTNAME,
                        OrderAPI.ADDRES,
                        OrderAPI.METROSTATION,
                        OrderAPI.PHONE,
                        OrderAPI.RENTTIME,
                        OrderAPI.DELIVERYDATE,
                        OrderAPI.COMMENT,
                        OrderAPI.COLOR_GREY
                )},
                {"Заказ с двумя цветами (BLACK и GREY)", new Order(
                        OrderAPI.FIRSTNAME,
                        OrderAPI.LASTNAME,
                        OrderAPI.ADDRES,
                        OrderAPI.METROSTATION,
                        OrderAPI.PHONE,
                        OrderAPI.RENTTIME,
                        OrderAPI.DELIVERYDATE,
                        OrderAPI.COMMENT,
                        OrderAPI.COLOR_BOTH
                )},
                {"Заказ без указания цвета", new Order(
                        OrderAPI.FIRSTNAME,
                        OrderAPI.LASTNAME,
                        OrderAPI.ADDRES,
                        OrderAPI.METROSTATION,
                        OrderAPI.PHONE,
                        OrderAPI.RENTTIME,
                        OrderAPI.DELIVERYDATE,
                        OrderAPI.COMMENT,
                        null
                )}
        };
    }

    @Test
    @Description("Создание заказа с цветом: {0}")
    public void shouldCreateOrderWithGivenColors() {
        Response response = orderClient.createOrder(testOrder);
        response.then()
                .statusCode(201)
                .body("track", notNullValue());
        track = response.path("track");
    }

    @After
    public void cancelOrder() {
        if (track != 0) {
            Response response = orderClient.cancelOrder(track);
            response.then().statusCode(200);
        }
    }
}