import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.response.Response;
import model.Courier;
import model.CourierAPI;
import org.junit.BeforeClass;
import org.junit.Test;

import static model.CourierAPI.*;
import static org.hamcrest.Matchers.*;

public class CourierCreationNegativeTest {

    private static CourierAPI courierApi;

    @BeforeClass
    public static void setUp() {
        courierApi = new CourierAPI();
        RestAssured.baseURI = courierApi.getBaseUri();
        // Глобальная настройка Jackson: не сериализовать null-поля
        RestAssured.config = RestAssuredConfig.config()
                .objectMapperConfig(new ObjectMapperConfig().jackson2ObjectMapperFactory(
                        (cls, charset) -> new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL)
                ));
    }

    @Test
    @Description("Создание курьера без пароля → статус 400 и сообщение об ошибке")
    public void shouldNotCreateCourierWhenPasswordIsNull() {
        Courier courier = new Courier(LOGIN, null);
        Response response = courierApi.createCourier(courier);
        response.then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @Description("Создание курьера без логина → статус 400 и сообщение об ошибке")
    public void shouldNotCreateCourierWhenLoginIsNull() {
        Courier courier = new Courier(null, PASSWORD);
        Response response = courierApi.createCourier(courier);
        response.then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }
}