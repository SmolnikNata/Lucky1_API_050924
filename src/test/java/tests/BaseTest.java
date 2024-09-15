package tests;

import dto.LoginUserRequest;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.io.File;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class BaseTest {
    final static String BASE_URI = "http://chatty.telran-edu.de:8989";

    // final static String APP_ID_VALUE = "65faa99005388ea765125437";// мой APP_ID ключ
    // спецификация, для всех запросов одна/ хотим передавать в наших запросах апп йди
    static RequestSpecification specification = new RequestSpecBuilder()//такой класс,позволяет общие настройки держать вместе
            // актуальны для всех запросов, везде добавлять эти настройки в
            // запросах  / повторяющиеся части
            .setBaseUri(BASE_URI) //
            //.addHeader("app-id", APP_ID_VALUE)// в постман апп айди стоит в header(ключ, значение)
            .setContentType(ContentType.JSON)  // чтобы передавать в формате JSON
            .build();  // завершаем метод билд


    // метод, валиден для всех get запросов

    public String loginAccessToken(String email, String password) {
        LoginUserRequest requestBody = new LoginUserRequest(email, password);
        Response response = postRequest("/api/auth/login", 200, requestBody);

        String loginResponseAccessToken = response.body().jsonPath().getString("accessToken");
        assertFalse(loginResponseAccessToken.isEmpty());
        return loginResponseAccessToken;
    }

    public String getUserId(String accessToken) {
        Response meResponse = getRequest("/api/me", 200, accessToken);
        String userId = meResponse.body().jsonPath().getString("id");
        assertFalse(userId.isEmpty());
        return userId;
    }

    public static Response getRequest(String endpoint, Integer expectedStatusCode, String accessToken) {
        Response response = given()
                .spec(specification)
                .header("Authorization", "Bearer " + accessToken)
                .when()
                .log().all()
                .get(endpoint)
                .then()
                .log().all()
                .statusCode(expectedStatusCode)
                .extract().response();
        return response;

    }

    //универсальный метод для всех POST
    public static Response postRequest(String endpoint, Integer expectedStatusCode, Object body) {
        Response response = given()
                .spec(specification)
                .body(body)
                .when()
                .log().all()
                .post(endpoint)
                .then()
                .log().all()
                .statusCode(expectedStatusCode)
                .extract().response();
        return response;
    }

    //универсальный метод для всех PUT
    public static Response putRequest(String endpoint, Object body, Integer expectedStatusCode, String accessToken) {
        Response response = given()
                .spec(specification)
                .header("Authorization", "Bearer " + accessToken)
                .body(body)
                .contentType("application/json")
                .when()
                .log().all()
                .put(endpoint)
                .then()
                .log().all()
                .statusCode(expectedStatusCode)
                .extract().response();
        return response;
    }

    public static Response deleteRequest(String endpoint, Integer expectedStatusCode, String accessToken) {
        Response response = given()
                .spec(specification)
                .header("Authorization", "Bearer " + accessToken)
                .when()
                .log().all()
                .delete(endpoint)
                .then()
                .log().all()
                .statusCode(expectedStatusCode)
                .extract().response();
        return response;
    }

    // Метод для загрузки файлов через POST запрос с использованием multipart/form-data
    public static Response postMultipartRequest(String endpoint, File file, Integer expectedStatusCode, String accessToken) {
        Response response = given()
                .spec(specification)
                .header("Authorization", "Bearer " + accessToken)
                .contentType(ContentType.MULTIPART)
                .multiPart("multipartFile", file, "image/png")
                .when()
                .log()
                .all()
                .post(endpoint)
                .then()
                .log()
                .all()
                .statusCode(expectedStatusCode)
                .extract()
                .response();
        return response;
    }


}
