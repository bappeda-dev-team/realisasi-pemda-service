package cc.kertaskerja.pegawai.domain;

import cc.kertaskerja.integration.kepegawaian.PegawaiClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class PegawaiService {
    private final PegawaiClient pegawaiClient;

    public PegawaiService(PegawaiClient pegawaiClient) {
        this.pegawaiClient = pegawaiClient;
    }

    public Mono<List<PegawaiClient.PegawaiData>> findAllPegawai() {
        return pegawaiClient.fetchAllPegawai();
    }
}