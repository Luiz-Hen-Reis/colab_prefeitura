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

@RestController
@RequestMapping("/occurrences")
public class OccurrenceController {

    @Autowired
    private CreateOccurrenceUseCase createOccurrenceUseCase;

    @Autowired
    private FetchUserOccurrencesUseCase fetchUserOccurrencesUseCase;

    @PostMapping
    public ResponseEntity<CreateOccurrenceResponseDTO> create(@RequestBody CreateOccurrenceRequestDTO dto) {
        var response = createOccurrenceUseCase.createWithoutImage(dto);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping(value = "/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> uploadImage(
        @PathVariable String id,
        @RequestPart("image") MultipartFile image
    ) {
        createOccurrenceUseCase.saveImage(UUID.fromString(id), image);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/me")
    public ResponseEntity<OccurrencesResponseDTO> listUserOccurrences() {
        var result = this.fetchUserOccurrencesUseCase.execute();
        return ResponseEntity.ok().body(result);
    }
}
