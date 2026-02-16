import io.qameta.allure.Step;
import io.restassured.RestAssured;
import model.Courier;
import model.CourierAPI;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static model.CourierAPI.*;


public class CourierDuplicateTest {

    private static CourierAPI apiClient;
    private Courier testCourier;

    @BeforeClass
    public static void setUpBaseUrl() {
        apiClient = new CourierAPI();
        RestAssured.baseURI = apiClient.getBaseUri();
    }

    @Before
    @Step("Подготовка: создание и авторизация уникального курьера")
    public void createAndLoginCourier() {
        // Используем статические данные из CourierAPI, но можно и локальные
        testCourier = new Courier(LOGIN, PASSWORD, FIRSTNAME);
        apiClient.createCourierExpectStatus200OK(testCourier);
        apiClient.loginCourierExpectStatus200OK(testCourier);
    }

    @Test
    @Step("Попытка повторного создания того же курьера → статус 409 Conflict")
    public void shouldReturn409WhenCreatingDuplicateCourier() {
        apiClient.createDoubleCourierExpectStatus409CONFLICT(testCourier);
    }

    @After
    @Step("Очистка: удаление созданного курьера по ID")
    public void tearDown() {
        apiClient.deleteCourierExpectStatus200OK();
    }
}