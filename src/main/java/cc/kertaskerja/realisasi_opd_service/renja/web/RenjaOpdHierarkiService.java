package cc.kertaskerja.realisasi_opd_service.renja.web;

import cc.kertaskerja.realisasi_opd_service.renja_pagu.domain.RenjaPagu;
import cc.kertaskerja.realisasi_opd_service.renja_pagu.domain.RenjaPaguRepository;
import cc.kertaskerja.realisasi_opd_service.renja_target.domain.RenjaTarget;
import cc.kertaskerja.realisasi_opd_service.renja_target.domain.RenjaTargetRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.*;

@Service
public class RenjaOpdHierarkiService {
    private final RenjaTargetRepository renjaTargetRepository;
    private final RenjaPaguRepository renjaPaguRepository;

    public RenjaOpdHierarkiService(RenjaTargetRepository renjaTargetRepository, RenjaPaguRepository renjaPaguRepository) {
        this.renjaTargetRepository = renjaTargetRepository;
        this.renjaPaguRepository = renjaPaguRepository;
    }

    public Mono<RenjaOpdHierarkiResponse> getHierarkiByKodeOpdTahunBulan(String kodeOpd, String tahun, String bulan) {
        Mono<List<RenjaTarget>> targetsMono = renjaTargetRepository.findAllByTahunAndBulanAndKodeOpd(tahun, bulan, kodeOpd).collectList();
        Mono<List<RenjaPagu>> pagusMono = renjaPaguRepository.findAllByTahunAndBulanAndKodeOpd(tahun, bulan, kodeOpd).collectList();

        return Mono.zip(targetsMono, pagusMono)
                .map(tuple -> buildResponse(kodeOpd, tahun, bulan, tuple.getT1(), tuple.getT2()));
    }

    private RenjaOpdHierarkiResponse buildResponse(String kodeOpd, String tahun, String bulan, List<RenjaTarget> targets, List<RenjaPagu> pagus) {
        Map<String, Node> nodes = new HashMap<>();

        for (RenjaTarget target : targets) {
            String kodeRenja = target.kodeRenja();
            if (kodeRenja == null || kodeRenja.isBlank()) {
                continue;
            }
            Node node = nodes.computeIfAbsent(kodeRenja, ignored -> new Node());
            node.targets.add(new RenjaOpdHierarkiResponse.TargetItem(target.targetId(), target.target()));
            if (target.indikatorId() != null || target.indikator() != null) {
                node.indikators.add(new RenjaOpdHierarkiResponse.IndikatorItem(target.indikatorId(), target.indikator()));
            }
        }

        long paguTotal = 0;
        for (RenjaPagu pagu : pagus) {
            String kodeRenja = pagu.kodeRenja();
            if (kodeRenja == null || kodeRenja.isBlank()) {
                continue;
            }
            Node node = nodes.computeIfAbsent(kodeRenja, ignored -> new Node());
            long value = pagu.pagu() == null ? 0 : pagu.pagu();
            node.pagu += value;
            paguTotal += value;
        }

        List<RenjaOpdHierarkiResponse.RenjaItem> urusanItems = buildUrusan(nodes);
        RenjaOpdHierarkiResponse.DataItem dataItem = new RenjaOpdHierarkiResponse.DataItem(
                kodeOpd,
                tahun,
                bulan,
                paguTotal,
                urusanItems
        );
        return new RenjaOpdHierarkiResponse(List.of(dataItem));
    }

    private List<RenjaOpdHierarkiResponse.RenjaItem> buildUrusan(Map<String, Node> nodes) {
        List<String> allCodes = new ArrayList<>(nodes.keySet());
        allCodes.sort(this::compareKodeRenja);

        List<RenjaOpdHierarkiResponse.RenjaItem> result = new ArrayList<>();
        for (String urusanCode : allCodes) {
            if (!isUrusan(urusanCode)) {
                continue;
            }
            Node urusanNode = nodes.get(urusanCode);
            result.add(new RenjaOpdHierarkiResponse.RenjaItem(
                    urusanCode,
                    null,
                    "URUSAN",
                    dedupTargets(urusanNode.targets),
                    urusanNode.pagu,
                    dedupIndikators(urusanNode.indikators),
                    buildBidang(nodes, allCodes, urusanCode),
                    null,
                    null,
                    null
            ));
        }
        return result;
    }

    private List<RenjaOpdHierarkiResponse.RenjaItem> buildBidang(Map<String, Node> nodes, List<String> allCodes, String urusanCode) {
        List<RenjaOpdHierarkiResponse.RenjaItem> result = new ArrayList<>();
        for (String bidangCode : allCodes) {
            if (!isBidangUrusan(bidangCode) || !isDirectChild(urusanCode, bidangCode)) {
                continue;
            }
            Node bidangNode = nodes.get(bidangCode);
            result.add(new RenjaOpdHierarkiResponse.RenjaItem(
                    bidangCode,
                    null,
                    "BIDANGURUSAN",
                    dedupTargets(bidangNode.targets),
                    bidangNode.pagu,
                    dedupIndikators(bidangNode.indikators),
                    null,
                    buildProgram(nodes, allCodes, bidangCode),
                    null,
                    null
            ));
        }
        return result;
    }

    private List<RenjaOpdHierarkiResponse.RenjaItem> buildProgram(Map<String, Node> nodes, List<String> allCodes, String bidangCode) {
        List<RenjaOpdHierarkiResponse.RenjaItem> result = new ArrayList<>();
        for (String programCode : allCodes) {
            if (!isProgram(programCode) || !isDirectChild(bidangCode, programCode)) {
                continue;
            }
            Node programNode = nodes.get(programCode);
            result.add(new RenjaOpdHierarkiResponse.RenjaItem(
                    programCode,
                    null,
                    "PROGRAM",
                    dedupTargets(programNode.targets),
                    programNode.pagu,
                    dedupIndikators(programNode.indikators),
                    null,
                    null,
                    buildKegiatan(nodes, allCodes, programCode),
                    null
            ));
        }
        return result;
    }

    private List<RenjaOpdHierarkiResponse.RenjaItem> buildKegiatan(Map<String, Node> nodes, List<String> allCodes, String programCode) {
        List<RenjaOpdHierarkiResponse.RenjaItem> result = new ArrayList<>();
        for (String kegiatanCode : allCodes) {
            if (!isKegiatan(kegiatanCode) || !isDirectChild(programCode, kegiatanCode)) {
                continue;
            }
            Node kegiatanNode = nodes.get(kegiatanCode);
            result.add(new RenjaOpdHierarkiResponse.RenjaItem(
                    kegiatanCode,
                    null,
                    "KEGIATAN",
                    dedupTargets(kegiatanNode.targets),
                    kegiatanNode.pagu,
                    dedupIndikators(kegiatanNode.indikators),
                    null,
                    null,
                    null,
                    buildSubkegiatan(nodes, allCodes, kegiatanCode)
            ));
        }
        return result;
    }

    private List<RenjaOpdHierarkiResponse.RenjaItem> buildSubkegiatan(Map<String, Node> nodes, List<String> allCodes, String kegiatanCode) {
        List<RenjaOpdHierarkiResponse.RenjaItem> result = new ArrayList<>();
        for (String subkegiatanCode : allCodes) {
            if (!isSubkegiatan(subkegiatanCode) || !isDirectChild(kegiatanCode, subkegiatanCode)) {
                continue;
            }
            Node subkegiatanNode = nodes.get(subkegiatanCode);
            result.add(new RenjaOpdHierarkiResponse.RenjaItem(
                    subkegiatanCode,
                    null,
                    "SUBKEGIATAN",
                    null,
                    subkegiatanNode.pagu,
                    dedupIndikators(subkegiatanNode.indikators),
                    null,
                    null,
                    null,
                    null
            ));
        }
        return result;
    }

    private boolean isDirectChild(String parent, String child) {
        List<String> parentParts = splitKode(parent);
        List<String> childParts = splitKode(child);

        if (isProgram(parent) && isKegiatan(child)) {
            if (childParts.size() != parentParts.size() + 2) {
                return false;
            }
        } else if (isKegiatan(parent) && isSubkegiatan(child)) {
            if (childParts.size() != parentParts.size() + 1) {
                return false;
            }
        } else if (childParts.size() != parentParts.size() + 1) {
            return false;
        }

        for (int i = 0; i < parentParts.size(); i++) {
            if (!Objects.equals(parentParts.get(i), childParts.get(i))) {
                return false;
            }
        }
        return true;
    }

    private int levelOf(String kodeRenja) {
        return splitKode(kodeRenja).size();
    }

    private boolean isUrusan(String kodeRenja) {
        return levelOf(kodeRenja) == 1;
    }

    private boolean isBidangUrusan(String kodeRenja) {
        return levelOf(kodeRenja) == 2;
    }

    private boolean isProgram(String kodeRenja) {
        return levelOf(kodeRenja) == 3;
    }

    private boolean isKegiatan(String kodeRenja) {
        return levelOf(kodeRenja) == 5;
    }

    private boolean isSubkegiatan(String kodeRenja) {
        return levelOf(kodeRenja) == 6;
    }

    private List<String> splitKode(String kodeRenja) {
        return Arrays.asList(kodeRenja.split("\\."));
    }

    // RANGKAI KODE RENJA
    private int compareKodeRenja(String a, String b) {
        List<String> aParts = splitKode(a);
        List<String> bParts = splitKode(b);
        int min = Math.min(aParts.size(), bParts.size());
        for (int i = 0; i < min; i++) {
            int cmp = comparePart(aParts.get(i), bParts.get(i));
            if (cmp != 0) {
                return cmp;
            }
        }
        return Integer.compare(aParts.size(), bParts.size());
    }

    private int comparePart(String a, String b) {
        try {
            return Integer.compare(Integer.parseInt(a), Integer.parseInt(b));
        } catch (NumberFormatException ignored) {
            return a.compareTo(b);
        }
    }

    private List<RenjaOpdHierarkiResponse.TargetItem> dedupTargets(List<RenjaOpdHierarkiResponse.TargetItem> items) {
        LinkedHashMap<String, RenjaOpdHierarkiResponse.TargetItem> unique = new LinkedHashMap<>();
        for (RenjaOpdHierarkiResponse.TargetItem item : items) {
            String key = (item.idTarget() == null ? "" : item.idTarget()) + "|" + (item.target() == null ? "" : item.target());
            unique.putIfAbsent(key, item);
        }
        return new ArrayList<>(unique.values());
    }

    private List<RenjaOpdHierarkiResponse.IndikatorItem> dedupIndikators(List<RenjaOpdHierarkiResponse.IndikatorItem> items) {
        LinkedHashMap<String, RenjaOpdHierarkiResponse.IndikatorItem> unique = new LinkedHashMap<>();
        for (RenjaOpdHierarkiResponse.IndikatorItem item : items) {
            String key = (item.idIndikator() == null ? "" : item.idIndikator()) + "|" + (item.indikator() == null ? "" : item.indikator());
            unique.putIfAbsent(key, item);
        }
        return new ArrayList<>(unique.values());
    }

    private static class Node {
        long pagu;
        final List<RenjaOpdHierarkiResponse.TargetItem> targets = new ArrayList<>();
        final List<RenjaOpdHierarkiResponse.IndikatorItem> indikators = new ArrayList<>();

        private Node() {
        }
    }
}
