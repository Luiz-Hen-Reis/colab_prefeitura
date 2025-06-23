package com.henr.colab_prefeitura.modules.occurrences.controllers.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.henr.colab_prefeitura.modules.occurrences.dtos.OccurrencesResponseDTO;
import com.henr.colab_prefeitura.modules.occurrences.useCases.admin.FetchAllOccurrencesUseCase;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private FetchAllOccurrencesUseCase fetchAllOccurrencesUseCase;
    
    @GetMapping("/occurrences")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OccurrencesResponseDTO> listAllOccurrences() {
        var result = this.fetchAllOccurrencesUseCase.execute();
        return ResponseEntity.ok().body(result);
    }
}
