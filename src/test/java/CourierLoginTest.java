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
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class CourierLoginTest {

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
    @Step("Создание тестового курьера")
    public void createTestCourier() {
        testCourier = new Courier(LOGIN, PASSWORD, FIRSTNAME);
        Response response = apiClient.createCourier(testCourier);
        response.then().statusCode(201);
    }

    @Test
    @Description("Выполнение входа с корректными учётными данными → статус 200, получен ID")
    public void shouldLoginWithValidCredentials() {
        Response response = apiClient.loginCourier(testCourier);
        response.then().statusCode(200);
        courierId = response.path("id");
        assertThat(courierId, is(notNullValue()));
    }

    @After
    @Step("Удаление созданного курьера")
    public void deleteTestCourier() {
        if (courierId != 0) {
            Response response = apiClient.deleteCourier(courierId);
            response.then().statusCode(200);
        }
    }
}