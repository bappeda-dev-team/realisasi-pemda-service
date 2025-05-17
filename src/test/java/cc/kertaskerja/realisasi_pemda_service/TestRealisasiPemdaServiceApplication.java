package cc.kertaskerja.realisasi_pemda_service;

import org.springframework.boot.SpringApplication;

public class TestRealisasiPemdaServiceApplication {

	public static void main(String[] args) {
		SpringApplication.from(RealisasiPemdaServiceApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
