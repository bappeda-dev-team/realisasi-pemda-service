package cc.kertaskerja.realisasi_pemda_service.sasaran.domain;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class SasaranServiceTests {
    @Mock
    private SasaranRepository sasaranRepository;

    @InjectMocks
    private SasaranService sasaranService;
}
