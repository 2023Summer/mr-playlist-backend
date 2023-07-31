package summer.mrplaylist.common.config.jwt;

public interface JwtProperties {
    String SECRET_KEY = "SlNPTl9XRUJfVE9LRU5fVEVTVF9TRUNSRVRfS0VZUw=="; // 실제 서비스시 제외
    int ACCESS_TOKEN_EXPIRATION = 1000 * 60 * 30; // 30분
    int REFRESH_TOKEN_EXPIRATION = 1000 * 3600 * 24 * 15; // 15일
    String TOKEN_PREFIX = "Bearer ";
    String HEADER_STRING = "Authorization";
}
