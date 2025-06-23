package com.henr.colab_prefeitura.modules.occurrences.useCases;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.henr.colab_prefeitura.modules.occurrences.dtos.FetchUserOccurrencesResponseDTO;
import com.henr.colab_prefeitura.modules.occurrences.dtos.InnerFetchUserOccurrencesResponseDTO;
import com.henr.colab_prefeitura.modules.occurrences.repositories.OccurrenceRepository;
import com.henr.colab_prefeitura.modules.users.entities.User;
import com.henr.colab_prefeitura.providers.AuthenticatedUserProvider;

@Service
public class FetchUserOccurrencesUseCase {
    
    @Autowired
    private OccurrenceRepository occurrenceRepository;

    @Autowired
    private AuthenticatedUserProvider authenticatedUserProvider;

    public FetchUserOccurrencesResponseDTO execute() {
        User user = authenticatedUserProvider.getAuthenticatedUser();

        var occurrences = this.occurrenceRepository.findAllByUserId(user.getId());

        var occurrencesDto = occurrences.stream().map(occurrence -> 
            new InnerFetchUserOccurrencesResponseDTO(
                occurrence.getId(),
                occurrence.getTitle(),
                occurrence.getDescription(),
                occurrence.getType(),
                occurrence.getStatus(),
                occurrence.getPriority(),
                occurrence.getAddress(),
                occurrence.getLatitude(),
                occurrence.getLongitude(),
                occurrence.getImagePath(),
                occurrence.getUser().getId()
            )
        ).toList();

        return new FetchUserOccurrencesResponseDTO(occurrencesDto);
    }
}
