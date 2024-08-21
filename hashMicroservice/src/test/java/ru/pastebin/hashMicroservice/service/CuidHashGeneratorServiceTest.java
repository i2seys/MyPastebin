package ru.pastebin.hashMicroservice.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.internal.verification.Times;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.pastebin.hashMicroservice.model.HashEntity;
import ru.pastebin.hashMicroservice.repository.HashRepository;
import ru.pastebin.hashMicroservice.service.mockStub.CuidHashGeneratorServiceMock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CuidHashGeneratorServiceTest {
    private final String hashInDb = "aaaaaaaa";
    private final String hashNotInDb = "cf1ada27";
    private final int hashSize = 8;
    private final HashEntity notInDbEntity = new HashEntity(hashNotInDb);
    private final HashEntity inDbEntity = new HashEntity(hashInDb);
    @Mock
    private HashRepository hashRepository;
    @Captor
    private ArgumentCaptor<String> hashRepositoryFindCaptor;
    @Captor
    private ArgumentCaptor<HashEntity> hashRepositorySaveCaptor;

    private CuidHashGeneratorServiceMock hashGeneratorServiceMock;

    @Test
    void generateAndSaveHash_shouldGenerateAndSaveSuccessfully() {
        hashGeneratorServiceMock = new CuidHashGeneratorServiceMock(hashRepository, hashSize);
        hashGeneratorServiceMock.setGeneratedHashForOneTime(hashNotInDb);
        when(hashRepository.existsById(hashNotInDb)).thenReturn(false);

        hashGeneratorServiceMock.generateAndSaveHash();

        verify(hashRepository).existsById(hashNotInDb);
        verify(hashRepository, new Times(1)).save(notInDbEntity);
    }

    @Test
    void generateAndSaveHash_shouldGenerateAndSaveMoreThanOneTime() {
        hashGeneratorServiceMock = new CuidHashGeneratorServiceMock(hashRepository, hashSize);
        hashGeneratorServiceMock.setGeneratedHashForOneTime(hashInDb);
        when(hashRepository.existsById(hashInDb)).thenReturn(true);

        hashGeneratorServiceMock.generateAndSaveHash();

        verify(hashRepository, times(2)).existsById(hashRepositoryFindCaptor.capture());
        assertEquals(hashRepositoryFindCaptor.getAllValues().get(0), hashInDb);
        assertNotEquals(hashRepositoryFindCaptor.getAllValues().get(1), hashInDb);
        verify(hashRepository, times(1)).save(hashRepositorySaveCaptor.capture());
    }
}