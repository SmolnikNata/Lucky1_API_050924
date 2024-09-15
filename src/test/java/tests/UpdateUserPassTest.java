package tests;

import dto.LoginUserRequest;
import dto.UpdateUserPassRequest;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UpdateUserPassTest extends BaseTest{
    @Test
    public void updateUserPasswordWithIncorrectCurrentPassword() {  // login
        String email = "hirsch.mariia@icloud.com";
        String password = "Blabla2024!!";
        LoginUserRequest requestBody = new LoginUserRequest(email, password);
        Response response = postRequest("/api/auth/login", 200, requestBody);
        String loginResponseAccessToken = response.body().jsonPath().getString("accessToken");
        assertFalse(loginResponseAccessToken.isEmpty());

        UpdateUserPassRequest updatePassRequest = new UpdateUserPassRequest(); //   invalid old password
        updatePassRequest.setCurrentPassword("NeBlabla2024!!");
        updatePassRequest.setNewPassword("Blabla2024!!!!");
        updatePassRequest.setConfirmPassword("Blabla2024!!!!");
        String updatePasswordUrl = "http://chatty.telran-edu.de:8989/api/user/password/update";
        Response updatePasswordResponse = putRequest(updatePasswordUrl, updatePassRequest, 400, loginResponseAccessToken);

        String errorMessage = updatePasswordResponse.body().jsonPath().getString("message");
        assertTrue(errorMessage.contains("Current password is incorrect"));

        String httpStatus = updatePasswordResponse.body().jsonPath().getString("httpStatus");
        assertEquals("BAD_REQUEST", httpStatus, "Expected httpStatus: 'BAD_REQUEST'");
    }

    @Test
    public void updateUserPasswordWithShortNewPassword() {  // short password
        String email = "hirsch.mariia@icloud.com";
        String password = "Blabla2024!!";
        LoginUserRequest requestBody = new LoginUserRequest(email, password);
        Response response = postRequest("/api/auth/login", 200, requestBody);
        String loginResponseAccessToken = response.body().jsonPath().getString("accessToken");
        assertFalse(loginResponseAccessToken.isEmpty());

        UpdateUserPassRequest updatePassRequest = new UpdateUserPassRequest();
        updatePassRequest.setCurrentPassword("Blabla2024!!");
        updatePassRequest.setNewPassword("Bl2!");
        updatePassRequest.setConfirmPassword("Bl2!");

        String updatePasswordUrl = "http://chatty.telran-edu.de:8989/api/user/password/update";
        Response updatePasswordResponse = putRequest(updatePasswordUrl, updatePassRequest, 400, loginResponseAccessToken);

        assertEquals(400, updatePasswordResponse.statusCode(), "Expected 400 Bad Request due to short new password");
        String errorMessage = updatePasswordResponse.body().jsonPath().getString("newPassword[0]");
        assertTrue(errorMessage.contains("Password must contain at least 8 characters"));
    }

}
