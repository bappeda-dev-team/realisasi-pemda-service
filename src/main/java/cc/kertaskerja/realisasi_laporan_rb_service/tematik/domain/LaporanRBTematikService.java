package cc.kertaskerja.realisasi_laporan_rb_service.tematik.domain;

import cc.kertaskerja.capaian.domain.Capaian;
import cc.kertaskerja.integration.perencanaan.LaporanRBTematikClient;
import cc.kertaskerja.integration.perencanaan.laporanrbtematik.LaporanRBTematik;
import cc.kertaskerja.integration.upload.UploadClient;
import cc.kertaskerja.realisasi.domain.JenisRealisasi;
import cc.kertaskerja.realisasi_laporan_rb_service.tematik.web.FaktorPenghambatLaporanRBTematikRequest;
import cc.kertaskerja.realisasi_laporan_rb_service.tematik.web.FaktorPenunjangLaporanRBTematikRequest;
import cc.kertaskerja.realisasi_laporan_rb_service.tematik.web.LaporanRBTematikRequest;
import cc.kertaskerja.realisasi_laporan_rb_service.tematik.web.LaporanRBTematikResponse;
import cc.kertaskerja.realisasi_laporan_rb_service.tematik.web.PerencanaanLaporanRBTematikResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class LaporanRBTematikService {
    private static final Logger log = LoggerFactory.getLogger(LaporanRBTematikService.class);
    private final LaporanRBTematikRepository repository;
    private final LaporanRBTematikClient laporanRBTematikClient;
    private final UploadClient uploadClient;

    public LaporanRBTematikService(
            LaporanRBTematikRepository repository,
            LaporanRBTematikClient laporanRBTematikClient,
            UploadClient uploadClient
    ) {
        this.repository = repository;
        this.laporanRBTematikClient = laporanRBTematikClient;
        this.uploadClient = uploadClient;
    }

    public Mono<LaporanRBTematikResponse> createLaporanRBTematik(LaporanRBTematikRequest req) {
        return upsert(req)
                .map(entity -> LaporanRBTematikResponse.from(entity, null, null, null))
                .flatMap(response -> enrichWithPerencanaan(Mono.just(response), req));
    }

    public Mono<LaporanRBTematikResponse> updateFaktorPenunjang(FaktorPenunjangLaporanRBTematikRequest req) {
        return repository
                .findFirstByKodeOpdAndNipAndTahunAndBulanAndIdRbTematikAndIdIndikatorRbTematikAndIdTargetRbTematik(
                        req.kodeOpd(), req.nip(), req.tahun(), req.bulan(),
                        req.idRbTematik(), req.idIndikatorRbTematik(), req.idTargetRbTematik())
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Laporan RB tematik tidak ditemukan")))
                .flatMap(existing -> {
                    cc.kertaskerja.realisasi_laporan_rb_service.tematik.domain.LaporanRBTematik updated =
                            new cc.kertaskerja.realisasi_laporan_rb_service.tematik.domain.LaporanRBTematik(
                            existing.id(),
                            existing.kodeOpd(), existing.nip(), existing.tahun(), existing.bulan(),
                            existing.idRbTematik(), existing.idIndikatorRbTematik(), existing.idTargetRbTematik(),
                            existing.realisasi(), existing.jenisRealisasi(),
                            req.faktorPenunjang(), existing.faktorPenghambat(),
                            existing.buktiPendukung(), existing.keteranganBuktiPendukung(),
                            existing.createdBy(), existing.lastModifiedBy(),
                            existing.createdDate(), existing.lastModifiedDate()
                    );
                    return repository.save(updated);
                })
                .map(entity -> LaporanRBTematikResponse.from(entity, null, null, null))
                .flatMap(response -> enrichWithPerencanaan(Mono.just(response), toRequest(req)));
    }

    public Mono<LaporanRBTematikResponse> updateFaktorPenghambat(FaktorPenghambatLaporanRBTematikRequest req) {
        return repository
                .findFirstByKodeOpdAndNipAndTahunAndBulanAndIdRbTematikAndIdIndikatorRbTematikAndIdTargetRbTematik(
                        req.kodeOpd(), req.nip(), req.tahun(), req.bulan(),
                        req.idRbTematik(), req.idIndikatorRbTematik(), req.idTargetRbTematik())
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Laporan RB tematik tidak ditemukan")))
                .flatMap(existing -> {
                    cc.kertaskerja.realisasi_laporan_rb_service.tematik.domain.LaporanRBTematik updated =
                            new cc.kertaskerja.realisasi_laporan_rb_service.tematik.domain.LaporanRBTematik(
                            existing.id(),
                            existing.kodeOpd(), existing.nip(), existing.tahun(), existing.bulan(),
                            existing.idRbTematik(), existing.idIndikatorRbTematik(), existing.idTargetRbTematik(),
                            existing.realisasi(), existing.jenisRealisasi(),
                            existing.faktorPenunjang(), req.faktorPenghambat(),
                            existing.buktiPendukung(), existing.keteranganBuktiPendukung(),
                            existing.createdBy(), existing.lastModifiedBy(),
                            existing.createdDate(), existing.lastModifiedDate()
                    );
                    return repository.save(updated);
                })
                .map(entity -> LaporanRBTematikResponse.from(entity, null, null, null))
                .flatMap(response -> enrichWithPerencanaan(Mono.just(response), toRequest(req)));
    }

    private LaporanRBTematikRequest toRequest(FaktorPenunjangLaporanRBTematikRequest req) {
        return new LaporanRBTematikRequest(
                req.kodeOpd(), req.nip(), req.tahun(), req.bulan(),
                req.idRbTematik(), req.idIndikatorRbTematik(), req.idTargetRbTematik(),
                null, null, null);
    }

    private LaporanRBTematikRequest toRequest(FaktorPenghambatLaporanRBTematikRequest req) {
        return new LaporanRBTematikRequest(
                req.kodeOpd(), req.nip(), req.tahun(), req.bulan(),
                req.idRbTematik(), req.idIndikatorRbTematik(), req.idTargetRbTematik(),
                null, null, null);
    }

    private Mono<cc.kertaskerja.realisasi_laporan_rb_service.tematik.domain.LaporanRBTematik> upsert(LaporanRBTematikRequest req) {
        JenisRealisasi jenisRealisasi = JenisRealisasi.NAIK;
        String bukti = req.buktiPendukung() != null ? req.buktiPendukung() : "";
        return repository
                .findFirstByKodeOpdAndNipAndTahunAndBulanAndIdRbTematikAndIdIndikatorRbTematikAndIdTargetRbTematik(
                        req.kodeOpd(), req.nip(), req.tahun(), req.bulan(),
                        req.idRbTematik(), req.idIndikatorRbTematik(), req.idTargetRbTematik())
                .flatMap(existing -> {
                    cc.kertaskerja.realisasi_laporan_rb_service.tematik.domain.LaporanRBTematik updated =
                            new cc.kertaskerja.realisasi_laporan_rb_service.tematik.domain.LaporanRBTematik(
                            existing.id(),
                            existing.kodeOpd(), existing.nip(), existing.tahun(), existing.bulan(),
                            existing.idRbTematik(), existing.idIndikatorRbTematik(), existing.idTargetRbTematik(),
                            req.realisasi(), jenisRealisasi,
                            existing.faktorPenunjang(), existing.faktorPenghambat(),
                            bukti != null && !bukti.isBlank() ? bukti : existing.buktiPendukung(),
                            req.keteranganBuktiPendukung() != null ? req.keteranganBuktiPendukung() : existing.keteranganBuktiPendukung(),
                            existing.createdBy(), existing.lastModifiedBy(),
                            existing.createdDate(), existing.lastModifiedDate()
                    );
                    return repository.save(updated);
                })
                .switchIfEmpty(Mono.defer(() -> {
                    cc.kertaskerja.realisasi_laporan_rb_service.tematik.domain.LaporanRBTematik newEntity =
                            cc.kertaskerja.realisasi_laporan_rb_service.tematik.domain.LaporanRBTematik.of(
                            req.kodeOpd(), req.nip(), req.tahun(), req.bulan(),
                            req.idRbTematik(), req.idIndikatorRbTematik(), req.idTargetRbTematik(),
                            req.realisasi(), jenisRealisasi,
                            "", "", bukti, req.keteranganBuktiPendukung());
                    return repository.save(newEntity);
                }));
    }

    private Mono<LaporanRBTematikResponse> enrichWithPerencanaan(
            Mono<LaporanRBTematikResponse> responseMono,
            LaporanRBTematikRequest req
    ) {
        Mono<List<LaporanRBTematik.LaporanRBTematikData>> perencanaanMono =
                laporanRBTematikClient.fetchLaporanByTahun(Integer.parseInt(req.tahun()));

        return responseMono.flatMap(response -> perencanaanMono
                .map(data -> enrichWithPerencanaan(response, data))
                .switchIfEmpty(Mono.just(response))
        ).onErrorResume(e -> {
            log.warn("Gagal menghubungi perencanaan untuk kodeOpd={}, tahun={}: {}",
                    req.kodeOpd(), req.tahun(), e.getMessage());
            return responseMono;
        });
    }

    private LaporanRBTematikResponse enrichWithPerencanaan(
            LaporanRBTematikResponse response,
            List<LaporanRBTematik.LaporanRBTematikData> laporans
    ) {
        if (laporans == null) {
            return response;
        }
        LaporanRBTematik.LaporanRBTematikData matchingLaporan = laporans.stream()
                .filter(l -> l.id() != null && String.valueOf(l.id()).equals(response.idRbTematik()))
                .findFirst()
                .orElse(null);
        if (matchingLaporan == null) {
            return response;
        }
        LaporanRBTematik.IndikatorRBData matchingIndikator = matchingLaporan.indikator().stream()
                .filter(i -> i.id().equals(response.idIndikatorRbTematik()))
                .findFirst()
                .orElse(null);
        if (matchingIndikator == null) {
            return response;
        }
        LaporanRBTematik.TargetIndikatorRBData matchingTarget = matchingIndikator.target().stream()
                .filter(t -> t.id().equals(response.idTargetRbTematik()))
                .findFirst()
                .orElse(null);
        if (matchingTarget == null) {
            return response;
        }

        String targetStr = matchingTarget.targetNext();
        Double target = parseTargetDouble(targetStr);
        Double capaian = hitungCapaian(response.realisasi(), targetStr, response.jenisRealisasi());
        String keteranganCapaian = keteranganCapaian(response.realisasi(), targetStr, response.jenisRealisasi());

        return new LaporanRBTematikResponse(
                response.id(), response.kodeOpd(), response.nip(), response.tahun(), response.bulan(),
                response.idRbTematik(), response.idIndikatorRbTematik(), response.idTargetRbTematik(),
                response.realisasi(), response.jenisRealisasi(),
                response.faktorPenunjang(), response.faktorPenghambat(), response.buktiPendukung(),
                response.createdBy(), response.lastModifiedBy(),
                response.createdDate(), response.lastModifiedDate(),
                target, capaian, keteranganCapaian, response.keteranganBuktiPendukung()
        );
    }

    private Double parseTargetDouble(String target) {
        if (target == null || target.isBlank()) {
            return null;
        }
        try {
            return Double.parseDouble(target);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public Mono<PerencanaanLaporanRBTematikResponse> getPerencanaanByNip(String nip, String kodeOpd, int tahun, String bulan) {
        String tahunStr = String.valueOf(tahun);
        Mono<List<LaporanRBTematik.LaporanRBTematikData>> perencanaanMono =
                laporanRBTematikClient.fetchLaporanByTahun(tahun);
        Mono<List<cc.kertaskerja.realisasi_laporan_rb_service.tematik.domain.LaporanRBTematik>> realisasiMono =
                (bulan == null || bulan.isBlank())
                        ? repository.findAllByKodeOpdAndNipAndTahun(kodeOpd, nip, tahunStr).collectList()
                        : repository.findAllByKodeOpdAndNipAndTahunAndBulan(kodeOpd, nip, tahunStr, bulan).collectList();

        return Mono.zip(perencanaanMono, realisasiMono)
                .map(tuple -> buildResponse(
                        nip, kodeOpd, tahun, parseInteger(bulan),
                        tuple.getT1(), tuple.getT2()));
    }

    private PerencanaanLaporanRBTematikResponse buildResponse(
            String nip,
            String kodeOpd,
            int tahun,
            Integer bulan,
            List<LaporanRBTematik.LaporanRBTematikData> laporans,
            List<cc.kertaskerja.realisasi_laporan_rb_service.tematik.domain.LaporanRBTematik> realisasiList
    ) {
        Map<String, List<cc.kertaskerja.realisasi_laporan_rb_service.tematik.domain.LaporanRBTematik>> localByTarget =
                realisasiList.stream()
                        .collect(Collectors.groupingBy(r ->
                                buildTargetKey(r.idRbTematik(), r.idIndikatorRbTematik(), r.idTargetRbTematik())));

        List<PerencanaanLaporanRBTematikResponse.LaporanPerencanaanResponse> responseLaporans =
                laporans.stream()
                        .filter(l -> l.id() != null)
                        .map(l -> mapLaporan(l, localByTarget))
                        .collect(Collectors.toList());

        return new PerencanaanLaporanRBTematikResponse(
                nip, null, kodeOpd, tahun, bulan, responseLaporans);
    }

    private PerencanaanLaporanRBTematikResponse.LaporanPerencanaanResponse mapLaporan(
            LaporanRBTematik.LaporanRBTematikData laporan,
            Map<String, List<cc.kertaskerja.realisasi_laporan_rb_service.tematik.domain.LaporanRBTematik>> localByTarget
    ) {
        List<PerencanaanLaporanRBTematikResponse.IndikatorPerencanaanResponse> indikators =
                laporan.indikator().stream()
                        .map(ind -> mapIndikator(laporan.id(), ind, localByTarget))
                        .collect(Collectors.toList());

        List<PerencanaanLaporanRBTematikResponse.RencanaAksiPerencanaanResponse> rencanaAksis =
                laporan.rencanaAksis().stream()
                        .map(this::mapRencanaAksi)
                        .collect(Collectors.toList());

        return new PerencanaanLaporanRBTematikResponse.LaporanPerencanaanResponse(
                laporan.id(), laporan.jenisRb(), laporan.kegiatanUtama(), laporan.keterangan(),
                laporan.tahunBaseline(), laporan.tahunNext(),
                indikators, rencanaAksis);
    }

    private PerencanaanLaporanRBTematikResponse.IndikatorPerencanaanResponse mapIndikator(
            Long idRb,
            LaporanRBTematik.IndikatorRBData indikator,
            Map<String, List<cc.kertaskerja.realisasi_laporan_rb_service.tematik.domain.LaporanRBTematik>> localByTarget
    ) {
        List<PerencanaanLaporanRBTematikResponse.TargetPerencanaanResponse> targets =
                indikator.target().stream()
                        .map(t -> mapTarget(idRb, indikator, t, localByTarget))
                        .collect(Collectors.toList());

        return new PerencanaanLaporanRBTematikResponse.IndikatorPerencanaanResponse(
                indikator.id(), idRb, indikator.indikator(), targets);
    }

    private PerencanaanLaporanRBTematikResponse.TargetPerencanaanResponse mapTarget(
            Long idRb,
            LaporanRBTematik.IndikatorRBData indikator,
            LaporanRBTematik.TargetIndikatorRBData target,
            Map<String, List<cc.kertaskerja.realisasi_laporan_rb_service.tematik.domain.LaporanRBTematik>> localByTarget
    ) {
        String key = buildTargetKey(String.valueOf(idRb), indikator.id(), target.id());
        List<cc.kertaskerja.realisasi_laporan_rb_service.tematik.domain.LaporanRBTematik> locals = localByTarget.get(key);

        Double realisasi = null;
        Double capaian = null;
        String keteranganCapaian = null;
        String faktorPenunjang = null;
        String faktorPenghambat = null;
        String buktiPendukung = null;
        String keteranganBuktiPendukung = null;
        String jenisRealisasi = null;

        if (locals != null && !locals.isEmpty()) {
            Double totalRealisasi = locals.stream()
                    .filter(r -> r.realisasi() != null)
                    .mapToDouble(cc.kertaskerja.realisasi_laporan_rb_service.tematik.domain.LaporanRBTematik::realisasi)
                    .sum();
            realisasi = totalRealisasi == 0 ? null : totalRealisasi;

            cc.kertaskerja.realisasi_laporan_rb_service.tematik.domain.LaporanRBTematik first = locals.get(0);
            faktorPenunjang = first.faktorPenunjang();
            faktorPenghambat = first.faktorPenghambat();
            buktiPendukung = first.buktiPendukung();
            keteranganBuktiPendukung = first.keteranganBuktiPendukung();
            jenisRealisasi = first.jenisRealisasi() != null ? first.jenisRealisasi().name() : null;

            capaian = hitungCapaian(realisasi, target.targetNext(), first.jenisRealisasi());
            keteranganCapaian = keteranganCapaian(realisasi, target.targetNext(), first.jenisRealisasi());
        }

        return new PerencanaanLaporanRBTematikResponse.TargetPerencanaanResponse(
                target.id(), target.idIndikator(),
                target.tahunBaseline(), target.targetBaseline(), target.satuanBaseline(),
                target.tahunNext(), target.targetNext(), target.satuanNext(),
                realisasi, capaian, keteranganCapaian,
                faktorPenunjang, faktorPenghambat, buktiPendukung, keteranganBuktiPendukung,
                jenisRealisasi);
    }

    private PerencanaanLaporanRBTematikResponse.RencanaAksiPerencanaanResponse mapRencanaAksi(
            LaporanRBTematik.RencanaAksiData rencanaAksi
    ) {
        List<PerencanaanLaporanRBTematikResponse.IndikatorRencanaAksiPerencanaanResponse> indikatorRencanaAksis =
                rencanaAksi.indikatorRencanaAksis().stream()
                        .map(this::mapIndikatorRencanaAksi)
                        .collect(Collectors.toList());

        List<PerencanaanLaporanRBTematikResponse.OpdCrosscuttingPerencanaanResponse> opdCrosscuttings =
                rencanaAksi.opdCrosscuttings().stream()
                        .map(this::mapOpdCrosscutting)
                        .collect(Collectors.toList());

        return new PerencanaanLaporanRBTematikResponse.RencanaAksiPerencanaanResponse(
                rencanaAksi.idRencanaAksi(), rencanaAksi.rencanaAksi(),
                indikatorRencanaAksis,
                rencanaAksi.anggaran(), rencanaAksi.realisasiAnggaran(), rencanaAksi.capaianAnggaran(),
                rencanaAksi.opdKoordinator(), rencanaAksi.nipPelaksana(), rencanaAksi.namaPelaksana(),
                opdCrosscuttings);
    }

    private PerencanaanLaporanRBTematikResponse.IndikatorRencanaAksiPerencanaanResponse mapIndikatorRencanaAksi(
            LaporanRBTematik.IndikatorRencanaAksiData indikatorRencanaAksi
    ) {
        List<PerencanaanLaporanRBTematikResponse.TargetRencanaAksiPerencanaanResponse> targets =
                (indikatorRencanaAksi.targets() == null ? List.<LaporanRBTematik.TargetRencanaAksiData>of() : indikatorRencanaAksi.targets())
                        .stream()
                        .map(t -> new PerencanaanLaporanRBTematikResponse.TargetRencanaAksiPerencanaanResponse(
                                t.target(), t.realisasi(), t.satuan(), t.capaian(), t.tahun()))
                        .collect(Collectors.toList());

        return new PerencanaanLaporanRBTematikResponse.IndikatorRencanaAksiPerencanaanResponse(
                indikatorRencanaAksi.indikator(), targets);
    }

    private PerencanaanLaporanRBTematikResponse.OpdCrosscuttingPerencanaanResponse mapOpdCrosscutting(
            LaporanRBTematik.OpdCrosscuttingData opdCrosscutting
    ) {
        List<PerencanaanLaporanRBTematikResponse.PelaksanaCrosscuttingPerencanaanResponse> pelaksanaCrosscuttings =
                opdCrosscutting.pelaksanaCrosscuttings().stream()
                        .map(p -> new PerencanaanLaporanRBTematikResponse.PelaksanaCrosscuttingPerencanaanResponse(
                                p.nipPelaksana(), p.namaPelaksana()))
                        .collect(Collectors.toList());

        return new PerencanaanLaporanRBTematikResponse.OpdCrosscuttingPerencanaanResponse(
                opdCrosscutting.idPohon(), opdCrosscutting.kodeOpd(), opdCrosscutting.namaOpd(),
                pelaksanaCrosscuttings);
    }

    private String buildTargetKey(String idRbTematik, String idIndikatorRbTematik, String idTargetRbTematik) {
        return idRbTematik + "|" + idIndikatorRbTematik + "|" + idTargetRbTematik;
    }

    private Double hitungCapaian(Double realisasi, String target, JenisRealisasi jenisRealisasi) {
        if (realisasi == null || realisasi == 0 || target == null || target.isBlank()) {
            return null;
        }
        Capaian capaianObj = new Capaian(realisasi, target, jenisRealisasi);
        Double calculatedCapaian = capaianObj.hasilCapaian();
        return calculatedCapaian > 100 ? 100.0 : calculatedCapaian;
    }

    private String keteranganCapaian(Double realisasi, String target, JenisRealisasi jenisRealisasi) {
        if (realisasi == null || realisasi == 0 || target == null || target.isBlank()) {
            return null;
        }
        Capaian capaianObj = new Capaian(realisasi, target, jenisRealisasi);
        Double calculatedCapaian = capaianObj.hasilCapaian();
        return calculatedCapaian > 100
                ? "nilai capaian lebih dari 100% (" + String.format("%.2f%%", calculatedCapaian) + ")"
                : null;
    }

    private Integer parseInteger(String value) {
        return value == null ? null : Integer.parseInt(value);
    }

    public Mono<String> uploadFile(FilePart file) {
        return uploadClient.uploadFile(file)
                .map(UploadClient.UploadMetadata::url);
    }
}