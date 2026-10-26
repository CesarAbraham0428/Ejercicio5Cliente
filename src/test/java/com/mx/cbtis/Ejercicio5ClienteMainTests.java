package com.mx.cbtis;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mockStatic;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.boot.SpringApplication;

class Ejercicio5ClienteMainTests {

	@Test
	void mainArrancaLaAplicacion() {
		try (MockedStatic<SpringApplication> spring = mockStatic(SpringApplication.class)) {
			Ejercicio5ClienteApplication.main(new String[] {});

			spring.verify(() -> SpringApplication.run(eq(Ejercicio5ClienteApplication.class), any(String[].class)));
		}
	}
}
