package summer.mrplaylist.comment.dto;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Data
public class CommentForm {
    private String content;
}