package dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class CreatePostRequest {
    String title;
    String description;
    String body;
    String imageUrl;
    String publishDate;
    Boolean draft;
}
