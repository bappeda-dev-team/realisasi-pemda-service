package cc.kertaskerja.realisasi_opd_service.renaksi.domain;

import cc.kertaskerja.capaian.domain.Capaian;
import cc.kertaskerja.realisasi.domain.JenisRealisasi;
import cc.kertaskerja.realisasi_opd_service.renaksi.web.RenaksiOpdRequest;
import cc.kertaskerja.realisasi_opd_service.renaksi.web.renaksi_triwulan_response.RenaksiTriwulanRekapResponse;
import cc.kertaskerja.realisasi_opd_service.renaksi.web.renaksi_triwulan_response.TriwulanDetailResponse;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class RenaksiOpdService {
    private final RenaksiOpdRepository renaksiOpdRepository;

    public RenaksiOpdService(RenaksiOpdRepository renaksiOpdRepository) {
        this.renaksiOpdRepository = renaksiOpdRepository;
    }

    public Mono<RenaksiOpd> submitRealisasiRenaksi(RenaksiOpdRequest req) {
        return Mono.just(buildUncheckedRealisasiRenaksi(req)).flatMap(renaksiOpdRepository::save);
    }

    public Flux<RenaksiOpd> batchSubmitRealisasiRenaksi(@Valid List<RenaksiOpdRequest> requests) {
        return Flux.fromIterable(requests)
                .flatMap(req -> {
                    if (req.targetRealisasiId() != null) {
                        return renaksiOpdRepository.findById(req.targetRealisasiId())
                                .flatMap(existing -> renaksiOpdRepository.save(buildUpdated(existing, req)))
                                .switchIfEmpty(Mono.defer(() -> renaksiOpdRepository.save(buildUncheckedRealisasiRenaksi(req))));
                    }

                    return renaksiOpdRepository
                            .findFirstByNipAndBulanAndRekinIdAndRenaksiId(req.nip(), req.bulan(), req.rekinId(), req.renaksiId())
                            .flatMap(existing -> renaksiOpdRepository.save(buildUpdated(existing, req)))
                            .switchIfEmpty(Mono.defer(() -> renaksiOpdRepository.save(buildUncheckedRealisasiRenaksi(req))));
                });
    }

    public Mono<Void> deleteRealisasiRenaksi(Long id) {
        return renaksiOpdRepository.deleteById(id);
    }

    public Flux<RenaksiTriwulanRekapResponse> getRekapTriwulanByNipAndTahun(String kodeOpd, String nip, String tahun) {
        return renaksiOpdRepository.findAllByKodeOpdAndNipAndTahun(kodeOpd, nip, tahun)
                .collectList()
                .flatMapMany(list -> {
                    Map<GroupKey, List<RenaksiOpd>> grouped = new LinkedHashMap<>();
                    for (RenaksiOpd item : list) {
                        GroupKey key = new GroupKey(item.renaksiId(), item.renaksi(), item.rekinId(), item.rekin(), item.targetId(), item.nip());
                        grouped.computeIfAbsent(key, k -> new ArrayList<>()).add(item);
                    }

                    return Flux.fromIterable(grouped.entrySet().stream().map(e -> buildRow(e.getKey(), e.getValue())).toList());
                });
    }

    private RenaksiOpd buildUncheckedRealisasiRenaksi(RenaksiOpdRequest req) {
        return RenaksiOpd.of(req.renaksiId(), req.renaksi(), req.nip(), req.rekinId(), req.rekin(), req.targetId(), req.target(),
                req.realisasi(), req.satuan(), req.bulan(), req.tahun(), req.jenisRealisasi(), req.kodeOpd(), RenaksiOpdStatus.UNCHECKED);
    }

    private RenaksiOpd buildUpdated(RenaksiOpd existing, RenaksiOpdRequest req) {
        return new RenaksiOpd(existing.id(), existing.renaksiId(), existing.renaksi(), existing.nip(), existing.rekinId(), existing.rekin(),
                existing.targetId(), existing.target(), req.realisasi(), req.satuan(), req.bulan(), req.tahun(), req.jenisRealisasi(),
                req.kodeOpd(), RenaksiOpdStatus.UNCHECKED, existing.createdBy(), existing.lastModifiedBy(), existing.createdDate(),
                existing.lastModifiedDate(), existing.version());
    }

    private RenaksiTriwulanRekapResponse buildRow(GroupKey key, List<RenaksiOpd> items) {
        Map<Integer, List<RenaksiOpd>> triwulan = Map.of(1, new ArrayList<RenaksiOpd>(), 2, new ArrayList<>(), 3, new ArrayList<>(), 4, new ArrayList<>());
        for (RenaksiOpd item : items) {
            int tw = toTriwulan(item.bulan());
            if (tw != 0) triwulan.get(tw).add(item);
        }

        return new RenaksiTriwulanRekapResponse(
                key.renaksiId(), key.renaksi(), key.rekinId(), key.rekin(), key.targetId(), key.nip(),
                buildTriwulanDetail(triwulan.get(1), items),
                buildTriwulanDetail(triwulan.get(2), items),
                buildTriwulanDetail(triwulan.get(3), items),
                buildTriwulanDetail(triwulan.get(4), items)
        );
    }

    private TriwulanDetailResponse buildTriwulanDetail(List<RenaksiOpd> twItems, List<RenaksiOpd> allItems) {
        String targetRaw = allItems.stream().map(RenaksiOpd::target).filter(Objects::nonNull).findFirst().orElse("0");
        BigDecimal target = parseBigDecimal(targetRaw);
        int realisasi = twItems.stream().map(RenaksiOpd::realisasi).filter(Objects::nonNull).mapToInt(Integer::intValue).sum();
        String satuan = twItems.stream().map(RenaksiOpd::satuan).filter(Objects::nonNull).findFirst()
                .orElseGet(() -> allItems.stream().map(RenaksiOpd::satuan).filter(Objects::nonNull).findFirst().orElse(null));
        JenisRealisasi jenis = twItems.stream().map(RenaksiOpd::jenisRealisasi).filter(Objects::nonNull).findFirst()
                .orElseGet(() -> allItems.stream().map(RenaksiOpd::jenisRealisasi).filter(Objects::nonNull).findFirst().orElse(JenisRealisasi.NAIK));

        double capaianValue = new Capaian((double) realisasi, target.toPlainString(), jenis).hasilCapaian();
        String capaian = BigDecimal.valueOf(capaianValue).stripTrailingZeros().toPlainString();
        String keterangan = capaianValue > 100 ? "nilai capaian lebih dari 100%" : null;

        return new TriwulanDetailResponse(target, realisasi, satuan, capaian, keterangan);
    }

    private int toTriwulan(String bulan) {
        Integer m = toMonthNumber(bulan);
        if (m == null) return 0;
        return ((m - 1) / 3) + 1;
    }

    private Integer toMonthNumber(String bulan) {
        if (bulan == null || bulan.isBlank()) return null;
        try {
            int n = Integer.parseInt(bulan.trim());
            return (n >= 1 && n <= 12) ? n : null;
        } catch (NumberFormatException ignored) {
            return null;
        }
    }

    private BigDecimal parseBigDecimal(String value) {
        try {
            return new BigDecimal(value.trim());
        } catch (Exception ignored) {
            return BigDecimal.ZERO;
        }
    }

    private record GroupKey(String renaksiId, String renaksi, String rekinId, String rekin, String targetId, String nip) {
    }
}
