package cc.kertaskerja.integration.penetapan;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties("integration.penetapan")
public record PenetapanProperties(
        String baseUrl,
        boolean readThroughEnabled,
        Duration connectTimeout,
        Duration readTimeout
) {
}
