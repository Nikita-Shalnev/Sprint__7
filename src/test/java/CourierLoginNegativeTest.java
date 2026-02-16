import io.qameta.allure.Step;
import io.restassured.RestAssured;
import model.Courier;
import model.CourierAPI;
import org.junit.BeforeClass;
import org.junit.Test;
import static model.CourierAPI.*;


public class CourierLoginNegativeTest {

    private static CourierAPI apiClient;

    @BeforeClass
    public static void setUp() {
        apiClient = new CourierAPI();
        RestAssured.baseURI = apiClient.getBaseUri();
    }

        @Test
        @Step("Попытка входа с неверным логином (корректный пароль) → статус 404")
        public void shouldReturn404WhenLoginIsInvalid() {
            Courier courier = new Courier(apiClient.WRONGLOGIN, PASSWORD);
            apiClient.loginCourierExpectStatus404NOTFOUND(courier);
        }

        @Test
        @Step("Попытка входа с неверным паролем (корректный логин) → статус 404")
        public void shouldReturn404WhenPasswordIsInvalid() {
            Courier courier = new Courier(LOGIN, apiClient.WRONGPASSWORD);
            apiClient.loginCourierExpectStatus404NOTFOUND(courier);
        }

        @Test
        @Step("Попытка входа с несуществующим в базе пользователем → статус 404")
        public void shouldReturn404WhenUserDoesNotExist() {
            // Используем валидные по формату, но незарегистрированные данные
            Courier courier = new Courier(LOGIN, PASSWORD);
            apiClient.loginCourierExpectStatus404NOTFOUND(courier);
        }

        @Test
        @Step("Попытка входа без указания логина (login = null) → статус 400")
        public void shouldReturn400WhenLoginIsMissing() {
            Courier courier = new Courier(null, PASSWORD);
            apiClient.loginCourierExpectStatus400BADREQUEST(courier);
        }

        @Test
        @Step("Попытка входа без указания пароля (password = null) → статус 400")
        public void shouldReturn400WhenPasswordIsMissing() {
            Courier courier = new Courier(LOGIN, null);
            apiClient.loginCourierExpectStatus400BADREQUEST(courier);
        }
    }