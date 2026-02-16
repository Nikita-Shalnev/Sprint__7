import io.qameta.allure.Step;
import model.Order;
import model.OrderAPI;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class OrderWithoutColorTest {

    private OrderAPI orderClient;
    private Order testOrder;

    @Before
    @Step("Подготовка заказа без указания цвета")
    public void prepareTestOrder() {
        testOrder = new Order(
                OrderAPI.FIRSTNAME,
                OrderAPI.LASTNAME,
                OrderAPI.ADDRES,
                OrderAPI.METROSTATION,
                OrderAPI.PHONE,
                OrderAPI.RENTTIME,
                OrderAPI.DELIVERYDATE,
                OrderAPI.COMMENT,
                null);
        orderClient = new OrderAPI();
    }

    @Test
    @Step("Создание заказа без цвета → статус 201, трек получен")
    public void shouldCreateOrderWithoutColor() {
        orderClient.createOrderExpectStatus201CREATED(testOrder);
    }

    @After
    @Step("Удаление созданного заказа")
    public void cleanupOrder() {
        orderClient.deleteOrderExpectStatus200OK();
    }
}