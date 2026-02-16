import io.qameta.allure.Step;
import model.Order;
import model.OrderAPI;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class OrderColorTest {

    private final OrderAPI orderClient;
    private final Order testOrder;

    public OrderColorTest(String testDescription, Order order) {
        this.testOrder = order;
        this.orderClient = new OrderAPI();
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
    @Step("Создание заказа с цветом: {0}")
    public void shouldCreateOrderWithGivenColors() {
        orderClient.createOrderExpectStatus201CREATED(testOrder);

    }

    @After
    @Step("Отмена созданного заказа")
    public void cancelOrder() {
        orderClient.deleteOrderExpectStatus200OK();
    }
}