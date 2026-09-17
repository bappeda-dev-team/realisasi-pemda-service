package cc.kertaskerja.realisasi_laporan_rb_service.general.domain;

import cc.kertaskerja.capaian.domain.Capaian;
import cc.kertaskerja.integration.perencanaan.LaporanRBGeneralClient;
import cc.kertaskerja.integration.upload.UploadClient;
import cc.kertaskerja.integration.perencanaan.laporanrbgeneral.LaporanRBGeneral;
import cc.kertaskerja.realisasi.domain.JenisRealisasi;
import cc.kertaskerja.realisasi_laporan_rb_service.general.web.FaktorPenghambatLaporanRBGeneralRequest;
import cc.kertaskerja.realisasi_laporan_rb_service.general.web.FaktorPenunjangLaporanRBGeneralRequest;
import cc.kertaskerja.realisasi_laporan_rb_service.general.web.LaporanRBGeneralRequest;
import cc.kertaskerja.realisasi_laporan_rb_service.general.web.LaporanRBGeneralResponse;
import cc.kertaskerja.realisasi_laporan_rb_service.general.web.PerencanaanLaporanRBGeneralResponse;
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
public class LaporanRBGeneralService {
    private static final Logger log = LoggerFactory.getLogger(LaporanRBGeneralService.class);
    private final LaporanRBGeneralRepository repository;
    private final LaporanRBGeneralClient laporanRBGeneralClient;
    private final UploadClient uploadClient;

    public LaporanRBGeneralService(
            LaporanRBGeneralRepository repository,
            LaporanRBGeneralClient laporanRBGeneralClient,
            UploadClient uploadClient
    ) {
        this.repository = repository;
        this.laporanRBGeneralClient = laporanRBGeneralClient;
        this.uploadClient = uploadClient;
    }

    public Mono<LaporanRBGeneralResponse> createLaporanRBGeneral(LaporanRBGeneralRequest req) {
        return upsert(req)
                .map(entity -> LaporanRBGeneralResponse.from(entity, null, null, null))
                .flatMap(response -> enrichWithPerencanaan(Mono.just(response), req));
    }

    public Mono<LaporanRBGeneralResponse> updateFaktorPenunjang(FaktorPenunjangLaporanRBGeneralRequest req) {
        return repository
                .findFirstByKodeOpdAndNipAndTahunAndBulanAndIdRbGeneralAndIdIndikatorRbGeneralAndIdTargetRbGeneral(
                        req.kodeOpd(), req.nip(), req.tahun(), req.bulan(),
                        req.idRbGeneral(), req.idIndikatorRbGeneral(), req.idTargetRbGeneral())
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Laporan RB general tidak ditemukan")))
                .flatMap(existing -> {
                    cc.kertaskerja.realisasi_laporan_rb_service.general.domain.LaporanRBGeneral updated =
                            new cc.kertaskerja.realisasi_laporan_rb_service.general.domain.LaporanRBGeneral(
                            existing.id(),
                            existing.kodeOpd(), existing.nip(), existing.tahun(), existing.bulan(),
                            existing.idRbGeneral(), existing.idIndikatorRbGeneral(), existing.idTargetRbGeneral(),
                            existing.realisasi(), existing.jenisRealisasi(),
                            req.faktorPenunjang(), existing.faktorPenghambat(),
                            existing.buktiPendukung(), existing.keteranganBuktiPendukung(),
                            existing.createdBy(), existing.lastModifiedBy(),
                            existing.createdDate(), existing.lastModifiedDate()
                    );
                    return repository.save(updated);
                })
                .map(entity -> LaporanRBGeneralResponse.from(entity, null, null, null))
                .flatMap(response -> enrichWithPerencanaan(Mono.just(response), toRequest(req)));
    }

    public Mono<LaporanRBGeneralResponse> updateFaktorPenghambat(FaktorPenghambatLaporanRBGeneralRequest req) {
        return repository
                .findFirstByKodeOpdAndNipAndTahunAndBulanAndIdRbGeneralAndIdIndikatorRbGeneralAndIdTargetRbGeneral(
                        req.kodeOpd(), req.nip(), req.tahun(), req.bulan(),
                        req.idRbGeneral(), req.idIndikatorRbGeneral(), req.idTargetRbGeneral())
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Laporan RB general tidak ditemukan")))
                .flatMap(existing -> {
                    cc.kertaskerja.realisasi_laporan_rb_service.general.domain.LaporanRBGeneral updated =
                            new cc.kertaskerja.realisasi_laporan_rb_service.general.domain.LaporanRBGeneral(
                            existing.id(),
                            existing.kodeOpd(), existing.nip(), existing.tahun(), existing.bulan(),
                            existing.idRbGeneral(), existing.idIndikatorRbGeneral(), existing.idTargetRbGeneral(),
                            existing.realisasi(), existing.jenisRealisasi(),
                            existing.faktorPenunjang(), req.faktorPenghambat(),
                            existing.buktiPendukung(), existing.keteranganBuktiPendukung(),
                            existing.createdBy(), existing.lastModifiedBy(),
                            existing.createdDate(), existing.lastModifiedDate()
                    );
                    return repository.save(updated);
                })
                .map(entity -> LaporanRBGeneralResponse.from(entity, null, null, null))
                .flatMap(response -> enrichWithPerencanaan(Mono.just(response), toRequest(req)));
    }

    private LaporanRBGeneralRequest toRequest(FaktorPenunjangLaporanRBGeneralRequest req) {
        return new LaporanRBGeneralRequest(
                req.kodeOpd(), req.nip(), req.tahun(), req.bulan(),
                req.idRbGeneral(), req.idIndikatorRbGeneral(), req.idTargetRbGeneral(),
                null, null, null);
    }

    private LaporanRBGeneralRequest toRequest(FaktorPenghambatLaporanRBGeneralRequest req) {
        return new LaporanRBGeneralRequest(
                req.kodeOpd(), req.nip(), req.tahun(), req.bulan(),
                req.idRbGeneral(), req.idIndikatorRbGeneral(), req.idTargetRbGeneral(),
                null, null, null);
    }

    private Mono<cc.kertaskerja.realisasi_laporan_rb_service.general.domain.LaporanRBGeneral> upsert(LaporanRBGeneralRequest req) {
        JenisRealisasi jenisRealisasi = JenisRealisasi.NAIK;
        String bukti = req.buktiPendukung() != null ? req.buktiPendukung() : "";
        return repository
                .findFirstByKodeOpdAndNipAndTahunAndBulanAndIdRbGeneralAndIdIndikatorRbGeneralAndIdTargetRbGeneral(
                        req.kodeOpd(), req.nip(), req.tahun(), req.bulan(),
                        req.idRbGeneral(), req.idIndikatorRbGeneral(), req.idTargetRbGeneral())
                .flatMap(existing -> {
                    cc.kertaskerja.realisasi_laporan_rb_service.general.domain.LaporanRBGeneral updated =
                            new cc.kertaskerja.realisasi_laporan_rb_service.general.domain.LaporanRBGeneral(
                            existing.id(),
                            existing.kodeOpd(), existing.nip(), existing.tahun(), existing.bulan(),
                            existing.idRbGeneral(), existing.idIndikatorRbGeneral(), existing.idTargetRbGeneral(),
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
                    cc.kertaskerja.realisasi_laporan_rb_service.general.domain.LaporanRBGeneral newEntity =
                            cc.kertaskerja.realisasi_laporan_rb_service.general.domain.LaporanRBGeneral.of(
                            req.kodeOpd(), req.nip(), req.tahun(), req.bulan(),
                            req.idRbGeneral(), req.idIndikatorRbGeneral(), req.idTargetRbGeneral(),
                            req.realisasi(), jenisRealisasi,
                            "", "", bukti, req.keteranganBuktiPendukung());
                    return repository.save(newEntity);
                }));
    }

    private Mono<LaporanRBGeneralResponse> enrichWithPerencanaan(
            Mono<LaporanRBGeneralResponse> responseMono,
            LaporanRBGeneralRequest req
    ) {
        Mono<List<LaporanRBGeneral.LaporanRBGeneralData>> perencanaanMono =
                laporanRBGeneralClient.fetchLaporanByTahun(Integer.parseInt(req.tahun()));

        return responseMono.flatMap(response -> perencanaanMono
                .map(data -> enrichWithPerencanaan(response, data))
                .switchIfEmpty(Mono.just(response))
        ).onErrorResume(e -> {
            log.warn("Gagal menghubungi perencanaan untuk kodeOpd={}, tahun={}: {}",
                    req.kodeOpd(), req.tahun(), e.getMessage());
            return responseMono;
        });
    }

    private LaporanRBGeneralResponse enrichWithPerencanaan(
            LaporanRBGeneralResponse response,
            List<LaporanRBGeneral.LaporanRBGeneralData> laporans
    ) {
        if (laporans == null) {
            return response;
        }
        LaporanRBGeneral.LaporanRBGeneralData matchingLaporan = laporans.stream()
                .filter(l -> l.id() != null && String.valueOf(l.id()).equals(response.idRbGeneral()))
                .findFirst()
                .orElse(null);
        if (matchingLaporan == null) {
            return response;
        }
        LaporanRBGeneral.IndikatorRBData matchingIndikator = matchingLaporan.indikator().stream()
                .filter(i -> i.id().equals(response.idIndikatorRbGeneral()))
                .findFirst()
                .orElse(null);
        if (matchingIndikator == null) {
            return response;
        }
        LaporanRBGeneral.TargetIndikatorRBData matchingTarget = matchingIndikator.target().stream()
                .filter(t -> t.id().equals(response.idTargetRbGeneral()))
                .findFirst()
                .orElse(null);
        if (matchingTarget == null) {
            return response;
        }

        String targetStr = matchingTarget.targetNext();
        Double target = parseTargetDouble(targetStr);
        Double capaian = hitungCapaian(response.realisasi(), targetStr, response.jenisRealisasi());
        String keteranganCapaian = keteranganCapaian(response.realisasi(), targetStr, response.jenisRealisasi());

        return new LaporanRBGeneralResponse(
                response.id(), response.kodeOpd(), response.nip(), response.tahun(), response.bulan(),
                response.idRbGeneral(), response.idIndikatorRbGeneral(), response.idTargetRbGeneral(),
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

    public Mono<PerencanaanLaporanRBGeneralResponse> getPerencanaanByNip(String nip, String kodeOpd, int tahun, String bulan) {
        String tahunStr = String.valueOf(tahun);
        Mono<List<LaporanRBGeneral.LaporanRBGeneralData>> perencanaanMono =
                laporanRBGeneralClient.fetchLaporanByTahun(tahun);
        Mono<List<cc.kertaskerja.realisasi_laporan_rb_service.general.domain.LaporanRBGeneral>> realisasiMono =
                (bulan == null || bulan.isBlank())
                        ? repository.findAllByKodeOpdAndNipAndTahun(kodeOpd, nip, tahunStr).collectList()
                        : repository.findAllByKodeOpdAndNipAndTahunAndBulan(kodeOpd, nip, tahunStr, bulan).collectList();

        return Mono.zip(perencanaanMono, realisasiMono)
                .map(tuple -> buildResponse(
                        nip, kodeOpd, tahun, parseInteger(bulan),
                        tuple.getT1(), tuple.getT2()));
    }

    private PerencanaanLaporanRBGeneralResponse buildResponse(
            String nip,
            String kodeOpd,
            int tahun,
            Integer bulan,
            List<LaporanRBGeneral.LaporanRBGeneralData> laporans,
            List<cc.kertaskerja.realisasi_laporan_rb_service.general.domain.LaporanRBGeneral> realisasiList
    ) {
        Map<String, List<cc.kertaskerja.realisasi_laporan_rb_service.general.domain.LaporanRBGeneral>> localByTarget =
                realisasiList.stream()
                        .collect(Collectors.groupingBy(r ->
                                buildTargetKey(r.idRbGeneral(), r.idIndikatorRbGeneral(), r.idTargetRbGeneral())));

        List<PerencanaanLaporanRBGeneralResponse.LaporanPerencanaanResponse> responseLaporans =
                laporans.stream()
                        .filter(l -> l.id() != null)
                        .map(l -> mapLaporan(l, localByTarget))
                        .collect(Collectors.toList());

        return new PerencanaanLaporanRBGeneralResponse(
                nip, null, kodeOpd, tahun, bulan, responseLaporans);
    }

    private PerencanaanLaporanRBGeneralResponse.LaporanPerencanaanResponse mapLaporan(
            LaporanRBGeneral.LaporanRBGeneralData laporan,
            Map<String, List<cc.kertaskerja.realisasi_laporan_rb_service.general.domain.LaporanRBGeneral>> localByTarget
    ) {
        List<PerencanaanLaporanRBGeneralResponse.IndikatorPerencanaanResponse> indikators =
                laporan.indikator().stream()
                        .map(ind -> mapIndikator(laporan.id(), ind, localByTarget))
                        .collect(Collectors.toList());

        List<PerencanaanLaporanRBGeneralResponse.RencanaAksiPerencanaanResponse> rencanaAksis =
                laporan.rencanaAksis().stream()
                        .map(this::mapRencanaAksi)
                        .collect(Collectors.toList());

        return new PerencanaanLaporanRBGeneralResponse.LaporanPerencanaanResponse(
                laporan.id(), laporan.jenisRb(), laporan.kegiatanUtama(), laporan.keterangan(),
                laporan.tahunBaseline(), laporan.tahunNext(),
                indikators, rencanaAksis);
    }

    private PerencanaanLaporanRBGeneralResponse.IndikatorPerencanaanResponse mapIndikator(
            Long idRb,
            LaporanRBGeneral.IndikatorRBData indikator,
            Map<String, List<cc.kertaskerja.realisasi_laporan_rb_service.general.domain.LaporanRBGeneral>> localByTarget
    ) {
        List<PerencanaanLaporanRBGeneralResponse.TargetPerencanaanResponse> targets =
                indikator.target().stream()
                        .map(t -> mapTarget(idRb, indikator, t, localByTarget))
                        .collect(Collectors.toList());

        return new PerencanaanLaporanRBGeneralResponse.IndikatorPerencanaanResponse(
                indikator.id(), idRb, indikator.indikator(), targets);
    }

    private PerencanaanLaporanRBGeneralResponse.TargetPerencanaanResponse mapTarget(
            Long idRb,
            LaporanRBGeneral.IndikatorRBData indikator,
            LaporanRBGeneral.TargetIndikatorRBData target,
            Map<String, List<cc.kertaskerja.realisasi_laporan_rb_service.general.domain.LaporanRBGeneral>> localByTarget
    ) {
        String key = buildTargetKey(String.valueOf(idRb), indikator.id(), target.id());
        List<cc.kertaskerja.realisasi_laporan_rb_service.general.domain.LaporanRBGeneral> locals = localByTarget.get(key);

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
                    .mapToDouble(cc.kertaskerja.realisasi_laporan_rb_service.general.domain.LaporanRBGeneral::realisasi)
                    .sum();
            realisasi = totalRealisasi == 0 ? null : totalRealisasi;

            cc.kertaskerja.realisasi_laporan_rb_service.general.domain.LaporanRBGeneral first = locals.get(0);
            faktorPenunjang = first.faktorPenunjang();
            faktorPenghambat = first.faktorPenghambat();
            buktiPendukung = first.buktiPendukung();
            keteranganBuktiPendukung = first.keteranganBuktiPendukung();
            jenisRealisasi = first.jenisRealisasi() != null ? first.jenisRealisasi().name() : null;

            capaian = hitungCapaian(realisasi, target.targetNext(), first.jenisRealisasi());
            keteranganCapaian = keteranganCapaian(realisasi, target.targetNext(), first.jenisRealisasi());
        }

        return new PerencanaanLaporanRBGeneralResponse.TargetPerencanaanResponse(
                target.id(), target.idIndikator(),
                target.tahunBaseline(), target.targetBaseline(), target.satuanBaseline(),
                target.tahunNext(), target.targetNext(), target.satuanNext(),
                realisasi, capaian, keteranganCapaian,
                faktorPenunjang, faktorPenghambat, buktiPendukung, keteranganBuktiPendukung,
                jenisRealisasi);
    }

    private PerencanaanLaporanRBGeneralResponse.RencanaAksiPerencanaanResponse mapRencanaAksi(
            LaporanRBGeneral.RencanaAksiData rencanaAksi
    ) {
        List<PerencanaanLaporanRBGeneralResponse.IndikatorRencanaAksiPerencanaanResponse> indikatorRencanaAksis =
                rencanaAksi.indikatorRencanaAksis().stream()
                        .map(this::mapIndikatorRencanaAksi)
                        .collect(Collectors.toList());

        List<PerencanaanLaporanRBGeneralResponse.OpdCrosscuttingPerencanaanResponse> opdCrosscuttings =
                rencanaAksi.opdCrosscuttings().stream()
                        .map(this::mapOpdCrosscutting)
                        .collect(Collectors.toList());

        return new PerencanaanLaporanRBGeneralResponse.RencanaAksiPerencanaanResponse(
                rencanaAksi.idRencanaAksi(), rencanaAksi.rencanaAksi(),
                indikatorRencanaAksis,
                rencanaAksi.anggaran(), rencanaAksi.realisasiAnggaran(), rencanaAksi.capaianAnggaran(),
                rencanaAksi.opdKoordinator(), rencanaAksi.nipPelaksana(), rencanaAksi.namaPelaksana(),
                opdCrosscuttings);
    }

    private PerencanaanLaporanRBGeneralResponse.IndikatorRencanaAksiPerencanaanResponse mapIndikatorRencanaAksi(
            LaporanRBGeneral.IndikatorRencanaAksiData indikatorRencanaAksi
    ) {
        List<PerencanaanLaporanRBGeneralResponse.TargetRencanaAksiPerencanaanResponse> targets =
                (indikatorRencanaAksi.targets() == null ? List.<LaporanRBGeneral.TargetRencanaAksiData>of() : indikatorRencanaAksi.targets())
                        .stream()
                        .map(t -> new PerencanaanLaporanRBGeneralResponse.TargetRencanaAksiPerencanaanResponse(
                                t.target(), t.realisasi(), t.satuan(), t.capaian(), t.tahun()))
                        .collect(Collectors.toList());

        return new PerencanaanLaporanRBGeneralResponse.IndikatorRencanaAksiPerencanaanResponse(
                indikatorRencanaAksi.indikator(), targets);
    }

    private PerencanaanLaporanRBGeneralResponse.OpdCrosscuttingPerencanaanResponse mapOpdCrosscutting(
            LaporanRBGeneral.OpdCrosscuttingData opdCrosscutting
    ) {
        List<PerencanaanLaporanRBGeneralResponse.PelaksanaCrosscuttingPerencanaanResponse> pelaksanaCrosscuttings =
                opdCrosscutting.pelaksanaCrosscuttings().stream()
                        .map(p -> new PerencanaanLaporanRBGeneralResponse.PelaksanaCrosscuttingPerencanaanResponse(
                                p.nipPelaksana(), p.namaPelaksana()))
                        .collect(Collectors.toList());

        return new PerencanaanLaporanRBGeneralResponse.OpdCrosscuttingPerencanaanResponse(
                opdCrosscutting.idPohon(), opdCrosscutting.kodeOpd(), opdCrosscutting.namaOpd(),
                pelaksanaCrosscuttings);
    }

    private String buildTargetKey(String idRbGeneral, String idIndikatorRbGeneral, String idTargetRbGeneral) {
        return idRbGeneral + "|" + idIndikatorRbGeneral + "|" + idTargetRbGeneral;
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