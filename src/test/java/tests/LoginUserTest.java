package tests;

import dto.LoginUserRequest;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static tests.BaseTest.postRequest;

public class LoginUserTest {
    @Test
    public void successLoginUserRequiredFields() {
        String email = "hirsch.mariia@icloud.com";
        String password = "NewOne!!01";

        LoginUserRequest requestBody = new LoginUserRequest(email, password);
        Response response = postRequest("/api/auth/login", 200, requestBody);

        //String loginResponseAccessToken = response.body().jsonPath().getObject("", LoginResponse.class);
        //assertFalse(loginResponseAccessToken.isEmpty());

        String loginResponseAccessToken = response.body().jsonPath().getString("accessToken");
        assertFalse(loginResponseAccessToken.isEmpty());
    }

    // поля пустые//

    @Test
    public void withoutEmail() {
        String email = "";
        String password = "cat2016!";


        LoginUserRequest requestBody = new LoginUserRequest(email, password);
        Response response = postRequest("/api/auth/login", 400, requestBody);

        //String loginResponseAccessToken = response.body().jsonPath().getObject("", LoginResponse.class);
        //assertFalse(loginResponseAccessToken.isEmpty());

        List<String> errorMessages = response.body().jsonPath().getList("email", String.class);
        assertTrue(errorMessages.contains("Email cannot be empty"));
        assertTrue(errorMessages.contains("Invalid email format"));

    }

    @Test
    public void withoutPassword() {
        String email = "ghjk2@gmail.com";
        String password = "";

        LoginUserRequest requestBody = new LoginUserRequest(email, password);
        Response response = postRequest("/api/auth/login",400,requestBody);

        //String loginResponseAccessToken = response.body().jsonPath().getObject("", LoginResponse.class);
        //assertFalse(loginResponseAccessToken.isEmpty());

        List<String> errorMessages = response.body().jsonPath().getList("password", String.class);
        assertTrue(errorMessages.contains("Password cannot be empty"));


    }
    @Test
    public void invalidPasswordWithOnlyCharacters() {
        String email = "ghjk2@gmail.com";
        String password = "dsfhj";

        LoginUserRequest requestBody = new LoginUserRequest(email, password);
        Response response = postRequest("/api/auth/login",400, requestBody);

        //String loginResponseAccessToken = response.body().jsonPath().getObject("", LoginResponse.class);
        //assertFalse(loginResponseAccessToken.isEmpty());

        List<String> errorMessages = response.body().jsonPath().getList("password", String.class);
        assertTrue(errorMessages.contains("Password must contain at least 8 characters"));


    }
    @Test
    public void invalidPasswordWithOnlyNumbers() {
        String email = "ghjk2@gmail.com";
        String password = "12334";

        LoginUserRequest requestBody = new LoginUserRequest(email, password);
        Response response = postRequest("/api/auth/login",400, requestBody);

        //String loginResponseAccessToken = response.body().jsonPath().getObject("", LoginResponse.class);
        //assertFalse(loginResponseAccessToken.isEmpty());

        List<String> errorMessages = response.body().jsonPath().getList("password", String.class);
        assertTrue(errorMessages.contains("Password must contain letters and numbers"));

    }

}

