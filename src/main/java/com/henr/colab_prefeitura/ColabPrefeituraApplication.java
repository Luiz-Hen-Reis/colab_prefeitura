package com.henr.colab_prefeitura;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication
@OpenAPIDefinition(
	info = @Info(
		title = "API de Ocorrências Urbanas (Colab Prefeitura)",
		version = "1.0.0",
		description = "API REST para gerenciamento de ocorrências urbanas, permitindo que cidadãos reportem problemas como buracos, iluminação pública, coleta de lixo e outros serviços urbanos. Fornece endpoints para criação, consulta, atualização e exclusão das ocorrências."
	)
)

public class ColabPrefeituraApplication {

	public static void main(String[] args) {
		SpringApplication.run(ColabPrefeituraApplication.class, args);
	}

}
