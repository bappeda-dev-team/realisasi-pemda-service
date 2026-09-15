package cc.kertaskerja.opd.domain;

import cc.kertaskerja.integration.kepegawaian.OpdClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class OpdService {
    private final OpdClient opdClient;

    public OpdService(OpdClient opdClient) {
        this.opdClient = opdClient;
    }

    public Mono<List<OpdClient.OpdData>> findAllOpd() {
        return opdClient.fetchAllOpd();
    }
}