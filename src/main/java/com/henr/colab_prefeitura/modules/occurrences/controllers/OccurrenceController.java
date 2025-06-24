package com.henr.colab_prefeitura.modules.occurrences.controllers;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import com.henr.colab_prefeitura.modules.occurrences.dtos.CreateOccurrenceRequestDTO;
import com.henr.colab_prefeitura.modules.occurrences.dtos.CreateOccurrenceResponseDTO;
import com.henr.colab_prefeitura.modules.occurrences.dtos.OccurrencesResponseDTO;
import com.henr.colab_prefeitura.modules.occurrences.useCases.CreateOccurrenceUseCase;
import com.henr.colab_prefeitura.modules.occurrences.useCases.FetchUserOccurrencesUseCase;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("/occurrences")
@Tag(name = "Ocorrências", description = "Gerenciamento de ocorrências urbanas")
public class OccurrenceController {

    @Autowired
    private CreateOccurrenceUseCase createOccurrenceUseCase;

    @Autowired
    private FetchUserOccurrencesUseCase fetchUserOccurrencesUseCase;

    @Operation(
        summary = "Criar uma nova ocorrência (sem imagem)",
        description = "Registra uma nova ocorrência urbana com os dados enviados, exceto a imagem que deve ser enviada separadamente.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Ocorrência criada com sucesso", 
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = CreateOccurrenceResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos na requisição")
        }
    )
    @PostMapping
    public ResponseEntity<CreateOccurrenceResponseDTO> create(@RequestBody CreateOccurrenceRequestDTO dto) {
        var response = createOccurrenceUseCase.createWithoutImage(dto);
        return ResponseEntity.ok(response);
    }
    
    @Operation(
        summary = "Upload da imagem de uma ocorrência",
        description = "Adiciona uma imagem para a ocorrência identificada pelo ID informado. O arquivo deve ser enviado como multipart/form-data.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Imagem salva com sucesso"),
            @ApiResponse(responseCode = "400", description = "ID inválido ou arquivo inválido"),
            @ApiResponse(responseCode = "404", description = "Ocorrência não encontrada")
        }
    )
    @PostMapping(value = "/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> uploadImage(
        @PathVariable String id,
        @RequestPart("image") MultipartFile image
    ) {
        createOccurrenceUseCase.saveImage(UUID.fromString(id), image);
        return ResponseEntity.ok().build();
    }

    @Operation(
        summary = "Listar ocorrências do usuário autenticado",
        description = "Retorna uma lista completa das ocorrências criadas pelo usuário autenticado.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Lista de ocorrências retornada com sucesso", 
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = OccurrencesResponseDTO.class)))
        }
    )
    @GetMapping("/me")
    public ResponseEntity<OccurrencesResponseDTO> listUserOccurrences() {
        var result = this.fetchUserOccurrencesUseCase.execute();
        return ResponseEntity.ok().body(result);
    }
}
