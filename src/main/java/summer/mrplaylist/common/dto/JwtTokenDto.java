package summer.mrplaylist.common.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JwtTokenDto {
    private String grantType;
    private String accessToken;
    private String refreshToken;

    public static JwtTokenDto createJwtTokenDto(String accessToken, String refreshToken) {
        return JwtTokenDto.builder()
                .grantType("Bearer ")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
