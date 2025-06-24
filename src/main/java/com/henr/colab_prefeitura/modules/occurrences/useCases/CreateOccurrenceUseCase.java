package com.henr.colab_prefeitura.modules.occurrences.useCases;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.henr.colab_prefeitura.modules.occurrences.dtos.CreateOccurrenceRequestDTO;
import com.henr.colab_prefeitura.modules.occurrences.dtos.CreateOccurrenceResponseDTO;
import com.henr.colab_prefeitura.modules.occurrences.dtos.ReverseGeocodeResponseDTO;
import com.henr.colab_prefeitura.modules.occurrences.entities.Occurrence;
import com.henr.colab_prefeitura.modules.occurrences.enums.Priority;
import com.henr.colab_prefeitura.modules.occurrences.enums.Status;
import com.henr.colab_prefeitura.modules.occurrences.events.publishers.OccurrenceEventPublisher;
import com.henr.colab_prefeitura.modules.occurrences.repositories.OccurrenceRepository;
import com.henr.colab_prefeitura.modules.users.entities.User;
import com.henr.colab_prefeitura.providers.AuthenticatedUserProvider;

@Service
public class CreateOccurrenceUseCase {

    @Autowired
    private OccurrenceRepository occurrenceRepository;

    @Autowired
    private AuthenticatedUserProvider authenticatedUserProvider;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private OccurrenceEventPublisher eventPublisher;

    public CreateOccurrenceResponseDTO createWithoutImage(CreateOccurrenceRequestDTO dto) {
        validateCoordinatesInSaoPaulo(dto.latitude(), dto.longitude());

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

        Occurrence savedOccurrence = this.occurrenceRepository.save(occurrence);

        this.eventPublisher.publishOccurrenceToClassify(savedOccurrence.getId());

        return new CreateOccurrenceResponseDTO(
            savedOccurrence.getTitle(),
            savedOccurrence.getDescription(),
            savedOccurrence.getType(),
            savedOccurrence.getAddress(),
            savedOccurrence.getLatitude(),
            savedOccurrence.getLongitude(),
            null,
            savedOccurrence.getPriority(),
            savedOccurrence.getStatus(),
            savedOccurrence.getUser().getId().toString()
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
                dir.mkdirs();
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

    private void validateCoordinatesInSaoPaulo(BigDecimal latitude, BigDecimal longitude) {
        if (latitude == null || longitude == null) {
            throw new IllegalArgumentException("Latitude e longitude são obrigatórios.");
        }

        String url = "https://nominatim.openstreetmap.org/reverse" +
                "?format=json" +
                "&lat=" + latitude.toPlainString() +
                "&lon=" + longitude.toPlainString() +
                "&zoom=10" +
                "&addressdetails=1";

        var headers = new HttpHeaders();
        headers.add("User-Agent", "ColabPrefeitura/1.0");

        var entity = new HttpEntity<>(headers);

        var response = restTemplate.exchange(
            url,
            HttpMethod.GET,
            entity,
            ReverseGeocodeResponseDTO.class
        );

        var body = response.getBody();

        if (body == null || body.address == null) {
            throw new IllegalArgumentException("Não foi possível verificar a localização.");
        }

        String city = body.address.city != null ? body.address.city :
                        body.address.town != null ? body.address.town :
                        body.address.municipality;

        if (city == null || !"São Paulo".equalsIgnoreCase(city)) {
            throw new IllegalArgumentException("A coordenada informada está fora da cidade de São Paulo.");
        }
    }
}
