package cc.kertaskerja.realisasi_pemda_service.tujuan.domain;

import cc.kertaskerja.realisasi_pemda_service.realisasi.domain.JenisRealisasi;
import cc.kertaskerja.realisasi_pemda_service.tujuan.web.TujuanRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TujuanServiceTests {
    @Mock
    private TujuanRepository tujuanRepository;

    @InjectMocks
    private TujuanService tujuanService;

    @Test
    void batchSubmitRealisasiTujuanShouldBuildAndSaveAllItem() {
        // given
        TujuanRequest r1 = new TujuanRequest("T1", "I1", "100.0", 50.0, "unit1", "2025", JenisRealisasi.NAIK);
        TujuanRequest r2 = new TujuanRequest("T2", "I2", "200.0", 75.0, "unit2", "2026", JenisRealisasi.NAIK);
        when(tujuanRepository.save(ArgumentMatchers.any(Tujuan.class)))
                .thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));
        // when
        Flux<Tujuan> result = tujuanService.batchSubmitRealisasiTujuan(List.of(r1, r2));

        //then
        StepVerifier.create(result)
                .expectNextMatches(t ->
                        t.tujuanId().equals("T1") &&
                        t.indikatorId().equals("I1") &&
                        t.target().equals("100.0") &&
                        t.realisasi().equals(50.0)
                )
                .expectNextMatches(t ->
                        t.tujuanId().equals("T2") &&
                        t.indikatorId().equals("I2") &&
                        t.target().equals("200.0") &&
                        t.realisasi().equals(75.0)
                )
                .verifyComplete();
        verify(tujuanRepository, times(2)).save(any(Tujuan.class));
    }

}
