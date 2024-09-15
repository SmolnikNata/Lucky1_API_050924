package tests;

import dto.UpdatePostRequest;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UpdatePostTest extends BaseTest{

    @Test
    public void updatePostSuccessfully() {
        String accessToken = loginAccessToken("hirsch.mariia@icloud.com", "Blabla2024!!");
        String userId = getUserId(accessToken);
//        String email = "hirsch.mariia@icloud.com";
//        String password = "Blabla2024!!";
//        LoginUserRequest requestBody = new LoginUserRequest(email, password);
//        Response response = postRequest("/api/auth/login", 200, requestBody);
//        String loginResponseAccessToken = response.body().jsonPath().getString("accessToken");
//        assertFalse(loginResponseAccessToken.isEmpty());

        UpdatePostRequest updateRequest = new UpdatePostRequest();
        updateRequest.setTitle("Dulce et Decorum Est/Blabla/030303");
        updateRequest.setDescription("Ut debitis iusto sint/Blabla/030303");
        updateRequest.setBody("Hic sit nisi dolorem ipsum. Perferendis et labore ratione reiciendis iste molestiae accusantium. Quas facere quam. Iusto neque est sed eos qui non est/Blabla/030303");
        updateRequest.setDraft(false); //?? выключен
        String updateUrl = "http://chatty.telran-edu.de:8989/api/posts/ad548295-7969-417d-971b-d260b42c2f8d";

        Response updateResponse = putRequest(updateUrl, updateRequest, 200, accessToken);
        assertEquals(200, updateResponse.statusCode());

        assertEquals("Dulce et Decorum Est/Blabla/030303", updateResponse.body().jsonPath().getString("title"));
        assertEquals("Ut debitis iusto sint/Blabla/030303", updateResponse.body().jsonPath().getString("description"));
        assertEquals("Hic sit nisi dolorem ipsum. Perferendis et labore ratione reiciendis iste molestiae accusantium. Quas facere quam. Iusto neque est sed eos qui non est/Blabla/030303", updateResponse.body().jsonPath().getString("body"));
        //assertEquals(false, updateResponse.body().jsonPath().getBoolean("draft"));
    }
}
