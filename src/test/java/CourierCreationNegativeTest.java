import io.qameta.allure.Step;
import io.restassured.RestAssured;
import model.Courier;
import model.CourierAPI;
import org.junit.BeforeClass;
import org.junit.Test;
import static model.CourierAPI.*;


public class CourierCreationNegativeTest {

    private static CourierAPI courierApi;

    @BeforeClass
    public static void setUp() {
        courierApi = new CourierAPI();
        RestAssured.baseURI = courierApi.getBaseUri();
    }

    @Test
    @Step("Проверка: создание курьера без пароля (password = null) → статус 400")
    public void shouldNotCreateCourierWhenPasswordIsNull() {
        Courier courierWithoutPassword = new Courier(LOGIN, null);
        courierApi.createCourierExpectStatus400BADREQUEST(courierWithoutPassword);
    }

    @Test
    @Step("Проверка: создание курьера без логина (login = null) → статус 400")
    public void shouldNotCreateCourierWhenLoginIsNull() {
        Courier courierWithoutLogin = new Courier(null, PASSWORD);
        courierApi.createCourierExpectStatus400BADREQUEST(courierWithoutLogin);
    }
}