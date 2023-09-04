package summer.mrplaylist;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import summer.mrplaylist.common.config.jwt.JwtProperties;

@EnableConfigurationProperties(JwtProperties.class)
@SpringBootApplication
public class PlaylistApplication {

	static {
		System.setProperty("com.amazonaws.sdk.disableEc2Metadata", "true");
	}
	public static void main(String[] args) {
		SpringApplication.run(PlaylistApplication.class, args);
	}

}
