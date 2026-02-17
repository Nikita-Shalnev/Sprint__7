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
import org.junit.*;

import static model.CourierAPI.*;
import static org.hamcrest.Matchers.*;

public class CourierDuplicateTest {

    private static CourierAPI apiClient;
    private Courier testCourier;
    private int courierId;

    @BeforeClass
    public static void setUpBaseUrl() {
        apiClient = new CourierAPI();
        RestAssured.baseURI = apiClient.getBaseUri();
        // Глобальная настройка Jackson: не сериализовать null-поля
        RestAssured.config = RestAssuredConfig.config()
                .objectMapperConfig(new ObjectMapperConfig().jackson2ObjectMapperFactory(
                        (cls, charset) -> new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL)
                ));
    }

    @Before
    @Step("Подготовка: создание и авторизация уникального курьера")
    public void createAndLoginCourier() {
        testCourier = new Courier(LOGIN, PASSWORD, FIRSTNAME);

        Response createResp = apiClient.createCourier(testCourier);
        createResp.then().statusCode(201);

        Response loginResp = apiClient.loginCourier(testCourier);
        loginResp.then().statusCode(200);
        courierId = loginResp.path("id");
    }

    @Test
    @Description("Попытка создать курьера с уже существующим логином → статус 409 и сообщение об ошибке")
    public void shouldReturn409WhenCreatingDuplicateCourier() {
        Response response = apiClient.createDoubleCourier(testCourier);
        response.then()
                .statusCode(409)
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }

    @After
    @Step("Очистка: удаление созданного курьера")
    public void tearDown() {
        if (courierId != 0) {
            Response deleteResp = apiClient.deleteCourier(courierId);
            deleteResp.then().statusCode(200);
        }
    }
}