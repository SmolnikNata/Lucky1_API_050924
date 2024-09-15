package tests;

import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DeleteUserTest extends BaseTest{
    @Test
    public void deleteUserWithoutPermission() {
//        String email = "hirsch.mariia@icloud.com";
//        String password = "Blabla2024!!";
//        LoginUserRequest requestBody = new LoginUserRequest(email, password);
//        Response response = postRequest("/api/auth/login", 200, requestBody);
//        String loginResponseAccessToken = response.body().jsonPath().getString("accessToken");
//
//        assertFalse(loginResponseAccessToken.isEmpty(), "Access token should not be empty");
//
//        String deleteUrl = "http://chatty.telran-edu.de:8989/api/users/a2475ea7-7d39-446c-90b1-c7a1514ae04d";
//        Response deleteResponse = deleteRequest(deleteUrl, 403,  loginResponseAccessToken);
//
//        assertEquals(403, deleteResponse.statusCode());  // ???
//
//        String expectedErrorMessage = "You don't have permission to delete users";
//        String actualErrorMessage = deleteResponse.body().jsonPath().getString("message");
//        assertEquals(expectedErrorMessage, actualErrorMessage,
//                "Expected error message to be: " + expectedErrorMessage);//??
//
//
//        String expectedHttpStatus = "FORBIDDEN";
//        String actualHttpStatus = deleteResponse.body().jsonPath().getString("httpStatus");
//        assertEquals(expectedHttpStatus, actualHttpStatus,
//                "Expected HTTP status to be: " + expectedHttpStatus);
        String accessToken = loginAccessToken("hirsch.mariia@icloud.com", "NewOne!!01"); // получение токена
        String userId = getUserId(accessToken); // получение userId

        String deleteUrl = BASE_URI + "/api/users/" + userId;
        Response deleteResponse = deleteRequest(deleteUrl, 403, accessToken);

        assertEquals(403, deleteResponse.statusCode(), "Expected 403 Forbidden due to lack of permissions");
        assertEquals("You don't have permission to delete users",
                deleteResponse.body().jsonPath().getString("message"),
                "Expected error message to be: 'You don't have permission to delete users'");
        assertEquals("FORBIDDEN",
                deleteResponse.body().jsonPath().getString("httpStatus"),
                "Expected HTTP status to be: 'FORBIDDEN'");
    }
    @Test
    public void deleteAdminWithAdminPermission() {// Faker
        String accessToken = loginAccessToken("Admin.kater23155@gmail.com", "1234566789AA"); // получение токена
        String userId = getUserId(accessToken); // получение userId

        String deleteUrl = BASE_URI + "/api/users/" + userId;
        Response deleteResponse = deleteRequest(deleteUrl, 200, accessToken);

        assertEquals(200, deleteResponse.statusCode());

    }
    @Test
    public void deleteUserWithAdminPermission() {

        String accessToken = loginAccessToken("Admin.kater231556@gmail.com", "11236589JJ"); // получение токена
        String deleteUrl = BASE_URI + "/api/users/" + "a50df9b2-e8d1-4247-8d7d-77ed7732c01b";
        Response deleteResponse = deleteRequest(deleteUrl, 200, accessToken);
        assertEquals(200, deleteResponse.statusCode());

    }
}


