import io.qameta.allure.Step;
import io.restassured.RestAssured;
import model.Courier;
import model.CourierAPI;
import model.OrderAPI;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static model.CourierAPI.*;

public class OrderListTest {

    private static CourierAPI courierClient;
    private OrderAPI orderClient;
    private final Courier testCourier = new Courier(LOGIN, PASSWORD, FIRSTNAME);

    @BeforeClass
    public static void setUpBaseUrl() {
        courierClient = new CourierAPI();
        RestAssured.baseURI = courierClient.getBaseUri();
    }

    @Before
    @Step("Подготовка тестовых данных: создание и авторизация курьера")
    public void prepareTestData() {
        courierClient.createCourierExpectStatus200OK(testCourier);
        courierClient.loginCourierExpectStatus200OK(testCourier);
        orderClient = new OrderAPI();
    }

    @Test
    @Step("Получение списка заказов для созданного курьера → статус 200")
    public void shouldReturnOrderListForCourier() {
        orderClient.getOrderListExpectStatus200OK(courierClient.getId());
    }

    @After
    @Step("Очистка: удаление тестового курьера")
    public void cleanupCourier() {
        courierClient.deleteCourierExpectStatus200OK();
    }
}