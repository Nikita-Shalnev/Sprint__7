package model;

import com.github.javafaker.Faker;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class CourierAPI {

    private static final String BASE_URI = "https://qa-scooter.praktikum-services.ru";

    public String getBaseUri() {
        return BASE_URI;
    }

    public static final String COURIER_ENDPOINT = "/api/v1/courier";
    public static final String LOGIN_COURIER_ENDPOINT = "/api/v1/courier/login";
    public static final String DELETE_COURIER_ENDPOINT = "/api/v1/courier/";

    static Faker faker = new Faker();
    public static final String LOGIN = faker.name().lastName() + System.currentTimeMillis();
    public static final String PASSWORD = faker.regexify("[0-9]{4}");
    public static final String FIRSTNAME = faker.name().firstName();

    public final String WRONGLOGIN = "&логин";
    public final String WRONGPASSWORD = "№пароль";

    @Step("Создание курьера")
    public Response createCourier(Courier courier) {
        return given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(courier)
                .when()
                .post(COURIER_ENDPOINT)
                .then()
                .log().body()
                .extract().response();
    }

    @Step("Логин курьера")
    public Response loginCourier(Courier courier) {
        return given()
                .log().body()
                .contentType(ContentType.JSON)
                .body(courier)
                .when()
                .post(LOGIN_COURIER_ENDPOINT)
                .then()
                .log().body()
                .extract().response();
    }

    @Step("Удаление курьера")
    public Response deleteCourier(int id) {
        return given()
                .log().uri()
                .contentType(ContentType.JSON)
                .delete(DELETE_COURIER_ENDPOINT + id)
                .then()
                .log().body()
                .extract().response();
    }

    @Step("Попытка создать дубликат курьера")
    public Response createDoubleCourier(Courier courier) {
        return given()
                .log().body()
                .contentType(ContentType.JSON)
                .body(courier)
                .when()
                .post(COURIER_ENDPOINT)
                .then()
                .log().all()
                .extract().response();
    }
}



