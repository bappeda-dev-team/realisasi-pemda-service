package cc.kertaskerja.integration.perencanaan;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties("integration.perencanaan")
public record PerencanaanProperties(
        String baseUrl,
        String authBaseUrl,
        String username,
        String password,
        boolean readThroughEnabled,
        Duration connectTimeout,
        Duration readTimeout
) {
}
