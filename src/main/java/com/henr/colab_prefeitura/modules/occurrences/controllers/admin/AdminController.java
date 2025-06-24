package com.henr.colab_prefeitura.modules.occurrences.controllers.admin;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.henr.colab_prefeitura.modules.occurrences.dtos.ChangeOccurrencePriorityRequestDTO;
import com.henr.colab_prefeitura.modules.occurrences.dtos.ChangeOccurrencePriorityResponseDTO;
import com.henr.colab_prefeitura.modules.occurrences.dtos.OccurrencesResponseDTO;
import com.henr.colab_prefeitura.modules.occurrences.useCases.admin.ChangeOccurrencePriorityUseCase;
import com.henr.colab_prefeitura.modules.occurrences.useCases.admin.FetchAllOccurrencesUseCase;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private FetchAllOccurrencesUseCase fetchAllOccurrencesUseCase;

    @Autowired
    private ChangeOccurrencePriorityUseCase changeOccurrencePriorityUseCase;
    
    @GetMapping("/occurrences")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OccurrencesResponseDTO> listAllOccurrences() {
        var result = this.fetchAllOccurrencesUseCase.execute();
        return ResponseEntity.ok().body(result);
    }

    @PatchMapping("/occurrences/{occurrenceId}/priority")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ChangeOccurrencePriorityResponseDTO> updatePriority(
        @PathVariable String occurrenceId,
        @Valid @RequestBody ChangeOccurrencePriorityRequestDTO request
     ) {
        var result = this.changeOccurrencePriorityUseCase.execute(UUID.fromString(occurrenceId), request.priority());
        return ResponseEntity.ok().body(result);
    }
}
