package com.henr.colab_prefeitura.modules.occurrences.controllers.admin;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.henr.colab_prefeitura.modules.occurrences.dtos.ChangeOccurrencePriorityRequestDTO;
import com.henr.colab_prefeitura.modules.occurrences.dtos.ChangeOccurrencePriorityResponseDTO;
import com.henr.colab_prefeitura.modules.occurrences.dtos.ChangeOccurrenceStatusRequestDTO;
import com.henr.colab_prefeitura.modules.occurrences.dtos.ChangeOccurrenceStatusResponseDTO;
import com.henr.colab_prefeitura.modules.occurrences.dtos.OccurrencesResponseDTO;
import com.henr.colab_prefeitura.modules.occurrences.useCases.admin.ChangeOccurrencePriorityUseCase;
import com.henr.colab_prefeitura.modules.occurrences.useCases.admin.ChangeOccurrenceStatusUseCase;
import com.henr.colab_prefeitura.modules.occurrences.useCases.admin.FetchAllOccurrencesUseCase;

import jakarta.validation.Valid;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@RequestMapping("/admin")
@Tag(name = "Administração", description = "Endpoints administrativos para gerenciamento das ocorrências")
public class AdminController {

    @Autowired
    private FetchAllOccurrencesUseCase fetchAllOccurrencesUseCase;

    @Autowired
    private ChangeOccurrencePriorityUseCase changeOccurrencePriorityUseCase;

    @Autowired
    private ChangeOccurrenceStatusUseCase changeOccurrenceStatusUseCase;
    
    @Operation(
        summary = "Listar todas as ocorrências",
        description = "Retorna todas as ocorrências cadastradas no sistema. Requer permissão de administrador.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = OccurrencesResponseDTO.class))),
            @ApiResponse(responseCode = "403", description = "Acesso negado - usuário sem permissão")
        }
    )
    @GetMapping("/occurrences")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OccurrencesResponseDTO> listAllOccurrences() {
        var result = this.fetchAllOccurrencesUseCase.execute();
        return ResponseEntity.ok().body(result);
    }

    @Operation(
        summary = "Atualizar prioridade de uma ocorrência",
        description = "Altera a prioridade da ocorrência informada pelo ID. Requer permissão de administrador.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Prioridade atualizada com sucesso",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ChangeOccurrencePriorityResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "ID inválido ou dados inválidos"),
            @ApiResponse(responseCode = "403", description = "Acesso negado - usuário sem permissão"),
            @ApiResponse(responseCode = "404", description = "Ocorrência não encontrada")
        }
    )
    @PatchMapping("/occurrences/{occurrenceId}/priority")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ChangeOccurrencePriorityResponseDTO> updatePriority(
        @PathVariable String occurrenceId,
        @Valid @RequestBody ChangeOccurrencePriorityRequestDTO request
     ) {
        var result = this.changeOccurrencePriorityUseCase.execute(UUID.fromString(occurrenceId), request.priority());
        return ResponseEntity.ok().body(result);
    }

    @Operation(
        summary = "Atualizar status de uma ocorrência",
        description = "Altera o status da ocorrência informada pelo ID. Requer permissão de administrador.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Status atualizado com sucesso",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ChangeOccurrenceStatusResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "ID inválido ou dados inválidos"),
            @ApiResponse(responseCode = "403", description = "Acesso negado - usuário sem permissão"),
            @ApiResponse(responseCode = "404", description = "Ocorrência não encontrada")
        }
    )
    @PatchMapping("/occurrences/{occurrenceId}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ChangeOccurrenceStatusResponseDTO> updateStatus(
        @PathVariable String occurrenceId,
        @Valid @RequestBody ChangeOccurrenceStatusRequestDTO request
     ) {
        var result = this.changeOccurrenceStatusUseCase.execute(UUID.fromString(occurrenceId), request.status());
        return ResponseEntity.ok().body(result);
    }
}
