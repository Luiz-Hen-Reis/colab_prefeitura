package com.henr.colab_prefeitura.modules.occurrences.useCases;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.henr.colab_prefeitura.modules.occurrences.dtos.CreateOccurrenceRequestDTO;
import com.henr.colab_prefeitura.modules.occurrences.dtos.CreateOccurrenceResponseDTO;
import com.henr.colab_prefeitura.modules.occurrences.entities.Occurrence;
import com.henr.colab_prefeitura.modules.occurrences.enums.Priority;
import com.henr.colab_prefeitura.modules.occurrences.enums.Status;
import com.henr.colab_prefeitura.modules.occurrences.repositories.OccurrenceRepository;
import com.henr.colab_prefeitura.modules.users.entities.User;
import com.henr.colab_prefeitura.providers.AuthenticatedUserProvider;

@ExtendWith(MockitoExtension.class)
public class CreateOccurrenceUseCaseTest {

    @Mock
    private OccurrenceRepository occurrenceRepository;

    @Mock
    private AuthenticatedUserProvider authenticatedUserProvider;

    @InjectMocks
    private CreateOccurrenceUseCase createOccurrenceUseCase;

    private CreateOccurrenceRequestDTO dto;
    private User fakeUser;
    private MultipartFile image;

    @BeforeEach
    void setup() {
        dto = new CreateOccurrenceRequestDTO(
            "Buraco na rua",
            "Grande buraco na esquina",
            "BURACO",
            "Rua Exemplo, 123",
            new BigDecimal("-23.5505"),
            new BigDecimal("-46.6333")
        );

        fakeUser = User.builder()
            .id(UUID.randomUUID())
            .name("João da Silva")
            .email("joao@email.com")
            .build();

        image = new MockMultipartFile(
            "image",
            "imagem.jpg",
            "image/jpeg",
            "conteudo-fake".getBytes()
        );
    }

    @Test
    void should_create_occurrence_without_image_successfully() {
        when(authenticatedUserProvider.getAuthenticatedUser()).thenReturn(fakeUser);
        when(occurrenceRepository.save(any(Occurrence.class))).thenAnswer(invocation -> {
            Occurrence o = invocation.getArgument(0);
            o.setId(UUID.randomUUID());
            return o;
        });

        CreateOccurrenceResponseDTO response = createOccurrenceUseCase.createWithoutImage(dto);

        assertEquals(dto.title(), response.title());
        assertEquals(dto.description(), response.description());
        assertEquals(dto.type(), response.type());
        assertEquals(dto.address(), response.address());
        assertEquals(dto.latitude(), response.latitude());
        assertEquals(dto.longitude(), response.longitude());
        assertNull(response.image_path());
        assertEquals(Status.PENDING, response.status());
        assertEquals(Priority.IN_REVIEW, response.priority());
        assertEquals(fakeUser.getId().toString(), response.user_id());
    }

    @Test
    void should_save_image_successfully() {
        UUID occurrenceId = UUID.randomUUID();

        Occurrence occurrence = Occurrence.builder()
            .id(occurrenceId)
            .title("Buraco na rua")
            .description("desc")
            .type("BURACO")
            .address("Rua X")
            .latitude(new BigDecimal("-23.5"))
            .longitude(new BigDecimal("-46.6"))
            .status(Status.PENDING)
            .priority(Priority.IN_REVIEW)
            .user(fakeUser)
            .build();

        when(occurrenceRepository.findById(occurrenceId)).thenReturn(Optional.of(occurrence));
        when(occurrenceRepository.save(any(Occurrence.class))).thenReturn(occurrence);

        assertDoesNotThrow(() -> createOccurrenceUseCase.saveImage(occurrenceId, image));
    }

    @Test
    void should_throw_exception_when_image_fails_to_save() throws IOException {
        UUID occurrenceId = UUID.randomUUID();

        Occurrence occurrence = Occurrence.builder()
            .id(occurrenceId)
            .user(fakeUser)
            .build();

        MultipartFile brokenImage = mock(MultipartFile.class);
        when(brokenImage.getOriginalFilename()).thenReturn("imagem.jpg");
        doThrow(new IOException("Erro")).when(brokenImage).transferTo(any(File.class));

        when(occurrenceRepository.findById(occurrenceId)).thenReturn(Optional.of(occurrence));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            createOccurrenceUseCase.saveImage(occurrenceId, brokenImage);
        });

        assertEquals("Erro ao salvar imagem", exception.getMessage());
    }

    @Test
    void should_throw_exception_if_occurrence_not_found_when_uploading_image() {
        UUID fakeId = UUID.randomUUID();
        when(occurrenceRepository.findById(fakeId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            createOccurrenceUseCase.saveImage(fakeId, image);
        });

        assertEquals("Ocorrência não encontrada", exception.getMessage());
    }
}
