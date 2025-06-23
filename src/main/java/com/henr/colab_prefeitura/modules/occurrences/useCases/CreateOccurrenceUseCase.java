package com.henr.colab_prefeitura.modules.occurrences.useCases;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.henr.colab_prefeitura.modules.occurrences.dtos.CreateOccurrenceRequestDTO;
import com.henr.colab_prefeitura.modules.occurrences.dtos.CreateOccurrenceResponseDTO;
import com.henr.colab_prefeitura.modules.occurrences.entities.Occurrence;
import com.henr.colab_prefeitura.modules.occurrences.enums.Priority;
import com.henr.colab_prefeitura.modules.occurrences.enums.Status;
import com.henr.colab_prefeitura.modules.occurrences.repositories.OccurrenceRepository;
import com.henr.colab_prefeitura.modules.users.entities.User;
import com.henr.colab_prefeitura.providers.AuthenticatedUserProvider;

@Service
public class CreateOccurrenceUseCase {

    @Autowired
    private OccurrenceRepository occurrenceRepository;

    @Autowired
    private AuthenticatedUserProvider authenticatedUserProvider;

    public CreateOccurrenceResponseDTO createWithoutImage(CreateOccurrenceRequestDTO dto) {
        User user = authenticatedUserProvider.getAuthenticatedUser();

        Occurrence occurrence = Occurrence.builder()
            .title(dto.title())
            .description(dto.description())
            .type(dto.type())
            .address(dto.address())
            .latitude(dto.latitude())
            .longitude(dto.longitude())
            .status(Status.PENDING)
            .priority(Priority.IN_REVIEW)
            .user(user)
            .build();

        Occurrence saved = occurrenceRepository.save(occurrence);

        return new CreateOccurrenceResponseDTO(
            saved.getTitle(),
            saved.getDescription(),
            saved.getType(),
            saved.getAddress(),
            saved.getLatitude(),
            saved.getLongitude(),
            null,
            saved.getPriority(),
            saved.getStatus(),
            saved.getUser().getId().toString()
        );
    }

    public void saveImage(UUID occurrenceId, MultipartFile image) {
        Occurrence occurrence = occurrenceRepository.findById(occurrenceId)
            .orElseThrow(() -> new RuntimeException("Ocorrência não encontrada"));

        String imagePath = saveImageToDisk(image);
        occurrence.setImagePath(imagePath);

        occurrenceRepository.save(occurrence);
    }

    private String saveImageToDisk(MultipartFile image) {
    try {
        String folder = System.getProperty("user.dir") + File.separator + "uploads";
        File dir = new File(folder);
        if (!dir.exists()) {
            boolean created = dir.mkdirs();
            System.out.println("Criando diretório uploads: " + created);
        }

        String originalFilename = image.getOriginalFilename();
        if (originalFilename == null || originalFilename.isBlank()) {
            throw new RuntimeException("Nome do arquivo inválido");
        }

        String filename = UUID.randomUUID() + "_" + originalFilename;
        File dest = new File(dir, filename);

        if (image.isEmpty()) {
            throw new RuntimeException("Arquivo vazio");
        }

        image.transferTo(dest);
        return dest.getAbsolutePath();
    } catch (IOException e) {
        e.printStackTrace();
        throw new RuntimeException("Erro ao salvar imagem", e);
    }
}

}
