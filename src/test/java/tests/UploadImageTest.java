package tests;

import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UploadImageTest extends BaseTest {
    @Test
    public void UploadImageSuccessfully() {
        String accessToken = loginAccessToken("ghjk2@gmail.com", "cat2016!");

        File file = new File("C:\\Users\\Natalia Smolnikova\\Desktop\\Снимок экрана 2024-03-06 094931.png"); // Укажите правильный путь к вашему изображению
        String uploadUrl = BASE_URI + "/api/images";

        // Выполняем POST запрос для загрузки изображения с использованием нового метода postMultipartRequest
        Response response = postMultipartRequest(uploadUrl, file , 201, accessToken);

        // Проверяем, что статус код 201 (означает успешное создание ресурса)
        assertEquals(201, response.statusCode(), "Expected status code 201 for successful image upload.");

        // Проверяем, что в ответе присутствует URL загруженного изображения
        String imageUrl = response.jsonPath().getString("imageUrl");
        assertNotNull(imageUrl, "Image URL should not be null or empty.");


    }
}
