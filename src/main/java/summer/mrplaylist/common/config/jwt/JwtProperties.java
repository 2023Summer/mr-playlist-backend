package summer.mrplaylist.common.config.jwt;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "spring.jwt")
@Data
public class JwtProperties {
    private String secretKey;
    private int accessTokenExpiration;
    private int refreshTokenExpiration = 1000 * 3600 * 24 * 15; // 15일
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
}
