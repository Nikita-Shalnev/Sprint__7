import io.qameta.allure.Step;
import io.restassured.RestAssured;
import model.Courier;
import model.CourierAPI;
import org.junit.*;
import static model.CourierAPI.*;

public class CourierLoginTest {

    private static CourierAPI apiClient;
    private Courier testCourier;

    @BeforeClass
    public static void setUpBaseUrl() {
        apiClient = new CourierAPI();
        RestAssured.baseURI = apiClient.getBaseUri();
    }

    @Before
    @Step("Создание тестового курьера перед проверкой логина")
    public void createTestCourier() {
        testCourier = new Courier(LOGIN, PASSWORD, FIRSTNAME);
        apiClient.createCourierExpectStatus200OK(testCourier);
    }

    @Test
    @Step("Выполнение входа с корректными учётными данными → статус 200, получен ID")
    public void shouldLoginWithValidCredentials() {
        apiClient.loginCourierExpectStatus200OK(testCourier);
    }

    @After
    @Step("Удаление созданного курьера по ID")
    public void deleteTestCourier() {
        apiClient.deleteCourierExpectStatus200OK();
    }
}