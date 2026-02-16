package model;

import com.github.javafaker.Faker;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

public class OrderAPI {

    static CourierAPI courierAPI = new CourierAPI();

    private Integer id; // track последнего созданного заказа

    public Integer getId() {
        return id;
    }

    private String url = courierAPI.getBaseUri();

    public final static String ORDER_ENDPOINT = "/api/v1/orders";
    public final static String ORDER_TRACK_ENDPOINT = "/api/v1/orders/track?t=";
    public final static String ORDER_DELETE_ENDPOINT = "/api/v1/orders/cancel";

    static Faker faker = new Faker();

    public static final String FIRSTNAME = faker.name().firstName() + System.currentTimeMillis();
    public static final String LASTNAME = faker.name().lastName() + System.currentTimeMillis();
    public static final String ADDRES = faker.address().cityName();
    public static final String METROSTATION = String.valueOf(faker.number().numberBetween(1, 30));
    public static final String PHONE = faker.numerify("+7 9## ### ####");
    public static final Integer RENTTIME = Integer.valueOf(faker.regexify("[1-9]{1}"));
    public static final String DELIVERYDATE = LocalDate.now().plusDays(new Random().nextInt(10) + 1).format(DateTimeFormatter.ISO_DATE);
    public static final String COMMENT = faker.lorem().sentence(3, 5);
    public static final List<String> COLOR_BLACK = Arrays.asList("BLACK");
    public static final List<String> COLOR_GREY = Arrays.asList("GREY");
    public static final List<String> COLOR_BOTH = Arrays.asList("BLACK", "GREY");

    @Step("Создание заказа")
    public void createOrderExpectStatus201CREATED(Order order) {
        id = given()
                .baseUri(url)
                .log().body()
                .contentType(ContentType.JSON)
                .body(order)
                .post(ORDER_ENDPOINT)
                .then()
                .statusCode(201)
                .body("track", notNullValue())
                .log().body()
                .log().status()
                .extract().path("track");

        // Дополнительный GET-запрос для проверки (можно оставить или убрать)
        given()
                .baseUri(url)
                .log().body()
                .get(ORDER_TRACK_ENDPOINT + id)
                .then()
                .statusCode(200)
                .log().body();
    }

    @Step("Получение списка заказов курьера")
    public void getOrderListExpectStatus200OK(int id) {
        given()
                .baseUri(url)
                .log().all()
                .contentType(ContentType.JSON)
                .get(ORDER_ENDPOINT + "?courierId=" + id)
                .then()
                .statusCode(200)
                .log().all()
                .log().status();
    }

    @Step("Отмена заказа")
    public void deleteOrderExpectStatus200OK() {
        given()
                .contentType(ContentType.JSON)
                .baseUri(url)
                .log().all()
                .put(ORDER_DELETE_ENDPOINT + "?track=" + id)
                .then()
                .statusCode(200)
                .log().all();
    }
}