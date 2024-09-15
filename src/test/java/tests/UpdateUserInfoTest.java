package tests;

import dto.UpdateUserInfoRequest;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UpdateUserInfoTest extends BaseTest {
    @Test
    public void updateUserInfoSuccessfully() {
        String accessToken = loginAccessToken("hirsch.mariia@icloud.com", "NewOne!!01");
        String userId = getUserId(accessToken);
        UpdateUserInfoRequest updateRequest = new UpdateUserInfoRequest().builder()
                .name("Toma")
                .surname("Tomovna")
                .phone("+1234567890")
                .birthDate("1992-01-01T00:00:00.000Z")
                .gender("MALE")
                .build();


        String updateUrl = BASE_URI + "/api/users/" + userId;
        Response updateResponse = putRequest(updateUrl, updateRequest, 200, accessToken);

        UpdateUserInfoRequest updatedUserInfo = updateResponse.body().as(UpdateUserInfoRequest.class);

        assertEquals("Toma", updatedUserInfo.getName());
        assertEquals("Tomovna", updatedUserInfo.getSurname());
        assertEquals("1992-01-01T00:00:00.000Z", updatedUserInfo.getBirthDate());
        assertEquals("+1234567890", updatedUserInfo.getPhone());
        assertEquals("MALE", updatedUserInfo.getGender());
    }

    @Test
    public void updateDateOfBirthday() {

        String accessToken = loginAccessToken("hirsch.mariia@icloud.com", "NewOne!!01");
        String userId = getUserId(accessToken);

        UpdateUserInfoRequest updateBirthdayRequest = new UpdateUserInfoRequest();

        updateBirthdayRequest.setBirthDate("1892-01-01T00:00:00.000Z");
        String updateBirthdayUrl = BASE_URI + "/api/users/" + userId;
        Response updateBirthdayResponse = putRequest(updateBirthdayUrl, updateBirthdayRequest, 400, accessToken);

        assertEquals(400, updateBirthdayResponse.statusCode());
        String errorMessage = updateBirthdayResponse.body().jsonPath().getString("birthDate");
        assertTrue(errorMessage.contains("Incorrect birthdate"));

    }

    @Test
    public void updateInfoWithInvalidPhoneFormat() {

        String accessToken = loginAccessToken("hirsch.mariia@icloud.com", "NewOne!!01");
        String userId = getUserId(accessToken);

        UpdateUserInfoRequest updateRequest = new UpdateUserInfoRequest();
        updateRequest.setPhone("123blabla");

        String updateUrl = BASE_URI + "/api/users/" + userId;
        Response updateResponse = putRequest(updateUrl, updateRequest, 400, accessToken);
        assertEquals(400, updateResponse.statusCode(), "Expected 400 Bad Request due to invalid phone format");

        List<String> errorMessages = updateResponse.body().jsonPath().getList("phone", String.class);//выплевывает список ошибок
        assertTrue(errorMessages.contains("Phone must contain + and numbers"));
        assertTrue(errorMessages.contains("Phone must contain from 10 to 12characters"));

    }

    @Test
    public void updateInfoWithExcessivelyLongName() {

        String accessToken = loginAccessToken("hirsch.mariia@icloud.com", "NewOne!!01");
        String userId = getUserId(accessToken);

        UpdateUserInfoRequest updateRequest = new UpdateUserInfoRequest();
        updateRequest.setName("Toma".repeat(50));
        updateRequest.setSurname("Tomovna");

        String updateUrl = BASE_URI + "/api/users/" + userId;
        Response updateResponse = putRequest(updateUrl, updateRequest, 400, accessToken);

        assertEquals(400, updateResponse.statusCode());
        assertTrue(updateResponse.body().asString().contains("Name must contain from 3 to 20 character"));
        ;

        assertEquals(400, updateResponse.statusCode());
        assertTrue(updateResponse.body().asString().contains("Name must contain from 3 to 20 character"));
    }

    @Test
    public void updateInfoWithInvalidGenderFormat() {

        String accessToken = loginAccessToken("hirsch.mariia@icloud.com", "NewOne!!01");
        String userId = getUserId(accessToken);

//        LoginUserRequest requestBody = new LoginUserRequest("hirsch.mariia@icloud.com", "Blabla2024!!");
//        Response response = postRequest("/api/auth/login", 200, requestBody);
//        String loginResponseAccessToken = response.body().jsonPath().getString("accessToken");
//        assertFalse(loginResponseAccessToken.isEmpty());
//
//        Response meResponse = getRequest("/api/me", 200, loginResponseAccessToken);
//        String userId = meResponse.body().jsonPath().getString("id");
//        assertFalse(userId.isEmpty());

        UpdateUserInfoRequest updateRequest = new UpdateUserInfoRequest();
        updateRequest.setGender("Transgender");

        String updateUrl = BASE_URI + "/api/users/" + userId;
        Response updateResponse = putRequest(updateUrl, updateRequest, 400, accessToken);

        assertEquals(400, updateResponse.statusCode());
        assertTrue(updateResponse.body().asString().contains("No enum constant org.example.chatty.entity.enums.Gender.Transgender"));
    }

    @Test
    public void emptyField() {

        String accessToken = loginAccessToken("hirsch.mariia@icloud.com", "NewOne!!01");
        String userId = getUserId(accessToken);

        UpdateUserInfoRequest updateRequest = new UpdateUserInfoRequest();
        updateRequest.setName("");
        updateRequest.setSurname("");
        updateRequest.setPhone("");
        updateRequest.setBirthDate("");
        updateRequest.setGender("");

        String updateUrl = BASE_URI + "/api/users/" + userId;
        Response updateResponse = putRequest(updateUrl, updateRequest, 400, accessToken);

        assertEquals(400, updateResponse.statusCode(), "Expected status code 400 due to validation errors.");//выплевывает список ошибок

        List<String> errorMessagesPhone = updateResponse.body().jsonPath().getList("phone", String.class);
        assertTrue(errorMessagesPhone.contains("Phone must contain + and numbers"));
        assertTrue(errorMessagesPhone.contains("Phone must contain from 10 to 12characters"));

        List<String> errorMessagesSurname = updateResponse.body().jsonPath().getList("surname", String.class);
        assertTrue(errorMessagesSurname.contains("Surname must contain from 3 to 20 characters"));
        assertTrue(errorMessagesSurname.contains("must match \"^[A-Za-z-]+$\""));    //  ?? "должно соответствовать \"^[A-Za-z-]+$\"" в свагере - проваливается


        //assertEquals(400, updateResponse.statusCode(), "Expected 400 Bad Request due to invalid phone format");
        List<String> errorMessagesName = updateResponse.body().jsonPath().getList("name", String.class);
        assertTrue(errorMessagesName.contains("must match \"^[A-Za-z-]+$\""));    // ?? "должно соответствовать \"^[A-Za-z-]+$\""
        assertTrue(errorMessagesName.contains("Name must contain from 3 to 20 characters"));


    }


}
