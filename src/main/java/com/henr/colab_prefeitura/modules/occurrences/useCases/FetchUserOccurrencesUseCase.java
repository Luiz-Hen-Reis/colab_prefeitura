package com.henr.colab_prefeitura.modules.occurrences.useCases;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.henr.colab_prefeitura.modules.occurrences.dtos.OccurrencesResponseDTO;
import com.henr.colab_prefeitura.modules.occurrences.mappers.OccurrenceMapper;
import com.henr.colab_prefeitura.modules.occurrences.repositories.OccurrenceRepository;
import com.henr.colab_prefeitura.modules.users.entities.User;
import com.henr.colab_prefeitura.providers.AuthenticatedUserProvider;

@Service
public class FetchUserOccurrencesUseCase {
    
    @Autowired
    private OccurrenceRepository occurrenceRepository;

    @Autowired
    private AuthenticatedUserProvider authenticatedUserProvider;

    public OccurrencesResponseDTO execute() {
        User user = authenticatedUserProvider.getAuthenticatedUser();

        var occurrences = this.occurrenceRepository.findAllByUserId(user.getId());

        var occurrencesDto = occurrences.stream().map(occurrence -> 
            OccurrenceMapper.toInnerDTO(occurrence)
        ).toList();

        return new OccurrencesResponseDTO(occurrencesDto);
    }
}
