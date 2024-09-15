package dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class UpdateUserInfoRequest {
    private String id;
    private String name;
    private String surname;
    private String birthDate;
    private String phone;
    private String gender;
    private String email;
    private String role;
    private String avatarUrl;
    private String backgroundUrl;
}

