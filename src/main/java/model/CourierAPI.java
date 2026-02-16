package model;

import com.github.javafaker.Faker;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;

import static io.restassured.RestAssured.given;
import static java.net.HttpURLConnection.*;

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

    private Integer id;

    public Integer getId() {
        return id;
    }

    public String getLogin() {
        return LOGIN;
    }

    public String getPassword() {
        return PASSWORD;
    }

    @Step("Создание курьера (ожидаемый статус 201 Created)")
    public void createCourierExpectStatus200OK(Courier courier) {
        given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(courier)
                .when()
                .post(COURIER_ENDPOINT)
                .then()
                .log().body()
                .statusCode(HTTP_CREATED);
    }

    @Step("Создание курьера (ожидаемый статус 400 Bad Request)")
    public void createCourierExpectStatus400BADREQUEST(Courier courier) {
        given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(courier)
                .when()
                .post(COURIER_ENDPOINT)
                .then()
                .log().body()
                .statusCode(HTTP_BAD_REQUEST);
    }

    @Step("Логин курьера (ожидаемый статус 200 OK)")
    public void loginCourierExpectStatus200OK(Courier courier) {
        id = given()
                .log().body()
                .contentType(ContentType.JSON)
                .body(courier)
                .when()
                .post(LOGIN_COURIER_ENDPOINT)
                .then()
                .log().body()
                .statusCode(HTTP_OK)
                .extract().path("id");
    }

    @Step("Логин курьера (ожидаемый статус 404 Not Found)")
    public void loginCourierExpectStatus404NOTFOUND(Courier courier) {
        given()
                .log().body()
                .contentType(ContentType.JSON)
                .body(courier)
                .when()
                .post(LOGIN_COURIER_ENDPOINT)
                .then()
                .log().body()
                .statusCode(HTTP_NOT_FOUND);
    }

    @Step("Логин курьера (ожидаемый статус 400 Bad Request)")
    public void loginCourierExpectStatus400BADREQUEST(Courier courier) {
        given()
                .log().body()
                .contentType(ContentType.JSON)
                .body("{\"password\": \"1234\"}")
                .when()
                .post(LOGIN_COURIER_ENDPOINT)
                .then()
                .log().body()
                .statusCode(HTTP_BAD_REQUEST);
    }

    @Step("Удаление курьера (ожидаемый статус 200 OK)")
    public void deleteCourierExpectStatus200OK() {
        given()
                .log().uri()
                .contentType(ContentType.JSON)
                .delete(DELETE_COURIER_ENDPOINT + id)
                .then()
                .log().body()
                .statusCode(HTTP_OK);
    }

    @Step("Попытка создать дубликат курьера (ожидаемый статус 409 Conflict)")
    public void createDoubleCourierExpectStatus409CONFLICT(Courier courier) {
        given()
                .log().body()
                .contentType(ContentType.JSON)
                .body(courier)
                .post(COURIER_ENDPOINT)
                .then()
                .log().all()
                .statusCode(HTTP_CONFLICT);
    }
}




